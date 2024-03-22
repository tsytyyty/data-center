package com.data.center.utils;

import org.springframework.scheduling.annotation.Async;

public class ConnectionPoolUtil {

    /**
     * 异步创建连接
     */
    @Async
    public void createConnectionAsync() {
        // 在这里执行异步操作
        System.out.println("Start asyncMethod");
        try {
            // 模拟异步操作耗时
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End asyncMethod");
    }



}
