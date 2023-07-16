package Kanchanjunga.Entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Payment {

	@Id
	private UUID id;
	
	private double totalPrice;
	private double recievedPrice;
	private String netPrice;
	
	@DBRef
	private Orders orders;
}
