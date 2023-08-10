package Kanchanjunga.ServiceImpl;

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
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.OrdersRepo;
import Kanchanjunga.Reposioteries.PaymentRepo;
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

    @Override
    public Boolean createPayment(PaymentDTO request) {
        try {
            request.setId(UUID.randomUUID());
            Orders order = this.ordersRepo.findById(UUID.fromString(request.getOrderID()))
                    .orElseThrow(
                            () -> new ResourceNotFound("payment", "payment id", UUID.fromString(request.getOrderID())));
            request.setOrder(order);
            request.setCreatedDate(new Date());
            Payment payment = mapper.map(request, Payment.class);
            paymentRepo.save(payment);
            Boolean isUpdated = ordersService.updateStatus(UUID.fromString(request.getOrderID()), "paid");
            return isUpdated;
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
            Orders order = this.ordersRepo.findById(UUID.fromString(request.getOrderID()))
                    .orElseThrow(
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

}
