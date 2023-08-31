package ResturantBackend.Services;

import java.util.List;
import java.util.UUID;

import ResturantBackend.Entity.Table;

public interface TableService {

	
	
	boolean createTable(Table table);
	boolean updateTable(UUID id,Table table);
	boolean deleteTable (UUID id);
	
	List<Table> geTables();
	Table getTable(UUID id);
}
