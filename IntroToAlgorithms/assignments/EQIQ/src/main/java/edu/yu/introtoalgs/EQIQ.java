package edu.yu.introtoalgs;

import java.util.stream.IntStream;

public class EQIQ extends EQIQBase{

    double questions;
    double nepotismIndex;
    double times;
    double eqQuestions;
    double iqQuestions;

      /** Constructor: supplies the information needed to solve the EQIQ problem.
   * When the constructor invocation completes successfully, clients invocation
   * of every other API method must return in O(1) time.
   *
   * @param totalQuestion the number of questions on the candidate interview
   * test, must be greater than 1
   * @param eqSuccessRate the ith element of this array specifies the success
   * rate of the ith candidate for EQ questions.  Client maintains ownership.
   * @param iqSuccessRate the ith element of this array specifies the success
   * rate of the ith candidate for IQ questions.  Client maintains ownership.
   *
   * NOTE: the size of the two arrays must be identical, and greater than one.
   * @param nepotismIndex the index in the above arrays that specifies the
   * values of the nepotism candidate.  Candidate indices are numbered
   * 0..nCandidates -1.
   */
  //binary search to compare directions and max to find that to compare
  //logn of <n to O(nlogn)
    public EQIQ(int totalQuestions,
    double[] eqSuccessRate,
    double[] iqSuccessRate,
    int nepotismIndex)
    {
        super(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
        
        int length = eqSuccessRate.length;
        if (totalQuestions <= 1 || eqSuccessRate.length != iqSuccessRate.length || length <= 1) {
            throw new IllegalArgumentException();
        }

        this.questions = totalQuestions;

        double low = 0.0;
        double high = totalQuestions;
        double midpoint = (low+high)/2;
        double upper;
        double lower;
        //while (error> tol) {
        double n = Math.ceil(10/Math.log10(2));
        for (int i=0; i<n; i++) {
            midpoint = (low+high)/2.0;
            lower = findRelativePerformance(nepotismIndex, (low+midpoint)/2.0, eqSuccessRate, iqSuccessRate);
            upper = findRelativePerformance(nepotismIndex, (midpoint+high)/2.0, eqSuccessRate, iqSuccessRate);
            
            //System.out.printf("(%.2f, %.2f) : Upper: %.3f, lower: %.3f\n", (low+midpoint)/2.0, (midpoint+high)/2.0, upper, lower);
            //System.out.println(upper>lower);
            if (upper>lower) {
                low = midpoint;
            }
            if (upper<lower) {
                high = midpoint;
            }
        }
        this.times = findRelativePerformance(nepotismIndex, midpoint, eqSuccessRate, iqSuccessRate);
        this.eqQuestions = midpoint;
        this.iqQuestions = totalQuestions-midpoint;
    }

    /** Return true iff some combination of EQ and IQ questions allow the
    * "nepotism candidate" to win.
    */
    public boolean canNepotismSucceed() {
       return this.times>0;
    }

    /** If canNepotismSucceed() is true, returns the number of EQ questions
    * (accurate to three decimal places) in the optimal configuration for the
    * nepotism candidate; otherwise, returns -1.0
    */
    public double getNumberEQQuestions() {
        return canNepotismSucceed()? threeRound(eqQuestions): -1.0;
    }

    /** If canNepotismSucceed() is true, returns the number of IQ questions
    * (accurate to three decimal places) in the optimal configuration for the
    * neptotism candidate; otherwise, returns -1.0
    */
    public double getNumberIQQuestions() {
        return canNepotismSucceed()? threeRound(iqQuestions): -1.0;
    }

    /** If canNepotismSucceed() is true, returns the number of SECONDS (accurate
    * to three decimal places) by which the nepotism candidate completes the
    * test ahead of the next best candidate; ootherwise, returns -1.0
    */
    public double getNumberOfSecondsSuccess() {
        return canNepotismSucceed()? threeRound(times*3600): -1.0;
    }

    private double findRelativePerformance(int nepotismIndex, double eqQuestions, double[] eqSuccessRate, double[] iqSuccessRate) {
        double[] iqSuccesses = iqSuccessRate.clone();
        double[] eqSuccesses = eqSuccessRate.clone();
        
        double[] times = dotProduct(eqQuestions, eqSuccesses, iqSuccesses);

        int maxIndex = IntStream.range(0, times.length)
            .filter(i -> i != nepotismIndex)
            .reduce((i, j) -> times[i] < times[j] ? i : j)
            .orElseThrow();
        if (maxIndex==-1) {
            return Double.MIN_VALUE;
        }
        /*if (maxIndex==nepotismIndex) {
            maxIndex = IntStream.range(0, times.length)
            .filter(i -> i != nepotismIndex)
            .reduce((i, j) -> times[i] < times[j] ? i : j)
            .orElseThrow();
        }*/
 
        return times[maxIndex] - times[nepotismIndex];
    }

    private double[] dotProduct(double eqQuestions, double[] eqSuccessRate, double[] iqSuccessRate) {
        double[] product = new double[eqSuccessRate.length];
        //System.out.print(eqQuestions);
        for (int i=0; i<eqSuccessRate.length; i++) {
            product[i] = (eqQuestions / eqSuccessRate[i]) + ((this.questions - eqQuestions) / iqSuccessRate[i]);
        }

        return product;
    }

    private double threeRound(double num) {
        double tolerance = Math.pow(10, 3);
        return Math.round(num * tolerance) / tolerance;
    }
}
