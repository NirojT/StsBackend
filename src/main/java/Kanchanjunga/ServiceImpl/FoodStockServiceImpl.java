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

import Kanchanjunga.Dto.FoodStockDto;
import Kanchanjunga.Entity.FoodStock;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.FoodStockRepo;
import Kanchanjunga.Utility.FilesHelper;

@Service
public class FoodStockServiceImpl implements Kanchanjunga.Services.FoodStockService {

	@Autowired
	private FoodStockRepo foodStockRepo;
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

}
