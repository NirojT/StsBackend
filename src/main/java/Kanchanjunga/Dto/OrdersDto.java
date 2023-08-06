package Kanchanjunga.Dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import Kanchanjunga.Entity.Payment;
import Kanchanjunga.Entity.Users;
import lombok.Data;

@Data
public class OrdersDto {

	private UUID id;
	private String tableNo;
	private Double price;
	private int quantity;

	private String status = "pending";
	
	private List<AddOrderDto> items;

	private UserDTO users;

	private PaymentDTO payment;

	private List<DrinkMenuDto> drinkMenus;

	private List<FoodMenuDto> foodMenus;

	private Date createdDate;

	private Date lastModifiedDate;
}
