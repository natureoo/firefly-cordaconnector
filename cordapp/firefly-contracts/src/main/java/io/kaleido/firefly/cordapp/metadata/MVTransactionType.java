package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public enum MVTransactionType {
    DEBIT,
    CREDIT;
}
