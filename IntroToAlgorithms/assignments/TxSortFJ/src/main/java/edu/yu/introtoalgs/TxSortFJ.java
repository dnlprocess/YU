package edu.yu.introtoalgs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

import edu.yu.introtoalgs.TxBase;
import edu.yu.introtoalgs.TxSortFJBase;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinPool;

public class TxSortFJ extends TxSortFJBase {

    private class SortFJ extends RecursiveAction {

        private final int low;
        private final int high;
        private final int threshhold;
        private final Random rand = new Random();
        private final TxBase[] array;

        public SortFJ(int threshhold, TxBase[] array, int low, int high) {
            this.threshhold = threshhold;
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        public void compute() {
            int span = high - low;

            /*Tx pivot = array[low + rand.nextInt(span)];

            int i = (low - 1);

            for (int j = low; j <= high - 1; j++) {
                if (pivot.compareTo(array[j])) {
                    i++;
                    swap(arr, i, j);
                }
            }
            swap(arr, i + 1, high);*/

            if (span <= threshhold) {
                sortSequentially(array, low, high);
            } // sequential processing
            else {
                int partitionIndex = partition(array, low, high, rand);

                SortFJ left = new SortFJ(threshold, array, low, partitionIndex);
                SortFJ right = new SortFJ(threshold, array, partitionIndex+2, high);

                left.fork();
                right.compute();
                left.join();

            }
        }
    }

    TxBase[] txs;

    private int partition(Tx[] array, int low, int high, Random rand) {
        int index = low + rand.nextInt(span);
        TxBase pivot = array[index];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (j==index) {
                continue;
            }
            if (pivot.compareTo(array[j])) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, index);
        return i + 1;
    }//Partition for quicksort around random element to eliminate need for shuffling

    private void swap(TxBase[] array, int low, int high) {
        TxBase temp = array[low];
        array[low] = array[high];
        array[high] = temp;
    }

    private void sortSequentially(int low, int high) {
        int span = high - low;
        for (int i = 1; i < span; ++i) {
            TxBase pivot = arr[i];
            int j = i - 1;
    
            while (j >= 0 && pivot.compareTo(arr[j])) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }//insertion sort

	public TxSortFJ(List<TxBase> transactions) {
		super(transactions);
        this.txs = transactions.toArray();
		parallelism = Runtime.getRuntime().availableProcessors();
        threshold = parallelism*2;
        SortFJ task = new SortFJ(threshold, txs, 0, txs.length);
        final ForkJoinPool fjPool = new ForkJoinPool(parallelism);
        parallelSum = fjPool.invoke(task);
        fjPool.shutdown();
        this.txs = transactions;
	}
    
    /** Returns an array of transactions, sorted in ascending order of
   * TxBase.time() values: any instances with null TxBase.time() values precede
   * all other transaction instances in the sort results.
   *
   * @return the transaction instances passed to the constructor, returned as
   * an array, and sorted as specified above.  Students MAY ONLY use the
   * ForkJoin and their own code in their implementation.
   */
    public TxBase[] sort() {
        return this.txs;
    }
}