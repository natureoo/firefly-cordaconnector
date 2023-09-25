package io.kaleido.firefly.cordapp.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.TypeOnlyCommandData;
import net.corda.core.transactions.LedgerTransaction;


public final class CashMovementContract implements Contract {
    public void verify( LedgerTransaction tx) {

    }


    public interface Commands extends CommandData {

        public static final class CashLoadRequestCmd extends TypeOnlyCommandData implements Commands {
        }


        public static final class CashLoadCompleteCmd extends TypeOnlyCommandData implements Commands {
        }


        public static final class CashLoadFailCmd extends TypeOnlyCommandData implements Commands {
        }


        public static final class CashTransferRequestCmd extends TypeOnlyCommandData implements Commands {
        }

        public static final class CashTransferCompleteCmd extends TypeOnlyCommandData implements Commands {
        }

        public static final class CashTransferFailCmd extends TypeOnlyCommandData implements Commands {
        }

        public static final class CashUnloadRequestCmd extends TypeOnlyCommandData implements Commands {
        }

        public static final class CashUnloadCompleteCmd extends TypeOnlyCommandData implements Commands {
        }

        public static final class CashUnloadFailCmd extends TypeOnlyCommandData implements Commands {
        }

    }
}
