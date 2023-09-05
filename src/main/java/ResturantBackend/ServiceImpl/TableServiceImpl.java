package ResturantBackend.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ResturantBackend.Entity.Table;
import ResturantBackend.ErrorHandlers.ResourceNotFound;
import ResturantBackend.Reposioteries.TableRepo;
import ResturantBackend.Services.TableService;

@Service
public class TableServiceImpl implements TableService {

	@Autowired
	private TableRepo tableRepo;

	@Override
	public boolean createTable(Table table) {
		try {
			table.setId(UUID.randomUUID());
			table.setAvailable(true);
			Table savedTable = this.tableRepo.save(table);
			

			if (savedTable != null) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateTable(UUID id, Table table) {

		try {
			Table tableFromDb = this.tableRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Table", "Table Id", id));

			tableFromDb.setTableNo(table.getTableNo());

			Table updatedTable = this.tableRepo.save(tableFromDb);

			if (updatedTable != null)
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteTable(UUID id) {
		try {

			Table tableFromDb = this.tableRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Table", "Table Id", id));

			this.tableRepo.delete(tableFromDb);

			Optional<Table> checking = this.tableRepo.findById(id);

			return checking.isPresent() ? false : true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public List<Table> geTables() {
		try {
			List<Table> tables = this.tableRepo.findAll();

			return tables.size() > 0 ? tables : null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Table getTable(UUID id) {
		try {
			Table tableFromDb = this.tableRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("Table", "Table Id", id));

			return tableFromDb != null ? tableFromDb : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
