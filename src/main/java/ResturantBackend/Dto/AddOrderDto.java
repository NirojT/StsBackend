package ResturantBackend.Dto;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class AddOrderDto {

	private String name;
	private String type;
	private String description;
	private String category;
	private String remarks;
	private int quantity;
	private String imageName;
	private Double price;
	private Date expireDate;
	private UUID foodMenuId;
	private UUID drinkMenuId;

}
