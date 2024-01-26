package ResturantBackend.Controller;

import ResturantBackend.Entity.Table;
import ResturantBackend.Reposioteries.TableRepo;
import ResturantBackend.Utility.ExcelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private TableRepo tableRepo;

    String filename = "tableExceldata.xlsx";

    @GetMapping("/get")
    public ResponseEntity<Resource> exportTablesToExcel() {
        try {
            List<Table> tables = tableRepo.findAll(); // Retrieve your data from the repository

            ByteArrayInputStream excelData = ExcelHelper.dataToExcel(tables);
            InputStreamResource file = new InputStreamResource(excelData);

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
