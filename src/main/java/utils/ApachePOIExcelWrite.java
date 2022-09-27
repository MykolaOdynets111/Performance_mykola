package utils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ApachePOIExcelWrite {
    public static final Map<String, Object> testresultdata = new LinkedHashMap<>();

    public static Map<String, Object> setupTestResultData() {
        return testresultdata;
    }

    public static void setupAfterSuite(int noOfThreads) {
        //write excel file and file name is TestResult.xls
        Set<String> keyset = testresultdata.keySet();
        //create a new work book
        HSSFWorkbook workbook = new HSSFWorkbook();
        //create a new work sheet
        HSSFSheet sheet = workbook.createSheet("Test Result");
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object objArr = testresultdata.get(key);
            int cellnum = 0;
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue(key);
            Cell cell1 = row.createCell(cellnum++);
            cell1.setCellValue(String.valueOf(objArr));

        }
        try {
            FileOutputStream out = new FileOutputStream(new File("excelReport_" + noOfThreads + "chats.xls"));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
