package Kanchanjunga.ServiceImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import Kanchanjunga.KanchanjungaApplication;
import Kanchanjunga.Dto.DrinkMenuDto;
import Kanchanjunga.Entity.DrinkMenu;
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
    public void updateMenuDrinks(DrinkMenuDto data, UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'updateMenuDrinks'");
    }

    @Override
    public void deleteMenuDrinks(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteMenuDrinks'");
    }

    @Override
    public void getAllDrinksMenu() {
        throw new UnsupportedOperationException("Unimplemented method 'getAllDrinksMenu'");
    }

    @Override
    public void getDrinkMenuByID(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'getDrinkMenuByID'");
    }

}
