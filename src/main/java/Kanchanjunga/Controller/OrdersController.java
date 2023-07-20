package Kanchanjunga.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	
	
	@PostMapping("create")
	public ResponseEntity<?> createOrders(
			@RequestBody OrdersDto ordersDto,
			@RequestParam UUID userId,
			@RequestParam UUID paymentId,
			@RequestParam UUID foodMenuId,
			@RequestParam UUID drinkMenuId
			
			) {
	
		try {

			Boolean isSaved = ordersService.createOrders(ordersDto, userId, paymentId, foodMenuId, drinkMenuId);
			HashMap<String, Object> response = new HashMap<>();
			if (isSaved) {
				response.put("status", 200);
				response.put("message", "Orders  saved successfully ");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "Orders not saved ");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
		try {

			Boolean isUpdated= this.ordersService.updateOrders(id, tableNo, price, quantity, item);
			Map<String, Object> response = new HashMap<>();

			if (isUpdated) {
				response.put("status", 200);
				response.put("message", "Orders updated successfully");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "Orders update failed");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@GetMapping("get-all")
	public ResponseEntity<?> getAllFoodsStock() {

		try {

			 List<OrdersDto> allOrders = this.ordersService.getAllOrders();
			 
			Map<String, Object> response = new HashMap<>();
			if (allOrders != null) {
				response.put("status", 200);
				response.put("Orders", allOrders);
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "Orders not found");
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
			 OrdersDto ordersByID = this.ordersService.getOrdersByID(ids);
			if (ordersByID != null) {
				response.put("status", 200);
				response.put("Order", ordersByID);
				return ResponseEntity.status(200).body(response);

			}
			response.put("status", 400);
			response.put("message", "order is empty 🤢🤢");
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
			Boolean deleteOrders  = this.ordersService.deleteOrders(ids);
			if (deleteOrders) {
				response.put("status", 200);
				response.put("message", "Orders Deleted Succesfully 😁😘");
				return ResponseEntity.status(200).body(response);
			}
			response.put("status", 400);
			response.put("message", "Orders not deleted (:");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


}
