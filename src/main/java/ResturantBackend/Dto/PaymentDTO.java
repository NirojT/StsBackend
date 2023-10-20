package ResturantBackend.Dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ResturantBackend.Entity.Orders;
import lombok.Data;

@Data
public class PaymentDTO {

    private UUID id;
    private String tableNo;

    private String billNo;
    private List<AddOrderDto> items;
    private double totalPrice;
    private double receivedPrice;
    private double netPrice;

    private String orderID;
    private Orders orders;
    private Date createdDate;
    private String createdNepDate;



    private Date lastModifiedDate;
}