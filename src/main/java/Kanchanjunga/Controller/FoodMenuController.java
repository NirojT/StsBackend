package Kanchanjunga.Controller;



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
import Kanchanjunga.Entity.FoodMenu;
import Kanchanjunga.Services.FoodMenuService;

@RestController
@RequestMapping("/api/foods/menu/")
public class FoodMenuController {

	@Autowired
	private FoodMenuService foodMenuService;

	@PostMapping("create")
	public ResponseEntity<?> createFoodMenu(@ModelAttribute FoodMenuDto foodMenuDto) {

		try {

			Boolean isSaved = foodMenuService.createFoodMenu(foodMenuDto);
			HashMap<String, Object> response = new HashMap<>();
			if (isSaved) {
				response.put("status", 200);
				response.put("message", "food added successfully");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "food not added");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@PutMapping("update/{id}")
	public ResponseEntity<?> updateFoodMenu(
			@PathVariable UUID id,
			@RequestParam String name,
			@RequestParam Double price,
			@RequestParam String category,
			@RequestParam String description,
			@RequestParam(required = false) MultipartFile image, 
			@RequestParam(required = false) String imageName

	) {
		try {

			Boolean updateFoodMenu = this.foodMenuService.updateFoodMenu(id, name, price, category, description,
					image, imageName);
			Map<String, Object> response = new HashMap<>();

			if (updateFoodMenu) {
				response.put("status", 200);
				response.put("message", "foodMenu updated successfully");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "foodMenu updatation failed");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping("get-all")
	public ResponseEntity<?> getAllFoodsMenu() {

		try {

			List<FoodMenuDto> foodMenus = this.foodMenuService.getAllFoodMenu();
			Map<String, Object> response = new HashMap<>();
			if (foodMenus != null) {
				response.put("status", 200);
				response.put("FoodMenus", foodMenus);
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
	public ResponseEntity<?> getFoodMenuById(@PathVariable("id") UUID ids) {
		Map<String, Object> response = new HashMap<>();
		try {
			 FoodMenuDto foodMenuByID = this.foodMenuService.getFoodMenuByID(ids);
			if (foodMenuByID != null) {
				response.put("status", 200);
				response.put("FoodMenu", foodMenuByID);
				return ResponseEntity.status(200).body(response);

			}
			response.put("status", 400);
			response.put("message", "FoodMenu  not found");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteFoodMenu(@PathVariable("id") UUID ids) {
		Map<String, Object> response = new HashMap<>();

		try {
			Boolean deleteFoodMenu  = this.foodMenuService.deleteFoodMenu(ids);
			if (deleteFoodMenu) {
				response.put("status", 200);
				response.put("message", "FoodMenus Deleted Succesfully");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "FoodMenus not deleted (:");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


}
