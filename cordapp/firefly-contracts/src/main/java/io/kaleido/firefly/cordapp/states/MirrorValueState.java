package io.kaleido.firefly.cordapp.states;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import io.kaleido.firefly.cordapp.contracts.MirrorValueContract;
import io.kaleido.firefly.cordapp.metadata.MVTransactionType;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

@BelongsToContract(MirrorValueContract.class)
public class MirrorValueState implements ContractState, FireflyEvent  {
    private final Party owner;
    private final Party issuer;
    private final BigDecimal instructedMVUnit;
    private final Currency instructedMVCurrency;
    private final MVTransactionType transactionType;
    private final String settlementBankRef;
    private final Instant entryDate;

    public MirrorValueState(Party owner, Party issuer, BigDecimal instructedMVUnit, Currency instructedMVCurrency, MVTransactionType transactionType, String settlementBankRef, Instant entryDate) {
        this.owner = owner;
        this.issuer = issuer;
        this.instructedMVUnit = instructedMVUnit;
        this.instructedMVCurrency = instructedMVCurrency;
        this.transactionType = transactionType;
        this.settlementBankRef = settlementBankRef;
        this.entryDate = entryDate;
    }

    public Party getOwner() {
        return owner;
    }

    public Party getIssuer() {
        return issuer;
    }

    public BigDecimal getInstructedMVUnit() {
        return instructedMVUnit;
    }

    public Currency getInstructedMVCurrency() {
        return instructedMVCurrency;
    }

    public MVTransactionType getTransactionType() {
        return transactionType;
    }

    public String getSettlementBankRef() {
        return settlementBankRef;
    }

    public Instant getEntryDate() {
        return entryDate;
    }


    @Override
    public List<AbstractParty> getParticipants() {
        List list = new ArrayList();
        if(this.owner != null)
            list.add(this.owner);
        if(this.issuer != null)
            list.add(this.issuer);
        return list;
    }

    @Override
    public Party getAuthor() {
        return this.owner;
    }
}
