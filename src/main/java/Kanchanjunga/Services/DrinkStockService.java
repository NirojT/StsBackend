package Kanchanjunga.Services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.DrinkStockDto;

public interface DrinkStockService {

	Boolean createStockDrinks(DrinkStockDto drinkStockDto);

	Boolean updateStockDrinks(UUID id, String name, Double price, int quantity,
			String supplier,
			Date expireDate,
			String category,
			String description,
			MultipartFile image);

	Boolean deleteStockDrinks(UUID id);

	List<DrinkStockDto> getAllDrinksStock();

	DrinkStockDto getDrinkStockByID(UUID id);
}
