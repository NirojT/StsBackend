package Kanchanjunga.Dto;

import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Entity.Orders;
import lombok.Data;

@Data
public class FoodMenuDto {
	private UUID id;
	private String name;
	private Double price;
	private String category;
	private String description;
	private String remarks;
	private String type;
	private String remarks;
	private int quantity;
	private MultipartFile image;
	private String imageName;

	private Orders orders;
	private Date createdDate;
	private Date lastModifiedDate;
}
