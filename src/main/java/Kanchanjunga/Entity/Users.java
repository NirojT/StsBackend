package Kanchanjunga.Entity;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collation = "User")
@Data
public class Users {
	
	private UUID id;
	private String name;
	private String role;
	private String contactNo;
	private String address;
	private String password;

	
}
