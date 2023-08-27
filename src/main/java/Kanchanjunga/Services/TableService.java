package Kanchanjunga.Services;

import java.util.List;
import java.util.UUID;

import Kanchanjunga.Entity.Table;

public interface TableService {

	
	
	boolean createTable(Table table);
	boolean updateTable(UUID id,Table table);
	boolean deleteTable (UUID id);
	
	List<Table> geTables();
	Table getTable(UUID id);
}
