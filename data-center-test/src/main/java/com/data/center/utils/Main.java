package com.data.center.utils;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        Runnable runnable = () -> {
            System.out.println("这是runnable的输出");
        };
        threadPool.execute(runnable);
        Callable<Integer> callable = () -> {
            int a = 1;
            int b = 2;
            System.out.println("这是callable的输出");
            return a + b;
        };
        Future<Integer> submit = threadPool.submit(callable);
        Integer i = submit.get();
        System.out.println("这是callable的计算结果：" + i);
        threadPool.shutdown();
    }
}