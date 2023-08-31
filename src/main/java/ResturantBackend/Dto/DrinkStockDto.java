package ResturantBackend.Dto;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrinkStockDto {

	private UUID id;
	private String name;
	private Double price;
	private int quantity;
	private String supplier;
	private Date expireDate;

	private String category;
	private String description;


	private Date createdDate;
	private Date lastModifiedDate;
}
