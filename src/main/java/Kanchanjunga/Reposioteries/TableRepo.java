package Kanchanjunga.Reposioteries;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import Kanchanjunga.Entity.Table;

public interface TableRepo extends MongoRepository<Table, UUID>{

	// Find a table by its tableNo
    Optional<Table> findByTableNo(String tableNo);
	
}
