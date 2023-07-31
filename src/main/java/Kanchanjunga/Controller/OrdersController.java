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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Dto.OrdersDto;
import Kanchanjunga.Services.OrdersService;

@RestController
@RequestMapping("/api/orders/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/","http://localhost:5173/", "http://192.168.0.102:5173/" }, allowCredentials = "true")
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	
	
	@PostMapping("create")
	public ResponseEntity<?> createOrders(
			@RequestBody OrdersDto ordersDto,
			@RequestParam UUID userId,
			@RequestParam UUID foodMenuId,
			@RequestParam UUID drinkMenuId
			
			) {
		HashMap<String, Object> response = new HashMap<>();
		try {

			Boolean isSaved = ordersService.createOrders(ordersDto, userId, foodMenuId, drinkMenuId);
			
		
				response.put("status", isSaved ? 200 : 400);
				response.put("message", isSaved ? "Orders  saved successfully": "Orders not saved");
				return ResponseEntity.status(200).body(response);
			
			

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}


	@PutMapping("update/{id}")
	public ResponseEntity<?> updateFoodStock(
			@PathVariable  UUID id,
			@RequestParam(required = false) String tableNo,
			@RequestParam(required = false) Double price,
			@RequestParam(required = false) int quantity, 
			@RequestParam(required = false) String item
		

	) {
		Map<String, Object> response = new HashMap<>();
		try {

			Boolean isUpdated= this.ordersService.updateOrders(id, tableNo, price, quantity, item);
			

			
				response.put("status",isUpdated ? 200 :400);
				response.put("message",isUpdated ? "Orders updated successfully":"Orders update failed");
				return ResponseEntity.status(200).body(response);
			
			

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}

	}

	@GetMapping("get-all")
	public ResponseEntity<?> getAllFoodsStock() {
		Map<String, Object> response = new HashMap<>();
		try {

			 List<OrdersDto> allOrders = this.ordersService.getAllOrders();
			 
			
				response.put("status",allOrders != null ? 200 : 400);
				response.put("Orders",allOrders != null ? allOrders : "Orders not found");
				return ResponseEntity.status(200).body(response);
			
			

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	@GetMapping("get/{id}")
	public ResponseEntity<?> getFoodStockById(@PathVariable("id") UUID ids) {
		Map<String, Object> response = new HashMap<>();
		try {
			 OrdersDto ordersByID = this.ordersService.getOrdersByID(ids);
		
				response.put("status",ordersByID != null ? 200 :400);
				response.put("Order",ordersByID != null ? ordersByID :"order is empty 🤢🤢");
				return ResponseEntity.status(200).body(response);


		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteFoodStock(@PathVariable("id") UUID ids) {
		Map<String, Object> response = new HashMap<>();

		try {
			Boolean deleteOrders  = this.ordersService.deleteOrders(ids);
		
				response.put("status",deleteOrders ? 200 :400);
				response.put("message",deleteOrders ?  "Orders Deleted Succesfully 😁😘" : "Orders not deleted (:");
				return ResponseEntity.status(200).body(response);
		
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}

	}


}
