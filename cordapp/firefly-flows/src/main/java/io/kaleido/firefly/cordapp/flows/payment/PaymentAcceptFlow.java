package io.kaleido.firefly.cordapp.flows.payment;

import co.paralleluniverse.fibers.Suspendable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.kaleido.firefly.cordapp.contracts.PaymentContract;
import io.kaleido.firefly.cordapp.flows.CreateFireflyEventFlow;
import io.kaleido.firefly.cordapp.metadata.PaymentStatus;
import io.kaleido.firefly.cordapp.states.BroadcastBatch;
import io.kaleido.firefly.cordapp.states.PaymentState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
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
import java.util.UUID;


public class PaymentAcceptFlow {

    @InitiatingFlow
    @StartableByRPC
    public static class PaymentAcceptance extends CreateFireflyEventFlow<PaymentState> {
        private UniqueIdentifier linearId;
        private String settlementBankRef;
        private Instant settlementDate;


        private PaymentState paymentOutput;

        public PaymentAcceptance( UniqueIdentifier linearId, String settlementBankRef, Instant settlementDate) {
            super(new ArrayList<>(), UUID.randomUUID());
            this.linearId = linearId;
            this.settlementBankRef = settlementBankRef;
            this.settlementDate = settlementDate;
        }

//        public PaymentAcceptance(UniqueIdentifier linearId) {
//            this(linearId, null, null);
//        }

        public static class GENERATING_TRANSACTION extends ProgressTracker.Step {
            public GENERATING_TRANSACTION() {
                super("Generating transaction");
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
                super("Recording transaction");
            }

            @Override
            public ProgressTracker childProgressTracker() {
                return FinalityFlow.tracker();
            }
        }

        public static ProgressTracker tracker() {
            return new ProgressTracker(
                    new GENERATING_TRANSACTION(),
                    new VERIFYING_TRANSACTION(),
                    new SIGNING_TRANSACTION(),
                    new FINALISING_TRANSACTION()
            );
        }

        private final ProgressTracker progressTracker = tracker();

        @Override
        @Suspendable
        public SignedTransaction call() throws FlowException {
            // Stage 1
            QueryCriteria linearStateQueryCriteria = new QueryCriteria.LinearStateQueryCriteria()
                    .withUuid(Arrays.asList(linearId.getId())).withStatus(Vault.StateStatus.UNCONSUMED);
            StateAndRef<PaymentState> paymentInput = getServiceHub().getVaultService().queryBy(PaymentState.class,linearStateQueryCriteria).getStates().get(0);
            PaymentState paymentOutput = new PaymentState();
            try {
                paymentOutput = (PaymentState) BeanUtils.cloneBean(paymentInput.getState().getData());
            }catch (Exception e){
                e.printStackTrace();
            }
            paymentOutput.setStatus(PaymentStatus.PAYMENT_COMPLETED);
            paymentOutput.setSettlementBankRef(settlementBankRef);
            paymentOutput.setSettlementDate(settlementDate);
            paymentOutput.setSettlementDate(Instant.now());
            Command<PaymentContract.Commands.Accept> command = new Command<>(new PaymentContract.Commands.Accept(), getOurIdentity().getOwningKey());
            Party notary = paymentInput.getState().getNotary();
            TransactionBuilder txBuilder = new TransactionBuilder(notary)
                    .addInputState(paymentInput)
                    .addOutputState(paymentOutput)
                    .addCommand(command);
            // Stage 2
//            setStep(VERIFYING_TRANSACTION);
            txBuilder.verify(getServiceHub());
            // Stage 3
//            setStep(SIGNING_TRANSACTION);
            SignedTransaction signedTx = getServiceHub().signInitialTransaction(txBuilder);
            // Stage 4
//            setStep(FINALISING_TRANSACTION);
//            paymentOutput.getParticipants().remove(getOurIdentity());
//            List<FlowSession> receivers = new ArrayList<FlowSession>();
//            for (Object obj : paymentOutput.getParticipants()) {
//                Party party = (Party) obj;
//                receivers.add(initiateFlow(party));
//            }

            List<FlowSession> receivers = new ArrayList<FlowSession>();
            for (Object obj : paymentOutput.getParticipants()) {
                Party party = (Party) obj;
                if(!party.equals(getOurIdentity()))
                    receivers.add(initiateFlow(party));
            }
            this.paymentOutput = paymentOutput;
            return subFlow(new FinalityFlow(signedTx, receivers));
        }

        @Override
        public PaymentState getFireflyEvent() {
//            List<Party> participants = new ArrayList<>(this.observers);
//            participants.add(getOurIdentity());
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//            String payloadRef = "";
//            try {
//                payloadRef = mapper.writeValueAsString(this.paymentOutput);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            System.out.println("payloadRef: " + payloadRef);
//            return new BroadcastBatch(getOurIdentity(), batchId, payloadRef, participants);
            return paymentOutput;
        }

        @InitiatedBy(PaymentAcceptFlow.PaymentAcceptance.class)
        public static class Acceptor extends FlowLogic<SignedTransaction> {

            private final FlowSession otherPartySession;

            public Acceptor(FlowSession otherPartySession) {
                this.otherPartySession = otherPartySession;
            }

            @Suspendable
            @Override
            public SignedTransaction call() throws FlowException {
//                class SignTxFlow extends SignTransactionFlow {
//                    private SignTxFlow(FlowSession otherPartyFlow, ProgressTracker progressTracker) {
//                        super(otherPartyFlow, progressTracker);
//                    }
//
//                    @Override
//                    protected void checkTransaction(SignedTransaction stx) {
////                    requireThat(require -> {
////                        ContractState output = stx.getTx().getOutputs().get(0).getData();
////                        require.using("This must be an IOU transaction.", output instanceof IOUState);
////                        IOUState iou = (IOUState) output;
////                        require.using("I won't accept IOUs with a value over 100.", iou.getValue() <= 100);
////                        return null;
////                    });
//                    }
//                }
//                final SignTxFlow signTxFlow = new SignTxFlow(otherPartySession, SignTransactionFlow.Companion.tracker());
//                final SecureHash txId = subFlow(signTxFlow).getId();

                return subFlow(new ReceiveFinalityFlow(otherPartySession));
            }
        }
    }
}
