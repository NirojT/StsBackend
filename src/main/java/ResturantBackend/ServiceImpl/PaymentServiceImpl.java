package ResturantBackend.ServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import ResturantBackend.Utility.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ResturantBackend.ResturantApplication;
import ResturantBackend.Dto.PaymentDTO;
import ResturantBackend.Entity.Orders;
import ResturantBackend.Entity.Payment;
import ResturantBackend.Entity.Table;
import ResturantBackend.ErrorHandlers.ResourceNotFound;
import ResturantBackend.Reposioteries.OrdersRepo;
import ResturantBackend.Reposioteries.PaymentRepo;
import ResturantBackend.Reposioteries.TableRepo;
import ResturantBackend.Services.OrdersService;
import ResturantBackend.Services.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private OrdersRepo ordersRepo;
	@Autowired
	private OrdersService ordersService;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private TableRepo tableRepo;

	



	private int count = 1;

	private String generateBill() {
		String formatted = String.format("%03d", count);
		count++;
		return formatted;
	}


	@Override
	public Boolean createPayment(PaymentDTO request) {
		try {
			// changing id from string to uuid or u can say type casting

			if (request != null) {
				request.setId(UUID.randomUUID());
				request.setCreatedDate(new Date());
				request.setBillNo(generateBill());

				UUID orderId = UUID.fromString(request.getOrderID());


				Orders order = this.ordersRepo.findById(orderId)
						.orElseThrow(() -> new ResourceNotFound("payment", "payment id", orderId));
				if (order != null) {
					order.setStatus("paid");

					// setting table visibility
					if (order.getTable() != null) {
						Table table = order.getTable();

						table.setAvailable(true);

						tableRepo.save(table);
						order.setTable(table);
					}
					this.ordersRepo.save(order);

					request.setOrders(order);

				}

				Payment payment = mapper.map(request, Payment.class);
				payment.setCreatedNepDate(ResturantApplication.CurrentNepaliDate);
				paymentRepo.save(payment);

				return true;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public List<PaymentDTO> getAllPayments() {
		try {
			List<Payment> payments = paymentRepo.findAll();

			List<PaymentDTO> paymentsDTO = payments.stream().map(order -> {
				PaymentDTO paymentDTO = this.mapper.map(order, PaymentDTO.class);
				paymentDTO.setOrders(order.getOrders());

				if (order.getOrders() != null) {
					if (order.getOrders().getTableNo() != null) {
						paymentDTO.setTableNo(order.getOrders().getTableNo());
					}
					if (order.getOrders().getItems() != null) {
						paymentDTO.setItems(order.getOrders().getItems());
					}
				}

				return paymentDTO;
			}).collect(Collectors.toList());

			return paymentsDTO;
		} catch (Exception e) {
			e.printStackTrace();
			// You might want to handle the exception better, such as logging it.
			// In this example, we're rethrowing the exception.
			throw new RuntimeException("Error retrieving payments", e);
		}
	}

	@Override
	public List<PaymentDTO> getLatestPayments() {
		try {
			List<Payment> payments = paymentRepo.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));

			List<PaymentDTO> paymentsDTO = payments.stream().map(order -> {
				PaymentDTO paymentDTO = this.mapper.map(order, PaymentDTO.class);
				paymentDTO.setOrders(order.getOrders());

				if (order.getOrders() != null) {
					if (order.getOrders().getTableNo() != null) {
						paymentDTO.setTableNo(order.getOrders().getTableNo());
					}
					if (order.getOrders().getItems() != null) {
						paymentDTO.setItems(order.getOrders().getItems());
					}
				}

				return paymentDTO;
			}).collect(Collectors.toList());

			return paymentsDTO;
		} catch (Exception e) {
			e.printStackTrace();
			// You might want to handle the exception better, such as logging it.
			// In this example, we're rethrowing the exception.
			throw new RuntimeException("Error retrieving payments", e);
		}
	}

	@Override
	public PaymentDTO getPaymentByID(UUID id) {
		try {
			Payment payment = this.paymentRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("payment", "payment id", id));
			PaymentDTO paymentDTO = this.mapper.map(payment, PaymentDTO.class);
			paymentDTO.setOrders(payment.getOrders());
			return paymentDTO != null ? paymentDTO : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean deletePayment(UUID id) {
		try {
			Payment payment = this.paymentRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("payment", "payment id", id));
			this.paymentRepo.delete(payment);
			Optional<Payment> deletedPayment = this.paymentRepo.findById(id);
			return deletedPayment.isPresent() ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Boolean updatePayment(UUID id, PaymentDTO request) {
		try {
			Payment payment = this.paymentRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("payment", "payment id", id));
			Orders order = this.ordersRepo.findById(UUID.fromString(request.getOrderID())).orElseThrow(
					() -> new ResourceNotFound("payment", "payment id", UUID.fromString(request.getOrderID())));
			payment.setNetPrice(request.getNetPrice());
			payment.setReceivedPrice(request.getReceivedPrice());
			payment.setTotalPrice(request.getTotalPrice());
			payment.setOrders(order);

			Payment updatedPayment = this.paymentRepo.save(payment);
			return updatedPayment != null ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	// getting sells data with in 1 day acccording to date
	@Override
	public Double getSellsBy1Day() {

		try {
			LocalDate currentDate = LocalDate.now();
			LocalDateTime startOfDay = currentDate.atStartOfDay();
			LocalDateTime endOfDay = currentDate.atTime(LocalTime.MAX);

			List<Payment> payments = this.paymentRepo.findPaymentBy1Day(startOfDay, endOfDay);

			double TotalAmt = payments.stream().mapToDouble((payment) -> payment.getTotalPrice()).sum();

			return TotalAmt;
		} catch (Exception e) {
			e.printStackTrace();
			return 0.00;
		}
	}

	// for 1 week the sell amount will change sunday to end of saturday
	@Override
	public Double getTotalSellAmtWeekly() {
		try {
			LocalDate currentDate = LocalDate.now();

			DayOfWeek firstDayOfWeek = DayOfWeek.SUNDAY; // Define the first day of the week

			int daysUntilFirstDay = (currentDate.getDayOfWeek().getValue() + 7 - firstDayOfWeek.getValue()) % 7;

			LocalDate startOfWeek = currentDate.minusDays(daysUntilFirstDay);
			LocalDate endOfWeek = startOfWeek.plusDays(6);

			LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
			LocalDateTime endOfWeekDateTime = endOfWeek.atTime(LocalTime.MAX);
			List<Payment> payments = this.paymentRepo.findPaymentBy1Week(startOfWeekDateTime, endOfWeekDateTime);

			double totalAmt = payments.stream().mapToDouble(Payment::getTotalPrice).sum();


			return totalAmt;
		} catch (Exception e) {
			e.printStackTrace();
			return 0.00;
		}
	}

	// get total sell or addidng price 1 month
	@Override
	public Double getTotalSellAmtMonthly() {

		try {
			String currentNepaliMonthString = ResturantApplication.CurrentNepaliYearMonth;


			List<Payment> payments = this.paymentRepo.findPaymentsByNepaliMonth(currentNepaliMonthString);

			

			return payments.stream().mapToDouble(Payment::getTotalPrice).sum();

		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}

	}
	// get total sell or addidng price whole year

	public double[] getMonthlySellDataWholeYear() {

		try {
			//2080
			int currentNepaliYear = ResturantApplication.CurrentNepaliYear;
			double[] monthlySell = new double[12];
			List<Payment> payments = this.paymentRepo.findAll();

			for (int month = 1; month <= 12; month++) {


				String nepaliMonthPrefix = String.format("%04d/%02d", currentNepaliYear, month);

				double totalAmt = payments.stream()
						.filter(payment -> {
							boolean b=false;
							if(payment.getCreatedNepDate()!=null){
								 b = payment.getCreatedNepDate().startsWith(nepaliMonthPrefix);
							}

							return b;
						})
						.mapToDouble(Payment::getTotalPrice)
						.sum();

				monthlySell[month - 1] = totalAmt;
			}

			return monthlySell;
		} catch (Exception e) {
			e.printStackTrace();
			return new double[12];
		}
	}

	public double getYearlySalesReport() {
		double YearlySalesAmt = 0.0;
		try {
			//2080
			int currentNepaliYear = ResturantApplication.CurrentNepaliYear;

			List<Payment> payments = this.paymentRepo.findAll();
			for (int month = 1; month <= 12; month++) {


				String nepaliMonthPrefix = String.format("%04d/%02d", currentNepaliYear, month);

				double totalAmt = payments.stream()
						.filter(payment -> {
							boolean b=false;
							if(payment.getCreatedNepDate()!=null){
								b = payment.getCreatedNepDate().startsWith(nepaliMonthPrefix);
							}

							return b;
						})
						.mapToDouble(Payment::getTotalPrice)
						.sum();

				YearlySalesAmt += totalAmt;

			}

			if (YearlySalesAmt != 0.0) {
				return YearlySalesAmt;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public double getMonthlyMaxSell() {
		try {
			//2080
			int currentNepaliYear = ResturantApplication.CurrentNepaliYear;
			double[] monthlySell = new double[12];
			List<Payment> payments = this.paymentRepo.findAll();

			for (int month = 1; month <= 12; month++) {


				String nepaliMonthPrefix = String.format("%04d/%02d", currentNepaliYear, month);

				double totalAmt = payments.stream()
						.filter(payment ->{
							boolean b=false;
							if(payment.getCreatedNepDate()!=null){
								b = payment.getCreatedNepDate().startsWith(nepaliMonthPrefix);
							}

							return b;
						})
						.mapToDouble(Payment::getTotalPrice)
						.sum();

				monthlySell[month - 1] = totalAmt;
			}
			double maxAmt =0;
			for(double amt:monthlySell){
				if (amt > maxAmt){
					maxAmt=amt;
				}
			}

			return maxAmt;
		} catch (Exception e) {
			e.printStackTrace();
			return 0.00;
		}
	}

	@Override
	public List<List<PaymentDTO>> getMonthlyPaymentWholeYear() {

		try {
//			int currentYear = YearMonth.now().getYear();
			//2080
			int currentNepaliYear = ResturantApplication.CurrentNepaliYear;

			List<List<PaymentDTO>> monthlyPayments = new ArrayList<>();
			List<Payment> payments = this.paymentRepo.findAll();
			for (int month = 1; month <= 12; month++) {

				String nepaliMonthPrefix = String.format("%04d/%02d", currentNepaliYear, month);

				List<PaymentDTO> paymentDto = payments.stream()
						.map((payment) -> {
									PaymentDTO	paymentDTO=	this.mapper.map(payment, PaymentDTO.class);
									paymentDTO.setOrders(payment.getOrders());

									if (payment.getOrders() != null) {
										if (payment.getOrders().getTableNo() != null) {
											paymentDTO.setTableNo(payment.getOrders().getTableNo());
										}
										if (payment.getOrders().getItems() != null) {
											paymentDTO.setItems(payment.getOrders().getItems());
										}
									}

									return paymentDTO;
						}

						)
						.filter(paymentDto1 -> {
							boolean b=false;
							if(paymentDto1.getCreatedNepDate()!=null){
								b = paymentDto1.getCreatedNepDate().startsWith(nepaliMonthPrefix);
							}

							return b;
						})
						.collect(Collectors.toList());

				monthlyPayments.add(paymentDto);
			}

			return monthlyPayments;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>(); // Return an empty list in case of an exception
		}
	}

}