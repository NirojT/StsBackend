package Kanchanjunga.ServiceImpl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import Kanchanjunga.Dto.PaymentDTO;
import Kanchanjunga.Entity.Orders;
import Kanchanjunga.Entity.Payment;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.OrdersRepo;
import Kanchanjunga.Reposioteries.PaymentRepo;
import Kanchanjunga.Services.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private OrdersRepo ordersRepo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Boolean createPayment(PaymentDTO request) {
        try {
            request.setId(UUID.randomUUID());
            Orders order = this.ordersRepo.findById(UUID.fromString(request.getOrderID()))
                    .orElseThrow(
                            () -> new ResourceNotFound("payment", "order id", UUID.fromString(request.getOrderID())));
            request.setOrder(order);
            Payment payment = mapper.map(request, Payment.class);
            paymentRepo.save(payment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Payment> getAllPayments() {
        try {
            List<Payment> payments = paymentRepo.findAll();
            for (Payment payment : payments) {
                Orders orders = payment.getOrders();
                // Do something with the orders
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
