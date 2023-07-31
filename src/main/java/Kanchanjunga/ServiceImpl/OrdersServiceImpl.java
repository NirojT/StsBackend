package Kanchanjunga.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Kanchanjunga.Dto.OrdersDto;
import Kanchanjunga.Entity.DrinkMenu;
import Kanchanjunga.Entity.FoodMenu;
import Kanchanjunga.Entity.Orders;
import Kanchanjunga.Entity.Payment;
import Kanchanjunga.Entity.Users;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.DrinkMenuRepo;
import Kanchanjunga.Reposioteries.FoodMenuRepo;
import Kanchanjunga.Reposioteries.OrdersRepo;
import Kanchanjunga.Reposioteries.PaymentRepo;
import Kanchanjunga.Reposioteries.UserRepo;

@Service
public class OrdersServiceImpl implements Kanchanjunga.Services.OrdersService {

	@Autowired
	private OrdersRepo ordersRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private FoodMenuRepo foodMenuRepo;

	@Autowired
	private DrinkMenuRepo drinkMenuRepo;



	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean createOrders(OrdersDto orderDto, UUID userId, UUID foodMenuId, UUID drinkMenuId) {
		try {
			Users userFromDb = this.userRepo.findById(userId)
					.orElseThrow(() -> new ResourceNotFound("User", "user id", userId));

			FoodMenu foodMenuFromDb = this.foodMenuRepo.findById(userId)
					.orElseThrow(() -> new ResourceNotFound("foodMenu", "foodMenu id", foodMenuId));

			DrinkMenu drinkMenuFromDb = this.drinkMenuRepo.findById(userId)
					.orElseThrow(() -> new ResourceNotFound("drinkMenu", "drinkMenu id", drinkMenuId));

			
			Orders orders = this.mapper.map(orderDto, Orders.class);
			orders.setId(UUID.randomUUID());
			orderDto.setCreatedDate(new Date());
			orders.setUsers(userFromDb);
			orders.setFoodMenu(foodMenuFromDb);
			orders.setDrinkMenu(drinkMenuFromDb);
	

			Orders savedOrder = this.ordersRepo.save(orders);

			if (savedOrder != null) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean updateOrders(UUID id, String tableNo, Double price, int quantity, String item) {

		try {

			Orders ordersFromDb = this.ordersRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("order", "order id", id));

			ordersFromDb.setTableNo(tableNo);
			ordersFromDb.setPrice(price);
			ordersFromDb.setQuantity(quantity);
			ordersFromDb.setItem(item);
			Orders updatedOrder = this.ordersRepo.save(ordersFromDb);

			if (updatedOrder != null) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean deleteOrders(UUID id) {

		try {
			Orders ordersFromDb = this.ordersRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("order", "order id", id));

			this.ordersRepo.delete(ordersFromDb);
			Optional<Orders> isAvailable = this.ordersRepo.findById(id);

			if (isAvailable.isPresent()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return false;
	}

	@Override
	public List<OrdersDto> getAllOrders() {
		try {
			List<Orders> allOrders = this.ordersRepo.findAll();

			List<OrdersDto> allOrdersDto = allOrders.stream().map((order)->{
				OrdersDto ordersDto = this.mapper.map(order, OrdersDto.class);
				ordersDto.setUsers(order.getUsers());
				ordersDto.setPayment(order.getPayment());
				return ordersDto;
			}).collect(Collectors.toList());
			
			if (allOrdersDto.size()>0) {
				return allOrdersDto;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public OrdersDto getOrdersByID(UUID id) {
		try {
			Orders ordersFromDb = this.ordersRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("order", "order id", id));

			OrdersDto ordersDto = this.mapper.map(ordersFromDb, OrdersDto.class);
			

			if (ordersDto!=null) {
				return ordersDto;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

}
