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
        double startTime = System.currentTimeMillis();
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

        double endTime = System.currentTimeMillis();
        double executionTime = endTime - startTime;

        System.out.println("Кол-во потоков - " + numThreads);
        System.out.println("Количество элементов в TreeSet: " + allNumbers.size());
        System.out.println("Время выполнения: " + executionTime/1000 + " секунд");
        System.out.println(allNumbers.first());
        System.out.println(allNumbers.last());

    }

    private static void someProcess(int start, int end) {
        int i = start;
        while (i < end) {
            allNumbers.add(i);
            System.out.println("Added " + i);
            i++;
        }
    }
}
