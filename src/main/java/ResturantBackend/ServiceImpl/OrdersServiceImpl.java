package ResturantBackend.ServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ResturantBackend.Dto.AddOrderDto;
import ResturantBackend.Dto.DrinkMenuDto;
import ResturantBackend.Dto.FoodMenuDto;
import ResturantBackend.Dto.OrderRequest;
import ResturantBackend.Dto.OrdersDto;
import ResturantBackend.Entity.DrinkMenu;
import ResturantBackend.Entity.FoodMenu;
import ResturantBackend.Entity.Orders;
import ResturantBackend.Entity.Payment;
import ResturantBackend.Entity.Table;
import ResturantBackend.Entity.Users;
import ResturantBackend.ErrorHandlers.ResourceNotFound;
import ResturantBackend.Reposioteries.DrinkMenuRepo;
import ResturantBackend.Reposioteries.FoodMenuRepo;
import ResturantBackend.Reposioteries.OrdersRepo;
import ResturantBackend.Reposioteries.TableRepo;
import ResturantBackend.Reposioteries.UserRepo;

@Service
public class OrdersServiceImpl implements ResturantBackend.Services.OrdersService {

	@Autowired
	private OrdersRepo ordersRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private FoodMenuRepo foodMenuRepo;

	@Autowired
	private DrinkMenuRepo drinkMenuRepo;

	@Autowired
	private TableRepo tableRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

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

			orders.setCreatedDate(new Date());
			orders.setPrice(Double.parseDouble(orderRequest.getTotalPrice()));
			orders.setItems(item);
			orders.setRemarks(orderRequest.getRemarks());

			String tableNo = orderRequest.getTableNo();
			Table tableFromDB = this.tableRepo.findByTableNo(tableNo).get();

			if (orderRequest.getTableNo().equalsIgnoreCase("TakeAway")) {

				tableFromDB.setAvailable(true);

			} else {
				tableFromDB.setAvailable(false);

			}

			this.tableRepo.save(tableFromDB);
			orders.setTableNo(tableNo);

			orders.setTable(tableFromDB);
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
	public Boolean updateOrders(UUID id, OrderRequest orderRequest) {

		try {
			Orders ordersFromDb = this.ordersRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("order", "order id", id));
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
			ordersFromDb.setItems(item);
			ordersFromDb.setTableNo(orderRequest.getTableNo());
			ordersFromDb.setPrice(Double.parseDouble(orderRequest.getTotalPrice()));
			ordersFromDb.setStatus(ordersFromDb.getStatus());
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

				if (order.getUsers() != null) {
					ordersDto.getUsers().setImageName(order.getUsers().getImage());
					ordersDto.getUsers().setPassword(null);
				}

				ordersDto.setItems(order.getItems());
				ordersDto.setRemarks(order.getRemarks());
				ordersDto.setTable(order.getTable());
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
			ordersDto.setTable(ordersFromDb.getTable());

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
				if (ordersDto.getUsers() != null) {
					ordersDto.getUsers().setImageName(order.getUsers().getImage());
					ordersDto.getUsers().setPassword(null);
				}

				ordersDto.setItems(order.getItems());
				ordersDto.setRemarks(order.getRemarks());
				ordersDto.setTable(order.getTable());
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
	public List<OrdersDto> getLatestOrdersInTable() {
		try {
			List<Orders> allOrders = this.ordersRepo.findAll(Sort.by(Sort.Direction.DESC, "createdDate")).stream()
					.collect(Collectors.toList());
			
			List<OrdersDto> allOrdersDto = allOrders.stream().map((order) -> {
				
				OrdersDto ordersDto = this.mapper.map(order, OrdersDto.class);
				if (ordersDto.getUsers() != null) {
					ordersDto.getUsers().setImageName(order.getUsers().getImage());
					ordersDto.getUsers().setPassword(null);
				}
				
				ordersDto.setItems(order.getItems());
				ordersDto.setRemarks(order.getRemarks());
				ordersDto.setTable(order.getTable());
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

			return numberOfOrders;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<OrdersDto> getMyOrders(String name) {
		try {
			List<Orders> orders = this.ordersRepo.findAll();
			List<OrdersDto> data = orders.stream().map(order -> {
				return this.mapper.map(order, OrdersDto.class);
			}).collect(Collectors.toList());
			return orders.size() > 0 ? data : Collections.emptyList();
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
			if (status.equalsIgnoreCase("canceled") && ordersFromDb.getTable() != null) {
				Table table = ordersFromDb.getTable();
				table.setAvailable(true);
				tableRepo.save(table);
			}

			Orders updatedOrder = this.ordersRepo.save(ordersFromDb);
			if (updatedOrder != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean updateTableAvailable(UUID id) {
		try {
			Orders ordersFromDb = this.ordersRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("order", "order id", id));

			Table table = ordersFromDb.getTable();
			table.setAvailable(true);
			this.tableRepo.save(table);

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
	public List<FoodMenu> getMostOrderedFoods() {
		List<Orders> ordersList = this.ordersRepo.findAll();

		Map<UUID, Integer> foodMenuFrequency = new HashMap<>();

		ordersList.forEach(order -> {
			List<AddOrderDto> items = order.getItems();
			if (items != null) {
				items.forEach(item -> {
					UUID foodMenuId = item.getFoodMenuId();

					if (foodMenuId != null) {
						foodMenuFrequency.put(foodMenuId, foodMenuFrequency.getOrDefault(foodMenuId, 0) + 1);

					}
				});
			}
		});

		List<UUID> orderedFoodMenuIds = new ArrayList<>(foodMenuFrequency.keySet());

		orderedFoodMenuIds.sort((id1, id2) -> Integer.compare(foodMenuFrequency.get(id2), foodMenuFrequency.get(id1)));

		List<FoodMenu> orderedFoodMenus = new ArrayList<>();

		orderedFoodMenuIds.forEach(foodMenuId -> {
			FoodMenu foodMenu = this.foodMenuRepo.findById(foodMenuId)
					.orElseThrow(() -> new ResourceNotFound("foodmenu", "foodmenu id", foodMenuId));
			foodMenu.setFrequency(foodMenuFrequency.get(foodMenuId));
			orderedFoodMenus.add(foodMenu);

		});

		return orderedFoodMenus.size() > 0 ? orderedFoodMenus : null;
	}

	@Override
	public List<DrinkMenu> getMostOrderedDrinks() {

		List<Orders> orders = this.ordersRepo.findAll();

		Map<UUID, Integer> drinkMenuFrequency = new HashMap<>();

		orders.stream().forEach((order) -> {
			List<AddOrderDto> items = order.getItems();
			if (items != null) {
				items.stream().forEach((item) -> {
					UUID drinkMenuId = item.getDrinkMenuId();
					if (drinkMenuId != null) {
						drinkMenuFrequency.put(drinkMenuId, drinkMenuFrequency.getOrDefault(drinkMenuId, 0) + 1);
					}
				});
			}

		});

		List<UUID> orderedDrinkMenusIDs = new ArrayList<>(drinkMenuFrequency.keySet());

		orderedDrinkMenusIDs
				.sort((id1, id2) -> Integer.compare(drinkMenuFrequency.get(id2), drinkMenuFrequency.get(id1)));

		List<DrinkMenu> orderedDrinkMenus = new ArrayList<>();

		orderedDrinkMenusIDs.stream().forEach(id -> {
			DrinkMenu drinkMenu = this.drinkMenuRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("DrinkMenu", "DrinkMenu id", id));

			drinkMenu.setFrequency(drinkMenuFrequency.get(id));

			orderedDrinkMenus.add(drinkMenu);
		});

		return orderedDrinkMenus.size() > 0 ? orderedDrinkMenus : null;
	}

	@Override
	public Boolean fakeDeleteOrders(UUID id) {
		try {
			Orders ordersFromDb = this.ordersRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("order", "order id", id));
			ordersFromDb.setFakeDelete(true);
			this.ordersRepo.save(ordersFromDb);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
