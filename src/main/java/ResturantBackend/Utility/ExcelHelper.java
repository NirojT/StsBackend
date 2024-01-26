package ResturantBackend.Utility;

import ResturantBackend.Entity.Table;
import ResturantBackend.Reposioteries.TableRepo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {

    public static String[] Headers = { "id", "tableNo", "isAvailable" };

    public static String Sheet_Name = "Table Data";

    @Autowired
    private TableRepo tableRepo;

    public static ByteArrayInputStream dataToExcel(List<Table> tables) {
        try( ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Workbook workbook = new XSSFWorkbook()
        ) {



            // create sheet
            Sheet sheet = workbook.createSheet(Sheet_Name);

            // create header row
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < Headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(Headers[col]);
            }

            // create data rows
            int rowIdx = 1;
            for (Table table : tables) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(table.getId().toString());
                row.createCell(1).setCellValue(table.getTableNo());
                row.createCell(2).setCellValue(table.isAvailable());
                // Add more columns as needed
            }

            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }
}
