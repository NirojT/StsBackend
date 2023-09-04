package ResturantBackend.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import ResturantBackend.Dto.FoodMenuDto;

public interface FoodMenuService {

	Boolean createFoodMenu(FoodMenuDto foodMenuDto);

	Boolean updateFoodMenu(UUID id, String name, Double price, String category, String description,String type, MultipartFile image);

	Boolean deleteFoodMenu(UUID id);
	Boolean fakeDeleteFoodMenu(UUID id);

	List<FoodMenuDto> getAllFoodMenu();

	FoodMenuDto getFoodMenuByID(UUID id);

}
