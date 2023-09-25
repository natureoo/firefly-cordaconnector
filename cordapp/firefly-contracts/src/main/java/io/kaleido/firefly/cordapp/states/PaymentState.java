package io.kaleido.firefly.cordapp.states;

import io.kaleido.firefly.cordapp.contracts.PaymentContract;
import io.kaleido.firefly.cordapp.metadata.*;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

//class PaymentState$WhenMappings {
//    // $FF: synthetic field
//    public static int[] $EnumSwitchMapping$0 = new int[PartyRole.values().length];
//
//    static {
//        $EnumSwitchMapping$0[PartyRole.SETTLEMENT_BANK.ordinal()] = 1;
//        $EnumSwitchMapping$0[PartyRole.PAYER.ordinal()] = 2;
//    }
//}
// PaymentState.java


@BelongsToContract(PaymentContract.class)
//public class PaymentState implements LinearState, QueryableState, VerifiableState {
public class PaymentState implements LinearState, VerifiableState, FireflyEvent {

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
    
    private String settlementBankRef;
    
    private Instant settlementDate;
    
    private Instant valueDate;
    
    private String rejectCode;
    
    private String rejectReason;
    
    private Instant rejectDate;
    
    private Instant entryDate;
    
    private BankInformation bankInformation;
    
    private String paymentType;
    
    private PaymentStatus status;
    
    private UniqueIdentifier linearId;

    
    public List getParticipants() {
//        List list =  Arrays.asList(this.settlementBank, this.payer.getIdentity(), payee.getIdentity());
        List list = new ArrayList();
        if(this.settlementBank != null)
            list.add(this.settlementBank);
        if(this.payer != null && this.payer.getIdentity() != null)
            list.add(this.payer.getIdentity());
        if(this.payee != null && this.payee.getIdentity() != null)
            list.add(this.payee.getIdentity());
        return list;
    }

    
//    public PersistentState generateMappedObject( MappedSchema schema) {
//        if (schema instanceof PaymentSchemaV1) {
//            return new PaymentSchemaV1.PersistentPayment(
//                    this.settlementBank.getName().toString(),
//                    this.payer.getIdentity().toString(),
//                    this.payee.getIdentity().toString(),
//                    this.fromAccount.getAccountNumber(),
//                    this.fromAccount.getAccountName(),
//                    this.toAccount.getAccountNumber(),
//                    this.toAccount.getAccountName(),
//                    this.customerRefNumber,
//                    this.remittanceInformation.getNarratives().toString(),
//                    this.purposeCode.getPurposeCode(),
//                    this.fromAccount.getUltimateName(),
//                    this.toAccount.getUltimateName(),
//                    this.instructedMVUnit,
//                    this.instructedMVCurrency,
//                    this.settlementBankRef,
//                    this.settlementDate,
//                    this.valueDate,
//                    this.rejectCode,
//                    this.rejectReason,
//                    this.rejectDate,
//                    this.entryDate,
//                    this.bankInformation.getBankInfos().toString(),
//                    this.paymentType,
//                    this.status,
//                    this.linearId.getId());
//        } else {
//            throw new IllegalArgumentException("Unrecognised schema $schema");
//        }
//
//    }
//
//
//    public Iterable supportedSchemas() {
//        return Arrays.asList(new PaymentSchemaV1());
//    }

    
    public Party partyOf( PartyRole role) {
        // $FF: Couldn't be decompiled
        if(role == PartyRole.SETTLEMENT_BANK)
            return settlementBank;
        else if(role == PartyRole.PAYER)
            return payer.getIdentity();
        else
            throw new IllegalArgumentException("Illegal party role " + role.toString());
    }

    
    public Party getSettlementBank() {
        return this.settlementBank;
    }

    
    public Participant getPayer() {
        return this.payer;
    }

    
    public Participant getPayee() {
        return this.payee;
    }

    
    public AccountInfo getFromAccount() {
        return this.fromAccount;
    }

    
    public AccountInfo getToAccount() {
        return this.toAccount;
    }

    
    public String getCustomerRefNumber() {
        return this.customerRefNumber;
    }

    
    public BigDecimal getInstructedMVUnit() {
        return this.instructedMVUnit;
    }

    
    public Currency getInstructedMVCurrency() {
        return this.instructedMVCurrency;
    }

    
    public ChargeBearer getChargeBearer() {
        return this.chargeBearer;
    }

    
    public RemittanceInformation getRemittanceInformation() {
        return this.remittanceInformation;
    }

    
    public PurposeCode getPurposeCode() {
        return this.purposeCode;
    }

    
    public String getSettlementBankRef() {
        return this.settlementBankRef;
    }

    
    public Instant getSettlementDate() {
        return this.settlementDate;
    }

    
    public Instant getValueDate() {
        return this.valueDate;
    }

    
    public String getRejectCode() {
        return this.rejectCode;
    }

    
    public String getRejectReason() {
        return this.rejectReason;
    }

    
    public Instant getRejectDate() {
        return this.rejectDate;
    }

    
    public Instant getEntryDate() {
        return this.entryDate;
    }

    
    public BankInformation getBankInformation() {
        return this.bankInformation;
    }

    
    public String getPaymentType() {
        return this.paymentType;
    }

    
    public PaymentStatus getStatus() {
        return this.status;
    }

    
    public UniqueIdentifier getLinearId() {
        return this.linearId;
    }

    public void setSettlementBank(Party settlementBank) {
        this.settlementBank = settlementBank;
    }

    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    public void setPayee(Participant payee) {
        this.payee = payee;
    }

    public void setFromAccount(AccountInfo fromAccount) {
        this.fromAccount = fromAccount;
    }

    public void setToAccount(AccountInfo toAccount) {
        this.toAccount = toAccount;
    }

    public void setCustomerRefNumber(String customerRefNumber) {
        this.customerRefNumber = customerRefNumber;
    }

    public void setInstructedMVUnit(BigDecimal instructedMVUnit) {
        this.instructedMVUnit = instructedMVUnit;
    }

    public void setInstructedMVCurrency(Currency instructedMVCurrency) {
        this.instructedMVCurrency = instructedMVCurrency;
    }

    public void setChargeBearer(ChargeBearer chargeBearer) {
        this.chargeBearer = chargeBearer;
    }

    public void setRemittanceInformation(RemittanceInformation remittanceInformation) {
        this.remittanceInformation = remittanceInformation;
    }

    public void setPurposeCode(PurposeCode purposeCode) {
        this.purposeCode = purposeCode;
    }

    public void setSettlementBankRef(String settlementBankRef) {
        this.settlementBankRef = settlementBankRef;
    }

    public void setSettlementDate(Instant settlementDate) {
        this.settlementDate = settlementDate;
    }

    public void setValueDate(Instant valueDate) {
        this.valueDate = valueDate;
    }

    public void setRejectCode(String rejectCode) {
        this.rejectCode = rejectCode;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public void setRejectDate(Instant rejectDate) {
        this.rejectDate = rejectDate;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public void setBankInformation(BankInformation bankInformation) {
        this.bankInformation = bankInformation;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setLinearId(UniqueIdentifier linearId) {
        this.linearId = linearId;
    }

    public PaymentState(){}

    public PaymentState( Party settlementBank,  Participant payer,  Participant payee,  AccountInfo fromAccount,  AccountInfo toAccount,  String customerRefNumber,  BigDecimal instructedMVUnit,  Currency instructedMVCurrency,  ChargeBearer chargeBearer,  RemittanceInformation remittanceInformation,  PurposeCode purposeCode,  String settlementBankRef,  Instant settlementDate,  Instant valueDate,  String rejectCode,  String rejectReason,  Instant rejectDate,  Instant entryDate,  BankInformation bankInformation,  String paymentType,  PaymentStatus status,  UniqueIdentifier linearId) {

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
        this.settlementBankRef = settlementBankRef;
        this.settlementDate = settlementDate;
        this.valueDate = valueDate;
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
        this.rejectDate = rejectDate;
        this.entryDate = entryDate;
        this.bankInformation = bankInformation;
        this.paymentType = paymentType;
        this.status = status;
        this.linearId = linearId;
    }
    
    public String toString() {
        return "PaymentState(settlementBank=" + this.settlementBank + ", payer=" + this.payer + ", payee=" + this.payee + ", fromAccount=" + this.fromAccount + ", toAccount=" + this.toAccount + ", customerRefNumber=" + this.customerRefNumber + ", instructedMVUnit=" + this.instructedMVUnit + ", instructedMVCurrency=" + this.instructedMVCurrency + ", chargeBearer=" + this.chargeBearer + ", remittanceInformation=" + this.remittanceInformation + ", purposeCode=" + this.purposeCode + ", settlementBankRef=" + this.settlementBankRef + ", settlementDate=" + this.settlementDate + ", valueDate=" + this.valueDate + ", rejectCode=" + this.rejectCode + ", rejectReason=" + this.rejectReason + ", rejectDate=" + this.rejectDate + ", entryDate=" + this.entryDate + ", bankInformation=" + this.bankInformation + ", paymentType=" + this.paymentType + ", status=" + this.getStatus() + ", linearId=" + this.getLinearId() + ")";
    }


    @Override
    public Party getAuthor() {
        return this.settlementBank;
    }
}
