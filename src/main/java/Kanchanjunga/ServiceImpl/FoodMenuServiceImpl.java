package Kanchanjunga.ServiceImpl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.FoodMenuDto;
import Kanchanjunga.Entity.FoodMenu;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.FoodMenuRepo;
import Kanchanjunga.Utility.FilesHelper;

@Service
public class FoodMenuServiceImpl implements Kanchanjunga.Services.FoodMenuService {

	@Autowired
	private FoodMenuRepo foodMenuRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private FilesHelper filesHelper;

	@Override
	public Boolean createFoodMenu(FoodMenuDto foodMenuDto) {
		try {
			foodMenuDto.setId(UUID.randomUUID());
			FoodMenu createFoodMenu = this.mapper.map(foodMenuDto, FoodMenu.class);

			String filename = filesHelper.saveFile(foodMenuDto.getImage());

			createFoodMenu.setImage(filename);
			
			// in frontend we are seperating the food and drink item so dont remove this code !!!!!!!!!!!
			createFoodMenu.setType("Food");
			
			
			createFoodMenu.setCreatedDate(new Date());
			this.foodMenuRepo.save(createFoodMenu);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean updateFoodMenu(UUID id, String name, Double price, String category, String description, String type,
			MultipartFile image) {
		try {
			FoodMenu foodMenu = this.foodMenuRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
			// if user wants to update image
			if (image != null) {
				String filename = filesHelper.saveFile(image);

				// deleting file in project folder too after updating
				
				Boolean isDeleted = this.filesHelper.deleteExistingFile(foodMenu.getImage());

				if (isDeleted) {
					foodMenu.setName(name);
					foodMenu.setPrice(price);
					foodMenu.setType(type);
					foodMenu.setImage(filename);
					foodMenu.setCategory(category);
					foodMenu.setDescription(description);

					this.foodMenuRepo.save(foodMenu);

					return true;
				}

			}

			// if user dont want to update image

		
			foodMenu.setName(name);
			foodMenu.setPrice(price);
			foodMenu.setType(type);
			foodMenu.setImage(foodMenu.getImage());
			foodMenu.setCategory(category);
			foodMenu.setDescription(description);

			this.foodMenuRepo.save(foodMenu);

			return true;
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Boolean deleteFoodMenu(UUID id) {
		try {

			FoodMenu foodMenu = this.foodMenuRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Food", "Food Id", id));
			
			Boolean isDeleted = this.filesHelper.deleteExistingFile(foodMenu.getImage());
			
			if (isDeleted) {
				this.foodMenuRepo.delete(foodMenu);
				Optional<FoodMenu> checking = this.foodMenuRepo.findById(id);

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
	public List<FoodMenuDto> getAllFoodMenu() {
		try {
			List<FoodMenu> allFoodMenu = foodMenuRepo.findAll();

			List<FoodMenuDto> foodMenuDtos = allFoodMenu.stream().map(food -> {
				FoodMenuDto foodMenuDto = mapper.map(food, FoodMenuDto.class);
				foodMenuDto.setImageName(food.getImage());
				foodMenuDto.setOrders(food.getOrders());// Set the image from FoodMenu to FoodMenuDto
				return foodMenuDto;
			}).collect(Collectors.toList());

			if (!foodMenuDtos.isEmpty()) {
				return foodMenuDtos;
			}
		} catch (Exception e) {
			// Handle the exception appropriately (e.g., log it)
		}
		return Collections.emptyList();
	}

	@Override
	public FoodMenuDto getFoodMenuByID(UUID id) {
		try {

			FoodMenu foodMenu = this.foodMenuRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Food", "Food Id", id));

			FoodMenuDto foodMenuDto = this.mapper.map(foodMenu, FoodMenuDto.class);
			foodMenuDto.setImageName(foodMenu.getImage());

			if (foodMenuDto != null) {
				return foodMenuDto;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}

}
