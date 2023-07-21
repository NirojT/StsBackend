package Kanchanjunga.Services;

import java.util.List;
import java.util.UUID;

import Kanchanjunga.Dto.PaymentDTO;

public interface PaymentService {
    Boolean createPayment(PaymentDTO payment);

    List<PaymentDTO> getAllPayments();

    PaymentDTO getPaymentByID(UUID id);

    Boolean deletePayment(UUID id);

    Boolean updatePayment(UUID id, PaymentDTO payment);
}
