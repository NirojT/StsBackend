package ResturantBackend.Dto;

import ResturantBackend.Entity.Orders;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class PaymentDTO {

    private UUID id;
    private String tableNo;
    private String type;
    private String creditName;
    private String creditPhoneNo;
    private double advanceAmt;
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