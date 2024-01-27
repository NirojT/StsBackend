package ResturantBackend.Services;

import java.util.List;
import java.util.UUID;

import ResturantBackend.Dto.PaymentDTO;

public interface PaymentService {

	Boolean createPayment(PaymentDTO payment);

	List<PaymentDTO> getAllPayments();

	PaymentDTO getPaymentByID(UUID id);

	Boolean deletePayment(UUID id);

	Boolean updatePayment(UUID id, PaymentDTO payment);

	Double getSellsBy1Day();

	Double getTotalSellAmtWeekly();

	Double getTotalSellAmtMonthly();

	double[] getMonthlySellDataWholeYear();

	double getYearlySalesReport();
	double getMonthlyMaxSell();
	List<PaymentDTO> getLatestPayments();

	List<List<PaymentDTO>> getMonthlyPaymentWholeYear();

    Boolean clearCredits(UUID id);
}