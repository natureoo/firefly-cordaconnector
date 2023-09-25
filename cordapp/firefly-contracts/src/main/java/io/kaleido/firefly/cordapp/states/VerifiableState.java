package io.kaleido.firefly.cordapp.states;

import io.kaleido.firefly.cordapp.metadata.PartyRole;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

public interface VerifiableState extends ContractState {
    
    Enum getStatus();


    Party partyOf( PartyRole role);
}
