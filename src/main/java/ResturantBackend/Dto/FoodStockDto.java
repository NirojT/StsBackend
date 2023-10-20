package ResturantBackend.Dto;

import java.util.Date;
import java.util.UUID;


import lombok.Data;

@Data
public class FoodStockDto {
	private UUID id;
	private String name;
	private Double price;
	private int quantity;
	private String supplier;
	private Date expireDate;

	private String category;
	private String description;
	
	private Date createdDate;
	private String createdNepDate;
	private Date lastModifiedDate;
}
