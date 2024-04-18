package com.data.center.controller;

import com.data.center.data.OriginalData;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/receive/data")
public class DataReceiveController implements OriginalData {

    @PostMapping("/CustomerInformation")
    public Boolean receiveDataCustomerInformation(@RequestBody List<CustomerInformation> list) {
        data_customer_information.add(list);
        return true;
    }

    @PostMapping("/LogisticsInformation")
    public Boolean receiveDataLogisticsInformation(@RequestBody List<LogisticsInformation> list) {
        data_logistics_information.add(list);
        return true;
    }

    @PostMapping("/LoadingTable")
    public Boolean receiveDataLoadingTable(@RequestBody List<LoadingTable> list) {
        data_loading_table.add(list);
        return true;
    }

    @PostMapping("/UnloadingTable")
    public Boolean receiveDataUnloadingTable(@RequestBody List<UnloadingTable> list) {
        data_unloading_table.add(list);
        return true;
    }

}
