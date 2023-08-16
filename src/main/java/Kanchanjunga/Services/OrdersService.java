package Kanchanjunga.Services;

import java.util.List;
import java.util.UUID;

import Kanchanjunga.Dto.OrderRequest;
import Kanchanjunga.Dto.OrdersDto;
import Kanchanjunga.Entity.FoodMenu;

public interface OrdersService {

	Boolean createOrders(OrderRequest orderRequest, String username);

	Boolean updateOrders(UUID id, OrderRequest orderRequest);

	Boolean deleteOrders(UUID id);

	List<OrdersDto> getAllOrders();

	List<OrdersDto> getLatestOrders();

	OrdersDto getOrdersByID(UUID id);

	List<OrdersDto> getMyOrders(String name);

	int getNoOfOrdersBy1Day();

	int getOrderNoWeekly();

	int getNoOfOrdersByCurrentMonth();

	Boolean updateStatus(UUID id, String status);
	
	List<FoodMenu> getMostOrderedFood();

}
