package Kanchanjunga.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.DrinkMenuDto;

public interface DrinkMenuService {
	Boolean createMenuDrinks(DrinkMenuDto data);

	Boolean updateMenuDrinks(UUID id, String name, Double price, String category, String description,
			MultipartFile image);

	Boolean deleteMenuDrinks(UUID id);

	List<DrinkMenuDto> getAllDrinksMenu();

	DrinkMenuDto getDrinkMenuByID(UUID id);
}
