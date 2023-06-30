package com.commons.java8;

import lombok.SneakyThrows;

import java.util.concurrent.*;

/**
 * Description:
 * CompletableFuture练习
 * Project: commons
 * Author: chish
 * Create Time:2023/6/12 22:45
 */
public class CompletableFuturePractice {


//    @SneakyThrows
//    public static void main(String[] args) {
//        CompletableFuture<String> cf1 = new CompletableFuture<>();
//        Thread newThread = new Thread(() -> {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            cf1.complete("5s已到，完成");
//        });
//        newThread.start();
//        String result = cf1.get();
//        System.out.println(result);
//    }


//    @SneakyThrows
//    public static void main(String[] args) {
//        // 不指定线程池
//        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
//            System.out.println(" 执行cf1 ");
//            return "complete";
//        });
//        String result = cf1.get();
//        System.out.println(result);
//        // 指定线程池
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
//        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
//            System.out.println(" 执行cf2 ");
//            return "complete";
//        }, executor);
//        String result2 = cf2.get();
//        System.out.println(result2);
//    }


    @SneakyThrows
    public static void main(String[] args) {
        CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> {
            System.out.println("执行cf1");
        });
        System.out.println("get result: " + cf1.get());
    }

}