package Kanchanjunga.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.DrinkMenuDto;
import Kanchanjunga.Entity.DrinkMenu;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.DrinkMenuRepo;
import Kanchanjunga.Services.DrinkMenuService;
import Kanchanjunga.Utility.FilesHelper;

@Service
public class DrinkMenuServiceImpl implements DrinkMenuService {

	@Autowired
	private DrinkMenuRepo drinkMenuRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private FilesHelper fileHelper;

	@Override
	public Boolean createMenuDrinks(DrinkMenuDto data) {
		try {
			data.setId(UUID.randomUUID());
			DrinkMenu drinkMenu = mapper.map(data, DrinkMenu.class);
			String filename = fileHelper.saveFile(data.getImage());
			drinkMenu.setImage(filename);
			drinkMenu.setCreatedDate(new Date());
			drinkMenuRepo.save(drinkMenu);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean updateMenuDrinks(UUID id, String name, Double price, String category, String description,
			MultipartFile image) {
		try {
			// if user wants to update image
			DrinkMenu drinkFromDb = this.drinkMenuRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
			if (image != null) {
				String filename = fileHelper.saveFile(image);
				// deleting file in project folder too after updating
				Boolean isDeleted = fileHelper.deleteExistingFile(drinkFromDb.getImage());
				if (isDeleted) {
					drinkFromDb.setName(name);
					drinkFromDb.setPrice(price);
					drinkFromDb.setImage(filename);
					drinkFromDb.setCategory(category);
					drinkFromDb.setDescription(description);
					this.drinkMenuRepo.save(drinkFromDb);
					return true;
				}
				return false;
			}
			// if user don't want to update image
			drinkFromDb.setName(name);
			drinkFromDb.setPrice(price);
			drinkFromDb.setImage(drinkFromDb.getImage());
			drinkFromDb.setCategory(category);
			drinkFromDb.setDescription(description);
			this.drinkMenuRepo.save(drinkFromDb);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean deleteMenuDrinks(UUID id) {
		try {
			DrinkMenu drinkFromDb = this.drinkMenuRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
			Boolean isDeleted = fileHelper.deleteExistingFile(drinkFromDb.getImage());
			if (isDeleted) {
				this.drinkMenuRepo.delete(drinkFromDb);
				Optional<DrinkMenu> checking = this.drinkMenuRepo.findById(id);
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
	public List<DrinkMenuDto> getAllDrinksMenu() {
		try {
			List<DrinkMenu> allDrinkMenu = drinkMenuRepo.findAll();
			List<DrinkMenuDto> drinkMenuDtos = allDrinkMenu.stream()
					.map((drink) -> {
						DrinkMenuDto drinkMenuDto = this.mapper.map(drink, DrinkMenuDto.class);
						drinkMenuDto.setImageName(drink.getImage());
						return drinkMenuDto;
					}).collect(Collectors.toList());

			if (drinkMenuDtos.size() > 0) {
				return drinkMenuDtos;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public DrinkMenuDto getDrinkMenuByID(UUID id) {
		try {
			DrinkMenu drinkFromDb = this.drinkMenuRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
			DrinkMenuDto drinkMenuDto = this.mapper.map(drinkFromDb, DrinkMenuDto.class);
			drinkMenuDto.setImageName(drinkFromDb.getImage());
			return drinkMenuDto;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}