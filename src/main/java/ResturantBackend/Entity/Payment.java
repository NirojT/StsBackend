package ResturantBackend.Entity;

import java.util.Date;
import java.util.List;
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

	private double totalPrice;
	private double receivedPrice;
	private double netPrice;
	 private String tableNo;
	
	private List<?> orderedItems ;

	@DBRef
	private Orders orders;

	
	
	@CreatedDate
	private Date createdDate;

	@LastModifiedDate
	private Date lastModifiedDate;

}
