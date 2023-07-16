package Kanchanjunga.Entity;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class FoodStock {
	
	
	private UUID id;
	private String name;
	private String price;
	private String quantity;
	private String supplier;
	private String expireDate;

	private String category;
	private String description;
	private String image;

}
