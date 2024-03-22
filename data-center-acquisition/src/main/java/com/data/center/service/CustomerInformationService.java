package com.data.center.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.center.domain.Do.CustomerInformation;
import com.data.center.mapper.CustomerInformationMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerInformationService extends ServiceImpl<CustomerInformationMapper, CustomerInformation> {

    public void importDataFromExcel(String filePath) {
        List<CustomerInformation> list = new ArrayList<>();
        try {
            FileInputStream excelFile = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(4);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    String name = row.getCell(0).getStringCellValue();
                    String idCard = row.getCell(1).getStringCellValue();
                    String phoneNumber = row.getCell(2).getStringCellValue();
                    String address = row.getCell(3).getStringCellValue();

                    CustomerInformation customerInformation = new CustomerInformation();
                    customerInformation.setName(name);
                    customerInformation.setIdCard(idCard);
                    customerInformation.setPhoneNumber(phoneNumber);
                    customerInformation.setAddress(address);

                    list.add(customerInformation);
                }
            }
//            list.forEach(System.out::println);
            saveBatch(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
