package Kanchanjunga.Services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.DrinkMenuDto;
import Kanchanjunga.Dto.DrinkStockDto;
import Kanchanjunga.Entity.DrinkMenu;
import Kanchanjunga.Entity.DrinkStock;

public interface DrinkStockService {

	Boolean createStockDrinks(DrinkStockDto drinkStockDto);

	Boolean updateStockDrinks(UUID id, String name, Double price,int quantity,
			String supplier, 
			Date expireDate,
			String category,
			String description,
			MultipartFile image,
			String imageName);

	Boolean deleteStockDrinks(UUID id);

	List<DrinkStockDto> getAllDrinksStock();

	DrinkStockDto getDrinkStockByID(UUID id);
}

