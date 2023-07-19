package Kanchanjunga.Entity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Document
public class FoodMenu {

	@Id
	private UUID id;
	private String name;
	private Double price;
	private String category;
	private String description;
	private String image;
	
	@DBRef
	private List<Orders>  orders;
}

