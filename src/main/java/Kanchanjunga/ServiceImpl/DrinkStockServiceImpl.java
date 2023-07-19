package Kanchanjunga.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.DrinkStockDto;
import Kanchanjunga.Entity.DrinkStock;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.DrinkStockRepo;
import Kanchanjunga.Services.DrinkStockService;
import Kanchanjunga.Utility.FilesHelper;

@Service
public class DrinkStockServiceImpl implements DrinkStockService {

	@Autowired
	private DrinkStockRepo drinkStockRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private FilesHelper fileHelper;

	@Override
	public Boolean createStockDrinks(DrinkStockDto drinkStockDto) {
		try {
			drinkStockDto.setId(UUID.randomUUID());
			DrinkStock drinkStock = mapper.map(drinkStockDto, DrinkStock.class);
			String filename = fileHelper.saveFile(drinkStockDto.getImage());
			drinkStock.setImage(filename);
			drinkStock.setCreatedDate(new Date());
			drinkStockRepo.save(drinkStock);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean updateStockDrinks(UUID id, String name, Double price, int quantity, String supplier, Date expireDate,
			String category, String description, MultipartFile image) {
		try {
			DrinkStock dbStockDrink = this.drinkStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
			if (image != null) {
				String filename = fileHelper.saveFile(image);
				Boolean isDeleted = fileHelper.deleteExistingFile(dbStockDrink.getImage());
				if (isDeleted) {
					dbStockDrink.setName(name);
					dbStockDrink.setPrice(price);
					dbStockDrink.setQuantity(quantity);
					dbStockDrink.setSupplier(supplier);
					dbStockDrink.setExpireDate(expireDate);
					dbStockDrink.setDescription(description);
					dbStockDrink.setCategory(category);
					dbStockDrink.setImage(filename);
					this.drinkStockRepo.save(dbStockDrink);
					return true;
				}
				return false;
			}
			dbStockDrink.setName(name);
			dbStockDrink.setPrice(price);
			dbStockDrink.setQuantity(quantity);
			dbStockDrink.setSupplier(supplier);
			dbStockDrink.setExpireDate(expireDate);
			dbStockDrink.setDescription(description);
			dbStockDrink.setCategory(category);
			dbStockDrink.setImage(dbStockDrink.getImage());
			this.drinkStockRepo.save(dbStockDrink);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean deleteStockDrinks(UUID id) {
		try {
			DrinkStock dbStockDrink = this.drinkStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
			Boolean isDeleted = fileHelper.deleteExistingFile(dbStockDrink.getImage());
			if (isDeleted) {
				this.drinkStockRepo.delete(dbStockDrink);
				Optional<DrinkStock> checking = this.drinkStockRepo.findById(id);
				if (checking.isPresent()) {
					return false;
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<DrinkStockDto> getAllDrinksStock() {
		try {
			List<DrinkStock> allDrinkMenu = drinkStockRepo.findAll();
			List<DrinkStockDto> stockDrinks = allDrinkMenu.stream().map((drink) -> {
				DrinkStockDto drinkStockDTO = this.mapper.map(drink, DrinkStockDto.class);
				drinkStockDTO.setImageName(drink.getImage());
				return drinkStockDTO;
			}).toList();
			if (stockDrinks.size() > 0) {
				return stockDrinks;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public DrinkStockDto getDrinkStockByID(UUID id) {
		try {
			DrinkStock dbStockDrink = this.drinkStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
			DrinkStockDto drinkStockDTO = this.mapper.map(dbStockDrink, DrinkStockDto.class);
			drinkStockDTO.setImageName(dbStockDrink.getImage());
			if (drinkStockDTO != null) {
				return drinkStockDTO;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
