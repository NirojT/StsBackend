package Kanchanjunga.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Users {
	
	
	private String name;
	private String role;
	private String contact;
	

	
}
