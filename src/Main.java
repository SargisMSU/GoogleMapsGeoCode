import geo.DeoCodeDecoder;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.ExcelIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) throws IOException {
        HashSet<Integer> errors = new HashSet<>();
        XSSFWorkbook workbook = ExcelIO.getWorkbook("data_total.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);
        int countOfRows = ExcelIO.getCountOfRows(sheet);
        System.out.println("countOfRows = " + countOfRows);

        for (int i = 1; i <= countOfRows; i++) {
            System.out.println("i = " + i);
            XSSFRow row = sheet.getRow(i);
            String address = ExcelIO.getAddressFromRow(row);
            if (address.trim().length() == 0){
                errors.add(i);
                continue;
            }
            String latAndLong = DeoCodeDecoder.getLatAndLong(address);
            if (latAndLong.length() == 0){
                errors.add(i);
                continue;
            }
            String[] latAndLongSplit = latAndLong.split(";");
            ExcelIO.setCoordsInRow(row, latAndLongSplit[0], latAndLongSplit[1]);
        }
        ExcelIO.writeExcel(workbook, "result.xlsx");
        Files.write(Paths.get("errors.txt"), errors.toString().getBytes());
    }
}
