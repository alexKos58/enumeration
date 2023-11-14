package ru.kostyushin;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    static SortedSet<Integer> allNumbers = new ConcurrentSkipListSet<>(new TreeSet<>());
    static int numThreads = 10;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int maxNumber = 30_000_000;

        // количество обрабатываемых чисел в одном потоке
        int countNumbersInThread = maxNumber / numThreads;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            int start = i * countNumbersInThread;
            int end = (i + 1) * countNumbersInThread;

            executor.submit(() -> someProcess(start, end));
        }

        executor.shutdown();

        try {
            boolean b = executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Количество элементов в TreeSet: " + allNumbers.size());
        System.out.println("Время выполнения: " + executionTime + " миллисекунд");
        System.out.println(allNumbers.first());
        System.out.println(allNumbers.last());

    }

    private static void someProcess(int start, int end) {
        for (int i = start; i < end; i++) {
            allNumbers.add(i);
            System.out.println("Added " + i);
        }
    }
}
