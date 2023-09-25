package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public enum DvPStatus {
    DVP_DRAFTED,
    DVP_SELLER_SIGNED,
    DVP_PROPOSED,
    DVP_APPROVED,
    DVP_SETTLED,
    DVP_CASH_UNLOADED;
}
