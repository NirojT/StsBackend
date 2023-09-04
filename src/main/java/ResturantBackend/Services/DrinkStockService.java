package ResturantBackend.Services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ResturantBackend.Dto.DrinkStockDto;

public interface DrinkStockService {

	Boolean createStockDrinks(DrinkStockDto drinkStockDto);

	Boolean updateStockDrinks(UUID id, String name, Double price, Integer quantity,
			String supplier,
			Date expireDate,
			String category,
			String description
			);

	Boolean deleteStockDrinks(UUID id);
	Boolean fakeDeleteStockDrinks(UUID id);

	List<DrinkStockDto> getAllDrinksStock();

	DrinkStockDto getDrinkStockByID(UUID id);
	
	List<Map<String, Object>> getStockDrinkNameAndQuantity();
}
