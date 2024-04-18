package com.data.center.feignClient;

import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "analysis-server", path = "/analysis")
public interface DataAnalysisFeignClient {

    @PostMapping(value = "/receive/data/CustomerInformation", produces = "application/json")
    Boolean sendDataCustomerInformation(@RequestBody List<CustomerInformation> list);

    @PostMapping(value = "/receive/data/LogisticsInformation", produces = "application/json")
    Boolean sendDataLogisticsInformation(@RequestBody List<LogisticsInformation> list);

    @PostMapping(value = "/receive/data/LoadingTable", produces = "application/json")
    Boolean sendDataLoadingTable(@RequestBody List<LoadingTable> list);

    @PostMapping(value = "/receive/data/UnloadingTable", produces = "application/json")
    Boolean sendDataUnloadingTable(@RequestBody List<UnloadingTable> list);

}
