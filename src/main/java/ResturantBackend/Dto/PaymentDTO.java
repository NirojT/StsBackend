package ResturantBackend.Dto;

import java.util.Date;
import java.util.UUID;

import ResturantBackend.Entity.Orders;
import lombok.Data;

@Data
public class PaymentDTO {

    private UUID id;

    private double totalPrice;
    private double receivedPrice;
    private double netPrice;

    private String orderID;
    private Orders order;
    private Date createdDate;
    
   

    private Date lastModifiedDate;
}
