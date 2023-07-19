package Kanchanjunga.Controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Dto.DrinkMenuDto;
import Kanchanjunga.Services.DrinkMenuService;

@RestController
@RequestMapping("/api/drinks/menu/")
public class DrinkMenuController {

    @Autowired
    private DrinkMenuService drinkMenuService;

    @PostMapping("create")
    public ResponseEntity<?> createDrinksMenu(@ModelAttribute DrinkMenuDto request) {
        Boolean isSaved = drinkMenuService.createMenuDrinks(request);
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (isSaved) {
            response.put("status", 200);
            response.put("message", "drinks added successfully");
            return ResponseEntity.status(200).body(response);
        }
        response.put("status", 400);
        response.put("message", "drinks not added");
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllDrinksMenu() {
        List<DrinkMenuDto> drinks = this.drinkMenuService.getAllDrinksMenu();
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (drinks != null) {
            response.put("status", 200);
            response.put("message", "drinks fetched successfully");
            response.put("drinks", drinks);
            return ResponseEntity.status(200).body(response);
        }
        response.put("status", 400);
        response.put("message", "drinks not found");
        return ResponseEntity.status(200).body(response);
    }
}