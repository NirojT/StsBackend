package Kanchanjunga.Controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Dto.PaymentDTO;
import Kanchanjunga.Services.PaymentService;

@RestController
@RequestMapping("/api/payment/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("create")
    private ResponseEntity<?> createPayment(@RequestBody PaymentDTO request) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Boolean isSaved = paymentService.createPayment(request);
            response.put("status", isSaved ? 200 : 400);
            response.put("message", isSaved ? "payment created successfully" : "payment not created");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", 500);
            response.put("message", "something went wrong");
            return ResponseEntity.status(500).body(response);
        }
    }

    

}
