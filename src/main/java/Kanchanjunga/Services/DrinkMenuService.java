package Kanchanjunga.Services;

import java.util.List;
import java.util.UUID;

import Kanchanjunga.Dto.DrinkMenuDto;

public interface DrinkMenuService {
    Boolean createMenuDrinks(DrinkMenuDto data);

    void updateMenuDrinks(DrinkMenuDto data, UUID id);

    void deleteMenuDrinks(UUID id);

    List<DrinkMenuDto> getAllDrinksMenu();

    void getDrinkMenuByID(UUID id);
}
