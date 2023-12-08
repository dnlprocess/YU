package edu.yu.introtoalgs;

import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

import edu.yu.introtoalgs.TxBase;
import edu.yu.introtoalgs.TxSortFJBase;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinPool;

public class TxSortFJ extends TxSortFJBase {

    private class SortFJ extends RecursiveAction {

        private final int low;
        private final int high;
        private final int threshhold;
        private final Random rand;
        private final TxBase[] array;

        public SortFJ(int threshhold, TxBase[] array, int low, int high, Random rand) {
            this.threshhold = threshhold;
            this.array = array;
            this.low = low;
            this.high = high;
            this.rand = rand;
        }

        @Override
        public void compute() {
            if (high-low <= threshhold) {
                sortSequentialInsertion(array, low, high);
            } // sequential processing
            else {
                int partitionIndex = quickPartition(array, low, high, rand);

                SortFJ left = new SortFJ(threshhold, array, low, partitionIndex, rand);
                SortFJ right = new SortFJ(threshhold, array, partitionIndex+1, high, rand);//Do not need to sort pivot

                left.fork();
                right.compute();
                left.join();
            }
        }

        private int quickPartition(TxBase[] array, int low, int high, Random rand) {
            int index = low + rand.nextInt(high-low);

            TxBase pivot = array[index];
            swap(array, index, high-1);//Put pivot to side

            int i = low - 1;

            for (int j = low; j < high-1; j++) {
                if (pivot.compareTo(array[j])>0) {
                    i++;
                    swap(array, i, j);
                }
            }

            swap(array, i + 1, high-1);
            return i + 1;
        }//Partition for quicksort around random element to eliminate need for shuffling

        private void swap(TxBase[] array, int low, int high) {
            TxBase temp = array[low];
            array[low] = array[high];
            array[high] = temp;
        }

        private void sortSequentialInsertion(TxBase[] array, int low, int high) {
            for (int i = low; i < high-1; i++) {
                TxBase pivot = array[i+1];
                int j = i;
        
                while (j >= low && pivot.compareTo(array[j])<0) {
                    array[j + 1] = array[j];
                    j = j - 1;
                }
                array[j + 1] = pivot;
            }
        }//insertion sort

    }

    private TxBase[] txs;

    
	public TxSortFJ(List<TxBase> transactions) {
		super(transactions);
        this.txs = transactions.toArray(new TxBase[transactions.size()]);
		int parallelism = Runtime.getRuntime().availableProcessors();
        int threshhold = 10;
        SortFJ task = new SortFJ(threshhold, txs, 0, txs.length, new Random());
        final ForkJoinPool fjPool = new ForkJoinPool(parallelism);
        fjPool.invoke(task);
        fjPool.shutdown();
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