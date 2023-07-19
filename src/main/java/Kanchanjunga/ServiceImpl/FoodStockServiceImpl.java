package Kanchanjunga.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.FoodStockDto;
import Kanchanjunga.Entity.FoodStock;
import Kanchanjunga.Reposioteries.FoodStockRepo;

@Service
public class FoodStockServiceImpl implements Kanchanjunga.Services.FoodStockService {

	@Autowired
	private FoodStockRepo foodStockRepo;
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Boolean createStockFood(FoodStockDto foodStockDto) {
		try {
			foodStockDto.setId(UUID.randomUUID());
			FoodStock foodStock = this.mapper.map(foodStockDto, FoodStock.class);
			String filename = foodStockDto.getImage().getOriginalFilename();
			String uploadDir =System.getProperty("user.dir")+"/src/main/resources/static";
			
			
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Boolean updateStockFood(UUID id, String name, Double price, int quantity, String supplier, Date expireDate,
			String category, String description, MultipartFile image, String imageName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteStockFood(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FoodStockDto> getAllFoodStock() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FoodStockDto getFoodStockByID(UUID id) {
		
		return null;
	}

}
