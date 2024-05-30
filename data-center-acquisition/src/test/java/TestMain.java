//import com.alibaba.fastjson.JSON;
//import com.data.center.pojo.Do.DataSourceDo;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class TestMain {
//    public static void main(String[] args) {
//        DataSourceDo d = new DataSourceDo();
//        d.setUrl("http://127.0.0.1:9090");
//        d.setUsername("admin");
//        d.setPassword("admin1234");
//        d.setBucketName("test");
//        Map<String, String> dataAndType = new HashMap<>();
//        dataAndType.put("卸货表2021.txt", "UnloadingTable");
//        dataAndType.put("装货表2021.txt", "LoadingTable");
//        dataAndType.put("客户信息2021.xlsx", "CustomerInformation");
//        dataAndType.put("物流信息2021.txt", "LogisticsInformation");
////        d.setId("c91af1ff5d0a44a28eed3598fddab714");
//        d.setType("minio");
//        d.setDataAndType(dataAndType);
//        System.out.println(JSON.toJSON(d));
//    }
//}
