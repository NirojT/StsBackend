package Kanchanjunga.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.DrinkStockDto;
import Kanchanjunga.Reposioteries.DrinkStockRepo;
import Kanchanjunga.Services.DrinkStockService;

@Service
public class DrinkStockServiceImpl implements DrinkStockService {

	@Autowired
	private DrinkStockRepo drinkStockRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Boolean createStockDrinks(DrinkStockDto drinkStockDto) {
		try {
			drinkStockDto.setId(UUID.randomUUID());
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public Boolean updateStockDrinks(UUID id, String name, Double price, int quantity, String supplier, Date expireDate,
			String category, String description, MultipartFile image, String imageName) {

		return null;
	}

	@Override
	public Boolean deleteStockDrinks(UUID id) {
		return null;
	}

	@Override
	public List<DrinkStockDto> getAllDrinksStock() {
		return null;
	}

	@Override
	public DrinkStockDto getDrinkStockByID(UUID id) {
		return null;
	}

}
