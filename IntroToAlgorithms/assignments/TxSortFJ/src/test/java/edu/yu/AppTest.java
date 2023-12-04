package edu.yu;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void demo() {
    final SoftAssert softAssert = new SoftAssert();
    final int nAccounts = 2;
    final int nTxs = 5;
    final List<TxBase> txs = new ArrayList<>();
    final Account[] accounts = new Account [nAccounts];

    for (int i=0; i<nAccounts; i++) {
        accounts[i] = new Account();
    }
    logger.info("Created {} Accounts", nAccounts);

    for (int i=0; i<nTxs; i++) {
        // being silly here: no point in making this look more real
        final Account account1 = accounts[random.nextInt(0, nAccounts)];
        final Account account2 = accounts[random.nextInt(0, nAccounts)];
        txs.add(new Tx(account1, account2 , 1));
    }
    Collections.shuffle(txs);
    logger.info("Created {} Txs" , txs.size());
    try {
        final TxSortFJBase txSortFJ = new TxSortFJ ( txs ) ;
        final TxBase[] fjTxs = txSortFJ.sort();
        final boolean isSorted = isSorted(fjTxs);
        softAssert.assertTrue(isSorted, "*** Txs should have been (but are not) sorted");
    }
    catch(Exception e) {
        final String msg = "Unexpected exception running test: ";
        logger.error(msg, e);
        softAssert.fail(msg+e.toString());
    }
    finally {
        softAssert.assertAll("demo");
    }
}
}
