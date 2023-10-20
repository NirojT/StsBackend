package ResturantBackend.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ResturantBackend.Dto.OrderRequest;
import ResturantBackend.Dto.OrdersDto;
import ResturantBackend.JWT.JwtHelper;
import ResturantBackend.Services.OrdersService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orders/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/","http://192.168.0.107:5173/",
		"https://cute-taiyaki-355152.netlify.app","http://192.168.16.104:5173/",
		"http://192.168.0.116",
		"http://192.168.0.102:5173/" }, allowCredentials = "true")
public class OrdersController {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private JwtHelper jwtHelper;

//	@Autowired
//	private SimpMessagingTemplate messagingTemplate;

	@PostMapping("create")
	public ResponseEntity<?> createOrders(@RequestBody OrderRequest orderRequest, HttpServletRequest request

	) {
		HashMap<String, Object> response = new HashMap<>();
		try {

			String requestHeader = request.getHeader("Authorization");
			String token = null;
			if (requestHeader != null && requestHeader.startsWith("Bearer")) {
				token = requestHeader.substring(7);

				Boolean isValid = jwtHelper.validateLoginToken(token);

				if (!isValid) {
					response.put("status", 400);
					response.put("message", "invalid token");
					return ResponseEntity.status(200).body(response);
				}

				String username = jwtHelper.extractUsername(token);

				Boolean isSaved = ordersService.createOrders(orderRequest, username);

				 // Send a WebSocket message when the order is created
//		        if (isSaved) {
//		            messagingTemplate.convertAndSend("/topic/orders", "New order created");
//		        }
				response.put("status", isSaved ? 200 : 400);
				response.put("message", isSaved ? "Orders  saved successfully" : "Orders not saved");
				return ResponseEntity.status(200).body(response);

			}

			return ResponseEntity.status(200).body("fail");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}
//	@SendTo("/topic/message")
//	public List<OrdersDto> broadcastMessage(@Payload List<OrdersDto> OrdersDto) {
//		return OrdersDto;
//	}

	@PutMapping("update/{id}")
	public ResponseEntity<?> updateFoodStock(@PathVariable UUID id, @RequestBody OrderRequest orderRequest

	) {
		Map<String, Object> response = new HashMap<>();
		try {
			Boolean isUpdated = this.ordersService.updateOrders(id, orderRequest);

			response.put("status", isUpdated ? 200 : 400);
			response.put("message", isUpdated ? "Orders updated successfully" : "Orders update failed");
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

			response.put("status", allOrders != null ? 200 : 400);
			response.put("Orders", allOrders != null ? allOrders : "Orders not found");

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

			response.put("status", ordersByID != null ? 200 : 400);
			response.put("Order", ordersByID != null ? ordersByID : "order is empty 🤢🤢");
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
			Boolean deleteOrders = this.ordersService.deleteOrders(ids);

			response.put("status", deleteOrders ? 200 : 400);
			response.put("message", deleteOrders ? "Orders Deleted Succesfully 😁😘" : "Orders not deleted (:");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}

	}

	@PatchMapping("update/status/{id}")
	public ResponseEntity<?> updateOrderStatus(@PathVariable UUID id, @RequestParam String status) {
		Map<String, Object> response = new HashMap<>();
		try {
			
			
			Boolean isUpdated = this.ordersService.updateStatus(id, status);
			response.put("status", isUpdated ? 200 : 400);
			response.put("message", isUpdated ? "Status updated successfully" : "Orders update failed");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong...:( ");
			return ResponseEntity.status(200).body(response);
		}
	}

	@GetMapping("get-my-order")
	public ResponseEntity<?> getMyOrders(HttpServletRequest request

	) {
		HashMap<String, Object> response = new HashMap<>();
		try {

			String requestHeader = request.getHeader("Authorization");
			String token = null;
			if (requestHeader != null && requestHeader.startsWith("Bearer")) {
				token = requestHeader.substring(7);

				Boolean isValid = jwtHelper.validateLoginToken(token);

				if (!isValid) {
					response.put("status", 400);
					response.put("message", "invalid token");
					return ResponseEntity.status(200).body(response);
				}

				String username = jwtHelper.extractUsername(token);

				List<OrdersDto> orders = this.ordersService.getMyOrders(username);
				response.put("status", orders.size() > 0 ? 200 : 400);
				response.put(orders.size() > 0 ? "orders" : "message", orders.size() > 0 ? orders : "no orders found");
				return ResponseEntity.status(200).body(response);
			}
			return ResponseEntity.status(200).body("fail");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong...");
			return ResponseEntity.status(200).body(response);
		}
	}
	
	@GetMapping("get-latestTable")
	public ResponseEntity<?> getLatestOrders() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<OrdersDto> allOrders = this.ordersService.getLatestOrdersInTable();
			response.put("status", allOrders != null ? 200 : 400);
			response.put("orders", allOrders != null ? allOrders : "Orders not found");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}
	

}
