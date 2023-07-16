package Kanchanjunga.Dto;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Entity.Orders;
import lombok.Data;

@Data
public class FoodMenuDto {
	private UUID id;
	private String name;
	private String price;
	private String category;
	private String description;
	private MultipartFile image;
	
	private List<Orders>  orders;
}
