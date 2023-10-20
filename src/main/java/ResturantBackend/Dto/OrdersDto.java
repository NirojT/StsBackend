package ResturantBackend.Dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ResturantBackend.Entity.Table;
import lombok.Data;

@Data
public class OrdersDto {

	private UUID id;
	private String tableNo;
	private String remarks;
	private Double price;
	private int quantity;

	private String status = "pending";

	private List<AddOrderDto> items;

	private UserDTO users;

	private PaymentDTO payment;

	private List<DrinkMenuDto> drinkMenus;

	private List<FoodMenuDto> foodMenus;

	private Table table;

	private Date createdDate;
	private String createdNepDate;

	private Date lastModifiedDate;
}