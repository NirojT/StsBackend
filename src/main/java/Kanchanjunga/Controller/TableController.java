package Kanchanjunga.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Entity.Table;
import Kanchanjunga.Services.TableService;

@RestController
@RequestMapping("api/table/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/","http://localhost:5173/", "http://192.168.0.102:5173/" }
, allowCredentials = "true")
public class TableController {

	
	@Autowired
	private TableService tableService;
	
	@PostMapping("create")
	public ResponseEntity<?> createTable(@RequestBody Table table){
		Map<String, Object> response = new HashMap<>();
		try {
		
			Boolean isCreated = this.tableService.createTable(table);
			response.put("status", isCreated ? 200 : 400);
			response.put("message",isCreated ? "Table created successfully" : "Table create failed");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
		
	}
	@PutMapping("update/{id}")
	public ResponseEntity<?>updateTable(@PathVariable UUID id,@RequestBody Table table){
		Map<String, Object> response = new HashMap<>();
		try {
			
			Boolean isUpdated = this.tableService.updateTable(id, table);
			response.put("status", isUpdated ? 200 : 400);
			response.put("message",isUpdated ? "Table updated successfully" : "Table update failed");
			return ResponseEntity.status(200).body(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
		
	}
	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteTable(@PathVariable ("id") UUID ids){
		Map<String, Object> response = new HashMap<>();
		try {
			
			Boolean isDeleted = this.tableService.deleteTable(ids);
			response.put("status", isDeleted ? 200 : 400);
			response.put("message",isDeleted ? "Table deleted successfully" : "Table delete failed");
			return ResponseEntity.status(200).body(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
		
	}
	@GetMapping("get-all")
	public ResponseEntity<?> getTables(){
		Map<String, Object> response = new HashMap<>();
		try {
			
			 List<Table> alltables = this.tableService.geTables();
			response.put("status", alltables!=null ? 200 : 400);
			response.put("tables",alltables!=null ? alltables : "Table empty");
			return ResponseEntity.status(200).body(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
		
	}
	@GetMapping("get/{id}")
	public ResponseEntity<?> getTable(@PathVariable UUID id){
		Map<String, Object> response = new HashMap<>();
		try {
			
			Table table = this.tableService.getTable(id);
			response.put("status", table!=null ? 200 : 400);
			response.put("table",table!=null ? table : "Table did not found");
			return ResponseEntity.status(200).body(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
		
	}
	
	
	
}
