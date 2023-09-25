package io.kaleido.firefly.cordapp.flows.dvp;

import co.paralleluniverse.fibers.Suspendable;
import io.kaleido.firefly.cordapp.contracts.CashMovementContract;
import io.kaleido.firefly.cordapp.flows.CreateFireflyEventFlow;
import io.kaleido.firefly.cordapp.metadata.AccountInfo;
import io.kaleido.firefly.cordapp.metadata.CashMovementStatus;
import io.kaleido.firefly.cordapp.metadata.Participant;
import io.kaleido.firefly.cordapp.states.CashMovementState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static io.kaleido.firefly.cordapp.flows.dvp.LoadRequestFlow.LoadRequest.tracker;

public class LoadRequestFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class LoadRequest extends CreateFireflyEventFlow<CashMovementState> {
        private Party settlementBank;
        private Participant payer;
        private Participant payee;
        private AccountInfo fromAccount;
        private AccountInfo toAccount;
        private String customerRefNumber;
        private BigDecimal instructedMVUnit;
        private Currency instructedMVCurrency;
//        private String batchId;

//        public LoadRequest(String batchId, UUID groupId, Party settlementBank, Participant payer, Participant payee, AccountInfo fromAccount, AccountInfo toAccount, String customerRefNumber, BigDecimal instructedMVUnit, Currency instructedMVCurrency) {
        public LoadRequest( Party settlementBank, Participant payer, Participant payee, AccountInfo fromAccount, AccountInfo toAccount, String customerRefNumber, BigDecimal instructedMVUnit, Currency instructedMVCurrency) {
            super(Arrays.asList( settlementBank, payer.getIdentity(), payee.getIdentity()), UUID.randomUUID());
            this.settlementBank = settlementBank;
            this.payer = payer;
            this.payee = payee;
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.customerRefNumber = customerRefNumber;
            this.instructedMVUnit = instructedMVUnit;
            this.instructedMVCurrency = instructedMVCurrency;
//            this.batchId = batchId;
        }

        /**
         * The progress tracker checkpoints each stage of the flow and outputs the specified messages when each
         * checkpoint is reached in the code.
         */
        public static class GENERATING_TRANSACTION extends ProgressTracker.Step {
            public GENERATING_TRANSACTION() {
                super("Generating transaction based on a new Cash Load Request");
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

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // obtain a reference from a notary we wish to use
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().stream()
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Empty notaryIdentities"));

        // Stage 1 - Generating Transaction
//        setStep(GENERATING_TRANSACTION);
        CashMovementState cashMovementState = new CashMovementState(
                getServiceHub().getMyInfo().getLegalIdentities().get(0), // operator
                settlementBank,
                payer,
                payee,
                fromAccount,
                toAccount,
                customerRefNumber,
                instructedMVUnit,
                instructedMVCurrency,
                Instant.now(),
                CashMovementStatus.LOAD_REQUEST,
                new UniqueIdentifier()
        );
        Command<CashMovementContract.Commands.CashLoadRequestCmd> txCmd = new Command<>(
                new CashMovementContract.Commands.CashLoadRequestCmd(),
                cashMovementState.getOperator().getOwningKey()
        );
        TransactionBuilder txBuilder = new TransactionBuilder(notary)
                .addOutputState(cashMovementState)
                .addCommand(txCmd);

        // Stage 2 - Verifying Transaction
//        setStep(VERIFYING_TRANSACTION);
        // Verify that the transaction is valid
        txBuilder.verify(getServiceHub());

        // Stage 3 - Signing Transaction
//        setStep(SIGNING_TRANSACTION);
        // Verify and sign the transaction
        SignedTransaction stx = getServiceHub().signInitialTransaction(txBuilder);
//        List<Party> receivers = List.of(settlementBank, payer.getIdentity(), payee.getIdentity()).stream()
//                .filter(party -> !party.equals(getOurIdentity()))
//                .collect(Collectors.toList());
        List<FlowSession> receivers = new ArrayList<FlowSession>();
        for (Object obj : cashMovementState.getParticipants()) {
            Party party = (Party) obj;
            if(!party.equals(getOurIdentity()))
                receivers.add(initiateFlow(party));
        }
//        List<FlowSession> sessions = receivers.stream()
//                .map(this::initiateFlow)
//                .collect(Collectors.toList());

        return subFlow(new FinalityFlow(stx, receivers));
    }


    @InitiatedBy(LoadRequestFlow.LoadRequest.class)
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
}
