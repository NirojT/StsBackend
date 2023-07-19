package Kanchanjunga.ServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.server.UID;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.KanchanjungaApplication;
import Kanchanjunga.Dto.FoodMenuDto;
import Kanchanjunga.Entity.DrinkMenu;
import Kanchanjunga.Entity.FoodMenu;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.FoodMenuRepo;

@Service
public class FoodMenuServiceImpl implements Kanchanjunga.Services.FoodMenuService {

	@Autowired
	private FoodMenuRepo foodMenuRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean createFoodMenu(FoodMenuDto foodMenuDto) {
		try {
			foodMenuDto.setId(UUID.randomUUID());
			FoodMenu createFoodMenu = this.mapper.map(foodMenuDto, FoodMenu.class);

			String filename = foodMenuDto.getImage().getOriginalFilename();
			String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static";
			Path paths = Paths.get(uploadDir);
			if (!Files.exists(paths)) {
				Files.createDirectories(paths);
			}
			Path imagePath = paths.resolve(filename);
			try (InputStream inputStream = foodMenuDto.getImage().getInputStream()) {
				Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new RuntimeException("Fail to save file " + filename, e);
			}
			createFoodMenu.setImage(KanchanjungaApplication.SERVERURL + filename);
			this.foodMenuRepo.save(createFoodMenu);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean updateFoodMenu(UUID id, String name, Double price, String category, String description,
			MultipartFile image, String imageName) {
		try {
			// if user wants to update image
			if (image != null) {
				String filename = image.getOriginalFilename();
				// uuid for unique name of image
				UID uid = new UID();
				String uidString = uid.toString().replace(':', '_');

				String extension = "";
				int dotIndex = image.getOriginalFilename().lastIndexOf('.');
				if (dotIndex > 0) {
					extension = filename.substring(dotIndex);
					filename = filename.substring(0, dotIndex);

				}
				// for multipart
				String filenames = filename + "_" + uidString + extension;

				String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static";

				Path paths = Paths.get(uploadDir);

				if (!Files.exists(paths)) {
					Files.createDirectories(paths);
				}

				Path imagePath = paths.resolve(filename);
				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					throw new RuntimeException("Fail to save file " + filename, e);
				}

				// deleting file in project folder too after updating
				FoodMenu foodMenu = this.foodMenuRepo.findById(id)
						.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));

				String deletePhoto = foodMenu.getImage().replace(KanchanjungaApplication.SERVERURL, "");
				System.out.println(deletePhoto);
				Path filePath = Paths.get(uploadDir, deletePhoto);
				Files.deleteIfExists(filePath);

				foodMenu.setName(name);
				foodMenu.setPrice(price);
				foodMenu.setImage(KanchanjungaApplication.SERVERURL + filenames);
				foodMenu.setCategory(category);
				foodMenu.setDescription(description);

				this.foodMenuRepo.save(foodMenu);

				return true;

			}

			// if user dont want to update image
			else {

				FoodMenu foodMenu = this.foodMenuRepo.findById(id)
						.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
				foodMenu.setName(name);
				foodMenu.setPrice(price);
				foodMenu.setImage(KanchanjungaApplication.SERVERURL + imageName);
				foodMenu.setCategory(category);
				foodMenu.setDescription(description);

				this.foodMenuRepo.save(foodMenu);

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Boolean deleteFoodMenu(UUID id) {
		try {

			FoodMenu foodMenu = this.foodMenuRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Food", "Food Id", id));
			this.foodMenuRepo.delete(foodMenu);
			Optional<FoodMenu> checking = this.foodMenuRepo.findById(id);

			if (checking.isPresent()) {
				return false;
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<FoodMenuDto> getAllFoodMenu() {
	    try {
	        List<FoodMenu> allFoodMenu = foodMenuRepo.findAll();

	        List<FoodMenuDto> foodMenuDtos = allFoodMenu.stream()
	                .map(food -> {
	                    FoodMenuDto foodMenuDto = mapper.map(food, FoodMenuDto.class);
	                    foodMenuDto.setImageName(food.getImage()); // Set the image from FoodMenu to FoodMenuDto
	                    return foodMenuDto;
	                })
	                .collect(Collectors.toList());

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
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
