package Kanchanjunga.Dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Entity.Orders;
import lombok.Data;

@Data
public class DrinkMenuDto {
	private UUID id;
	private String name;
	private Double price;
	private String category;
	private String description;
	private MultipartFile image;
	private String imageName;

	private List<Orders> orders;
	private Date createdDate;
	private Date lastModifiedDate;
}
