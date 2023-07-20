package Kanchanjunga.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.FoodMenuDto;
import Kanchanjunga.Dto.FoodStockDto;
import Kanchanjunga.Services.FoodMenuService;
import Kanchanjunga.Services.FoodStockService;

@RestController
@RequestMapping("/api/foods/stock/")
public class FoodStockController {

	@Autowired
	private FoodStockService foodStockService;

	@PostMapping("create")
	public ResponseEntity<?> createFoodStock(@ModelAttribute FoodStockDto foodStockDto) {

		try {

			Boolean isSaved = foodStockService.createStockFood(foodStockDto);
			HashMap<String, Object> response = new HashMap<>();
			if (isSaved) {
				response.put("status", 200);
				response.put("message", "food  added successfully to stock");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "food not added to stock ");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@PutMapping("update/{id}")
	public ResponseEntity<?> updateFoodStock(
			@PathVariable(required = false) UUID id,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Double price,
			@RequestParam(required = false) int quantity, 
			@RequestParam(required = false) String supplier, 
			@RequestParam(required = false) Date expireDate,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) MultipartFile image

	) {
		try {

			Boolean updateFoodMenu = this.foodStockService.updateStockFood(id, name, price, quantity, supplier, expireDate
					, category, description, image);
			Map<String, Object> response = new HashMap<>();

			if (updateFoodMenu) {
				response.put("status", 200);
				response.put("message", "foodStock updated successfully");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "foodStock update failed");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping("get-all")
	public ResponseEntity<?> getAllFoodsStock() {

		try {

			 List<FoodStockDto> allFoodStock = this.foodStockService.getAllFoodStock();
			Map<String, Object> response = new HashMap<>();
			if (allFoodStock != null) {
				response.put("status", 200);
				response.put("Foods", allFoodStock);
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "Food Menus not found");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("get/{id}")
	public ResponseEntity<?> getFoodStockById(@PathVariable("id") UUID ids) {
		Map<String, Object> response = new HashMap<>();
		try {
			 FoodStockDto foodStockByID = this.foodStockService.getFoodStockByID(ids);
			if (foodStockByID != null) {
				response.put("status", 200);
				response.put("Foods", foodStockByID);
				return ResponseEntity.status(200).body(response);

			}
			response.put("status", 400);
			response.put("message", "FoodStock is empty 🤢🤢");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteFoodStock(@PathVariable("id") UUID ids) {
		Map<String, Object> response = new HashMap<>();

		try {
			Boolean deleteFoodStock  = this.foodStockService.deleteStockFood(ids);
			if (deleteFoodStock) {
				response.put("status", 200);
				response.put("message", "Food Deleted Succesfully from stock");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "Food not deleted from stock(:");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


}
