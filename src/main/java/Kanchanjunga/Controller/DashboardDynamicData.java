package Kanchanjunga.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Dto.OrdersDto;
import Kanchanjunga.Services.FoodStockService;
import Kanchanjunga.Services.OrdersService;
import Kanchanjunga.Services.PaymentService;

@RestController
@RequestMapping("/api/dynamic/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/",
		"http://192.168.0.102:5173/" }, allowCredentials = "true")
public class DashboardDynamicData {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private FoodStockService foodStockService;

	// give latest orders up to 12 latest according to date
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

	// gives the total no. of orders changing 1 day 12:01am to 12:00pm
	@GetMapping("get-noDay")
	// orders start..................................
	public ResponseEntity<?> getNoOfOrdersBy1Day() {
		Map<String, Object> response = new HashMap<>();
		try {
			int noOfOrders = this.ordersService.getNoOfOrdersBy1Day();
			response.put("status", noOfOrders > 0 ? 200 : 400);
			response.put("ordersNo", noOfOrders > 0 ? noOfOrders : "no orders are ordered today");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	// gives the total no. of orders changing sun to end of Sat
	@GetMapping("get-noWeek")
	public ResponseEntity<?> getOrderNoWeekly() {
		Map<String, Object> response = new HashMap<>();
		try {
			int noOfOrders = this.ordersService.getOrderNoWeekly();
			response.put("status", noOfOrders > 0 ? 200 : 400);
			response.put("ordersNo", noOfOrders > 0 ? noOfOrders : "no orders are ordered in this week");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	// gives the total no. of orders changing sun to end of Sat
	@GetMapping("get-noMonth")
	public ResponseEntity<?> getNoOfOrdersByCurrentMonth() {
		Map<String, Object> response = new HashMap<>();
		try {
			int noOfOrders = this.ordersService.getNoOfOrdersByCurrentMonth();
			response.put("status", noOfOrders > 0 ? 200 : 400);
			response.put("ordersNo", noOfOrders > 0 ? noOfOrders : "no orders are ordered in the month");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	// ..................................orders end
	// get total sell or addidng price 1month acccording to digit of month (not by
	// day )
	
	@GetMapping("get-totalSellDaily")
	public ResponseEntity<?> getSellsBy1Day() {
		Map<String, Object> response = new HashMap<>();
		try {
			Double totalSellAmt = this.paymentService.getSellsBy1Day();
			response.put("status", totalSellAmt > 0 ? 200 : 400);
			response.put("SellsAmtMonthly", totalSellAmt > 0 ? totalSellAmt : "Sells didnot went well today");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}
	
	
	
	
	@GetMapping("get-totalSellMonthly")
	public ResponseEntity<?> getTotalSellAmtMonthly() {
		Map<String, Object> response = new HashMap<>();
		try {
			Double totalSellAmt = this.paymentService.getTotalSellAmtMonthly();
			response.put("status", totalSellAmt > 0 ? 200 : 400);
			response.put("SellsAmtMonthly", totalSellAmt > 0 ? totalSellAmt : "Sells didnot went well in this month");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}



	// for 1 week the sell amount will change by counting days i.e 7
	@GetMapping("get-totalAmtWeekly")
	public ResponseEntity<?> getTotalSellAmtWeekly() {
		Map<String, Object> response = new HashMap<>();
		try {
			Double totalSellAmt = this.paymentService.getTotalSellAmtWeekly();
			response.put("status", totalSellAmt > 0 ? 200 : 400);
			response.put("SellsAmtWeekly", totalSellAmt > 0 ? totalSellAmt : "Sells didnot went well in this week");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}
	
	
	//expenses
	@GetMapping("get-totalExpenseMonthly")
	public ResponseEntity<?> getMonthlyExpense() {
		Map<String, Object> response = new HashMap<>();
		try {
			Double totalSellAmt = this.foodStockService.getMonthlyExpense();
			response.put("status", totalSellAmt > 0 ? 200 : 400);
			response.put("ExpenseAmtMonthly", totalSellAmt > 0 ? totalSellAmt : "No expense in this month");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}
}
