import java.net.*;
import java.io.*;
import java.util.*;

public class a2_James {
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int[] numbers = new int[n];

        // Generate random numbers and display them
        for (int i = 0; i < n; i++) {
            numbers[i] = (int) (Math.random() * 101);
        }
        System.out.println("Generated numbers: " + Arrays.toString(numbers));

        // Creates maxThread 
        MaxCalculator maxCalculator = new MaxCalculator(numbers);
        Thread maxThread = new Thread(maxCalculator);

        // Creates minThread 
        MinCalculator minCalculator = new MinCalculator(numbers);
        Thread minThread = new Thread(minCalculator);

        // Creates meanThread 
        MeanCalculator meanCalculator = new MeanCalculator(numbers);
        Thread meanThread = new Thread(meanCalculator);

        // Creates medianThread 
        MedianCalculator medianCalculator = new MedianCalculator(numbers);
        Thread medianThread = new Thread(medianCalculator);

        // Creates sdThread 
        StandardDeviationCalculator standardDeviationCalculator = new StandardDeviationCalculator(numbers);
        Thread sdThread = new Thread(standardDeviationCalculator);

        // Start worker threads
        maxThread.start();
        minThread.start();
        meanThread.start();
        medianThread.start();
        sdThread.start();

        try {
            // Wait for all worker threads to complete
            maxThread.join();
            minThread.join();
            meanThread.join();
            medianThread.join();
            sdThread.join();

            // Get results from each worker thread and display them
            System.out.println("Maximum value: " + maxCalculator.getMax());
            System.out.println("Minimum value: " + minCalculator.getMin());
            System.out.println("Mean value: " + meanCalculator.getMean());
            System.out.println("Median value: " + medianCalculator.getMedian());
            System.out.println("Standard deviation: " + standardDeviationCalculator.getStandardDeviation());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MaxCalculator implements Runnable {
    private final int[] numbers;
    private int max = Integer.MIN_VALUE;

    public MaxCalculator(int[] numbers) {
        this.numbers = numbers;
    }

    public int getMax() {
        return max;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("MaxCalculator");
        for (int number : numbers) {
            if (number > max) {
                max = number;
            }
        }
        System.out.println("Maximum value calculated by " + Thread.currentThread().getName());
    }
}

class MinCalculator implements Runnable {
    private final int[] numbers;
    private int min = Integer.MAX_VALUE;

    public MinCalculator(int[] numbers) {
        this.numbers = numbers;
    }

    public int getMin() {
        return min;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("MinCalculator");
        for (int number : numbers) {
            if (number < min) {
                min = number;
            }
        }
        System.out.println("Minimum value calculated by " + Thread.currentThread().getName());
    }
}

class MeanCalculator implements Runnable {
    private final int[] numbers;
    private double mean = 0.0;

    public MeanCalculator(int[] numbers) {
        this.numbers = numbers;
    }

    public double getMean() {
        return mean;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("MeanCalculator");
        for (int number : numbers) {
            mean += number;
        }
        mean /= numbers.length;
        System.out.println("Mean value calculated by " + Thread.currentThread().getName());
    }
}

class MedianCalculator implements Runnable {
    private final int[] numbers;
    private double median = 0.0;

    public MedianCalculator(int[] numbers) {
        this.numbers = numbers;
    }

    public double getMedian() {
        return median;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("MedianCalculator");
        Arrays.sort(numbers);
        int length = numbers.length;
        int mid = length/2;
        if (length % 2 == 0) {
            median = (numbers[mid-1] + numbers[mid]) / 2.0;
        } else {
            median = numbers[mid];
        }
        System.out.println("Median value calculated by " + Thread.currentThread().getName());
    }
}

class StandardDeviationCalculator implements Runnable {
    private final int[] numbers;
    private double sd = 0;

    public StandardDeviationCalculator(int[] numbers) {
        this.numbers = numbers;
    }

    public double getStandardDeviation() {
        return sd;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("StandardDeviationCalculator");
        double sumSquaredDiff = 0.0;
        MeanCalculator meanCalculator = new MeanCalculator(numbers);
        meanCalculator.run();
        double mean = meanCalculator.getMean();
        for (int i = 0; i < numbers.length; i++) {
            sumSquaredDiff += Math.pow(numbers[i] - mean, 2);
        }

        sd = Math.sqrt(sumSquaredDiff / numbers.length);

        System.out.println("StandardDeviation value calculated by " + Thread.currentThread().getName());
    }
}