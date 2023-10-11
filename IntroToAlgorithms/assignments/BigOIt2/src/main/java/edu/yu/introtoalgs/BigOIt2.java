package edu.yu.introtoalgs;

import edu.yu.introtoalgs.BigOIt2Base;
import edu.yu.introtoalgs.BigOMeasurable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Timer timer = new Timer(timeOutInMs);

        List<Integer> nValues = new ArrayList<>();
        List<Double> runTimes = new ArrayList<>();

        List<Double> slopes = new ArrayList<>();
        List<Double> ratios = new ArrayList<>();

        BigOMeasurable oMeasurable1;
        BigOMeasurable oMeasurable2;
        BigOMeasurable oMeasurable3;
        BigOMeasurable oMeasurable4;
        try {
            oMeasurable1 = (BigOMeasurable) Class.forName(bigOMeasurable).getConstructor().newInstance();
            oMeasurable2 = (BigOMeasurable) Class.forName(bigOMeasurable).getConstructor().newInstance();
            oMeasurable3 = (BigOMeasurable) Class.forName(bigOMeasurable).getConstructor().newInstance();
            oMeasurable4 = (BigOMeasurable) Class.forName(bigOMeasurable).getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        try {
            timer.start();

            //warm-up
            for (int i=0; i < 10; i++) {
                oMeasurable1.setup(5);
                oMeasurable1.execute();
            }

            while (!timer.isTimedOut()) {
                int n = nValues.isEmpty()? 8: 2 * nValues.get(nValues.size()-1);

                Callable<Double> testTask1 = () -> {
                    oMeasurable1.setup(n);
                    long start = System.nanoTime();
                    oMeasurable1.execute();
                    long end = System.nanoTime();
                    return (double) (end - start);
                };
                Callable<Double> testTask2 = () -> {
                    oMeasurable2.setup(n);
                    long start = System.nanoTime();
                    oMeasurable2.execute();
                    long end = System.nanoTime();
                    return (double) (end - start);
                };
                Callable<Double> testTask3 = () -> {
                    oMeasurable3.setup(n+1);
                    long start = System.nanoTime();
                    oMeasurable3.execute();
                    long end = System.nanoTime();
                    return (double) (end - start);
                };
                Callable<Double> testTask4 = () -> {
                    oMeasurable4.setup(n-1);
                    long start = System.nanoTime();
                    oMeasurable4.execute();
                    long end = System.nanoTime();
                    return (double) (end - start);
                };

                Future<Double> testFuture1 = executor.submit(testTask1);
                Future<Double> testFuture2 = executor.submit(testTask2);
                Future<Double> testFuture3 = executor.submit(testTask3);
                Future<Double> testFuture4 = executor.submit(testTask4);

                if (timer.isTimedOut()) {
                    break;
                }

                nValues.add(n);
                try {
                    runTimes.add((testFuture1.get() + testFuture2.get() + testFuture3.get() + testFuture4.get())/4);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // could be outofmemory
                    break;
                }

                if (runTimes.size() < 4) {
                    ratios.add(Double.NaN);
                    continue;
                }
                
                ratios.add(runTimes.get(runTimes.size()-1)/runTimes.get(runTimes.size()-2));
        
                
                if (ratios.get(ratios.size()-1) < 1 && runTimes.get(runTimes.size()-2)/runTimes.get(runTimes.size()-3) < 1) {// decreasing despite doubling n
                    System.out.printf("error: %.2f, runtime less: %.2f, runtime more: %.2f", ratios.get(ratios.size()-1), runTimes.get(runTimes.size()-2), runTimes.get(runTimes.size()-1));
                    throw new IllegalArgumentException();
                }
        
                System.out.println(ratios.get(ratios.size()-1));
                
                if (runTimes.size() < 6) {
                    continue;
                }
                //double powerMSE = calculateSlope(nValues, runTimes, slopes);
                
                while (ratios.remove(Double.NaN)){};
                /*double zScore = 1;
                double meanRuntime = runTimes.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);
                double std = Math.sqrt(runTimes.stream().map(i -> Math.pow(i-meanRuntime, 2)).mapToDouble(Double::doubleValue).sum()/(n-1));
                if (Math.abs(meanRuntime - runTimes.get(0)) > std * zScore) {
                    nValues.remove(0);
                    runTimes.remove(0);
                    ratios.remove(0);
                    //slopes.remove(0);
                }

                double ratioMSE = IntStream.range(1, runTimes.size())
                .mapToObj(i -> Math.pow(runTimes.get(i-1)*ratios.get(ratios.size()-1) - runTimes.get(i), 2))
                .mapToDouble(Double::doubleValue).sum()/(n-1);

                if (ratioMSE > powerMSE) {
                    ratios.remove(ratios.size()-1);
                    ratios.add(slopes.get(slopes.size()-1));
                }*/
            }

        }
        finally {
            executor.shutdown();
        }
        double ratio = ratios.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);
        double meanRuntime = runTimes.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);
        double std = Math.sqrt(runTimes.stream().map(i -> Math.pow(i-meanRuntime, 2)).mapToDouble(Double::doubleValue).sum()/(runTimes.size()-1));
        if (runTimes.size() < 4 || std/meanRuntime > 4) {
            System.out.printf("Size is %d, and rsd is %.2f", runTimes.size(), std/meanRuntime);
            return Double.NaN;
        }
        long endTime = System.nanoTime();
        
        System.out.printf("Size is %d, and rsd is %.2f", runTimes.size(), std/meanRuntime);
            
        //System.out.printf("ratio: %.2f, slope: %.2f", ratios.get(ratios.size()-1), slopes.get(slopes.size()-1));
        System.out.println("ratios");
        ratios.stream().forEach(element -> System.out.print(element + "\t"));
        System.out.println("\nslopes");
        slopes.stream().forEach(element -> System.out.print(element + "\t"));
        System.out.printf("n: %d", nValues.get(nValues.size()-1));
        System.out.printf("\n ratio avg: %.2f", ratio);
        System.out.printf("time: %.2f", (double) (endTime-startTime));
        return ratio;
        //return Math.pow(2, slopes.get(slopes.size()-1));
    }

    private double calculateSlope(List<Integer> nValues, List<Double> runTimes, List<Double> slopes) {
        List<Integer> x = nValues.stream().skip(4).collect(Collectors.toList());
        List<Double> y = runTimes.stream().skip(4).collect(Collectors.toList());
        
        List<Double> logX = x.stream().map(i -> Math.log(i)).collect(Collectors.toList());
        List<Double> logY = y.stream().map(i -> Math.log(i)).collect(Collectors.toList());
        int n = logX.size();
        //Use linear regression with logs of values as trying to fit to power law
        //Formuals from: https://mathworld.wolfram.com/LeastSquaresFittingPowerLaw.html

        double sumN = logX.stream().mapToDouble(Double::doubleValue).sum();
        double sumTimes = logY.stream().mapToDouble(Double::doubleValue).sum();
        double sumNSquared = logX.stream().map(i -> i*i).mapToDouble(Double::doubleValue).sum();
        System.out.printf("nvalue size: %d, runtime size: %d", nValues.size(), runTimes.size());
        double sumNRuntimes = IntStream.range(0, n)
        .mapToObj(i -> logX.get(i)*logY.get(i)).mapToDouble(Double::doubleValue).sum();

        double numerator = (n * sumNRuntimes) - (sumN * sumTimes);
        double denominator = (n * sumNSquared) - (sumN*sumN);

        if (denominator == 0) {
            return Double.NaN;
        }

        double power = numerator / denominator;

        slopes.add(Math.pow(2, power));

        double multiplicativeConstant = (sumTimes - (power * sumN)) / n;

        List<Double> predRuntimes = x.stream().map(i -> multiplicativeConstant * Math.pow(i, power)).collect(Collectors.toList());

        List<Double> rss = IntStream.range(0, n)
        .mapToObj(i -> Math.pow(y.get(i) - predRuntimes.get(i), 2))
        .collect(Collectors.toList());

        double mse = rss.stream().mapToDouble(Double::doubleValue).sum()/n;
        
        return mse;
    }
}