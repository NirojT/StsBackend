package Kanchanjunga.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import Kanchanjunga.Dto.AddOrderDto;
import Kanchanjunga.Dto.OrderRequest;
import Kanchanjunga.Dto.OrdersDto;
import Kanchanjunga.Entity.Users;
import Kanchanjunga.JWT.JwtHelper;
import Kanchanjunga.Reposioteries.UserRepo;
import Kanchanjunga.Services.OrdersService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orders/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/",
		"http://192.168.0.102:5173/" }, allowCredentials = "true")
public class OrdersController {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private JwtHelper jwtHelper;

	@PostMapping("create")
	public ResponseEntity<?> createOrders(
			@RequestBody OrderRequest orderRequest,
			HttpServletRequest request

	) {

		System.out.println(orderRequest.getTableNo());
		System.out.println(orderRequest.getTotalPrice());

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
				System.out.println(username);

				Boolean isSaved = ordersService.createOrders(orderRequest, username);

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
	// @PostMapping("create")
	// public ResponseEntity<?> createOrders(
	// @RequestParam UUID userId,
	// @RequestParam UUID foodMenuId,
	// @RequestParam UUID drinkMenuId,
	// @RequestBody List<OrdersDto> ordersDto
	//
	// ) {
	// HashMap<String, Object> response = new HashMap<>();
	// try {
	//
	// Boolean isSaved = ordersService.createOrders(ordersDto, userId, foodMenuId,
	// drinkMenuId);
	//
	//
	// response.put("status", isSaved ? 200 : 400);
	// response.put("message", isSaved ? "Orders saved successfully": "Orders not
	// saved");
	// return ResponseEntity.status(200).body(response);
	//
	//
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// response.put("status", 500);
	// response.put("message", "something went wrong... ");
	// return ResponseEntity.status(200).body(response);
	// }
	// }

	@PutMapping("update/{id}")
	public ResponseEntity<?> updateFoodStock(
			@PathVariable UUID id,
			@RequestParam(required = false) String tableNo,
			@RequestParam(required = false) Double price,
			@RequestParam(required = false) List<AddOrderDto> item,
			@RequestParam(required = false) String status

	) {
		Map<String, Object> response = new HashMap<>();
		try {

			Boolean isUpdated = this.ordersService.updateOrders(id, tableNo, price, item, status);

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

	@GetMapping("get-latest")
	public ResponseEntity<?> getLatestOrders() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<OrdersDto> allOrders = this.ordersService.getLatestOrders();
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
	
	@GetMapping("get-No")
	public ResponseEntity<?> getNoOfOrders() {
		Map<String, Object> response = new HashMap<>();
		try {
			int noOfOrders = this.ordersService.getNoOfOrders();
			response.put("status",noOfOrders>=0 ? 200 : 400 );
			response.put("ordersNo",noOfOrders>=0 ? noOfOrders : "no orders are ordered" );
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}
	
	
	@GetMapping("get-totalSellMonthly")
	public ResponseEntity<?> getTotalSellAmt() {
		Map<String, Object> response = new HashMap<>();
		try {
			 Double totalSellAmt = this.ordersService.getTotalSellAmtMonthly();
			response.put("status",totalSellAmt>=0 ? 200 : 400 );
			response.put("SellsAmtMonthly",totalSellAmt>=0 ? totalSellAmt : "Sells didnot went well in this month" );
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}
	@GetMapping("get-totalAmtWeekly")
	public ResponseEntity<?> getTotalSellAmtWeekly() {
		Map<String, Object> response = new HashMap<>();
		try {
			Double totalSellAmt = this.ordersService.getTotalSellAmtWeekly();
			response.put("status",totalSellAmt>=0 ? 200 : 400 );
			response.put("SellsAmtWeekly",totalSellAmt>=0 ? totalSellAmt : "Sells didnot went well in this week" );
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}
	
	
	
	
	
	
	
	
}
