package Kanchanjunga.Entity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collation = "User")
@Data
public class Users {
	
	@Id
	private UUID id;
	private String name;
	private String role;
	private String contactNo;
	private String address;
	private String password;

	@DBRef
	private List<Orders>  orders;
}
