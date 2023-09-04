package ResturantBackend.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import ResturantBackend.Dto.DrinkMenuDto;
import ResturantBackend.Services.DrinkMenuService;

@RestController
@RequestMapping("/api/drinks/menu/")

@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/",
		"https://64f1a1ae3172de413ab9674b--cute-taiyaki-355152.netlify.app/",
		"http://192.168.0.102:5173/" }, allowCredentials = "true")
public class DrinkMenuController {

	@Autowired
	private DrinkMenuService drinkMenuService;

	@PostMapping("create")
	public ResponseEntity<?> createDrinksMenu(@ModelAttribute DrinkMenuDto request) {
		HashMap<String, Object> response = new HashMap<>();
		try {
			Boolean isSaved = drinkMenuService.createMenuDrinks(request);

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
			response.put("status", 500);
			response.put("message", "something went wrong");
			return ResponseEntity.status(500).body(response);
		}
	}

	@PutMapping("update/{id}")
	public ResponseEntity<?> updateDrinksMenu(@PathVariable UUID id,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Double price,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String remarks,
			@RequestParam(required = false) MultipartFile image) {
		Map<String, Object> response = new HashMap<>();
		try {
			Boolean updateMenuDrinks = this.drinkMenuService.updateMenuDrinks(id, name, price, category, description,
					remarks,
					image);

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
			response.put("status", 500);
			response.put("message", "something went wrong");
			return ResponseEntity.status(500).body(response);
		}

	}

	@GetMapping("get-all")
	public ResponseEntity<?> getAllDrinksMenu() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<DrinkMenuDto> allDrinksMenu = this.drinkMenuService.getAllDrinksMenu();
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
			response.put("status", 500);
			response.put("message", "something went wrong");
			return ResponseEntity.status(500).body(response);
		}
	}

	@GetMapping("get/{id}")
	public ResponseEntity<?> getDrinksMenuById(@PathVariable("id") UUID id) {
		Map<String, Object> response = new HashMap<>();
		try {
			DrinkMenuDto drinkMenuByID = this.drinkMenuService.getDrinkMenuByID(id);
			response.put("status", drinkMenuByID != null ? 200 : 400);
			response.put(drinkMenuByID != null ? "drink" : "message",
					drinkMenuByID != null ? drinkMenuByID : "drinkMenu not found");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong");
			return ResponseEntity.status(500).body(response);
		}
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteDrinksMenu(@PathVariable("id") UUID ids) {
		Map<String, Object> response = new HashMap<>();
		try {
			Boolean deleteMenuDrinks = this.drinkMenuService.deleteMenuDrinks(ids);
			response.put("status", deleteMenuDrinks ? 200 : 400);
			response.put("message", deleteMenuDrinks ? "drink menu deleted successfully" : "drink menu not deleted (:");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong");
			return ResponseEntity.status(500).body(response);
		}

	}
	@DeleteMapping("fake-delete/{id}")
	public ResponseEntity<?> fakeDeleteDrinksMenu(@PathVariable("id") UUID ids) {
		Map<String, Object> response = new HashMap<>();
		try {
			Boolean deleteMenuDrinks = this.drinkMenuService.fakeDeleteMenuDrinks(ids);
			response.put("status", deleteMenuDrinks ? 200 : 400);
			response.put("message", deleteMenuDrinks ? "drink menu deleted successfully" : "drink menu not deleted (:");
			return ResponseEntity.status(200).body(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong");
			return ResponseEntity.status(500).body(response);
		}
		
	}

}
