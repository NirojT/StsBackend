package Kanchanjunga.Entity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data	
@Document
public class DrinkMenu {

	@Id
	private UUID id;
	private String name;
	private String price;
	private String category;
	private String description;
	private String image;
	
	@DBRef
	private List<Orders>  orders;
}
