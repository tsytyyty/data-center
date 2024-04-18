package com.data.center.controller;

import com.data.center.contant.RedisConstant;
import com.data.center.data.AfterCleanData;
import com.data.center.data.OriginalData;
import com.data.center.pojo.result.Result;
import com.data.center.service.DataCleanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@Tag(name = "数据清洗")
public class DataCleanController implements AfterCleanData, OriginalData, RedisConstant {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private DataCleanService dataCleanService;

    @GetMapping("/data/clean")
    @Operation(summary = "数据清洗api")
    public Result dataClean(){
        //加锁
        redissonClient.getLock(LOCK_TRANSMIT_CLEAN).lock();
        try {
            dataCleanService.dataClean();
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage() + e);
            return new Result(500, "数据清洗失败", null);
        } finally {
            //解锁
            redissonClient.getLock("data_transmit_and_clean").unlock();
        }
        return new Result(200, "数据清洗成功", null);
    }

}
