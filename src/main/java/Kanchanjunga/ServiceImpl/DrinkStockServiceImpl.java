package Kanchanjunga.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.DrinkStockDto;
import Kanchanjunga.Services.DrinkStockService;

public class DrinkStockServiceImpl implements DrinkStockService {

	@Override
	public Boolean createStockDrinks(DrinkStockDto drinkStockDto) {
		
		return null;
	}

	@Override
	public Boolean updateStockDrinks(UUID id, String name, Double price, int quantity, String supplier, Date expireDate,
			String category, String description, MultipartFile image, String imageName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteStockDrinks(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DrinkStockDto> getAllDrinksStock() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DrinkStockDto getDrinkStockByID(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

}
