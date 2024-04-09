package com.data.center.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.data.center.pojo.Do.CustomerInformation;
import com.data.center.pojo.Do.LoadingTable;
import com.data.center.pojo.Do.LogisticsInformation;
import com.data.center.pojo.Do.UnloadingTable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 解析xlsx格式数据
 */
public class XlsxUtil {

    public static <T> List<T> loadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
        List<LoadingTable> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        S3ObjectInputStream excelFile = client.getObject(bucketName, fileName).getObjectContent();
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                LoadingTable loadingTable = new LoadingTable();
                loadingTable.setShipCompanies(row.getCell(0).getStringCellValue());
                loadingTable.setShipName(row.getCell(1).getStringCellValue());
                loadingTable.setWorkBeginTime(dateFormat.parse(row.getCell(2).getStringCellValue()));
                loadingTable.setWorkEndTime(dateFormat.parse(row.getCell(3).getStringCellValue()));
                loadingTable.setDepartureTime(dateFormat.parse(row.getCell(4).getStringCellValue()));
                loadingTable.setArriveTime(dateFormat.parse(row.getCell(5).getStringCellValue()));
                loadingTable.setPort(row.getCell(6).getStringCellValue());
                loadingTable.setLogisticsId(row.getCell(7).getStringCellValue());
                loadingTable.setContainerId(row.getCell(8).getStringCellValue());
                loadingTable.setContainerSize(Integer.parseInt(row.getCell(9).getStringCellValue()));
                loadingTable.setDeparturePlace(row.getCell(10).getStringCellValue());
                loadingTable.setDestinationPlace(row.getCell(11).getStringCellValue());
                list.add(loadingTable);
            }
        }
        excelFile.close();
        workbook.close();
        return (List<T>) list;
    }

    public static <T> List<T> unloadingTableData(AmazonS3 client, String bucketName, String fileName) throws IOException, ParseException {
        List<UnloadingTable> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        S3ObjectInputStream excelFile = client.getObject(bucketName, fileName).getObjectContent();
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                UnloadingTable unloadingTable = new UnloadingTable();
                unloadingTable.setShipCompanies(row.getCell(0).getStringCellValue());
                unloadingTable.setShipName(row.getCell(1).getStringCellValue());
                unloadingTable.setWorkBeginTime(dateFormat.parse(row.getCell(2).getStringCellValue()));
                unloadingTable.setWorkEndTime(dateFormat.parse(row.getCell(3).getStringCellValue()));
                unloadingTable.setDepartureTime(dateFormat.parse(row.getCell(4).getStringCellValue()));
                unloadingTable.setArriveTime(dateFormat.parse(row.getCell(5).getStringCellValue()));
                unloadingTable.setPort(row.getCell(6).getStringCellValue());
                unloadingTable.setLogisticsId(row.getCell(7).getStringCellValue());
                unloadingTable.setContainerId(row.getCell(8).getStringCellValue());
                unloadingTable.setContainerSize(Integer.parseInt(row.getCell(9).getStringCellValue()));
                unloadingTable.setDeparturePlace(row.getCell(10).getStringCellValue());
                unloadingTable.setDestinationPlace(row.getCell(11).getStringCellValue());
                list.add(unloadingTable);
            }
        }
        excelFile.close();
        workbook.close();
        return (List<T>) list;
    }

    public static <T> List<T> customerInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
        List<CustomerInformation> list = new ArrayList<>();
        S3ObjectInputStream excelFile = client.getObject(bucketName, fileName).getObjectContent();
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                CustomerInformation customerInformation = new CustomerInformation();
                customerInformation.setName(row.getCell(0).getStringCellValue());
                customerInformation.setIdCard(row.getCell(1).getStringCellValue());
                customerInformation.setPhoneNumber(row.getCell(2).getStringCellValue());
                customerInformation.setAddress(row.getCell(3).getStringCellValue());
                list.add(customerInformation);
            }
        }
        excelFile.close();
        workbook.close();
        return (List<T>) list;
    }

    public static <T> List<T> logisticsInformationData(AmazonS3 client, String bucketName, String fileName) throws IOException {
        List<LogisticsInformation> list = new ArrayList<>();
        S3ObjectInputStream excelFile = client.getObject(bucketName, fileName).getObjectContent();
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                LogisticsInformation logisticsInformation = new LogisticsInformation();
                logisticsInformation.setLogisticsId(row.getCell(0).getStringCellValue());
                logisticsInformation.setOwner(row.getCell(1).getStringCellValue());
                logisticsInformation.setOwnerId(row.getCell(2).getStringCellValue());
                logisticsInformation.setCompanyId(row.getCell(3).getStringCellValue());
                logisticsInformation.setContainerId(row.getCell(4).getStringCellValue());
                logisticsInformation.setGoodsName(row.getCell(5).getStringCellValue());
                logisticsInformation.setWeight(Integer.parseInt(row.getCell(6).getStringCellValue()));

                list.add(logisticsInformation);
            }
        }
        excelFile.close();
        workbook.close();
        return (List<T>) list;

    }
}
