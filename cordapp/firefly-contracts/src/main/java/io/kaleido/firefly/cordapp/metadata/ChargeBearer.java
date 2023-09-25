package io.kaleido.firefly.cordapp.metadata;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public enum ChargeBearer {
    DEBT,
    CRED,
    SHAR,
    SLEV,
    OTHR;
}
