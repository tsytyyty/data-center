package com.data.center.service;

public interface SynchronizationService {

    /**
     * 同步清洗后数据到数据库
     */
    void synchronizationAfterClean();

    /**
     * 同步分析后数据到数据库
     */
    void synchronizationAfterAnalysis();


}
