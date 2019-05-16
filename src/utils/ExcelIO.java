package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelIO {

    public static XSSFWorkbook getWorkbook(String url) {
        XSSFWorkbook excelBook = null;
        try {
            excelBook = new XSSFWorkbook(new FileInputStream(url));
        }catch (IOException e){
            e.printStackTrace();
        }
        return excelBook;
    }

    public static int getCountOfRows(XSSFSheet sheet){
        return sheet.getLastRowNum();
    }

    public static void setCoordsInRow(Row row, String longitude, String latitude) {
        row.getCell(7).setCellValue(longitude);
        row.getCell(8).setCellValue(latitude);
    }

    public static String getAddressFromRow(Row row){
        return row.getCell(6).getStringCellValue();
    }

    public static void writeExcel(Workbook excelBook, String newURL){
        try {
            FileOutputStream outputStream = new FileOutputStream(newURL, false);
            excelBook.write(outputStream);
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}