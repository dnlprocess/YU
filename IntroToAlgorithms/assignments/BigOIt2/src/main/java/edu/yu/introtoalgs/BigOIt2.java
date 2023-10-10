package edu.yu.introtoalgs;

import edu.yu.introtoalgs.BigOIt2Base;
import edu.yu.introtoalgs.BigOMeasurable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BigOIt2 extends BigOIt2Base {

    private class Timer {
        private final long timeOutInMs;
        private final AtomicBoolean timedOut = new AtomicBoolean(false);

        public Timer(long timeOutInMs) {
            this.timeOutInMs = timeOutInMs;
        }

        public void start() {
            long startTimeInMs = System.currentTimeMillis();

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                long elapsedTimeInMs = System.currentTimeMillis() - startTimeInMs;
                if (elapsedTimeInMs >= timeOutInMs) {
                    timedOut.set(true);
                    scheduler.shutdown();
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
        }

        public boolean isTimedOut() {
            return timedOut.get();
        }
    }

    public BigOIt2() {
    }

    /*
     * Overview:
     * doubling ratio:
     *  timer to keep track of overall running time. Returns latest slope if timeOut is reached (checks to see if accurate enough or else null)
     *  thread for above
     *  thread for each doubling
     *      timer to keep track of execution time
     *  thread for calculations
     */

    public double doublingRatio(String bigOMeasurable, long timeOutInMs) throws IllegalArgumentException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Timer timer = new Timer(timeOutInMs);

        List<Integer> nValues = new ArrayList<>();
        List<Double> runTimes = new ArrayList<>();

        List<Double> slopes = new ArrayList<>();
        List<Double> ratios = new ArrayList<>();

        try {
            BigOMeasurable oMeasurable = (BigOMeasurable) Class.forName(bigOMeasurable).getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        try {
            timer.start();

            while (!timer.isTimedOut()) {
                int n = nValues.isEmpty()? 8: 2 * nValues.get(nValues.size()-1);

                Callable<Double> testTask1 = () -> {
                    oMeasurable.setup(n);
                    long start = System.nanoTime();
                    oMeasurable.execute();
                    long end = System.nanoTime();
                    return (double) (end - start);
                };
                Callable<Double> testTask2 = () -> {
                    oMeasurable.setup(n);
                    long start = System.nanoTime();
                    oMeasurable.execute();
                    long end = System.nanoTime();
                    return (double) (end - start);
                };

                Future<Double> testFuture1 = executor.submit(testTask1);
                Future<Double> testFuture2 = executor.submit(testTask2);

                nValues.add(n);
                runTimes.add((testFuture1.get() + testFuture2.get())/2);

                if (timer.isTimedOut()) {
                    break;
                }



                calculateSlope(nValues, runTimes);

                //ratios
                
                if (runTimes.size() < 4) {
                    ratios.add(Double.NaN);
                    continue;
                }
                ratios.add(runTimes.get(runTimes.size()-1)/runTimes.get(runTimes.size()-2));
                if (ratios.get(ratios.size()-1) < 1) {// decreasing despite doubling n
                    throw new IllegalArgumentException();
                }
                
                double ratio = ratios.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);

                double meanRuntime = runTimes.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);
                double std = Math.sqrt(runTimes.stream().map(i -> Math.pow(i-meanRuntime, 2)).mapToDouble(Double::doubleValue).sum()/n);
                if (Math.abs(meanRuntime - ratios.get(0)) > std) {
                    nValues.remove(0);
                    runTimes.remove(0);
                }

                mse(ratio) vs mse(2^slope)
            }

        }
        finally {
            executor.shutdown();
        }
        return Math.pow(2, slopes.get(slopes.size()-1));
    }

    private double calculateSlope(List<Integer> nValues, List<Double> runTimes) {
        int n = nValues.size();
        List<Double> logX = nValues.stream().map(i -> Math.log(i)).collect(Collectors.toList());
        List<Double> logY = runTimes.stream().map(i -> Math.log(i)).collect(Collectors.toList());

        //Use linear regression with logs of values as trying to fit to power law
        //Formuals from: https://mathworld.wolfram.com/LeastSquaresFittingPowerLaw.html

        double sumN = logX.stream().mapToDouble(Double::doubleValue).sum();
        double sumTimes = logY.stream().mapToDouble(Double::doubleValue).sum();
        double sumNSquared = logX.stream().map(i -> i*i).mapToDouble(Double::doubleValue).sum();
        double sumNRuntimes = IntStream.range(0, n)
        .mapToObj(i -> logX.get(i)*logY.get(i)).mapToDouble(Double::doubleValue).sum();

        double numerator = (n * sumNRuntimes) - (sumN * sumTimes);
        double denominator = (n * sumNSquared) - (sumN*sumN);

        if (denominator == 0) {
            return Double.NaN;
        }

        double power = numerator / denominator;

        double multiplicativeConstant = (sumTimes - (power * sumN)) / n;

        List<Double> predRuntimes = nValues.stream().map(i -> multiplicativeConstant * Math.pow(i, power)).collect(Collectors.toList());

        List<Double> rss = IntStream.range(0, n)
        .mapToObj(i -> Math.pow(runTimes.get(i) - predRuntimes.get(i), 2))
        .collect(Collectors.toList());

        double mse = rss.stream().mapToDouble(Double::doubleValue).sum()/(n-2);

        if (n>=8) {
            double mse1 = rss.stream().skip(1).mapToDouble(Double::doubleValue).sum()/(n-3);
            if (mse1 < mse) {
                mse = mse1;
                nValues.remove(0);
                runTimes.remove(0);
            }
        }
        
        if (mse >= 1) {
            return Double.NaN;
        }
        return power;
    }

    private double altCalculateSlope(List<Integer> nValues, List<Double> runTimes) {
        int n = nValues.size();
        List<Double> ratios = IntStream.range(1, n)
        .mapToObj(i -> runTimes.get(i)/runTimes.get(i-1)).;
    }
}