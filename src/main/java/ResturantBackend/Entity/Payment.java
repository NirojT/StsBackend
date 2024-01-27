package ResturantBackend.Entity;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Payment {

	@Id
	private UUID id;

	private String billNo;
	private String type;
	private String creditName;
	private double totalPrice;
	private double receivedPrice;
	private double netPrice;

	@DBRef
	private Orders orders;



	@CreatedDate
	private Date createdDate;
	private String createdNepDate;

	@LastModifiedDate
	private Date lastModifiedDate;

}