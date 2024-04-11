package com.data.center.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析csv、txt格式数据
 */
public class CsvUtil {

    public static <T> List<Object> loadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
        List<Object> list = new ArrayList<>();
        S3ObjectInputStream inputStream = client.getObject(bucketName, fileName).getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FileUtil.getFileCode(inputStream)));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withQuote('\'') // 设置引号字符为单引号
                .withIgnoreEmptyLines(true)
                .withTrim());
        Iterable<CSVRecord> records = csvParser.getRecords();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        //舍去第一行
        records.iterator().next();
        for (CSVRecord record : records) {
            LoadingTable loadingTable = new LoadingTable();
            loadingTable.setShipCompanies(record.get(0));
            loadingTable.setShipName(record.get(1));
            loadingTable.setWorkBeginTime(df.parse(record.get(2).length() == 10 ? record.get(2) + " 00:00:00" : record.get(2)));
            loadingTable.setWorkEndTime(df.parse(record.get(3).length() == 10 ? record.get(3) + " 00:00:00" : record.get(3)));
            loadingTable.setDepartureTime(df.parse(record.get(4).length() == 10 ? record.get(4) + " 00:00:00" : record.get(4)));
            loadingTable.setArriveTime(df.parse(record.get(5).length() == 10 ? record.get(5) + " 00:00:00" : record.get(5)));
            loadingTable.setPort(record.get(6));
            loadingTable.setLogisticsId(record.get(7));
            loadingTable.setContainerId(record.get(8));
            loadingTable.setContainerSize(Integer.parseInt(record.get(9)));
            loadingTable.setDeparturePlace(record.get(10));
            loadingTable.setDestinationPlace(record.get(11));
            list.add(loadingTable);
        }
        inputStream.close();
        reader.close();
        return list;
    }

    public static <T> List<Object> unloadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
        List<Object> list = new ArrayList<>();
        S3ObjectInputStream inputStream = client.getObject(bucketName, fileName).getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FileUtil.getFileCode(inputStream)));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withQuote('\'') // 设置引号字符为单引号
                .withIgnoreEmptyLines(true)
                .withTrim());
        Iterable<CSVRecord> records = csvParser.getRecords();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        //舍去第一行
        records.iterator().next();
        for (CSVRecord record : records) {
            UnloadingTable unloadingTable = new UnloadingTable();
            unloadingTable.setShipCompanies(record.get(0));
            String workBeginTime = record.get(2);
            String workEndTime = record.get(3);
            String departureTime = record.get(4);
            String arriveTime = record.get(5);
            unloadingTable.setWorkBeginTime(df.parse(workBeginTime.length() > 10 ? workBeginTime : workBeginTime + " 00:00:00"));
            unloadingTable.setWorkEndTime(df.parse(workEndTime.length() > 10 ? workEndTime : workEndTime + " 00:00:00"));
            unloadingTable.setDepartureTime(df.parse(departureTime.length() > 10 ? departureTime : departureTime + " 00:00:00"));
            unloadingTable.setArriveTime(df.parse(arriveTime.length() > 10 ? arriveTime : arriveTime + " 00:00:00"));
            unloadingTable.setPort(record.get(6));
            unloadingTable.setLogisticsId(record.get(7));
            unloadingTable.setContainerId(record.get(8));
            unloadingTable.setContainerSize(Integer.parseInt(record.get(9)));
            unloadingTable.setDeparturePlace(record.get(10));
            unloadingTable.setDestinationPlace(record.get(11));
            list.add(unloadingTable);
        }
        inputStream.close();
        reader.close();
        return list;
    }

    public static <T> List<Object> customerInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
        List<Object> list = new ArrayList<>();
        S3ObjectInputStream inputStream = client.getObject(bucketName, fileName).getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FileUtil.getFileCode(inputStream)));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withQuote('\'') // 设置引号字符为单引号
                .withIgnoreEmptyLines(true)
                .withTrim());
        Iterable<CSVRecord> records = csvParser.getRecords();
        //舍去第一行
        records.iterator().next();
        for (CSVRecord record : records) {
            CustomerInformation customerInformation = new CustomerInformation();
            customerInformation.setName(record.get(0));
            customerInformation.setIdCard(record.get(1));
            customerInformation.setPhoneNumber(record.get(2));
            customerInformation.setAddress(record.get(3));
            list.add(customerInformation);
        }
        inputStream.close();
        reader.close();
        return list;
    }

    public static List<Object> logisticsInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
        List<Object> list = new ArrayList<>();
        S3ObjectInputStream inputStream = client.getObject(bucketName, fileName).getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FileUtil.getFileCode(inputStream)));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withQuote('\'') // 设置引号字符为单引号
                .withIgnoreEmptyLines(true)
                .withTrim());
        Iterable<CSVRecord> records = csvParser.getRecords();
        //舍去第一行
        records.iterator().next();
        for (CSVRecord record : records) {
            LogisticsInformation logisticsInformation = new LogisticsInformation();
            logisticsInformation.setLogisticsId(record.get(0));
            logisticsInformation.setOwner(record.get(1));
            logisticsInformation.setOwnerId(record.get(2));
            logisticsInformation.setCompanyId(record.get(3));
            logisticsInformation.setContainerId(record.get(4));
            logisticsInformation.setGoodsName(record.get(5));
            logisticsInformation.setWeight(Integer.parseInt(record.get(6)));
            list.add(logisticsInformation);
        }
        inputStream.close();
        reader.close();
        return list;

    }

}
