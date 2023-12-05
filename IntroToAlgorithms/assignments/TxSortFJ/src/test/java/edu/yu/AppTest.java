package edu.yu;

import edu.yu.introtoalgs.TxBase;
import edu.yu.introtoalgs.TxSortFJBase;
import edu.yu.introtoalgs.Tx;
import edu.yu.introtoalgs.TxSortFJ;
import edu.yu.introtoalgs.Account;

import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import org.junit.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private final Random rand = new Random();
    private static final Logger logger = LogManager.getLogger(AppTest.class);
    private final SoftAssertion softAssertion = new SoftAssertion();

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();
    /**
     * Rigorous Test :-)
     */
    @Test
    public void demo() {
        final int nAccounts = 20;
        final int nTxs = 30;
        final List<TxBase> txs = new ArrayList<>();
        final Account[] accounts = new Account [nAccounts];

        for (int i=0; i<nAccounts; i++) {
            accounts[i] = new Account();
        }
        logger.info("Created {} Accounts", nAccounts);

        for (int i=0; i<nTxs; i++) {
            // being silly here: no point in making this look more real
            final Account account1 = accounts[rand.nextInt(0, nAccounts)];
            final Account account2 = accounts[rand.nextInt(0, nAccounts)];
            txs.add(new Tx(account1, account2 , 1));
        }
        Collections.shuffle(txs);
        logger.info("Created {} Txs" , txs.size());
        TxBase[] txArray = txs.toArray(new TxBase[txs.size()]);
        //boolean isSorted = isSorted(txArray);
        //assertTrue(isSorted);
        final TxBase[] pTxs = txs.toArray(new TxBase[txs.size()]);
        Arrays.parallelSort(pTxs);
        boolean isSorted = isSorted(pTxs);
        System.out.println(Arrays.toString(pTxs));
        assertTrue(isSorted);

        final TxSortFJBase txSortFJ = new TxSortFJ(txs);
        final TxBase[] fjTxs = txSortFJ.sort();
        isSorted = isSorted(fjTxs);
        System.out.println(Arrays.toString(fjTxs));
        assertTrue(isSorted);
        try {
            //final TxSortFJBase txSortFJ = new TxSortFJ(txs);
            //final TxBase[] fjTxs = txSortFJ.sort();
            isSorted = isSorted(fjTxs);
            assertTrue(isSorted);
            //softAssertion.assertTrue(isSorted, "*** Txs should have been (but are not) sorted");
        }
        catch(Exception e) {
            final String msg = "Unexpected exception running test: ";
            logger.error(msg, e);
            softAssertion.fail(msg+e.toString());
        }
        //finally {
        //    softAssertion.assertAll();
        //}
    }

    private boolean isSorted(TxBase[] txs) {
        for (int i=0; i<txs.length-1; i++) {
            if (txs[i].compareTo(txs[i+1])>0) {
                System.out.printf("lower: %s%n, upper: %s%n", txs[i].time(), txs[i+1].time());
                return false;
            }
        }

        return true;
    }

    public class SoftAssertion {

        private final List<String> softAssertions = new ArrayList<>();
    
        public void assertTrue(boolean condition, String message) {
            if (!condition) {
                softAssertions.add(message);
            }
        }

        public void fail(String message) {
            softAssertions.add(message);
        }
    
        public void assertAll() {
            if (!softAssertions.isEmpty()) {
                Assert.fail("Soft assertions failed: " + softAssertions.toString());
            }
        }
    }
}
