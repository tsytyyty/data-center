package com.data.center.service.Impl;

import com.data.center.data.AfterAnalysisData;
import com.data.center.data.AfterCleanData;
import com.data.center.pojo.Do.*;
import com.data.center.service.DataAnalysisService;
import com.data.center.service.SynchronizationService;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DataAnalysisServiceImpl implements DataAnalysisService, AfterCleanData, AfterAnalysisData {

    @Autowired
    @Qualifier("cleanExecutor")
    private ThreadPoolTaskExecutor threadPool;

    @Autowired
    private SynchronizationService synchronizationService;

    private Map<String, Integer> portGoodsYearMonth_throughput = new HashMap<>();


    public void dataAnalysis() {
        //遍历装货表
        clean_loading_table.stream().forEach(loadingTable -> {
            String[] goods_weight = logisticsInformationMap.get(loadingTable.getLogisticsId()).split("_");
            String port = loadingTable.getPort();
            String goods = goods_weight[0];
            int year = loadingTable.getDepartureTime().getYear() + 1900;
            int month = loadingTable.getDepartureTime().getMonth() + 1;
            int weight = Integer.parseInt(goods_weight[1]);
            long time = loadingTable.getDepartureTime().getTime() - loadingTable.getWorkBeginTime().getTime();
            portGoodsYearMonthThroughput(port, goods, year, month, weight);
            //各港口货物出口量分析
            portGoods_output_MAP(port, goods, weight);
            //港口货物周转时间分析
            portGoods_flowTime_MAP(port, goods, time);
        });
        clean_loading_table.clear();

        //遍历卸货表
        clean_unloading_table.stream().forEach(unloadingTable -> {
            String[] goods_weight = logisticsInformationMap.get(unloadingTable.getLogisticsId()).split("_");
            String port = unloadingTable.getPort();
            String goods = goods_weight[0];
            int year = unloadingTable.getDepartureTime().getYear() + 1900;
            int month = unloadingTable.getDepartureTime().getMonth() + 1;
            int weight = Integer.parseInt(goods_weight[1]);
            long time = unloadingTable.getWorkEndTime().getTime() - unloadingTable.getArriveTime().getTime();
            portGoodsYearMonthThroughput(port, goods, year, month, weight);
            //各港口货物入口量分析
            portGoods_input_MAP(goods, port, weight);
            //港口货物周转时间分析
            portGoods_flowTime_MAP(port, goods, time);
        });
        clean_unloading_table.clear();

        portGoodsYearMonth_throughput.forEach((key, value) -> {
            String[] split = key.split("_");
            TabulateData t = new TabulateData();
            t.setPort(split[0]);
            t.setGoods(split[1]);
            t.setYear(Integer.parseInt(split[2]));
            t.setMonth(Integer.parseInt(split[3]));
            t.setThroughput(value);
            totalData.add(t);
            //统计
            port_throughput_MAP(t);
            yearPortMonth_throughput_MAP(t);
            portGoods_throughput_MAP(t);
            goods_throughput_MAP(t);
        });
        totalData.sort(Comparator.comparing(TabulateData::getPort).thenComparing(TabulateData::getGoods).thenComparing(TabulateData::getYear).thenComparing(TabulateData::getMonth));

        //多线程数据分析
        analysis();

        //多线程异步同步数据
        synchronizationService.synchronizationAfterAnalysis();
    }

    private void portGoodsYearMonthThroughput(String port, String goods, int year, int month, int weight) {
        String key = keyPortGoodsYearMonth(port, goods, year, month);
        if (portGoodsYearMonth_throughput.containsKey(keyPortGoodsYearMonth(port, goods, year, month))) {
            portGoodsYearMonth_throughput.replace(key, portGoodsYearMonth_throughput.get(key) + weight);
        } else {
            portGoodsYearMonth_throughput.put(key, weight);
        }
    }
    private String keyPortGoodsYearMonth(String port, String goods, int year, int month) {
        return port + "_" + goods + "_" + year + "_" + monthIntToString(month);
    }
    private String monthIntToString(int month) {
        if (month > 9) return String.valueOf(month);
        else return "0" + month;
    }


    /**
     * 数据分析
     */
    private void analysis() {
        List<Future<?>> futures = new ArrayList<>();
        futures.add(threadPool.submit(goods_throughput()));
        futures.add(threadPool.submit(yoyQoqEveryYear()));
        futures.add(threadPool.submit(portGoodsThroughput()));
        futures.add(threadPool.submit(portGoodsInput()));
        futures.add(threadPool.submit(portGoodsOutput()));
        futures.add(threadPool.submit(portGoodsFlowTime()));

        //等待所有执行完毕
        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 港口总吞吐量计算
     */
    private Map<String, Integer> port_throughput = new HashMap<>();
    private void port_throughput_MAP(TabulateData tabulateData) {
        String key = tabulateData.getPort();
        if (port_throughput.containsKey(key)) {
            port_throughput.replace(key, port_throughput.get(key) + tabulateData.getThroughput());
        } else {
            port_throughput.put(key, tabulateData.getThroughput());
        }
    }
    private void port_throughput() {
        AtomicLong total = new AtomicLong(0L);
        port_throughput.forEach((port, throughput) -> {
            PortThroughput p = new PortThroughput();
            p.setPort(port);
            p.setThroughput(throughput);
            total.addAndGet(throughput);
            portThroughputList.add(p);
        });
        portThroughputList.forEach(p -> {
            p.setPercentage(new BigDecimal(p.getThroughput())
                    .divide(new BigDecimal(total.get()), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
        });
    }


    /**
     * 货物总吞吐量分析
     */
    private Map<String, Integer> goods_throughput = new HashMap<>();
    private void goods_throughput_MAP(TabulateData tabulateData) {
        String key = tabulateData.getGoods();
        if (goods_throughput.containsKey(key)) {
            goods_throughput.replace(key, goods_throughput.get(key) + tabulateData.getThroughput());
        } else {
            goods_throughput.put(key, tabulateData.getThroughput());
        }
    }
    private Runnable goods_throughput() {
        return () -> {
            AtomicLong total = new AtomicLong(0L);
            goods_throughput.forEach((goods, throughput) -> {
                total.addAndGet(throughput);
                goodsThroughputList.add(new GoodsThroughput(0, goods, throughput, null));
            });
            goods_throughput.clear();
            goodsThroughputList.forEach(g -> {
                g.setPercentage(new BigDecimal(g.getThroughput())
                        .divide(new BigDecimal(total.get()), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            });
        };
    }


    /**
     * 全年各港口的同比环比
     */
    private Map<String, Integer> yearPortMonth_throughput = new HashMap<>();
    private void yearPortMonth_throughput_MAP(TabulateData tabulateData) {
        String key = keyYearPortMonth(tabulateData.getYear(), tabulateData.getPort(), tabulateData.getMonth());
        if (yearPortMonth_throughput.containsKey(key)) {
            yearPortMonth_throughput.replace(key, yearPortMonth_throughput.get(key) + tabulateData.getThroughput());
        } else {
            yearPortMonth_throughput.put(key, tabulateData.getThroughput());
        }
    }
    private String keyYearPortMonth(int year, String port, int month) {
        return year + "_" + port + "_" + monthIntToString(month);
    }
    public Runnable yoyQoqEveryYear() {
        return () -> {
            // 遍历yearPortMonth_throughput中的每条记录
            yearPortMonth_throughput.forEach((yearPortMonth, throughput) -> {
                // 分解yearPortMonth字符串，获取年、月、港口信息
                String[] split = yearPortMonth.split("_");
                int year = Integer.parseInt(split[0]);
                String port = split[1];
                int month = Integer.parseInt(split[2]);

                // 计算同比（YoY）增长率
                double yoy = 0;
                // 如果前一年同月的数据存在，则计算同比
                if (yearPortMonth_throughput.containsKey(keyYearPortMonth(year - 1, port, month))) {
                    yoy = (double) (yearPortMonth_throughput.get(yearPortMonth) - yearPortMonth_throughput.get(keyYearPortMonth(year - 1, port, month)))
                            / yearPortMonth_throughput.get(keyYearPortMonth(year - 1, port, month)) * 100;
                }

                // 计算环比（QoQ）增长率
                double qoq = 0;
                // 如果是1月，并且前一年12月的数据存在，则计算环比
                if (month == 1 && yearPortMonth_throughput.containsKey(keyYearPortMonth(year - 1, port, 12))) {
                    qoq = (double) (yearPortMonth_throughput.get(yearPortMonth) - yearPortMonth_throughput.get(keyYearPortMonth(year - 1, port, 12)))
                            / yearPortMonth_throughput.get(keyYearPortMonth(year - 1, port, 12)) * 100;
                }
                // 如果前一月的数据存在，则计算环比
                else if (yearPortMonth_throughput.containsKey(keyYearPortMonth(year, port, month - 1))) {
                    qoq = (double) (yearPortMonth_throughput.get(yearPortMonth) - yearPortMonth_throughput.get(keyYearPortMonth(year, port, month - 1)))
                            / yearPortMonth_throughput.get(keyYearPortMonth(year, port, month - 1)) * 100;
                }
                //过滤极端数据
                if(qoq > 1000 || qoq < -1000){
                    qoq = 0;
                }
                if(yoy > 1000 || yoy < -1000){
                    yoy = 0;
                }
                // 将计算结果添加到portYoyQoqList列表中
                portYoyQoqList.add(new PortYoyQoq(0, port, year, month, yearPortMonth_throughput.get(yearPortMonth),
                        new BigDecimal(yoy).setScale(2, RoundingMode.HALF_UP),
                        new BigDecimal(qoq).setScale(2, RoundingMode.HALF_UP)));
            });
            // 清空yearPortMonth_throughput映射，准备下一次计算
            yearPortMonth_throughput.clear();
            // 对portYoyQoqList列表按照港口、年份、月份进行排序
            portYoyQoqList.sort(Comparator.comparing(PortYoyQoq::getPort).thenComparing(PortYoyQoq::getYear).thenComparing(PortYoyQoq::getMonth));
        };
    }


    /**
     * 港口货物吞吐量分析
     */
    private Map<String, Integer> portGoods_throughput = new HashMap<>();

    private void portGoods_throughput_MAP(TabulateData tabulateData) {
        String key = keyPortGoods(tabulateData.getPort(), tabulateData.getGoods());
        if (portGoods_throughput.containsKey(key)) {
            portGoods_throughput.replace(key, portGoods_throughput.get(key) + tabulateData.getThroughput());
        } else {
            portGoods_throughput.put(key, tabulateData.getThroughput());
        }
    }
    private String keyPortGoods(String port, String goods) {
        return port + "_" + goods;
    }
    private Runnable portGoodsThroughput() {
        return () -> {
            port_throughput();
            portGoods_throughput.forEach((key, throughput) -> {
                String[] split = key.split("_");
                String port = split[0];
                String goods = split[1];
                BigDecimal percentage = new BigDecimal(throughput)
                        .divide(new BigDecimal(port_throughput.get(port)), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                portGoodsThroughputList.add(new PortGoodsThroughput(0, port, goods, throughput, percentage));
            });
            portGoods_throughput.clear();
            port_throughput.clear();
            portGoodsThroughputList.sort(Comparator.comparing(PortGoodsThroughput::getPort).thenComparing(PortGoodsThroughput::getGoods));
        };
    }


    /**
     * 港口货物入口量分析
     */
    private Map<String, Integer> portGoods_input = new HashMap<>();
    private void portGoods_input_MAP(String goods, String port, int num) {
        String key = keyPortGoods(port, goods);
        if (portGoods_input.containsKey(key)) {
            portGoods_input.replace(key, portGoods_input.get(key) + num);
        } else {
            portGoods_input.put(key, num);
        }
    }
    private Runnable portGoodsInput() {
        return () -> {
            //计算各个港口的入口总量
            Map<String, Integer> portInputTotal = new HashMap<>();
            portGoods_input.forEach((key, throughput) -> {
                String[] split = key.split("_");
                String goods = split[1];
                String port = split[0];
                if (portInputTotal.containsKey(port)) {
                    portInputTotal.replace(port, portInputTotal.get(port) + throughput);
                } else {
                    portInputTotal.put(port, throughput);
                }
                portGoodsInputList.add(new PortGoodsInput(0, port, goods, throughput, null));
            });
            portGoods_input.clear();
            portGoodsInputList.forEach(portGoodsInput -> {
                portGoodsInput.setPercentage(new BigDecimal(portGoodsInput.getInput())
                        .divide(new BigDecimal(portInputTotal.get(portGoodsInput.getPort())), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            });
            portGoodsInputList.sort(Comparator.comparing(PortGoodsInput::getPort).thenComparing(PortGoodsInput::getGoods));
        };
    }


    /**
     * 港口货物出口量分析
     */
    private Map<String, Integer> portGoods_output = new HashMap<>();
    private void portGoods_output_MAP(String port, String goods, int num) {
        String key = keyPortGoods(port, goods);
        if (portGoods_output.containsKey(key)) {
            portGoods_output.replace(key, portGoods_output.get(key) + num);
        } else {
            portGoods_output.put(key, num);
        }
    }
    private Runnable portGoodsOutput() {
        return () -> {
            Map<String, Integer> portOutputTotal = new HashMap<>();
            portGoods_output.forEach((key, output) -> {
                String[] split = key.split("_");
                String goods = split[1];
                String port = split[0];
                //计算计算各个港口的出口总量
                if (portOutputTotal.containsKey(port)) {
                    portOutputTotal.replace(port, portOutputTotal.get(port) + output);
                } else {
                    portOutputTotal.put(port, output);
                }
                //构建对象添加到list中
                portGoodsOutputList.add(new PortGoodsOutput(0, port, goods, output, null));
            });
            portGoods_output.clear();
            //计算百分比完善数据
            portGoodsOutputList.forEach(portGoodsOutput -> {
                portGoodsOutput.setPercentage(new BigDecimal(portGoodsOutput.getOutput())
                        .divide(new BigDecimal(portOutputTotal.get(portGoodsOutput.getPort())), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            });
            portGoodsOutputList.sort(Comparator.comparing(PortGoodsOutput::getPort).thenComparing(PortGoodsOutput::getGoods));
        };
    }


    /**
     * 各港口货物的平均流转时间分析
     */
    private Map<String, List<Long>> portGoods_flowTime = new HashMap<>();
    private void portGoods_flowTime_MAP(String port, String goods, long time) {
        String key = keyPortGoods(port, goods);
        if (portGoods_flowTime.containsKey(key)) {
            portGoods_flowTime.get(key).add(time + 3600000 * 24);
        } else {
            List<Long> list = new ArrayList<>();
            list.add(time);
            portGoods_flowTime.put(key, list);
        }
    }
    private Runnable portGoodsFlowTime() {
        return () -> {
            portGoods_flowTime.forEach((key, list) -> {
                String[] split = key.split("_");
                String port = split[0];
                String goods = split[1];
                long total = 0;
                //总时间（小时）
                for (long time : list){
                    total += time;
                }
                //平均时间（毫秒）
                double average = (double) total / list.size();
                //平均小时数
                BigDecimal averageHour = new BigDecimal(average / 3600000).setScale(2, RoundingMode.HALF_UP);
                portGoodsFlowTimeList.add(new PortGoodsFlowTime(0, port, goods, averageHour));
            });
            portGoods_flowTime.clear();
        };
    }


}
