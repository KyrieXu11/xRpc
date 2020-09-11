package com.kyriexu.client;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author KyrieXu
 * @since 2020/9/9 23:17
 **/
public class PerformanceTest {
    public static void main(String[] args) throws InterruptedException {
        // time = 800 时下面的代码执行是会抛出异常的 ： No buffer space available (maximum connections reached?)
        // Unable to establish loopback connection
        // failed to create a child event loop
        final int time = 800;
        int now = LocalDateTime.now().getSecond();
        CountDownLatch cnt = new CountDownLatch(time);
        ExecutorService executorService = Executors.newFixedThreadPool(time);
        for (int i = 0; i < time; i++) {
            executorService.execute(()->{
                ClientTest.main(null);
                cnt.countDown();
            });
        }
        cnt.await();
        int e = LocalDateTime.now().getSecond();
        System.out.println("time:"+(e - now));
        executorService.shutdown();
    }
}
