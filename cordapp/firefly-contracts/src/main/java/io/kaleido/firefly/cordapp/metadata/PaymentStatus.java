package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public enum PaymentStatus {
    PAYMENT_REQUEST,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED,
    PAYMENT_PENDING;
}
