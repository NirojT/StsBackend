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

import Kanchanjunga.Dto.DrinkMenuDto;
import Kanchanjunga.Services.DrinkMenuService;

@RestController
@RequestMapping("/api/drinks/menu/")
public class DrinkMenuController {

	@Autowired
	private DrinkMenuService drinkMenuService;

	@PostMapping("create")
	public ResponseEntity<?> createDrinksMenu(@ModelAttribute DrinkMenuDto request) {
		try {
			Boolean isSaved = drinkMenuService.createMenuDrinks(request);
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
	public ResponseEntity<?> updateDrinksMenu(@PathVariable UUID id,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Double price,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) MultipartFile image) {
		try {

			Boolean updateMenuDrinks = this.drinkMenuService.updateMenuDrinks(id, name, price, category, description,
					image);
			Map<String, Object> response = new HashMap<>();

			if (updateMenuDrinks) {
				response.put("status", 200);
				response.put("message", "drink menu updated successfully");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "drink menu update failed");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping("get-all")
	public ResponseEntity<?> getAllDrinksMenu() {
		try {
			List<DrinkMenuDto> allDrinksMenu = this.drinkMenuService.getAllDrinksMenu();
			Map<String, Object> response = new HashMap<>();
			if (allDrinksMenu != null) {
				response.put("status", 200);
				response.put("drinks", allDrinksMenu);
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "drinkMenus not found");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("get/{id}")
	public ResponseEntity<?> getDrinksMenuById(@PathVariable("id") UUID id) {
		Map<String, Object> response = new HashMap<>();
		try {
			DrinkMenuDto drinkMenuByID = this.drinkMenuService.getDrinkMenuByID(id);
			if (drinkMenuByID != null) {
				response.put("status", 200);
				response.put("drink", drinkMenuByID);
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
	public ResponseEntity<?> deleteDrinksMenu(@PathVariable("id") UUID ids) {
		Map<String, Object> response = new HashMap<>();
		try {
			Boolean deleteMenuDrinks = this.drinkMenuService.deleteMenuDrinks(ids);
			if (deleteMenuDrinks) {
				response.put("status", 200);
				response.put("message", "drinkMenu Deleted Successfully");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "drinkMenu not deleted (:");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
