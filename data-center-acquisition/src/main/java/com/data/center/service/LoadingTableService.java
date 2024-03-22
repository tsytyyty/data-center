package com.data.center.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.center.domain.Do.CustomerInformation;
import com.data.center.domain.Do.LoadingTable;
import com.data.center.mapper.LoadingTableMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoadingTableService extends ServiceImpl<LoadingTableMapper, LoadingTable> {

    public void importDataFromExcel(String filePath) {
        List<LoadingTable> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            FileInputStream excelFile = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    // 读取 Excel 中的每一行数据
                    String shipCompanies = row.getCell(0).getStringCellValue();
                    String shipName = row.getCell(1).getStringCellValue();
                    Date workBeginTime = dateFormat.parse(row.getCell(2).getStringCellValue());
                    Date workEndTime = dateFormat.parse(row.getCell(3).getStringCellValue());
                    Date departureTime = dateFormat.parse(row.getCell(4).getStringCellValue());
                    Date arriveTime = dateFormat.parse(row.getCell(5).getStringCellValue());
                    String port = row.getCell(6).getStringCellValue();
                    String orderId = row.getCell(7).getStringCellValue();
                    String containerId = row.getCell(8).getStringCellValue();
                    int containerSize = Integer.parseInt(row.getCell(9).getStringCellValue());
                    String departurePlace = row.getCell(10).getStringCellValue();
                    String destinationPlace = row.getCell(11).getStringCellValue();

                    // 创建 LoadingTable 对象，并赋值
                    LoadingTable loadingTable = new LoadingTable();
                    loadingTable.setShipCompanies(shipCompanies);
                    loadingTable.setShipName(shipName);
                    loadingTable.setWorkBeginTime(workBeginTime);
                    loadingTable.setWorkEndTime(workEndTime);
                    loadingTable.setDepartureTime(departureTime);
                    loadingTable.setArriveTime(arriveTime);
                    loadingTable.setPort(port);
                    loadingTable.setLogisticsId(orderId);
                    loadingTable.setContainerId(containerId);
                    loadingTable.setContainerSize(containerSize);
                    loadingTable.setDeparturePlace(departurePlace);
                    loadingTable.setDestinationPlace(destinationPlace);

                    // 将 LoadingTable 对象添加到列表中
                    list.add(loadingTable);
                }
            }
//            list.forEach(System.out::println);
            // 使用 saveBatch 方法批量插入数据
            saveBatch(list);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}


