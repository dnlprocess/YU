package edu.yu.introtoalgs;

import edu.yu.introtoalgs.BigOIt2Base;
import edu.yu.introtoalgs.BigOMeasurable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BigOIt2 extends BigOIt2Base {

    private class Timer {
        private final long timeOutInMs;
        private long startTimeInMs;
        private final AtomicBoolean timedOut = new AtomicBoolean(false);
        private ExecutorService executor;
        private List<Future<Double>> futures = new ArrayList<>();

        public Timer(long timeOutInMs) {
            this.timeOutInMs = timeOutInMs;
        }

        public void start(ExecutorService executor) {
            this.startTimeInMs = System.currentTimeMillis();

            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                //System.out.print("\n\n\n\n");
                //System.out.println(System.currentTimeMillis() - this.startTimeInMs);
                this.timedOut.set(true);
                interruptExecutor();
            }, timeOutInMs, TimeUnit.MILLISECONDS);
        }

        public long timeLeft() {
            return this.timeOutInMs - (System.currentTimeMillis() - this.startTimeInMs);
        }

        public void addFuture(Future<Double> future) {
            futures.add(future);
        }
    
        public void clearFutures() {
            futures.clear();
        }

        public void interruptExecutor() {
            for (Future<?> future : futures) {
                future.cancel(true);
            }
            if (executor != null && !executor.isShutdown()) {
                executor.shutdownNow();
            }
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
        //long startTime = System.nanoTime();
        Timer timer = new Timer(timeOutInMs - 100);
        int availableThreads = Runtime.getRuntime().availableProcessors()-1;
        ExecutorService executor = Executors.newFixedThreadPool(availableThreads);
        timer.start(executor);
        //ExecutorCompletionService<Double> completionService = new ExecutorCompletionService<>(executor);

        List<Integer> nValues = new ArrayList<>();
        List<Double> runTimes = new ArrayList<>();

        List<Double> slopes = new ArrayList<>();
        List<Double> ratios = new ArrayList<>();

        BigOMeasurable oMeasurable1;
        List<BigOMeasurable> oMeasureables = new ArrayList<>();

        try {
            oMeasurable1 = (BigOMeasurable) Class.forName(bigOMeasurable).getConstructor().newInstance();
            for (int i=0; i<availableThreads; i++) {
                oMeasureables.add((BigOMeasurable) Class.forName(bigOMeasurable).getConstructor().newInstance());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        try {
            //warm-up
            for (int i=0; i < 10; i++) {
                oMeasurable1.setup(5);
                oMeasurable1.execute();
            }

            while (!timer.isTimedOut() && !Thread.currentThread().isInterrupted()) {
                timer.clearFutures();
                int n = nValues.isEmpty()? 16: 2 * nValues.get(nValues.size()-1);
                List<Future<Double>> futures = new ArrayList<>();
                
                for (BigOMeasurable oMeasurable: oMeasureables) {
                    Callable<Double> test = createTest(oMeasurable, n);
                    Future<Double> future = executor.submit(test);
                    futures.add(future);
                    timer.addFuture(future);
                }

                List<Double> currRuntimes = new ArrayList<>();
                for (Future<Double> future: futures) {
                    try {
                        currRuntimes.add(future.get());
                    } catch (InterruptedException | ExecutionException | CancellationException e) {
                        //e.printStackTrace();
                        timer.interruptExecutor();
                        currRuntimes.add(0.0);
                    }
                }

                if (timer.isTimedOut()) {
                    break;
                }
                
                
                double avgRuntime = currRuntimes.stream().mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);

                if (avgRuntime == 0.0) {
                    break;
                }
                nValues.add(n);
                runTimes.add(avgRuntime);
                //System.out.printf("n: %d, how many runs: %d, avg: %f, std: %f\n", nValues.get(nValues.size()-1), currRuntimes.size(), avgRuntime/(1_000_000.0), Math.sqrt(currRuntimes.stream().map(i -> Math.pow(i-avgRuntime, 2)).mapToDouble(Double::doubleValue).sum()/(currRuntimes.size()-1))/(1_000_000.0));


                if (nValues.size() < 4) {
                    ratios.add(Double.NaN);
                    continue;
                }
                double ratio = runTimes.get(runTimes.size()-1)/runTimes.get(runTimes.size()-2);
                ratios.add(ratio);
    
                
                if (nValues.size() < 6) {
                    continue;
                }
                /*if (n > 8 && ratios.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) < 0.9) {// decreasing despite doubling n
                    System.out.printf("error: %f, runtime less: %f, runtime more: %f", ratios.get(ratios.size()-1), runTimes.get(runTimes.size()-2), runTimes.get(runTimes.size()-1));
                    System.out.printf("n less: %d, n more: %d", nValues.get(nValues.size()-2), nValues.get(nValues.size()-1));
        
                    throw new IllegalArgumentException();
                }*/
                //double powerMSE = calculateSlope(nValues, runTimes, slopes, ratio);
                    
                while (ratios.remove(Double.NaN)){};

                double meanRatio = ratios.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);
                double std = Math.sqrt(ratios.stream().map(i -> Math.pow(i-meanRatio, 2)).mapToDouble(Double::doubleValue).sum()/(ratios.size()));

                for (int i=0; i<Math.min(10, ratios.size()/2); i++) {
                    if (Math.abs(meanRatio - ratios.get(i)) > std) {
                        //System.out.printf("std: %f", std);
                        nValues.remove(i);
                        runTimes.remove(i);
                        ratios.remove(i);
                        //slopes.remove(0);
                        break;
                    }
                }

                if (nValues.get(nValues.size()-1)>512 && Math.abs(meanRatio - ratio) > std * 2) {
                    ratio = ratios.remove(ratios.size()-1);
                    ratios.add(ratios.size()-1, (2*ratio + (ratio < meanRatio ? 0.1 : -0.1) * Math.abs(ratio - meanRatio))/2);
                }

                /*
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
            executor.shutdownNow();
        }
        double meanRatio = ratios.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);
        double std = Math.sqrt(ratios.stream().map(i -> Math.pow(i-meanRatio, 2)).mapToDouble(Double::doubleValue).sum()/(ratios.size()));
        
        for (int i=0; i<ratios.size(); i++) {
            if (Math.abs(meanRatio - ratios.get(i)) > std * 2) {
                nValues.remove(i);
                runTimes.remove(i);
                ratios.remove(i);
            }
            else if (Math.abs(meanRatio - ratios.get(i)) > std * 1.5) {
                    double currRatio = ratios.remove(i);
                    ratios.add(i, currRatio + (currRatio < meanRatio ? 0.1 : -0.1) * Math.abs(currRatio - meanRatio));
            }
        }
        double ratio = weightedAverage(ratios);
        //System.out.println(std/meanRatio);
        if (nValues.size()<4 || std/meanRatio > 4) {
            //System.out.println(std/meanRatio);
            return Double.NaN;
        }
        //System.out.println("ratios");
        //ratios.stream().forEach(element -> System.out.print(element + "\t"));
        //double ratio = ratios.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        /*double slope = slopes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double meanRatio = ratios.stream().mapToDouble(Double::doubleValue).average().orElse(1.0);
       */ /*double std = Math.sqrt(ratios.stream().map(i -> Math.pow(i-meanRatio, 2)).mapToDouble(Double::doubleValue).sum()/(ratios.size()));
        System.out.printf("Size is %d, and rsd is %f, std: %f, meanRunTime: %f", runTimes.size(), std, std, meanRatio);
        boolean hasConverged = ratios.stream()
        .skip(1) // Skip the first element
        .allMatch(value -> {
            int index = ratios.indexOf(value);
            double previousValue = ratios.get(index - 1);
            double relativeChange = Math.abs((value - previousValue) / value);
            return relativeChange <= (0.1);
        });
        System.out.printf("has converged: %b", hasConverged);
        long endTime = System.nanoTime();

        //System.out.printf("ratio: %f, slope: %f", ratios.get(ratios.size()-1), slopes.get(slopes.size()-1));
        System.out.println("ratios");
        ratios.stream().forEach(element -> System.out.print(element + "\t"));
        System.out.println("\nslopes");
        slopes.stream().forEach(element -> System.out.print(element + "\t"));
        System.out.printf("\n slope avg: %f", slope);
        System.out.printf("n: %d", nValues.get(nValues.size()-1));
        System.out.printf("\n ratio avg: %f", ratio);
        System.out.printf("time: %f", (double) (endTime-startTime));
        System.out.printf("min n: %d", nValues.get(0));*/
        return (2*ratio+meanRatio)/3;
        //return Math.pow(2, slopes.get(slopes.size()-1));
    }

    private double calculateSlope(List<Integer> nValues, List<Double> runTimes, List<Double> slopes, double ratio) {
        List<Integer> x = nValues.stream().skip(4).collect(Collectors.toList());
        List<Double> y = runTimes.stream().skip(4).collect(Collectors.toList());
        
        List<Double> logX = x.stream().map(i -> Math.log(i)).collect(Collectors.toList());
        List<Double> logY = y.stream().map(i -> Math.log(i)).collect(Collectors.toList());
        int n = logX.size();
        //Use linear regression with logs of values as trying to fit to power law
        //Formuals from: https://mathworld.wolfram.com/LeastSquaresFittingPowerLaw.html

        double sumN = logX.stream().mapToDouble(Double::doubleValue).sum();
        double sumTimes = logY.stream().mapToDouble(Double::doubleValue).sum();
        /*
        double sumNSquared = logX.stream().map(i -> i*i).mapToDouble(Double::doubleValue).sum();
        System.out.printf("nvalue size: %d, runtime size: %d", nValues.size(), runTimes.size());
        double sumNRuntimes = IntStream.range(0, n)
        .mapToObj(i -> logX.get(i)*logY.get(i)).mapToDouble(Double::doubleValue).sum();

        double numerator = (n * sumNRuntimes) - (sumN * sumTimes);
        double denominator = (n * sumNSquared) - (sumN*sumN);

        if (denominator == 0) {
            return Double.NaN;
        }
        double power = numerator / denominator;*/
        double multiplicativeConstant = (sumTimes - (ratio * sumN)) / n;
        double power = (sumTimes - (multiplicativeConstant * n))/sumN;

        slopes.add(Math.pow(2, power));


        List<Double> predRuntimes = x.stream().map(i -> multiplicativeConstant * Math.pow(i, power)).collect(Collectors.toList());

        List<Double> rss = IntStream.range(0, n)
        .mapToObj(i -> Math.pow(y.get(i) - predRuntimes.get(i), 2))
        .collect(Collectors.toList());

        double mse = rss.stream().mapToDouble(Double::doubleValue).sum()/n;
        
        return mse;
    }

    public static double weightedAverage(List<Double> values) {
        int n = values.size();
        double weightedSum = 0.0;
        double totalWeight = 0.0;
    
        for (int i = 0; i < n; i++) {
            double weight = (n - i);
            weightedSum += values.get(i) * weight;
            totalWeight += weight;
        }
    
        return weightedSum / totalWeight;
    }

    private Callable<Double> createTest(BigOMeasurable oMeasurable, int n) {
        return () ->
        {
            oMeasurable.setup(n);
            Thread.sleep(0);
            long start = System.nanoTime();
            oMeasurable.execute();
            long end = System.nanoTime();
            return (double) (end - start);
        };
    }
}