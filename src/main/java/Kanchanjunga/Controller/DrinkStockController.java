package Kanchanjunga.Controller;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Dto.DrinkStockDto;
import Kanchanjunga.Services.DrinkStockService;

@RestController
@RequestMapping("/api/drinks/stock")

@CrossOrigin(origins = { "http://127.0.0.1:5173/","http://localhost:5173/", "http://192.168.0.102:5173/" }, allowCredentials = "true")

public class DrinkStockController {

    @Autowired
    private DrinkStockService drinkStockService;

    @PostMapping("create")
    public ResponseEntity<?> createDrinkStock(@RequestBody DrinkStockDto drinkStockDto) {
        try {
        	System.out.println(drinkStockDto.toString());
            Boolean isSaved = this.drinkStockService.createStockDrinks(drinkStockDto);
            HashMap<String, Object> response = new HashMap<>();
            if (isSaved) {
                response.put("status", 200);
                response.put("message", "drinks added successfully");
                return ResponseEntity.status(200).body(response);
            }
            response.put("status", 400);
            response.put("message", "drinks not added");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateDrinksStock(@PathVariable UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) String supplier,
            @RequestParam(required = false) Date expireDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String description) {
        try {
        	
            Boolean isUpdated = this.drinkStockService.updateStockDrinks(id, name, price, quantity, supplier,
                    expireDate, category, description);
            HashMap<String, Object> response = new HashMap<>();

            if (isUpdated) {
                response.put("status", 200);
                response.put("message", "drink stock updated successfully");
                return ResponseEntity.status(200).body(response);
            }
            response.put("status", 400);
            response.put("message", "drink stock update failed");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllDrinksStock() {
        try {
            List<DrinkStockDto> list = this.drinkStockService.getAllDrinksStock();
            Map<String, Object> response = new HashMap<>();
            if (list != null) {
                response.put("status", 200);
                response.put("drinks", list);
                return ResponseEntity.status(200).body(response);
            }
            response.put("status", 400);
            response.put("message", "drink stock not found 🤌");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getDrinkStockById(@PathVariable("id") UUID id) {
        Map<String, Object> response = new HashMap<>();
        try {
            DrinkStockDto drink = this.drinkStockService.getDrinkStockByID(id);
            if (drink != null) {
                response.put("status", 200);
                response.put("drink", drink);
                return ResponseEntity.status(200).body(response);
            }
            response.put("status", 400);
            response.put("message", "drinkMenu not found");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteDrinksMenu(@PathVariable("id") UUID id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean isDeleted = this.drinkStockService.deleteStockDrinks(id);
            if (isDeleted) {
                response.put("status", 200);
                response.put("message", "drink stock deleted successfully");
                return ResponseEntity.status(200).body(response);
            }
            response.put("status", 400);
            response.put("message", "drink stock not deleted (:");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
