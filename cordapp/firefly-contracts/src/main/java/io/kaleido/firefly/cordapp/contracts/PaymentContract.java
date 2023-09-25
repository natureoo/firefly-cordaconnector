package io.kaleido.firefly.cordapp.contracts;

import kotlin.jvm.internal.Intrinsics;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.TypeOnlyCommandData;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;


public final class PaymentContract implements Contract {
    public void verify( LedgerTransaction tx) {

    }


    public interface Commands extends CommandData {

        public static final class Create extends TypeOnlyCommandData implements Commands {
        }


        public static final class Await extends TypeOnlyCommandData implements Commands {
        }


        public static final class Accept extends TypeOnlyCommandData implements Commands {
        }


        public static final class Reject extends TypeOnlyCommandData implements Commands {
        }
    }
}
