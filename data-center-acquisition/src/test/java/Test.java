//import com.data.center.AcquisitionApplication;
//import com.data.center.pojo.Do.LoadingTable;
//import com.data.center.pojo.Do.LogisticsInformation;
//import com.data.center.pojo.Do.TabulateData;
//import com.data.center.pojo.Do.UnloadingTable;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.util.*;
//import java.util.concurrent.*;
//
//@SpringBootTest(classes = AcquisitionApplication.class)
//public class Test {

//    @Autowired
//    private CustomerInformationServiceImpl customerInformationService;
//    @Autowired
//    private LoadingTableServiceImpl loadingTableService;
//    @Autowired
//    private UnloadingTableServiceImpl unloadingTableService;
//    @Autowired
//    private LogisticsInformationServiceImpl logisticsInformationService;


//    @org.junit.jupiter.api.Test
//    public void test() throws ClassNotFoundException, IllegalAccessException, InstantiationException, ExecutionException, InterruptedException, SQLException, IOException, ParseException {
//        System.out.println(String.valueOf(UUID.randomUUID()));
//
//    }


    /**
     * 数据统计脚本
     */
//    Map<String, Integer> portGoodsYearMonth_throughput = new HashMap<>();

//    @org.junit.jupiter.api.Test
//    public void test1(){
//        List<LoadingTable> listA = loadingTableService.getBaseMapper().selectList(null);
//        List<UnloadingTable> listB = unloadingTableService.getBaseMapper().selectList(null);
//        List<LogisticsInformation> list = logisticsInformationService.getBaseMapper().selectList(null);
//        Map<String, LogisticsInformation> map = new HashMap<>();
//        list.stream().forEach(
//                logisticsInformation -> {
//                    map.put(logisticsInformation.getLogisticsId(), logisticsInformation);
//                });
//
//        List<TabulateData> result = new ArrayList<>();
//        listA.stream().forEach(a -> {
//            LogisticsInformation log = map.get(a.getLogisticsId());
//            String port = a.getPort();
//            String goods = log.getGoodsName();
//            int year = a.getDepartureTime().getYear() + 1900;
//            int month = a.getDepartureTime().getMonth() + 1;
//            int weight = log.getWeight();
//            portGoodsYearMonthThroughput(port, goods, year, month, weight);
//        });
//
//        listB.stream().forEach(b -> {
//            LogisticsInformation log = map.get(b.getLogisticsId());
//            String port = b.getPort();
//            String goods = log.getGoodsName();
//            int year = b.getDepartureTime().getYear() + 1900;
//            int month = b.getDepartureTime().getMonth() + 1;
//            int weight = log.getWeight();
//            portGoodsYearMonthThroughput(port, goods, year, month, weight);
//        });
//
//        portGoodsYearMonth_throughput.forEach((key, value) -> {
//            String[] split = key.split("_");
//            TabulateData tabulateData = new TabulateData();
//            tabulateData.setPort(split[0]);
//            tabulateData.setGood(split[1]);
//            tabulateData.setYear(Integer.parseInt(split[2]));
//            tabulateData.setMonth(Integer.parseInt(split[3]));
//            tabulateData.setThroughPut(value);
//            result.add(tabulateData);
//        });
//        result.sort((o1, o2) -> {
//            if (o1.getPort().equals(o2.getPort())){
//                if (Objects.equals(o1.getGood(), o2.getGood())){
//                    if (o1.getYear() == o2.getYear()){
//                        return o1.getMonth() - o2.getMonth();
//                    }else {
//                        return o1.getYear() - o2.getYear();
//                    }
//                }else {
//                    return o1.getGood().compareTo(o2.getGood());
//                }
//            }else {
//                return o1.getPort().compareTo(o2.getPort());
//            }
//        });
////        result.forEach(System.out::println);
//
//        try {
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(Files.newOutputStream(Paths.get("D:/tabulateData.csv")), StandardCharsets.UTF_8));
//            CSVFormat csvFormat = CSVFormat.EXCEL.withHeader("人员ID","人员姓名","年龄");
//            CSVPrinter printer = csvFormat.print(writer);
//            for (int i = 0; i < result.size(); i++) {
//                TabulateData tabulateData = result.get(i);
//                printer.printRecord(tabulateData.getPort(), tabulateData.getGood(), tabulateData.getYear(), tabulateData.getMonth(), tabulateData.getThroughPut());            }
//            printer.flush();
//            printer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void portGoodsYearMonthThroughput(String port, String goods, int year, int month, int weight){
//        String key = keyPortGoodsYearMonth(port, goods, year, month);
//        if (portGoodsYearMonth_throughput.containsKey(keyPortGoodsYearMonth(port, goods, year, month))){
//            portGoodsYearMonth_throughput.replace(key, portGoodsYearMonth_throughput.get(key) + weight);
//        }else {
//            portGoodsYearMonth_throughput.put(key, weight);
//        }
//    }
//    private String keyPortGoodsYearMonth(String port, String goods, int year, int month){
//        return port + "_" + goods + "_" + year + "_" + monthIntToString(month);
//    }
//    private String monthIntToString(int month){
//        if (month > 9) return String.valueOf(month);
//        else return "0" + month;
//    }


//}
