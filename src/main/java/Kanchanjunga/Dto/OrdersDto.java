package Kanchanjunga.Dto;

import java.util.Date;
import java.util.UUID;

import Kanchanjunga.Entity.DrinkMenu;
import Kanchanjunga.Entity.FoodMenu;
import Kanchanjunga.Entity.Payment;
import Kanchanjunga.Entity.Users;
import lombok.Data;

@Data
public class OrdersDto {

	private UUID id;
	private String tableNo;
	private Double price;
	private int quantity;
	private String item;


	
	private Users users;
	
	
	private Payment payment;
	
	
	private FoodMenu foodMenu;
	

	private DrinkMenu drinkMenu;
	
	
	private Date createdDate;

	
	private Date lastModifiedDate;
}
