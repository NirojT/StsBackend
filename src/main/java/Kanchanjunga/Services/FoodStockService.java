package Kanchanjunga.Services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.FoodStockDto;

public interface FoodStockService {

	Boolean createStockFood(FoodStockDto foodStockDto);

	Boolean updateStockFood(UUID id, String name, Double price, int quantity, String supplier, Date expireDate,
			String category, String description, MultipartFile image);

	Boolean deleteStockFood(UUID id);

	List<FoodStockDto> getAllFoodStock();

	FoodStockDto getFoodStockByID(UUID id);
}
