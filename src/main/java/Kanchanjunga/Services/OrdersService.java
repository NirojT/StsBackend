package Kanchanjunga.Services;

import java.util.List;
import java.util.UUID;

import Kanchanjunga.Dto.AddOrderDto;
import Kanchanjunga.Dto.OrderRequest;
import Kanchanjunga.Dto.OrdersDto;

public interface OrdersService {

	// Boolean createOrders(OrdersDto orderDto, UUID userId, UUID foodMenuId, UUID
	// drinkMenuId);
	Boolean createOrders(OrderRequest orderRequest, String username);

	Boolean updateOrders(UUID id, String tableNo, Double price, List<AddOrderDto> item, String status);

	Boolean deleteOrders(UUID id);

	List<OrdersDto> getAllOrders();

	List<OrdersDto> getLatestOrders();

	OrdersDto getOrdersByID(UUID id);

	
	int getNoOfOrdersBy1Day();
	
	 int getOrderNoWeekly();
	
	int getNoOfOrdersByCurrentMonth() ;
	


	Boolean updateStatus(UUID id, String status);

}
