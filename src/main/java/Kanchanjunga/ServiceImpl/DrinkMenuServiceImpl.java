package Kanchanjunga.ServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void updateMenuDrinks(DrinkMenuDto data, UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'updateMenuDrinks'");
    }

    @Override
    public void deleteMenuDrinks(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteMenuDrinks'");
    }

    @Override
    public List<DrinkMenuDto> getAllDrinksMenu() {
        try {
            List<DrinkMenu> allDrinks = this.drinkMenuRepo.findAll();
            // List<DrinkMenuDto> drinks = allDrinks.stream().map((drink) ->
            // this.mapper.map(drink, DrinkMenuDto.class))
            // .collect(Collectors.toList());
            List<DrinkMenuDto> drinks = allDrinks.stream().map(drink -> {
                DrinkMenuDto drinkMenuDTO = this.mapper.map(drink, DrinkMenuDto.class);
                drinkMenuDTO.setImageName(drink.getImage());
                return drinkMenuDTO;
            }).collect(Collectors.toList());
            return drinks;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void getDrinkMenuByID(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'getDrinkMenuByID'");
    }

}
