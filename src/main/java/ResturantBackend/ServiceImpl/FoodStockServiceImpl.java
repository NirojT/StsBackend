package ResturantBackend.ServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ResturantBackend.Dto.FoodStockDto;
import ResturantBackend.Entity.DrinkStock;
import ResturantBackend.Entity.FoodStock;
import ResturantBackend.ErrorHandlers.ResourceNotFound;
import ResturantBackend.Reposioteries.DrinkStockRepo;
import ResturantBackend.Reposioteries.FoodStockRepo;
import ResturantBackend.Services.FoodStockService;

@Service
public class FoodStockServiceImpl implements FoodStockService {

	@Autowired
	private FoodStockRepo foodStockRepo;

	@Autowired
	private DrinkStockRepo drinkStockRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean createStockFood(FoodStockDto foodStockDto) {
		try {
			foodStockDto.setId(UUID.randomUUID());
			FoodStock foodStock = this.mapper.map(foodStockDto, FoodStock.class);

			foodStock.setCreatedDate(new Date());
			foodStockRepo.save(foodStock);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public Boolean updateStockFood(UUID id, String name, Double price, int quantity, String supplier, Date expireDate,
			String category, String description) {
		try {
			FoodStock foodStockDb = this.foodStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));

			foodStockDb.setName(name);
			foodStockDb.setPrice(price);
			foodStockDb.setQuantity(quantity);
			foodStockDb.setSupplier(supplier);
			foodStockDb.setExpireDate(expireDate);
			foodStockDb.setCategory(category);
			foodStockDb.setDescription(description);

			this.foodStockRepo.save(foodStockDb);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean deleteStockFood(UUID id) {

		try {

			FoodStock foodStock = this.foodStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Food", "Food Id", id));

			this.foodStockRepo.delete(foodStock);
			Optional<FoodStock> checking = this.foodStockRepo.findById(id);

			if (checking.isPresent()) {
				return false;
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public List<FoodStockDto> getAllFoodStock() {

		try {
			List<FoodStock> allFoodStocks = this.foodStockRepo.findAll();
			List<FoodStockDto> allFoodStockDto = allFoodStocks.stream().map((stock) -> {
				FoodStockDto foodStockDto = this.mapper.map(stock, FoodStockDto.class);

				return foodStockDto;
			}).collect(Collectors.toList());

			if (allFoodStockDto.size() > 0) {
				return allFoodStockDto;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public FoodStockDto getFoodStockByID(UUID id) {

		try {
			FoodStock foodStock = this.foodStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Food", "Food Id", id));

			FoodStockDto foodStockDto = this.mapper.map(foodStock, FoodStockDto.class);

			if (foodStockDto != null) {
				return foodStockDto;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// getting expenses data with in 1 day acccording to date
	@Override
	public Double getExpenseBy1Day() {

		try {
			LocalDate currentDate = LocalDate.now();
			LocalDateTime startOfDay = currentDate.atStartOfDay();
			LocalDateTime endOfDay = currentDate.atTime(LocalTime.MAX);

			double foodExpense = this.foodStockRepo.findExpenseBy1Day(startOfDay, endOfDay).stream()
					.mapToDouble(FoodStock::getPrice).sum();

			double drinkExpense = this.drinkStockRepo.findExpenseBy1Day(startOfDay, endOfDay).stream()
					.mapToDouble(DrinkStock::getPrice).sum();

			double totalExpenses = 0.0;

			if (foodExpense != 0.0 || drinkExpense != 0.0) {
				totalExpenses = foodExpense + drinkExpense;
			}
			System.out.println("totalExpenses is " + totalExpenses);
			return totalExpenses;

		} catch (Exception e) {
			e.printStackTrace();
			return 0.00;
		}
	}

	// for 1 week the sell amount will change sunday to end of saturday
	@Override
	public Double getTotalExpenseWeekly() {
		try {
			LocalDate currentDate = LocalDate.now();

			DayOfWeek firstDayOfWeek = DayOfWeek.SUNDAY; // Define the first day of the week

			int daysUntilFirstDay = (currentDate.getDayOfWeek().getValue() + 7 - firstDayOfWeek.getValue()) % 7;

			System.out.println(daysUntilFirstDay);

			LocalDate startOfWeek = currentDate.minusDays(daysUntilFirstDay);
			LocalDate endOfWeek = startOfWeek.plusDays(6);

			LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
			LocalDateTime endOfWeekDateTime = endOfWeek.atTime(LocalTime.MAX);

			double foodExpense = this.foodStockRepo.findExpenseBy1Week(startOfWeekDateTime, endOfWeekDateTime).stream()
					.mapToDouble(FoodStock::getPrice).sum();

			double drinkExpense = this.drinkStockRepo.findExpenseBy1Week(startOfWeekDateTime, endOfWeekDateTime)
					.stream().mapToDouble(DrinkStock::getPrice).sum();

			double totalExpenses = 0.0;

			if (foodExpense != 0.0 || drinkExpense != 0.0) {
				totalExpenses = foodExpense + drinkExpense;
			}
			System.out.println("totalExpenses is " + totalExpenses);
			return totalExpenses;

		} catch (Exception e) {
			e.printStackTrace();
			return 0.00;
		}
	}

	// for drinkstock and foodstock based on monthly....

	// according to month
	@Override
	public double getMonthlyExpense() {

		try {
			YearMonth currentYearMonth = YearMonth.now();
			LocalDate startDate = currentYearMonth.atDay(1);
			LocalDate endDate = currentYearMonth.atEndOfMonth();

			double foodExpense = this.foodStockRepo.findExpenseByCurrentMonth(startDate, endDate).stream()
					.mapToDouble(FoodStock::getPrice).sum();

			double drinkExpense = this.drinkStockRepo.findExpenseByCurrentMonth(startDate, endDate).stream()
					.mapToDouble(DrinkStock::getPrice).sum();

			double totalExpenses = 0.0;
			if (foodExpense != 0.0 || drinkExpense != 0.0) {
				totalExpenses = foodExpense + drinkExpense;
			}
			System.out.println("totalExpenses is " + totalExpenses);
			return totalExpenses;
		} catch (Exception e) {
			e.printStackTrace();
			return 0.00;
		}
	}

	public double[] getMonthlyExpenseDataWholeYear() {

		try {
			int currentYear = YearMonth.now().getYear();

			double monthlyExpense[] = new double[12];

			for (int month = 1; month <= 12; month++) {

				YearMonth currentYearMonth = YearMonth.of(currentYear, month);
				LocalDate startDate = currentYearMonth.atDay(1);
				LocalDate endDate = currentYearMonth.atEndOfMonth();

				double foodExpense = this.foodStockRepo.findExpenseByCurrentMonth(startDate, endDate).stream()
						.mapToDouble(FoodStock::getPrice).sum();

				double drinkExpense = this.drinkStockRepo.findExpenseByCurrentMonth(startDate, endDate).stream()
						.mapToDouble(DrinkStock::getPrice).sum();

				monthlyExpense[month - 1] = foodExpense + drinkExpense;
		
			}
			
		
			return monthlyExpense;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("iam catch block....");
			return new double[12];
			
		}
	}
	public double getYearlyExpenseReport() {
		
		try {
			int currentYear = YearMonth.now().getYear();
			
			double YearlyExpense=0.0;
			
			for (int month = 1; month <= 12; month++) {
				
				YearMonth currentYearMonth = YearMonth.of(currentYear, month);
				LocalDate startDate = currentYearMonth.atDay(1);
				LocalDate endDate = currentYearMonth.atEndOfMonth();
				
				double foodExpense = this.foodStockRepo.findExpenseByCurrentMonth(startDate, endDate).stream()
						.mapToDouble(FoodStock::getPrice).sum();
				
				double drinkExpense = this.drinkStockRepo.findExpenseByCurrentMonth(startDate, endDate).stream()
						.mapToDouble(DrinkStock::getPrice).sum();
				
				YearlyExpense += foodExpense + drinkExpense;
				
				
				
			}
			
			if(YearlyExpense!=0.0)return YearlyExpense;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("iam catch block....");
			return 0.0;
			
		}
		return 0.0;
	}


	@Override
	public List<Map<String, Object>> getStockNameAndQuantity() {
		try {
			List<FoodStock> collectedStock = this.foodStockRepo.findAll(Sort.by(Sort.Direction.DESC,"createdDate"))
					.stream().limit(5).map((stock)->new FoodStock(stock.getName(), stock.getQuantity()))
					.collect(Collectors.toList());
			
			List<Map<String, Object>>stockList=new ArrayList<>();
			
			
			for(int i=0; i<collectedStock.size(); i++) {
				Map<String, Object> stocks=new HashMap<>();

	
				String name = collectedStock.get(i).getName();
				
				int quantity = collectedStock.get(i).getQuantity();
				
				stocks.put("name", name);
				stocks.put("quantity", quantity);
				
				stockList.add(stocks);
		
		}
			return stockList;
		}
			catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public Boolean fakeDeleteStockFood(UUID id) {
		try {
			FoodStock foodStock = this.foodStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Food", "Food Id", id));
			foodStock.setFakeDelete(true);
			this.foodStockRepo.save(foodStock);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

}
