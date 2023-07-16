package Kanchanjunga.Dto;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FoodStockDto {
	private UUID id;
	private String name;
	private String price;
	private String quantity;
	private String supplier;
	private String expireDate;

	private String category;
	private String description;
	private MultipartFile image;
}
