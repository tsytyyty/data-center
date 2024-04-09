package com.data.center.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.center.mapper.LogisticsInformationMapper;
import com.data.center.pojo.Do.LogisticsInformation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogisticsInformationService extends ServiceImpl<LogisticsInformationMapper, LogisticsInformation> {
    public void importDataFromExcel(String filePath) {
        List<LogisticsInformation> list = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            FileInputStream excelFile = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(2);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    // 读取 Excel 中的每一行数据
                    String logisticsId = row.getCell(0).getStringCellValue();
                    String owner = row.getCell(1).getStringCellValue();
                    String ownerId = row.getCell(2).getStringCellValue();
                    String companyId = row.getCell(3).getStringCellValue();
                    String containerId = row.getCell(4).getStringCellValue();
                    String goodsName = row.getCell(5).getStringCellValue();
                    int weight = Integer.parseInt(row.getCell(6).getStringCellValue());

                    // 创建 LoadingTable 对象，并赋值
                    LogisticsInformation logisticsInformation = new LogisticsInformation();
                    logisticsInformation.setLogisticsId(logisticsId);
                    logisticsInformation.setOwner(owner);
                    logisticsInformation.setOwnerId(ownerId);
                    logisticsInformation.setCompanyId(companyId);
                    logisticsInformation.setContainerId(containerId);
                    logisticsInformation.setGoodsName(goodsName);
                    logisticsInformation.setWeight(weight);

                    // 将 LoadingTable 对象添加到列表中
                    list.add(logisticsInformation);
                }
            }
//            list.forEach(System.out::println);
            // 使用 saveBatch 方法批量插入数据
            saveBatch(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logisticsInformation(String filePath) throws IOException {

        List<LogisticsInformation> list = new ArrayList<>();
        //数据解析
        FileInputStream fin = new FileInputStream(filePath);
        InputStreamReader reader = new InputStreamReader(fin, getFileCode(fin));
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        strTmp = buffReader.readLine();

        while((strTmp = buffReader.readLine())!= null){

            LogisticsInformation log = new LogisticsInformation();

            String[] split = strTmp.split("',");
            log.setLogisticsId(splitResult(split[0]));
            log.setOwner(splitResult(split[1]));
            log.setOwnerId(splitResult(split[2]));
            log.setCompanyId(splitResult(split[3]));
            log.setContainerId(splitResult(split[4]));
            log.setGoodsName(splitResult(split[5]));
            log.setWeight(Integer.parseInt(splitResult(split[6])));

            list.add(log);
        }
        buffReader.close();
//        list.forEach(System.out::println);
        saveBatch(list);
    }

    public static String splitResult(String once) {
        String result = "";
        for (int i = 0; i < once.length(); i++) {
            if (once.charAt(i) != '\'') {
                result += once.charAt(i);
            }
        }
        return result;
    }

    public static String getFileCode(InputStream in) {
        try {
            int p = (in.read() << 8) + in.read();
            String code = "GBK";

            switch (p) {
                case 59524:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                case 48581:
                    code = "GBK";
                    break;
                default:
            }
            return code;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
