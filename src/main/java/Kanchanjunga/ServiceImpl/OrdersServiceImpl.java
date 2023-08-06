package Kanchanjunga.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;

import Kanchanjunga.Dto.AddOrderDto;
import Kanchanjunga.Dto.DrinkMenuDto;
import Kanchanjunga.Dto.FoodMenuDto;
import Kanchanjunga.Dto.OrdersDto;
import Kanchanjunga.Dto.UserDTO;
import Kanchanjunga.Entity.DrinkMenu;
import Kanchanjunga.Entity.FoodMenu;
import Kanchanjunga.Entity.Orders;
import Kanchanjunga.Entity.Users;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.DrinkMenuRepo;
import Kanchanjunga.Reposioteries.FoodMenuRepo;
import Kanchanjunga.Reposioteries.OrdersRepo;
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
	public Boolean createOrders(List<AddOrderDto> addOrderDtos, String tableNo, String totalPrice, UUID userID) {

		try {
			List<AddOrderDto> item = addOrderDtos.stream().map((order) -> {
				UUID foodMenuId = order.getFoodMenuId();
				UUID drinkMenuId = order.getDrinkMenuId();

				int quantity = order.getQuantity();

				FoodMenu foodMenu;
				DrinkMenu drinkMenu;

				if (foodMenuId != null) {
					foodMenu = this.foodMenuRepo.findById(foodMenuId)
							.orElseThrow(() -> new ResourceNotFound("Food", "Food Id", foodMenuId));

					foodMenu.setQuantity(quantity);
				}
				if (drinkMenuId != null) {
					drinkMenu = this.drinkMenuRepo.findById(drinkMenuId)
							.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", drinkMenuId));
					drinkMenu.setQuantity(quantity);
				}

				return order;
			}).collect(Collectors.toList());

			Users userFromDb = this.userRepo.findById(userID)
					.orElseThrow(() -> new ResourceNotFound("User", "user id", userID));

			Orders orders = new Orders();
			orders.setTableNo(tableNo);
			orders.setCreatedDate(new Date());
			orders.setPrice(Double.parseDouble(totalPrice));
			orders.setItems(item);
			orders.setUsers(userFromDb);

			Orders savedOrder = this.ordersRepo.save(orders);
			if (savedOrder != null) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	// @Override
	// public Boolean createOrders(OrdersDto orderDto, UUID userId, UUID foodMenuId,
	// UUID drinkMenuId) {
	// try {
	// Users userFromDb = this.userRepo.findById(userId)
	// .orElseThrow(() -> new ResourceNotFound("User", "user id", userId));
	//
	// FoodMenu foodMenuFromDb = this.foodMenuRepo.findById(foodMenuId)
	// .orElseThrow(() -> new ResourceNotFound("foodMenu", "foodMenu id",
	// foodMenuId));
	//
	// DrinkMenu drinkMenuFromDb = this.drinkMenuRepo.findById(drinkMenuId)
	// .orElseThrow(() -> new ResourceNotFound("drinkMenu", "drinkMenu id",
	// drinkMenuId));
	//
	// UserDTO userDTO = this.mapper.map(userFromDb, UserDTO.class);
	//// FoodMenuDto foodMenuDto = this.mapper.map(foodMenuFromDb,
	// FoodMenuDto.class);
	//// DrinkMenuDto drinkMenuDto =
	// this.mapper.map(drinkMenuFromDb,DrinkMenuDto.class);
	////
	// userDTO.setImageName(userFromDb.getImage());
	//// foodMenuDto.setImageName(foodMenuFromDb.getImage());
	//// drinkMenuDto.setImageName(drinkMenuFromDb.getImage());
	////
	//
	// orderDto.setCreatedDate(new Date());
	// orderDto.setUsers(userDTO);
	//
	//// orderDto.setUsers(userDTO);
	//
	// Orders orders = this.mapper.map(orderDto, Orders.class);
	//
	//
	//
	// orders.setId(UUID.randomUUID());
	//
	// orders.setUsers(userFromDb);
	//
	// List<FoodMenu> foodMenusList = new ArrayList<>();
	// foodMenusList.add(foodMenuFromDb);
	//
	// List<DrinkMenu> drinkMenuList = new ArrayList<>();
	// drinkMenuList.add(drinkMenuFromDb);
	//
	// orders.setFoodMenus(foodMenusList);
	// orders.setDrinkMenus(drinkMenuList);
	//
	//
	//// orders.setFoodMenu(foodMenuFromDb);
	//// orders.setDrinkMenu(drinkMenuFromDb);
	//
	//
	// Orders savedOrder = this.ordersRepo.save(orders);
	//
	// if (savedOrder != null) {
	// return true;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	@Override
	public Boolean updateOrders(UUID id, String tableNo, Double price, List<AddOrderDto> item, String status) {

		try {

			Orders ordersFromDb = this.ordersRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("order", "order id", id));

			ordersFromDb.setTableNo(tableNo);
			ordersFromDb.setPrice(price);
			ordersFromDb.setItems(item);
			ordersFromDb.setStatus(status);

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

			// is

			List<OrdersDto> allOrdersDto = allOrders.stream().map((order) -> {
				OrdersDto ordersDto = this.mapper.map(order, OrdersDto.class);
				ordersDto.getUsers().setImageName(order.getUsers().getImage());
				ordersDto.getUsers().setPassword("");

				List<DrinkMenuDto> drinkMenuDtos = order.getDrinkMenus().stream().map(drink -> {
					DrinkMenuDto drinkDto = mapper.map(drink, DrinkMenuDto.class);
					drinkDto.setImageName(drink.getImage());
					return drinkDto;

				}).collect(Collectors.toList());

				List<FoodMenuDto> foodMenuDtos = order.getFoodMenus().stream().map(menu -> {
					FoodMenuDto foodMenuDto = mapper.map(menu, FoodMenuDto.class);
					foodMenuDto.setImageName(menu.getImage());
					return foodMenuDto;
				}).collect(Collectors.toList());

				ordersDto.setDrinkMenus(drinkMenuDtos);
				ordersDto.setFoodMenus(foodMenuDtos);

				return ordersDto;
			}).collect(Collectors.toList());

			if (allOrdersDto.size() > 0) {
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

			List<DrinkMenuDto> drinkMenuDtos = ordersFromDb.getDrinkMenus().stream().map(drink -> {
				DrinkMenuDto drinkDto = mapper.map(drink, DrinkMenuDto.class);
				drinkDto.setImageName(drink.getImage());
				return drinkDto;
			}).collect(Collectors.toList());

			List<FoodMenuDto> foodmenuDtos = ordersFromDb.getFoodMenus().stream().map(menu -> {
				FoodMenuDto foodMenuDto = mapper.map(menu, FoodMenuDto.class);
				foodMenuDto.setImageName(menu.getImage());
				return foodMenuDto;
			}).collect(Collectors.toList());

			OrdersDto ordersDto = this.mapper.map(ordersFromDb, OrdersDto.class);
			ordersDto.getUsers().setImageName(ordersFromDb.getUsers().getImage());
			ordersDto.getUsers().setPassword("");
			ordersDto.setFoodMenus(foodmenuDtos);
			ordersDto.setDrinkMenus(drinkMenuDtos);

			if (ordersDto != null) {
				return ordersDto;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;

	}

}
