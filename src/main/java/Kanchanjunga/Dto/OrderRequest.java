package Kanchanjunga.Dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
	private List<AddOrderDto> addOrderDtos;
	private String tableNo;
	private String totalPrice;
	private String remarks;
}
