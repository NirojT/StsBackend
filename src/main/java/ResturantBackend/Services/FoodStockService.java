package ResturantBackend.Services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ResturantBackend.Dto.FoodStockDto;

public interface FoodStockService {

	Boolean createStockFood(FoodStockDto foodStockDto);

	Boolean updateStockFood(UUID id, String name, Double price, int quantity, String supplier, Date expireDate,
			String category, String description);

	Boolean deleteStockFood(UUID id);
	Boolean fakeDeleteStockFood(UUID id);

	List<FoodStockDto> getAllFoodStock();

	FoodStockDto getFoodStockByID(UUID id);

	Double getExpenseBy1Day();

	Double getTotalExpenseWeekly();

	double getMonthlyExpense();

	double[] getMonthlyExpenseDataWholeYear();

	List<Map<String, Object>> getStockNameAndQuantity();
	double getYearlyExpenseReport();

}
