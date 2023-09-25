package io.kaleido.firefly.cordapp.states;

import io.kaleido.firefly.cordapp.contracts.CashMovementContract;
import io.kaleido.firefly.cordapp.metadata.AccountInfo;
import io.kaleido.firefly.cordapp.metadata.CashMovementStatus;
import io.kaleido.firefly.cordapp.metadata.Participant;
import io.kaleido.firefly.cordapp.metadata.PartyRole;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@BelongsToContract(CashMovementContract.class)
public class CashMovementState implements LinearState, VerifiableState, FireflyEvent  {
    private Party operator;
    private Party settlementBank;
    private Participant payer;
    private Participant payee;
    private AccountInfo fromAccount;
    private AccountInfo toAccount;
    private String customerRefNumber;
    private BigDecimal instructedMVUnit;
    private Currency instructedMVCurrency;
    private String settlementBankRef;
    private Instant settlementDate;
    private String rejectCode;
    private String rejectReason;
    private Instant rejectDate;
    private Instant entryDate;
    private CashMovementStatus status;
    private UniqueIdentifier linearId;

//    public CashMovementState(Party operator, Party settlementBank, Participant payer, Participant payee, AccountInfo fromAccount, AccountInfo toAccount, String customerRefNumber, BigDecimal instructedMVUnit, Currency instructedMVCurrency, String settlementBankRef, Instant settlementDate, String rejectCode, String rejectReason, Instant rejectDate, Instant entryDate, CashMovementStatus status, UniqueIdentifier linearId) {
//        this.operator = operator;
//        this.settlementBank = settlementBank;
//        this.payer = payer;
//        this.payee = payee;
//        this.fromAccount = fromAccount;
//        this.toAccount = toAccount;
//        this.customerRefNumber = customerRefNumber;
//        this.instructedMVUnit = instructedMVUnit;
//        this.instructedMVCurrency = instructedMVCurrency;
//        this.settlementBankRef = settlementBankRef;
//        this.settlementDate = settlementDate;
//        this.rejectCode = rejectCode;
//        this.rejectReason = rejectReason;
//        this.rejectDate = rejectDate;
//        this.entryDate = entryDate;
//        this.status = status;
//        this.linearId = linearId;
//    }

    public CashMovementState(Party operator, Party settlementBank, Participant payer, Participant payee, AccountInfo fromAccount, AccountInfo toAccount, String customerRefNumber, BigDecimal instructedMVUnit, Currency instructedMVCurrency, Instant entryDate, CashMovementStatus status, UniqueIdentifier linearId) {
        this.operator = operator;
        this.settlementBank = settlementBank;
        this.payer = payer;
        this.payee = payee;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.customerRefNumber = customerRefNumber;
        this.instructedMVUnit = instructedMVUnit;
        this.instructedMVCurrency = instructedMVCurrency;
        this.settlementBankRef = settlementBankRef;
        this.settlementDate = settlementDate;
        this.rejectCode = rejectCode;
        this.rejectReason = rejectReason;
        this.rejectDate = rejectDate;
        this.entryDate = entryDate;
        this.status = status;
        this.linearId = linearId;
    }

    public CashMovementState() {
    }

    public Party getOperator() {
        return operator;
    }

    public Party getSettlementBank() {
        return settlementBank;
    }

    public Participant getPayer() {
        return payer;
    }

    public Participant getPayee() {
        return payee;
    }

    public AccountInfo getFromAccount() {
        return fromAccount;
    }

    public AccountInfo getToAccount() {
        return toAccount;
    }

    public String getCustomerRefNumber() {
        return customerRefNumber;
    }

    public BigDecimal getInstructedMVUnit() {
        return instructedMVUnit;
    }

    public Currency getInstructedMVCurrency() {
        return instructedMVCurrency;
    }

    public String getSettlementBankRef() {
        return settlementBankRef;
    }

    public Instant getSettlementDate() {
        return settlementDate;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public Instant getRejectDate() {
        return rejectDate;
    }

    public Instant getEntryDate() {
        return entryDate;
    }

    public CashMovementStatus getStatus() {
        return status;
    }

    public UniqueIdentifier getLinearId() {
        return linearId;
    }

    public Party partyOf( PartyRole role) {
        // $FF: Couldn't be decompiled
        if(role == PartyRole.SETTLEMENT_BANK)
            return settlementBank;
        else if(role == PartyRole.PAYER)
            return payer.getIdentity();
        else if(role == PartyRole.OPERATOR)
            return operator;
        else if(role == PartyRole.PAYEE)
            return payee.getIdentity();
        else
            throw new IllegalArgumentException("Illegal party role " + role.toString());
    }

    public void setOperator(Party operator) {
        this.operator = operator;
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

    public void setSettlementBankRef(String settlementBankRef) {
        this.settlementBankRef = settlementBankRef;
    }

    public void setSettlementDate(Instant settlementDate) {
        this.settlementDate = settlementDate;
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

    public void setStatus(CashMovementStatus status) {
        this.status = status;
    }

    public void setLinearId(UniqueIdentifier linearId) {
        this.linearId = linearId;
    }

    @Override
    public String toString() {
        return "CashMovementState{" +
                "operator=" + operator +
                ", settlementBank=" + settlementBank +
                ", payer=" + payer +
                ", payee=" + payee +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", customerRefNumber='" + customerRefNumber + '\'' +
                ", instructedMVUnit=" + instructedMVUnit +
                ", instructedMVCurrency=" + instructedMVCurrency +
                ", settlementBankRef='" + settlementBankRef + '\'' +
                ", settlementDate=" + settlementDate +
                ", rejectCode='" + rejectCode + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", rejectDate=" + rejectDate +
                ", entryDate=" + entryDate +
                ", status=" + status +
                ", linearId=" + linearId +
                '}';
    }

    @Override
    public List<AbstractParty> getParticipants() {
//        List list =  Arrays.asList(this.settlementBank, this.payer.getIdentity(), payee.getIdentity());
        List list = new ArrayList();
        if(this.operator != null)
            list.add(this.operator);
        if(this.settlementBank != null)
            list.add(this.settlementBank);
        if(this.payer != null && this.payer.getIdentity() != null)
            list.add(this.payer.getIdentity());
        if(this.payee != null && this.payee.getIdentity() != null)
            list.add(this.payee.getIdentity());
        return list;
    }

    @Override
    public Party getAuthor() {
        return this.settlementBank;
    }
}
