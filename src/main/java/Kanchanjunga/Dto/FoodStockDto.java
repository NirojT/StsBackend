package Kanchanjunga.Dto;

import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FoodStockDto {
	private UUID id;
	private String name;
	private Double price;
	private int quantity;
	private String supplier;
	private Date expireDate;

	private String category;
	private String description;
	private MultipartFile image;
}
