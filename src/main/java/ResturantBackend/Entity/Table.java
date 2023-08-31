package ResturantBackend.Entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
@Builder
public class Table {

	@Id
	private UUID id;
	private String tableNo;
	private boolean isAvailable = true;

	@DBRef
	private Orders orders;

}
