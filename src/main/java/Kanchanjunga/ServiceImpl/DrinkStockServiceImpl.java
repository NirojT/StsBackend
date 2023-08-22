package Kanchanjunga.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Kanchanjunga.Dto.DrinkStockDto;
import Kanchanjunga.Entity.DrinkStock;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.DrinkStockRepo;
import Kanchanjunga.Services.DrinkStockService;

@Service
public class DrinkStockServiceImpl implements DrinkStockService {

	@Autowired
	private DrinkStockRepo drinkStockRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean createStockDrinks(DrinkStockDto drinkStockDto) {
		try {
			drinkStockDto.setId(UUID.randomUUID());
			drinkStockDto.setCreatedDate(new Date());
			System.out.println(drinkStockDto.toString());
			DrinkStock drinkStock = mapper.map(drinkStockDto, DrinkStock.class);
			
			System.out.println(drinkStock.toString());

			
			drinkStockRepo.save(drinkStock);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Boolean updateStockDrinks(UUID id, String name, Double price, Integer quantity, String supplier,
			Date expireDate,
			String category, String description) {
		try {
			DrinkStock dbStockDrink = this.drinkStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));

			dbStockDrink.setName(name);
			dbStockDrink.setPrice(price);
			if (quantity != null)
				dbStockDrink.setQuantity(quantity);

			dbStockDrink.setSupplier(supplier);
			dbStockDrink.setExpireDate(expireDate);
			dbStockDrink.setDescription(description);
			dbStockDrink.setCategory(category);

			if (dbStockDrink != null) {
				this.drinkStockRepo.save(dbStockDrink);
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public Boolean deleteStockDrinks(UUID id) {
		try {
			DrinkStock dbStockDrink = this.drinkStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));

			this.drinkStockRepo.delete(dbStockDrink);
			Optional<DrinkStock> checking = this.drinkStockRepo.findById(id);
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
	public List<DrinkStockDto> getAllDrinksStock() {
		try {
			List<DrinkStock> allDrinkMenu = drinkStockRepo.findAll();
			List<DrinkStockDto> stockDrinks = allDrinkMenu.stream().map((drink) -> {
				DrinkStockDto drinkStockDTO = this.mapper.map(drink, DrinkStockDto.class);

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

			return drinkStockDTO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Map<String, Object>> getStockDrinkNameAndQuantity() {
		try {
			List<DrinkStock> collectedStock = this.drinkStockRepo.findAll(Sort.by(Sort.Direction.DESC,"createdDate"))
					.stream().limit(5).map((stock)->new DrinkStock(stock.getName(), stock.getQuantity()))
					.collect(Collectors.toList());
			
			List<Map<String, Object>>stockList=new ArrayList<>();
			
			
			for(int i=0; i<collectedStock.size(); i++) {
				Map<String, Object> stocks=new HashMap<>();
				
				
				String name = collectedStock.get(i).getName();
				
				int quantity = collectedStock.get(i).getQuantity();
				
				stocks.put("name", name);
				stocks.put("quantity", quantity);
				
				stockList.add(stocks);
				
			}
			if( stockList.size()>0) {
				return stockList;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
		
	}
	

}
