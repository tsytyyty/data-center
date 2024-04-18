package com.data.center.utils;

import com.data.center.feignClient.DataAnalysisFeignClient;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;

@Component
public class AsyncSender {

    @Autowired
    private DataAnalysisFeignClient feignClient;

    @Async
    public Future<Boolean> sendCus(List<CustomerInformation> list) {
        Boolean flag = feignClient.sendDataCustomerInformation(list);
        return new AsyncResult<>(flag);
    }

    @Async
    public Future<Boolean> sendLog(List<LogisticsInformation> list) {
        Boolean flag = feignClient.sendDataLogisticsInformation(list);
        return new AsyncResult<>(flag);
    }

    @Async
    public Future<Boolean> sendUnload(List<UnloadingTable> list) {
        Boolean flag = feignClient.sendDataUnloadingTable(list);
        return new AsyncResult<>(flag);
    }

    @Async
    public Future<Boolean> sendLoad(List<LoadingTable> list) {
        Boolean flag = feignClient.sendDataLoadingTable(list);
        return new AsyncResult<>(flag);
    }

}
