package io.kaleido.firefly.cordapp.flows.payment;

import co.paralleluniverse.fibers.Suspendable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.kaleido.firefly.cordapp.contracts.PaymentContract;
import io.kaleido.firefly.cordapp.flows.CreateFireflyEventFlow;
import io.kaleido.firefly.cordapp.metadata.*;
import io.kaleido.firefly.cordapp.states.BroadcastBatch;
import io.kaleido.firefly.cordapp.states.PaymentState;
import io.kaleido.firefly.cordapp.util.StringUtil;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.ProgressTracker.Step;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * This flow allows two parties (the [Initiator] and the [Acceptor]) to come to an agreement about the IOU encapsulated
 * within an [IOUState].
 *
 * In our simple example, the [Acceptor] always accepts a valid IOU.
 *
 * These flows have deliberately been implemented by using only the call() method for ease of understanding. In
 * practice, we would recommend splitting up the various stages of the flow into sub-routines.
 *
 * All methods called within the [FlowLogic] subclass need to be annotated with the @Suspendable annotation.
 */
public class PaymentCreateFlow {
    @InitiatingFlow
    @StartableByRPC
    public static class PaymentRequest extends CreateFireflyEventFlow<PaymentState> {
        private Party settlementBank;
        private Participant payer;
        private Participant payee;
        private AccountInfo fromAccount;
        private AccountInfo toAccount;
        private String customerRefNumber;
        private BigDecimal instructedMVUnit;
        private Currency instructedMVCurrency;
        private ChargeBearer chargeBearer;
        private RemittanceInformation remittanceInformation;
        private PurposeCode purposeCode;
        private Instant valueDate;
        private BankInformation bankInformation;
        private String paymentType;

        private PaymentState paymentOutput;
        public PaymentRequest(
                Party settlementBank,
                Participant payer,
                Participant payee,
                AccountInfo fromAccount,
                AccountInfo toAccount,
                String customerRefNumber,
                BigDecimal instructedMVUnit,
                Currency instructedMVCurrency,
                ChargeBearer chargeBearer,
                RemittanceInformation remittanceInformation,
                PurposeCode purposeCode,
                Instant valueDate,
                BankInformation bankInformation,
                String paymentType
        ) {
            super(Arrays.asList(settlementBank, payer.getIdentity(), payee.getIdentity()), UUID.randomUUID());
            this.settlementBank = settlementBank;
            this.payer = payer;
            this.payee = payee;
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.customerRefNumber = customerRefNumber;
            this.instructedMVUnit = instructedMVUnit;
            this.instructedMVCurrency = instructedMVCurrency;
            this.chargeBearer = chargeBearer;
            this.remittanceInformation = remittanceInformation;
            this.purposeCode = purposeCode;
            this.valueDate = valueDate;
            this.bankInformation = bankInformation;
            this.paymentType = paymentType;
        }

        public PaymentRequest(
                              Party settlementBank,
                              Participant payer,

                              AccountInfo fromAccount,
                              AccountInfo toAccount,
                              String customerRefNumber,
                              BigDecimal instructedMVUnit,
                              Currency instructedMVCurrency,
                              ChargeBearer chargeBearer,
                              RemittanceInformation remittanceInformation,
                              PurposeCode purposeCode,
                              Instant valueDate,
                              BankInformation bankInformation,
                              String paymentType
        ) {
            super(Arrays.asList(settlementBank, payer.getIdentity()), UUID.randomUUID());
            this.settlementBank = settlementBank;
            this.payer = payer;
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.customerRefNumber = customerRefNumber;
            this.instructedMVUnit = instructedMVUnit;
            this.instructedMVCurrency = instructedMVCurrency;
            this.chargeBearer = chargeBearer;
            this.remittanceInformation = remittanceInformation;
            this.purposeCode = purposeCode;
            this.valueDate = valueDate;
            this.bankInformation = bankInformation;
            this.paymentType = paymentType;
        }

        public static class GENERATING_TRANSACTION extends Step {
            public GENERATING_TRANSACTION() {
                super("Generating transaction");
            }
        }

        public static class VERIFYING_TRANSACTION extends Step {
            public VERIFYING_TRANSACTION() {
                super("Verifying contract constraints");
            }
        }

        public static class SIGNING_TRANSACTION extends Step {
            public SIGNING_TRANSACTION() {
                super("Signing transaction with our private key");
            }
        }

        public static class FINALISING_TRANSACTION extends Step {
            public FINALISING_TRANSACTION() {
                super("Recording transaction");
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

        @Override
        public ProgressTracker getProgressTracker() {
            return tracker();
        }

        /**
         * The flow logic is encapsulated within the call() method.
         */
        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
//            logger.info("PaymentCreateFlow start to process: customerRefNumber[" + customerRefNumber + "]");
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().stream().findAny()
                    .orElseThrow(() -> new IllegalStateException("Empty notaryIdentities"));
//            logger.info("source narratives: {}", remittanceInformation.getNarratives());
            List<String> targetNarratives = new ArrayList<>();
            for (String narrative : remittanceInformation.getNarratives()) {
                if (StringUtil.isNotNullAndEmptyAfterTrim(narrative)) {
                    String targetNarrative = String.format("%1$-35s", narrative.trim());
                    targetNarratives.add(targetNarrative);
                }
            }
            RemittanceInformation targetRemittanceInformation = new RemittanceInformation(targetNarratives);
//            logger.info("target narratives: {}", targetRemittanceInformation.getNarratives());
//            logger.info("source bankInfos: {}", bankInformation.getBankInfos());
            List<String> targetBankInfos = new ArrayList<>();
            for (String bankInfo : bankInformation.getBankInfos()) {
                targetBankInfos.add(bankInfo.trim());
            }
            BankInformation targetBankInformation = new BankInformation(targetBankInfos);
//            logger.info("target bankInfos: {}", targetBankInformation.getBankInfos());
            // Stage 1
//            setStep(GENERATING_TRANSACTION);
            PaymentState paymentOutput = new PaymentState(
                    settlementBank,
                    payer,
                    payee,
                    fromAccount,
                    toAccount,
                    customerRefNumber,
                    instructedMVUnit,
                    instructedMVCurrency,
                    chargeBearer,
                    targetRemittanceInformation,
                    purposeCode,
                    "",
                    null,
                    valueDate,
                    "",
                    "",
                    null,
                    Instant.now(),
                    targetBankInformation,
                    paymentType,
                    PaymentStatus.PAYMENT_REQUEST,
                    new UniqueIdentifier()


            );
            Command command = new Command(new PaymentContract.Commands.Create(), getOurIdentity().getOwningKey());
            TransactionBuilder txBuilder = new TransactionBuilder(notary)
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
//            List<FlowSession> receivers = (paymentOutput.getParticipants() - getOurIdentity()).map(party -> initiateFlow(party));
//            paymentOutput.getParticipants().remove(getOurIdentity());
            List<FlowSession> receivers = new ArrayList<FlowSession>();
            for (Object obj : paymentOutput.getParticipants()) {
                Party party = (Party) obj;
                if(!party.equals(getOurIdentity()))
                  receivers.add(initiateFlow(party));
            }
            this.paymentOutput = paymentOutput;
//            List<FlowSession> receivers = paymentOutput.getParticipants().stream().map(party -> initiateFlow((Party) party)).to;
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


        @InitiatedBy(PaymentRequest.class)
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
