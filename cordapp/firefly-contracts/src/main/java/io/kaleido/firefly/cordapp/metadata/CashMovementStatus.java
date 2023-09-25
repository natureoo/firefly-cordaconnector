package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public enum CashMovementStatus {
    LOAD_REQUEST,
    LOAD_COMPLETED,
    LOAD_FAILED,
    TRANSFER_REQUEST,
    TRANSFER_COMPLETED,
    TRANSFER_FAILED,
    UNLOAD_REQUEST,
    UNLOAD_COMPLETED,
    UNLOAD_FAILED;
}
