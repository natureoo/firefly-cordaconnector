package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public enum PartyRole {
    OPERATOR,
    SETTLEMENT_BANK,
    PAYER,
    PAYEE,
    BUYER,
    SELLER;
}
