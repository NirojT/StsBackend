package Kanchanjunga.Entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import Kanchanjunga.Dto.AddOrderDto;
import lombok.Data;

@Data
@Document
public class Orders {

	@Id
	private UUID id;
	private String tableNo;
	private Double price;
	private int quantity;
	private String remarks;

	private List<AddOrderDto> items;
	private String status = "pending";

	@DBRef
	private Users users;

	@DBRef
	private Payment payment;

	@DBRef
	private List<FoodMenu> foodMenus;

	@DBRef
	private List<DrinkMenu> drinkMenus;

	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date lastModifiedDate;

}
