package ResturantBackend.Dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ResturantBackend.Entity.Orders;
import lombok.Data;

@Data
public class PaymentDTO {

    private UUID id;

    private double totalPrice;
    private double receivedPrice;
    private double netPrice;
    private List<?> orderedItems ;
    private String orderID;
    private Orders order;
    private Date createdDate;
    private String tableNo;
   

    private Date lastModifiedDate;
}
