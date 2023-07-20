package Kanchanjunga.Services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import Kanchanjunga.Dto.OrdersDto;

public interface OrdersService {

	Boolean createOrders(OrdersDto orderDto, UUID userId, UUID paymentId, UUID foodMenuId, UUID drinkMenuId);

	Boolean updateOrders(UUID id, String tableNo, Double price, int quantity, String item);

	Boolean deleteOrders(UUID id);

	List<OrdersDto> getAllOrders();

	OrdersDto getOrdersByID(UUID id);
}
