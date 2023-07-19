package Kanchanjunga.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.FoodStockDto;

public class FoodStockServiceImpl implements Kanchanjunga.Services.FoodStockService {

	@Override
	public Boolean createStockFood(FoodStockDto foodStockDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateStockFood(UUID id, String name, Double price, int quantity, String supplier, Date expireDate,
			String category, String description, MultipartFile image, String imageName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteStockFood(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FoodStockDto> getAllFoodStock() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FoodStockDto getFoodStockByID(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

}
