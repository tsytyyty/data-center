package com.data.center.controller;

import com.data.center.contant.RedisConstant;
import com.data.center.data.AfterCleanData;
import com.data.center.data.OriginalData;
import com.data.center.pojo.result.Result;
import com.data.center.service.DataCleanService;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class DataCleanController implements AfterCleanData, OriginalData, RedisConstant {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private DataCleanService dataCleanService;

    @GetMapping("/data/clean")
    public Result dataClean() {
        boolean getLock = false;
        //加锁
        try {
            getLock = redissonClient.getLock(LOCK_TRANSMIT_CLEAN).tryLock(10 , 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (getLock){
            try {
                dataCleanService.dataClean();
            } catch (ExecutionException | InterruptedException e) {
                log.error(e.getMessage() + e);
                return new Result(500, "数据清洗失败", null);
            } finally {
                //解锁
                redissonClient.getLock("data_transmit_and_clean").unlock();
            }
            return new Result(0, "数据清洗成功", null);
        }else {
            return Result.error("数据清洗失败，获取锁失败！");
        }
    }

}
