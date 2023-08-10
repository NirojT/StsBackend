package Kanchanjunga.ServiceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.FoodStockDto;
import Kanchanjunga.Entity.DrinkStock;
import Kanchanjunga.Entity.FoodStock;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.DrinkStockRepo;
import Kanchanjunga.Reposioteries.FoodStockRepo;
import Kanchanjunga.Utility.FilesHelper;

@Service
public class FoodStockServiceImpl implements Kanchanjunga.Services.FoodStockService {

	@Autowired
	private FoodStockRepo foodStockRepo;
	@Autowired
	private DrinkStockRepo drinkStockRepo ;
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private FilesHelper filesHelper;

	@Override
	public Boolean createStockFood(FoodStockDto foodStockDto) {
		try {
			foodStockDto.setId(UUID.randomUUID());
			FoodStock foodStock = this.mapper.map(foodStockDto, FoodStock.class);
			String filename = filesHelper.saveFile(foodStockDto.getImage());

			foodStock.setImage(filename);
			foodStock.setCreatedDate(new Date());
			foodStockRepo.save(foodStock);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public Boolean updateStockFood(UUID id, String name, Double price, int quantity, String supplier, Date expireDate,
			String category, String description, MultipartFile image) {
		try {
			FoodStock foodStockDb = this.foodStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", id));
			// if user wants to update image
			if (image != null) {
				String filename = filesHelper.saveFile(image);

				Boolean isdeleted = this.filesHelper.deleteExistingFile(foodStockDb.getImage());

				if (isdeleted) {
					foodStockDb.setName(name);
					foodStockDb.setPrice(price);
					foodStockDb.setQuantity(quantity);
					foodStockDb.setSupplier(supplier);
					foodStockDb.setExpireDate(expireDate);
					foodStockDb.setImage(filename);
					foodStockDb.setCategory(category);
					foodStockDb.setDescription(description);

					this.foodStockRepo.save(foodStockDb);

					return true;
				}

			}

			// if user dont want to update image

			foodStockDb.setName(name);
			foodStockDb.setPrice(price);
			foodStockDb.setImage(foodStockDb.getImage());
			foodStockDb.setCategory(category);
			foodStockDb.setDescription(description);

			this.foodStockRepo.save(foodStockDb);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean deleteStockFood(UUID id) {

		try {

			FoodStock foodStock = this.foodStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Food", "Food Id", id));

			this.foodStockRepo.delete(foodStock);
			Optional<FoodStock> checking = this.foodStockRepo.findById(id);

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
	public List<FoodStockDto> getAllFoodStock() {

		try {
			List<FoodStock> allFoodStocks = this.foodStockRepo.findAll();
			List<FoodStockDto> allFoodStockDto = allFoodStocks.stream().map((stock) -> {
				FoodStockDto foodStockDto = this.mapper.map(stock, FoodStockDto.class);
				foodStockDto.setImageName(stock.getImage());
				return foodStockDto;
			}).collect(Collectors.toList());

			if (allFoodStockDto.size() > 0) {
				return allFoodStockDto;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public FoodStockDto getFoodStockByID(UUID id) {

		try {
			FoodStock foodStock = this.foodStockRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Food", "Food Id", id));

			FoodStockDto foodStockDto = this.mapper.map(foodStock, FoodStockDto.class);
			foodStockDto.setImageName(foodStock.getImage());
			if (foodStockDto != null) {
				return foodStockDto;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//for drinkstock  and foodstock based on monthly....
//	@Override
//	public double getMonthlyExpense() {
//		//localDate give date only not time
//		LocalDate currentdate = LocalDate.now();
//		YearMonth currentYearMonth = YearMonth.from(currentdate);
//		
//		
//		double foodExpense = this.foodStockRepo.findAll().stream()
//				 .filter(stock -> YearMonth.from(stock.getCreatedDate().toInstant()).equals(currentYearMonth))
//				
//				.mapToDouble(stock->stock.getPrice()).sum();
//		double drinkExpense = this.drinkStockRepo.findAll().stream()
//				.filter(stock->YearMonth.from(stock.getCreatedDate().toInstant()).equals(currentYearMonth))
//				.mapToDouble(stock->stock.getPrice()).sum();
//		//alternative
//		//double sum = this.foodStockRepo.findAll().stream().mapToDouble(FoodStock::getPrice).sum();
//		double totalExpenses=0.0;
//		if(foodExpense!=0.0 || drinkExpense!=0.0)
//			{
//			 totalExpenses=foodExpense+drinkExpense;
//			}
//		System.out.println("totalExpenses is "+totalExpenses);
//		return totalExpenses;
//	}

	//according to month 
	@Override
	public double getMonthlyExpense() {
	    // Get the current date-time in the default time zone
	    ZonedDateTime currentDateTime = ZonedDateTime.now();

	    // Extract the year and month components
	    int year = currentDateTime.getYear();
	    int month = currentDateTime.getMonthValue();

	    // Create a YearMonth instance
	    YearMonth currentYearMonth = YearMonth.of(year, month);

	    double foodExpense = this.foodStockRepo.findAll().stream()
	            .filter(stock -> {
	                Instant createdInstant = stock.getCreatedDate().toInstant();
	                LocalDate createdDate = createdInstant.atZone(ZoneId.systemDefault()).toLocalDate();
	                return YearMonth.from(createdDate).equals(currentYearMonth);
	            })
	            .mapToDouble(FoodStock::getPrice)
	            .sum();

	    double drinkExpense = this.drinkStockRepo.findAll().stream()
	            .filter(stock -> {
	                Instant createdInstant = stock.getCreatedDate().toInstant();
	                LocalDate createdDate = createdInstant.atZone(ZoneId.systemDefault()).toLocalDate();
	                return YearMonth.from(createdDate).equals(currentYearMonth);
	            })
	            .mapToDouble(DrinkStock::getPrice)
	            .sum();

	    double totalExpenses = 0.0;
	    if (foodExpense != 0.0 || drinkExpense != 0) {
	        totalExpenses = foodExpense + drinkExpense;
	    }
	    System.out.println("totalExpenses is "+totalExpenses);
	    return totalExpenses;
	}
	
	
	
	// according to 30days exactly
//	public double getLast30DaysExpense() {
//	    // Get the current date
//	    LocalDate currentDate = LocalDate.now();
//
//	    // Calculate the date that is 30 days ago from the current date
//	    LocalDate thirtyDaysAgo = currentDate.minusDays(30);
//
//	    double foodExpense = this.foodStockRepo.findAll().stream()
//	            .filter(stock -> {
//	                LocalDate createdDate = stock.getCreatedDate().toInstant()
//	                        .atZone(ZoneId.systemDefault())
//	                        .toLocalDate();
//	                return !createdDate.isBefore(thirtyDaysAgo) && !createdDate.isAfter(currentDate);
//	            })
//	            .mapToDouble(FoodStock::getPrice)
//	            .sum();
//
//	    double drinkExpense = this.drinkStockRepo.findAll().stream()
//	            .filter(stock -> {
//	                LocalDate createdDate = stock.getCreatedDate().toInstant()
//	                        .atZone(ZoneId.systemDefault())
//	                        .toLocalDate();
//	                return !createdDate.isBefore(thirtyDaysAgo) && !createdDate.isAfter(currentDate);
//	            })
//	            .mapToDouble(DrinkStock::getPrice)
//	            .sum();
//
//	    double totalExpenses = 0.0;
//	    if (foodExpense != 0.0 || drinkExpense != 0) {
//	        totalExpenses = foodExpense + drinkExpense;
//	    }
//	    System.out.println("totalExpenses for the last 30 days is " + totalExpenses);
//	    return totalExpenses;
//	}
	
	
	
}
