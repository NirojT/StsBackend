package ResturantBackend.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ResturantBackend.Dto.OrdersDto;
import ResturantBackend.Entity.DrinkMenu;
import ResturantBackend.Entity.FoodMenu;
import ResturantBackend.Services.DrinkStockService;
import ResturantBackend.Services.FoodStockService;
import ResturantBackend.Services.OrdersService;
import ResturantBackend.Services.PaymentService;

@RestController
@RequestMapping("/api/dynamic/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/",
		"https://64f1a1ae3172de413ab9674b--cute-taiyaki-355152.netlify.app/",
		"http://192.168.0.102:5173/" }, allowCredentials = "true")
public class DashboardDynamicData {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private FoodStockService foodStockService;

	@Autowired
	private DrinkStockService drinkStockService;

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
			response.put("ordersNo", noOfOrders > 0 ? noOfOrders : "No orders are ordered today");
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
			response.put("SellsAmtDaily", totalSellAmt > 0 ? totalSellAmt : "Sells didnot went well today");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	// for 1 week the sell amount will change by counting days i.e 7
	@GetMapping("get-totalSellWeekly")
	public ResponseEntity<?> getTotalSellAmtWeekly() {
		Map<String, Object> response = new HashMap<>();
		try {
			Double totalSellAmt = this.paymentService.getTotalSellAmtWeekly();
			response.put("status", totalSellAmt > 0 ? 200 : 400);
			response.put("SellsAmtWeekly", totalSellAmt > 0 ? totalSellAmt : "Sells did not went well in this week");
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
			response.put("SellsAmtMonthly", totalSellAmt > 0 ? totalSellAmt : "Sells did not went well in this month");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	@GetMapping("get-totalSellDataWholeYear")
	public ResponseEntity<?> getMonthlySellDataWholeYear() {
		Map<String, Object> response = new HashMap<>();
		try {
			double[] monthlySellWholeYear = this.paymentService.getMonthlySellDataWholeYear();
			boolean allZero = true;
			for (double everyMonthSell : monthlySellWholeYear) {
				if (everyMonthSell != 0) {
					allZero = false;
					break;
				}
			}

			response.put("status", allZero ? 400 : 200);
			response.put("SellAmtYearly", allZero ? "No sell in this year" : monthlySellWholeYear);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	@GetMapping("get-YearlySalesReport")
	public ResponseEntity<?> getYearlySalesReport() {
		Map<String, Object> response = new HashMap<>();
		try {
			double yearlySalesReport = this.paymentService.getYearlySalesReport();

			response.put("status", yearlySalesReport == 0 ? 400 : 200);
			response.put("SalesReportAmt", yearlySalesReport == 0 ? "No sell in this year" : yearlySalesReport);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	// expenses
	@GetMapping("get-totalExpenseDaily")
	public ResponseEntity<?> getDailyExpense() {
		Map<String, Object> response = new HashMap<>();
		try {
			Double totalSellAmt = this.foodStockService.getExpenseBy1Day();
			response.put("status", totalSellAmt > 0 ? 200 : 400);
			response.put("ExpenseAmtDaily", totalSellAmt > 0 ? totalSellAmt : "No expense today");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	@GetMapping("get-totalExpenseWeekly")
	public ResponseEntity<?> getWeeklyExpense() {
		Map<String, Object> response = new HashMap<>();
		try {
			Double totalSellAmt = this.foodStockService.getTotalExpenseWeekly();
			response.put("status", totalSellAmt > 0 ? 200 : 400);
			response.put("ExpenseAmtWeekly", totalSellAmt > 0 ? totalSellAmt : "No expense in this week");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

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

	@GetMapping("get-YearlyExpenseReport")
	public ResponseEntity<?> getYearlyExpenseReport() {
		Map<String, Object> response = new HashMap<>();
		try {
			Double totalSellAmt = this.foodStockService.getYearlyExpenseReport();
			response.put("status", totalSellAmt > 0 ? 200 : 400);
			response.put("YearlyExpenseReport", totalSellAmt > 0 ? totalSellAmt : "No expense in this year");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	@GetMapping("get-totalExpenseDataWholeYear")
	public ResponseEntity<?> getMonthlyExpenseDataWholeYear() {
		Map<String, Object> response = new HashMap<>();
		try {
			double[] monthlyExpenseWholeYear = this.foodStockService.getMonthlyExpenseDataWholeYear();
			boolean allZero = true;
			for (double everyMonthExp : monthlyExpenseWholeYear) {
				if (everyMonthExp != 0) {
					allZero = false;
					break;
				}
			}

			response.put("status", allZero ? 400 : 200);
			response.put("ExpenseAmtYearly", allZero ? "No expense in this year" : monthlyExpenseWholeYear);
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	// expense ends......

	// get stockname and quantity for food
	@GetMapping("get-stockNameQuantity")
	public ResponseEntity<?> getStockNameAndQuantity() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Map<String, Object>> stockNameAndQuantity = this.foodStockService.getStockNameAndQuantity();
			response.put("status", stockNameAndQuantity != null ? 200 : 400);
			response.put("stockNameQuantity",
					stockNameAndQuantity != null ? stockNameAndQuantity : "No name found in stock in this year");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	// get stockname and quantity for drink
	@GetMapping("get-stockDrinkNameAndQuantity")
	public ResponseEntity<?> getStockDrinkNameAndQuantity() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Map<String, Object>> stockNameAndQuantity = this.drinkStockService.getStockDrinkNameAndQuantity();
			response.put("status", stockNameAndQuantity != null ? 200 : 400);
			response.put("stockDrinkNameQuantity",
					stockNameAndQuantity != null ? stockNameAndQuantity : "No name found in stock in this year");
			return ResponseEntity.status(200).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	@GetMapping("get-MostOrderedFood")
	public ResponseEntity<?> getMostOrderedFood() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<FoodMenu> mostOrderedFood = this.ordersService.getMostOrderedFoods();

			response.put("status", mostOrderedFood != null ? 200 : 400);
			response.put("mostOrderedFood", mostOrderedFood != null ? mostOrderedFood : "There are no mostOrderedFood");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

	@GetMapping("get-MostOrderedDrinks")
	public ResponseEntity<?> getMostOrderedDrinks() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<DrinkMenu> mostOrderedDrinks = this.ordersService.getMostOrderedDrinks();

			response.put("status", mostOrderedDrinks != null ? 200 : 400);
			response.put("mostOrderedDrink",
					mostOrderedDrinks != null ? mostOrderedDrinks : "There are no mostOrderedDrinks");
			return ResponseEntity.status(200).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}
	}

}