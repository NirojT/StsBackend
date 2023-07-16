package Kanchanjunga.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.annotation.Generated;
import lombok.Data;

@Document(collection = "employee")
@Data
public class Staffs {

	@Id
	private String id;
	private String name;
}
