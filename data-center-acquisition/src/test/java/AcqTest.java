import com.alibaba.fastjson.JSON;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.data.center.AcquisitionApplication;
import com.data.center.controller.DataSourceController;
import com.data.center.factory.DataSourceFactory;
import com.data.center.factory.MinioDataSource;
import com.data.center.pojo.Do.DataSourceDo;
import com.data.center.pojo.dto.DataSourceDto;
import com.data.center.pojo.result.Result;
import com.data.center.service.DataAcquisitionService;
import com.data.center.service.DataSourceService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@SpringBootTest(classes = AcquisitionApplication.class)
public class AcqTest {

    @Autowired
    private DataSourceController dataSourceController;
    @Autowired
    private DataSourceService dataSourceService;
    @Test
    public void test() throws Exception {
        DataSourceDo d = new DataSourceDo();
        d.setUrl("http://127.0.0.1:9090");
        d.setUsername("admin");
        d.setPassword("admin1234");
        d.setBucketName("test");
        Map<String, String> dataAndType = new HashMap<>();
        dataAndType.put("客户信息2021.xlsx", "CustomerInformation");
        dataAndType.put("客户信息2020.xlsx", "CustomerInformation");

        dataAndType.put("物流信息2020.xlsx", "LogisticsInformation");
        dataAndType.put("物流信息2021.txt", "LogisticsInformation");
        dataAndType.put("物流信息2022.txt", "LogisticsInformation");

        dataAndType.put("卸货表2020.xlsx", "UnloadingTable");
        dataAndType.put("卸货表2021.txt", "UnloadingTable");
        dataAndType.put("卸货表2022.txt", "UnloadingTable");

        dataAndType.put("装货表2020.xlsx", "LoadingTable");
        dataAndType.put("装货表2021.txt", "LoadingTable");
        dataAndType.put("装货表2022.txt", "LoadingTable");
        d.setId("c91af1ff5d0a44a28eed3598fddab714");
        d.setType("minio");
        d.setDataAndType(dataAndType);
        System.out.println(d);
//        Map<String, Object> map = dataSourceService.updateDataSource(d);
//        System.out.println(map.get("code"));


//        DataSourceDo d = new DataSourceDo();
//        d.setUrl("jdbc:mysql://localhost:3306?useSSL=true&useUnicode=true&cerEncoding=utf8");
//        d.setUsername("root");
//        d.setPassword("123456");
//        d.setDbName("a");
//        d.setType("mysql");
//        d.setName("123");
//        Map<String, String> dataAndType = new HashMap<>();
////        dataAndType.put("customer_information", "CustomerInformation");
////        dataAndType.put("loading_table", "LoadingTable");
//        d.setDataAndType(dataAndType);
//
//        d.setId("402deee5fc3a4edb8dc3e63d4cdce9c3");

//        Result result = dataSourceController.addDataSource(d);
//        System.out.println("================================================");
//        System.out.println(result);
    }



}
