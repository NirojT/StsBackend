package Kanchanjunga.Entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Table {
	
	@Id
	private UUID id;
	String tableNo;

}
