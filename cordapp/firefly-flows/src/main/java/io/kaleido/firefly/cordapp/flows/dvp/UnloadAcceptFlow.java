package io.kaleido.firefly.cordapp.flows.dvp;

import co.paralleluniverse.fibers.Suspendable;
import io.kaleido.firefly.cordapp.contracts.CashMovementContract;
import io.kaleido.firefly.cordapp.contracts.MirrorValueContract;
import io.kaleido.firefly.cordapp.metadata.CashMovementStatus;
import io.kaleido.firefly.cordapp.metadata.MVTransactionType;
import io.kaleido.firefly.cordapp.states.CashMovementState;
import io.kaleido.firefly.cordapp.states.MirrorValueState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import org.apache.commons.beanutils.BeanUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnloadAcceptFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class UnloadAcceptance extends FlowLogic<SignedTransaction> {
        private UniqueIdentifier linearId;
        private String settlementBankRef;
        private Instant settlementDate;

        public UnloadAcceptance(UniqueIdentifier linearId, String settlementBankRef, Instant settlementDate) {
            this.linearId = linearId;
            this.settlementBankRef = settlementBankRef;
            this.settlementDate = settlementDate;
        }

        public static class QUERYING_TRANSACTION extends ProgressTracker.Step {
            public QUERYING_TRANSACTION() {
                super("Querying transaction based on the linear ID");
            }
        }

        public static class MODIFYING_TRANSACTION extends ProgressTracker.Step {
            public MODIFYING_TRANSACTION() {
                super("Update the transaction based on the given arguments");
            }
        }

        public static class VERIFYING_TRANSACTION extends ProgressTracker.Step {
            public VERIFYING_TRANSACTION() {
                super("Verifying contract constraints");
            }
        }

        public static class SIGNING_TRANSACTION extends ProgressTracker.Step {
            public SIGNING_TRANSACTION() {
                super("Signing transaction with our private key");
            }
        }

        public static class FINALISING_TRANSACTION extends ProgressTracker.Step {
            public FINALISING_TRANSACTION() {
                super("Obtaining notary signature and recording transaction");
            }

            @Override
            public ProgressTracker childProgressTracker() {
                return FinalityFlow.tracker();
            }
        }

        public static ProgressTracker tracker() {
            return new ProgressTracker(
                    new QUERYING_TRANSACTION(),
                    new MODIFYING_TRANSACTION(),
                    new VERIFYING_TRANSACTION(),
                    new SIGNING_TRANSACTION(),
                    new FINALISING_TRANSACTION()
            );
        }

        private ProgressTracker progressTracker = tracker();

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            // Stage 1 - Querying previous transaction state
//            setStep(QUERYING_TRANSACTION);
            QueryCriteria linearStateQueryCriteria = new QueryCriteria.LinearStateQueryCriteria()
                    .withUuid(Arrays.asList(linearId.getId())).withStatus(Vault.StateStatus.UNCONSUMED);
            StateAndRef<CashMovementState> cashMovementStateAndRef = getServiceHub().getVaultService().queryBy(CashMovementState.class, linearStateQueryCriteria).getStates().get(0);
            // Stage 2 - Create the output state object based on the given arguments
            CashMovementState cashMovementState = cashMovementStateAndRef.getState().getData();
            CashMovementState cashMovementOutputState = new CashMovementState();
            try {
                cashMovementOutputState = (CashMovementState) BeanUtils.cloneBean(cashMovementState);
            }catch (Exception e){
                e.printStackTrace();
            }
            cashMovementOutputState.setStatus(CashMovementStatus.UNLOAD_COMPLETED);
            cashMovementOutputState.setSettlementBankRef(settlementBankRef);
            cashMovementOutputState.setSettlementDate(settlementDate);
            cashMovementOutputState.setEntryDate(Instant.now());
            // obtain a reference from a notary we wish to use
//            setStep(MODIFYING_TRANSACTION);
            Party notary = cashMovementStateAndRef.getState().getNotary();
            Command<CashMovementContract.Commands.CashUnloadCompleteCmd> txCmd = new Command<>(
                    new CashMovementContract.Commands.CashUnloadCompleteCmd(),
                    cashMovementOutputState.getSettlementBank().getOwningKey()
            );
            Command<MirrorValueContract.Commands.CreateCreditMVCmd> mvCmd = new Command<>(
                    new MirrorValueContract.Commands.CreateCreditMVCmd(),
                    getOurIdentity().getOwningKey()
            );
            MirrorValueState mvState = new MirrorValueState(
                    cashMovementState.getPayer().getIdentity(),
                    cashMovementState.getSettlementBank(),
                    cashMovementState.getInstructedMVUnit(),
                    cashMovementState.getInstructedMVCurrency(),
                    MVTransactionType.CREDIT,
                    settlementBankRef,
                    Instant.now()
            );
            TransactionBuilder txBuilder = new TransactionBuilder(notary);
            txBuilder.addInputState(cashMovementStateAndRef);
            txBuilder.addOutputState(cashMovementOutputState);
            txBuilder.addCommand(txCmd);
            txBuilder.addOutputState(mvState);
            txBuilder.addCommand(mvCmd);

//            setStep(VERIFYING_TRANSACTION);

//            if (!getOurIdentity().equals(cashMovementOutputState.getSettlementBank())) {
//                throw new IllegalArgumentException("CashLoadComplete can only be triggered by the settlement bank");
//            }

            txBuilder.verify(getServiceHub());

//            setStep(SIGNING_TRANSACTION);

            SignedTransaction signedTx = getServiceHub().signInitialTransaction(txBuilder);

            List<FlowSession> receivers = new ArrayList<>();
            if(!cashMovementOutputState.getOperator().equals(getOurIdentity()))
                receivers.add(initiateFlow(cashMovementOutputState.getOperator()));

            if(!cashMovementOutputState.getPayer().getIdentity().equals(getOurIdentity()))
                receivers.add(initiateFlow(cashMovementOutputState.getPayer().getIdentity()));

            if(!cashMovementOutputState.getPayee().getIdentity().equals(getOurIdentity()))
                receivers.add(initiateFlow(cashMovementOutputState.getPayee().getIdentity()));

//            List<FlowSession> receivers = Arrays.asList(
//                    initiateFlow(cashMovementOutputState.getOperator()),
//                    initiateFlow(cashMovementOutputState.getPayer().getIdentity()),
//                    initiateFlow(cashMovementOutputState.getPayee().getIdentity())
//            );
//            Iterator<FlowSession> it = receivers.iterator();
//            for(;it.hasNext(); ){
//                FlowSession flowSession = it.next();
//                if(flowSession.getCounterparty().equals(getOurIdentity()))
//                    it.remove();
//            }

            return subFlow(new FinalityFlow(signedTx, receivers));
        }
    }

    @InitiatedBy(UnloadAcceptFlow.UnloadAcceptance.class)
    public static class Acceptor extends FlowLogic<SignedTransaction> {

        private final FlowSession otherPartySession;

        public Acceptor(FlowSession otherPartySession) {
            this.otherPartySession = otherPartySession;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {

            return subFlow(new ReceiveFinalityFlow(otherPartySession));
        }
    }
}
