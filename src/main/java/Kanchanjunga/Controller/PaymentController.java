package Kanchanjunga.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Dto.PaymentDTO;
import Kanchanjunga.Services.PaymentService;

@RestController
@RequestMapping("/api/payment/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/","http://localhost:5173/", "http://192.168.0.102:5173/" }, allowCredentials = "true")
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

    @GetMapping("get-all")
    private ResponseEntity<?> getAllPayment() {
        HashMap<String, Object> response = new HashMap<>();
        try {
            List<PaymentDTO> payments = this.paymentService.getAllPayments();
            response.put("status", payments != null ? 200 : 400);
            response.put(payments != null ? "payments" : "message", payments != null ? payments : "payments not found");
            response.put("data", payments);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", 500);
            response.put("message", "something went wrong");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("get/{id}")
    private ResponseEntity<?> getPaymentById(@PathVariable UUID id) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            PaymentDTO payment = this.paymentService.getPaymentByID(id);
            response.put("status", payment != null ? 200 : 400);
            response.put(payment != null ? "payment" : "message", payment != null ? payment : "payment not found");
            response.put("data", payment);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", 500);
            response.put("message", "something went wrong");
            return ResponseEntity.status(500).body(response);
        }

    }

    @DeleteMapping("delete/{id}")
    private ResponseEntity<?> deletePayment(@PathVariable UUID id) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Boolean isDeleted = this.paymentService.deletePayment(id);
            response.put("status", isDeleted ? 200 : 400);
            response.put("message", isDeleted ? "payment deleted successfully" : "payment not deleted");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", 500);
            response.put("message", "something went wrong");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("update/{id}")
    private ResponseEntity<?> updatePayment(@PathVariable UUID id, @RequestBody PaymentDTO request) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Boolean isUpdated = this.paymentService.updatePayment(id, request);
            response.put("status", isUpdated ? 200 : 400);
            response.put("message", isUpdated ? "payment updated successfully" : "payment not updated");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", 500);
            response.put("message", "something went wrong");
            return ResponseEntity.status(500).body(response);
        }
    }
}
