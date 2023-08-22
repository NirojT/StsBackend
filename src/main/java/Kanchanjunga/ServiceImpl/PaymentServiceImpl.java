package Kanchanjunga.ServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Kanchanjunga.Dto.PaymentDTO;
import Kanchanjunga.Entity.Orders;
import Kanchanjunga.Entity.Payment;
import Kanchanjunga.Entity.Table;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.OrdersRepo;
import Kanchanjunga.Reposioteries.PaymentRepo;
import Kanchanjunga.Reposioteries.TableRepo;
import Kanchanjunga.Services.OrdersService;
import Kanchanjunga.Services.PaymentService;

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

	@Override
	public Boolean createPayment(PaymentDTO request) {
		try {
			// changing id from string to uuid or u can say type casting

			if (request != null) {
				request.setId(UUID.randomUUID());
				request.setCreatedDate(new Date());

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

					request.setOrder(order);

				}

				Payment payment = mapper.map(request, Payment.class);

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
				paymentDTO.setOrder(order.getOrders());
				return paymentDTO;
			}).collect(Collectors.toList());
			return paymentsDTO.size() > 0 ? paymentsDTO : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public PaymentDTO getPaymentByID(UUID id) {
		try {
			Payment payment = this.paymentRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("payment", "payment id", id));
			PaymentDTO paymentDTO = this.mapper.map(payment, PaymentDTO.class);
			paymentDTO.setOrder(payment.getOrders());
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

			System.out.println(totalAmt);

			System.out.println(startOfWeek);
			System.out.println(endOfWeek);
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
			YearMonth currentYearMonth = YearMonth.now();
			LocalDate startDate = currentYearMonth.atDay(1);
			LocalDate endDate = currentYearMonth.atEndOfMonth();

			List<Payment> sellsWithinCurrentMonth = this.paymentRepo.findPaymentByCurrentMonth(startDate, endDate);

			double totalAmt = sellsWithinCurrentMonth.stream().mapToDouble(Payment::getTotalPrice).sum();

			return totalAmt;
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}

	}
	// get total sell or addidng price whole year

	public double[] getMonthlySellDataWholeYear() {

		try {
			int currentYear = YearMonth.now().getYear();

			double monthlySell[] = new double[12];

			for (int month = 1; month <= 12; month++) {

				YearMonth currentYearMonth = YearMonth.of(currentYear, month);
				LocalDate startDate = currentYearMonth.atDay(1);
				LocalDate endDate = currentYearMonth.atEndOfMonth();

				double totalAmt = this.paymentRepo.findPaymentByCurrentMonth(startDate, endDate).stream()
						.mapToDouble(Payment::getTotalPrice).sum();

				monthlySell[month - 1] = totalAmt;

			}

			return monthlySell;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("iam catch block....");
			return new double[12];

		}
	}

	public double getYearlySalesReport() {
		double YearlySalesAmt = 0.0;
		try {
			int currentYear = YearMonth.now().getYear();

			for (int month = 1; month <= 12; month++) {

				YearMonth currentYearMonth = YearMonth.of(currentYear, month);
				LocalDate startDate = currentYearMonth.atDay(1);
				LocalDate endDate = currentYearMonth.atEndOfMonth();

				double totalAmt = this.paymentRepo.findPaymentByCurrentMonth(startDate, endDate).stream()
						.mapToDouble(Payment::getTotalPrice).sum();

				YearlySalesAmt += totalAmt;

			}

			if (YearlySalesAmt != 0.0) {
				System.out.println(YearlySalesAmt);
				return YearlySalesAmt;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
