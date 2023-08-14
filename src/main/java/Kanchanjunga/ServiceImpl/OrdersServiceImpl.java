package Kanchanjunga.ServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import Kanchanjunga.Dto.AddOrderDto;
import Kanchanjunga.Dto.DrinkMenuDto;
import Kanchanjunga.Dto.FoodMenuDto;
import Kanchanjunga.Dto.OrderRequest;
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
	public Boolean createOrders(OrderRequest orderRequest, String username) {

		try {
			List<AddOrderDto> addOrderDtos = orderRequest.getAddOrderDtos();

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
					order.setImageName(foodMenu.getImage());
					order.setName(foodMenu.getName());
					order.setPrice(foodMenu.getPrice());
					order.setType(foodMenu.getType());
					order.setCategory(foodMenu.getCategory());
					order.setDescription(foodMenu.getDescription());
				}

				if (drinkMenuId != null) {
					drinkMenu = this.drinkMenuRepo.findById(drinkMenuId)
							.orElseThrow(() -> new ResourceNotFound("Drink", "Drink Id", drinkMenuId));
					drinkMenu.setQuantity(quantity);
					order.setImageName(drinkMenu.getImage());
					order.setName(drinkMenu.getName());
					order.setPrice(drinkMenu.getPrice());
					order.setCategory(drinkMenu.getCategory());
					order.setDescription(drinkMenu.getDescription());

				}

				return order;
			}).collect(Collectors.toList());

			Users userFromDb = this.userRepo.findByName(username).get();

			Orders orders = new Orders();
			orders.setId(UUID.randomUUID());
			orders.setTableNo(orderRequest.getTableNo());
			orders.setCreatedDate(new Date());
			orders.setPrice(Double.parseDouble(orderRequest.getTotalPrice()));
			orders.setItems(item);
			orders.setRemarks(orderRequest.getRemarks());

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

			List<OrdersDto> allOrdersDto = allOrders.stream().map((order) -> {

				OrdersDto ordersDto = this.mapper.map(order, OrdersDto.class);
				ordersDto.getUsers().setImageName(order.getUsers().getImage());

				ordersDto.getUsers().setPassword(null);
				ordersDto.setItems(order.getItems());
				ordersDto.setRemarks(order.getRemarks());
				if (order.getDrinkMenus() != null) {

				}

				if (order.getFoodMenus() != null) {
					List<FoodMenuDto> foodMenuDtos = order.getFoodMenus().stream().map(menu -> {
						FoodMenuDto foodMenuDto = mapper.map(menu, FoodMenuDto.class);
						foodMenuDto.setImageName(menu.getImage());
						foodMenuDto.setPrice(menu.getPrice());
						return foodMenuDto;

					}).collect(Collectors.toList());
					ordersDto.setFoodMenus(foodMenuDtos);
				}

				return ordersDto;
			}).collect(Collectors.toList());

			return allOrdersDto;

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	@Override
	public OrdersDto getOrdersByID(UUID id) {
		try {
			Orders ordersFromDb = this.ordersRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("order", "order id", id));

			OrdersDto ordersDto = this.mapper.map(ordersFromDb, OrdersDto.class);

			if (ordersFromDb.getDrinkMenus() != null) {

				List<DrinkMenuDto> drinkMenuDtos = ordersFromDb.getDrinkMenus().stream().map(drink -> {
					DrinkMenuDto drinkDto = mapper.map(drink, DrinkMenuDto.class);
					drinkDto.setImageName(drink.getImage());
					return drinkDto;
				}).collect(Collectors.toList());
				ordersDto.setDrinkMenus(drinkMenuDtos);
			}

			if (ordersFromDb.getFoodMenus() != null) {

				List<FoodMenuDto> foodmenuDtos = ordersFromDb.getFoodMenus().stream().map(menu -> {
					FoodMenuDto foodMenuDto = mapper.map(menu, FoodMenuDto.class);
					foodMenuDto.setImageName(menu.getImage());
					return foodMenuDto;
				}).collect(Collectors.toList());
				ordersDto.setFoodMenus(foodmenuDtos);
			}

			ordersDto.getUsers().setImageName(ordersFromDb.getUsers().getImage());
			ordersDto.getUsers().setPassword("");
			ordersDto.setItems(ordersFromDb.getItems());

			if (ordersDto != null) {
				return ordersDto;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;

	}

	@Override
	public List<OrdersDto> getLatestOrders() {
		try {
			List<Orders> allOrders = this.ordersRepo.findAll(Sort.by(Sort.Direction.DESC, "createdDate")).stream()
					.limit(12).collect(Collectors.toList());

			List<OrdersDto> allOrdersDto = allOrders.stream().map((order) -> {

				OrdersDto ordersDto = this.mapper.map(order, OrdersDto.class);
				ordersDto.getUsers().setImageName(order.getUsers().getImage());

				ordersDto.getUsers().setPassword(null);
				ordersDto.setItems(order.getItems());
				ordersDto.setRemarks(order.getRemarks());
				if (order.getDrinkMenus() != null) {

				}

				if (order.getFoodMenus() != null) {
					List<FoodMenuDto> foodMenuDtos = order.getFoodMenus().stream().map(menu -> {
						FoodMenuDto foodMenuDto = mapper.map(menu, FoodMenuDto.class);
						foodMenuDto.setImageName(menu.getImage());
						foodMenuDto.setPrice(menu.getPrice());
						return foodMenuDto;

					}).collect(Collectors.toList());
					ordersDto.setFoodMenus(foodMenuDtos);
				}

				return ordersDto;
			}).collect(Collectors.toList());

			return allOrdersDto;

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	@Override
	public int getNoOfOrdersBy1Day() {
		try {
			LocalDate today = LocalDate.now();
			LocalDateTime startOfDay = today.atStartOfDay();
			LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

			List<Orders> allOrders = this.ordersRepo.findOrdersBy1Day(startOfDay, endOfDay);

			int noOfOrders = allOrders.size();
			return noOfOrders;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	// for 1 week the sell amount will change sunday to end of saturday
	@Override
	public int getOrderNoWeekly() {
		try {
			LocalDate currentDate = LocalDate.now();

			DayOfWeek firstDayOfWeek = DayOfWeek.SUNDAY; // Define the first day of the week

			int daysUntilFirstDay = (currentDate.getDayOfWeek().getValue() + 7 - firstDayOfWeek.getValue()) % 7;
			int value = currentDate.getDayOfWeek().getValue();
			LocalDate startOfWeek = currentDate.minusDays(daysUntilFirstDay);
			LocalDate endOfWeek = startOfWeek.plusDays(6);

			LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
			LocalDateTime endOfWeekDateTime = endOfWeek.atTime(LocalTime.MAX);

			List<Payment> payments = this.ordersRepo.findOrderNoBy1Week(startOfWeekDateTime, endOfWeekDateTime);

			int noOfOrders = payments.size();

			System.out.println(noOfOrders);
			System.out.println(value);
			System.out.println(startOfWeekDateTime);
			System.out.println(endOfWeekDateTime);
			return noOfOrders;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// getting no. of orders with in current month acccording to date
	@Override
	public int getNoOfOrdersByCurrentMonth() {

		try {
			YearMonth currentYearMonth = YearMonth.now();
			LocalDate startDate = currentYearMonth.atDay(1);
			LocalDate endDate = currentYearMonth.atEndOfMonth();

			List<Orders> ordersWithinCurrentMonth = this.ordersRepo.findOrdersByCurrentMonth(startDate, endDate);

			int numberOfOrders = ordersWithinCurrentMonth.size();
			System.out.println(numberOfOrders);
			return numberOfOrders;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<OrdersDto> getMyOrders(String name) {
		try {
			Optional<Users> user = userRepo.findByName(name);
			
			if (user.isPresent()) {
				Users userFromDb = user.get();
				List<Orders> orders = this.ordersRepo.findAll().stream()
						.filter(order -> {
							order.getUsers().setPassword(null);
							return order.getUsers().getId().equals(userFromDb.getId());
						}).collect(Collectors.toList());
				List<OrdersDto> data = orders.stream().map(order -> {
					return this.mapper.map(order, OrdersDto.class);
				}).collect(Collectors.toList());

				return orders.size() > 0 ? data : Collections.emptyList();
			}
			return Collections.emptyList();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public Boolean updateStatus(UUID id, String status) {
		try {
			Orders ordersFromDb = this.ordersRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("order", "order id", id));
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

}
