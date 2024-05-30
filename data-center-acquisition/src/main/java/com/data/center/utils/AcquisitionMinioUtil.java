package com.data.center.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Slf4j
public class AcquisitionMinioUtil <T> {

    public static Map<String, List<Object>> acquisitionToList(String name, AmazonS3 client, String bucketName, Map<String, String> file) {
        // 列出minio文件
        Map<String, List<Object>> map = new HashMap<>();
        List<Object> errorList = new ArrayList<>();
        ObjectListing objectListing = client.listObjects(bucketName);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        Set<String> minioFile = new HashSet<>();
        objectSummaries.forEach(s3ObjectSummary -> {
            minioFile.add(s3ObjectSummary.getKey());
        });

        //遍历数据源信息文件
        file.keySet().forEach(fileName -> {
            if (!minioFile.contains(fileName)) {
                errorList.add("数据源" + name + "中不存在目录文件：" + fileName);
            }
            try {
                switch (file.get(fileName)) {
                    case "LoadingTable":
                        List<Object> loadingTableList = loadingTableData(client, bucketName, fileName);
                        map.put(fileName, loadingTableList);
                        break;
                    case "UnloadingTable":
                        List<Object> unloadingTableList = unloadingTableData(client, bucketName, fileName);
                        map.put(fileName, unloadingTableList);
                        break;
                    case "CustomerInformation":
                        List<Object> customerInformationList = customerInformationData(client, bucketName, fileName);
                        map.put(fileName, customerInformationList);
                        break;
                    case "LogisticsInformation":
                        List<Object> logisticsInformationList = logisticsInformationData(client, bucketName, fileName);
                        map.put(fileName, logisticsInformationList);
                        break;
                }
            } catch (IOException | ParseException e) {
                log.error(e.getMessage());
                errorList.add("数据解析失败，数据源" + name + "目录文件：" + fileName);
            }
        });
        map.put("errorList", errorList);
        return map;
    }


    public static List<Object> loadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
        String[] split = fileName.split("\\.");
        String fileType = split[split.length - 1];
        switch (fileType){
            case "csv":
            case "txt":
                return CsvUtil.loadingTableData(client, bucketName, fileName);
            case "xlsx":
                return XlsxUtil.loadingTableData(client, bucketName, fileName);
            default:
                return null;
        }
    }

    public static List<Object> unloadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
        String[] split = fileName.split("\\.");
        String fileType = split[split.length - 1];
        switch (fileType){
            case "csv":
            case "txt":
                return CsvUtil.unloadingTableData(client, bucketName, fileName);
            case "xlsx":
                return XlsxUtil.unloadingTableData(client, bucketName, fileName);
            default:
                return null;
        }
    }

    public static List<Object> customerInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
        String[] split = fileName.split("\\.");
        String fileType = split[split.length - 1];
        switch (fileType){
            case "csv":
            case "txt":
                return CsvUtil.customerInformationData(client, bucketName, fileName);
            case "xlsx":
                return XlsxUtil.customerInformationData(client, bucketName, fileName);
            default:
                return null;
        }
    }

    public static List<Object> logisticsInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
        String[] split = fileName.split("\\.");
        String fileType = split[split.length - 1];
        switch (fileType){
            case "csv":
            case "txt":
                return CsvUtil.logisticsInformationData(client, bucketName, fileName);
            case "xlsx":
                return XlsxUtil.logisticsInformationData(client, bucketName, fileName);
            default:
                return null;
        }

    }
}
