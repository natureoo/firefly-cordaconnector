package io.kaleido.firefly.cordapp.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

public class MirrorValueContract implements Contract {
    public static final String ID = "com.hsbc.mirrorvalue.cordapp.contract.MirrorValueContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {

    }

    public interface Commands extends CommandData {
        class CreateDebitMVCmd implements Commands {}
        class CreateCreditMVCmd implements Commands {}
    }


}
