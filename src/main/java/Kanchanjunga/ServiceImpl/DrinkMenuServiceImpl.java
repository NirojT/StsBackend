package Kanchanjunga.ServiceImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.server.UID;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.KanchanjungaApplication;
import Kanchanjunga.Dto.DrinkMenuDto;
import Kanchanjunga.Entity.DrinkMenu;
import Kanchanjunga.Entity.Users;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.DrinkMenuRepo;
import Kanchanjunga.Services.DrinkMenuService;

@Service
public class DrinkMenuServiceImpl implements DrinkMenuService {

	@Autowired
	private DrinkMenuRepo drinkMenuRepo;

	// @Autowired
	// private DrinkMenu drinkMenu;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean createMenuDrinks(DrinkMenuDto data) {
		try {
			data.setId(UUID.randomUUID());
			DrinkMenu drinkMenu = mapper.map(data, DrinkMenu.class);
			String filename = data.getImage().getOriginalFilename();
			String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static";
			Path paths = Paths.get(uploadDir);
			if (!Files.exists(paths)) {
				Files.createDirectories(paths);
			}
			Path imagePath = paths.resolve(filename);
			try (InputStream inputStream = data.getImage().getInputStream()) {
				Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new RuntimeException("Fail to save file " + filename, e);
			}
			
			drinkMenu.setImage(KanchanjungaApplication.SERVERURL + filename);
			drinkMenu.setCreatedDate(new Date());
			drinkMenuRepo.save(drinkMenu);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// @Override
	// public void addMenuDrinks(DrinkMenuDto data) {
	// try {
	// // Create a new DrinkMenu entity
	// DrinkMenu drinkMenu = new DrinkMenu();
	// drinkMenu.setId(UUID.randomUUID());
	// drinkMenu.setName(data.getName());
	// drinkMenu.setPrice(data.getPrice());
	// drinkMenu.setCategory(data.getCategory());
	// drinkMenu.setDescription(data.getDescription());

	// // Save the uploaded image file to a folder
	// MultipartFile image = data.getImage();
	// String filename = image.getOriginalFilename();
	// File newFile = new File("src/uploads/" + filename);
	// image.transferTo(newFile);

	// // Set the image filename in the DrinkMenu entity
	// drinkMenu.setImage(filename);

	// // Save the DrinkMenu entity to the database
	// drinkMenuRepo.save(drinkMenu);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	@Override
	public Boolean updateMenuDrinks(UUID id, String name, Double price, String category, String description,
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
				DrinkMenu drinkFromDb = this.drinkMenuRepo.findById(id)
						.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
				
				String deletePhoto = drinkFromDb.getImage().replace(KanchanjungaApplication.SERVERURL, "");
				
				Path filePath = Paths.get(uploadDir, deletePhoto);
				Files.deleteIfExists(filePath);
				

				
				drinkFromDb.setName(name);
				drinkFromDb.setPrice(price);
				drinkFromDb.setImage(KanchanjungaApplication.SERVERURL + filenames);
				drinkFromDb.setCategory(category);
				drinkFromDb.setDescription(description);

				this.drinkMenuRepo.save(drinkFromDb);

				return true;

			}

			// if user dont want to update image
			else {

				DrinkMenu drinkFromDb = this.drinkMenuRepo.findById(id)
						.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
				drinkFromDb.setName(name);
				drinkFromDb.setPrice(price);
				drinkFromDb.setImage(KanchanjungaApplication.SERVERURL + imageName);
				drinkFromDb.setCategory(category);
				drinkFromDb.setDescription(description);

				this.drinkMenuRepo.save(drinkFromDb);

				return true;
			}

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
			
			// deleting file in project folder tooo
						String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static";

						String deletePhoto = drinkFromDb.getImage().replace(KanchanjungaApplication.SERVERURL, "");
						System.out.println(deletePhoto);
						Path filePath = Paths.get(uploadDirectory, deletePhoto);
						Files.deleteIfExists(filePath);
			
			this.drinkMenuRepo.delete(drinkFromDb);
			Optional<DrinkMenu> checking = this.drinkMenuRepo.findById(id);

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
	public List<DrinkMenuDto> getAllDrinksMenu() {
		try {
		
			List<DrinkMenu> allDrinkMenu = drinkMenuRepo.findAll();
			
			List<DrinkMenuDto> drinkMenuDtos = allDrinkMenu.stream()
			.map(drink->{
				DrinkMenuDto drinkMenuDto = this.mapper.map(drink, DrinkMenuDto.class);
				drinkMenuDto.setImageName(drink.getImage());
				return drinkMenuDto;
			}).collect(Collectors.toList());
			
			if (drinkMenuDtos.size() > 0) {
				return drinkMenuDtos;
			}
		} catch (Exception e) {
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
			if (drinkMenuDto != null) {
				return drinkMenuDto;
			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}