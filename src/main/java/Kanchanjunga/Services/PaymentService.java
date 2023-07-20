package Kanchanjunga.Services;

import java.util.List;

import Kanchanjunga.Dto.PaymentDTO;
import Kanchanjunga.Entity.Payment;

public interface PaymentService {
    Boolean createPayment(PaymentDTO payment);

    List<Payment> getAllPayments();
}
