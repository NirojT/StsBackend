package ResturantBackend.Controller;

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

import ResturantBackend.Dto.FoodStockDto;
import ResturantBackend.Services.FoodStockService;

@RestController
@RequestMapping("/api/foods/stock/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/",
		"https://64f1a1ae3172de413ab9674b--cute-taiyaki-355152.netlify.app/",
		"http://192.168.0.102:5173/" }, allowCredentials = "true")
public class FoodStockController {

	@Autowired
	private FoodStockService foodStockService;

	@PostMapping("create")
	public ResponseEntity<?> createFoodStock(@RequestBody FoodStockDto foodStockDto) {

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
	public ResponseEntity<?> updateFoodStock(@PathVariable(required = false) UUID id,
			@RequestParam(required = false) String name, @RequestParam(required = false) Double price,
			@RequestParam(required = false) int quantity, @RequestParam(required = false) String supplier,
			@RequestParam(required = false) Date expireDate, @RequestParam(required = false) String category,
			@RequestParam(required = false) String description

	) {
		try {

			Boolean updateFoodMenu = this.foodStockService.updateStockFood(id, name, price, quantity, supplier,
					expireDate, category, description);
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
				response.put("Food", foodStockByID);
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
			Boolean deleteFoodStock = this.foodStockService.deleteStockFood(ids);
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

	@GetMapping("get-expense")
	public ResponseEntity<?> getMonthlyExpense() {
		Map<String, Object> response = new HashMap<>();
		try {
			double monthlyExpense = this.foodStockService.getMonthlyExpense();

			response.put("status", monthlyExpense != 0.0 ? 200 : 400);
			response.put("monthlyExpense",
					monthlyExpense != 0.0 ? monthlyExpense : "There is no expense in this month...");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}
}
