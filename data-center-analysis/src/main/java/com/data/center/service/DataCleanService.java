package com.data.center.service;

import java.util.concurrent.ExecutionException;

public interface DataCleanService {

    /**
     * 数据清洗
     */
    void dataClean() throws ExecutionException, InterruptedException;

}
