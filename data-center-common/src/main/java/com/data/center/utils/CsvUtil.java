package com.data.center.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import org.apache.commons.csv.CSVFormat;
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

    public static <T> List<T> loadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
        List<LoadingTable> list = new ArrayList<>();
        S3ObjectInputStream inputStream = client.getObject(bucketName, fileName).getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FileUtil.getFileCode(inputStream)));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        //舍去第一行
        records.iterator().next();
        for (CSVRecord record : records) {
            LoadingTable loadingTable = new LoadingTable();
            loadingTable.setShipCompanies(record.get(0));
            loadingTable.setShipName(record.get(1));
            loadingTable.setWorkBeginTime(df.parse(record.get(2)));
            loadingTable.setWorkEndTime(df.parse(record.get(3)));
            loadingTable.setDepartureTime(df.parse(record.get(4)));
            loadingTable.setArriveTime(df.parse(record.get(5)));
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
        return (List<T>) list;
    }

    public static <T> List<T> unloadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
        List<UnloadingTable> list = new ArrayList<>();
        S3ObjectInputStream inputStream = client.getObject(bucketName, fileName).getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FileUtil.getFileCode(inputStream)));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        //舍去第一行
        records.iterator().next();
        for (CSVRecord record : records) {
            UnloadingTable unloadingTable = new UnloadingTable();
            unloadingTable.setShipCompanies(record.get(0));
            unloadingTable.setShipName(record.get(1));
            unloadingTable.setWorkBeginTime(df.parse(record.get(2)));
            unloadingTable.setWorkEndTime(df.parse(record.get(3)));
            unloadingTable.setDepartureTime(df.parse(record.get(4)));
            unloadingTable.setArriveTime(df.parse(record.get(5)));
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
        return (List<T>) list;
    }

    public static <T> List<T> customerInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
        List<CustomerInformation> list = new ArrayList<>();
        S3ObjectInputStream inputStream = client.getObject(bucketName, fileName).getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FileUtil.getFileCode(inputStream)));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
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
        return (List<T>) list;
    }

    public static <T> List<T> logisticsInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
        List<LogisticsInformation> list = new ArrayList<>();
        S3ObjectInputStream inputStream = client.getObject(bucketName, fileName).getObjectContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FileUtil.getFileCode(inputStream)));
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
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
        return (List<T>) list;

    }

}
