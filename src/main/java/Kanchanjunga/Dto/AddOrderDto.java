package Kanchanjunga.Dto;

import java.util.UUID;

import lombok.Data;

@Data
public class AddOrderDto {

	private String name;
	private UUID foodMenuId;
	private UUID drinkMenuId;

	private int quantity;

	private String imageName;


	private Double price;

	
}
