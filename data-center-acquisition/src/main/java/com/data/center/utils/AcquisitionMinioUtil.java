package com.data.center.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcquisitionMinioUtil {

    public static Map<String, List<T>> acquisitionToList(AmazonS3 client, String bucketName, Map<String, String> file) {
        // 列出文件
        Map<String, List<T>> map = new HashMap<>();
        ObjectListing objectListing = client.listObjects(bucketName);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        objectSummaries.forEach(s3ObjectSummary -> {
            String fileName = s3ObjectSummary.getKey();
            if (file.containsKey(s3ObjectSummary.getKey())) {
                try {
                    switch (file.get(fileName)) {
                        case "LoadingTable":
                            List<T> loadingTableList = loadingTableData(client, bucketName, fileName);
                            map.put(fileName, loadingTableList);
                            break;
                        case "UnloadingTable":
                            List<T> unloadingTableList = unloadingTableData(client, bucketName, fileName);
                            map.put(fileName, unloadingTableList);
                            break;
                        case "CustomerInformation":
                            List<T> customerInformationList = customerInformationData(client, bucketName, fileName);
                            map.put(fileName, customerInformationList);
                            break;
                        case "LogisticsInformation":
                            List<T> logisticsInformationList = logisticsInformationData(client, bucketName, fileName);
                            map.put(fileName, logisticsInformationList);
                            break;
                    }
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return map;
    }


    public static <T> List<T> loadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
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

    public static <T> List<T> unloadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
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

    public static <T> List<T> customerInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
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

    public static <T> List<T> logisticsInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
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