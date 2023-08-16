package Kanchanjunga.Entity;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document
public class FoodMenu {
	
	@Id
	private UUID id;
	private String name;
	private Double price;
	private String type;
	private String category;
	private String description;
	private String image;
	private int quantity;
	private int frequency;
	@DBRef
	private Orders orders;
	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date lastModifiedDate;

}
