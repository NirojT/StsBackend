package ResturantBackend.Reposioteries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ResturantBackend.Entity.FoodStock;
import ResturantBackend.Entity.Payment;

public interface FoodStockRepo extends MongoRepository<FoodStock, UUID> {

	@Query("{'createdNepDate':{ $regex: ?0}}")
	List<FoodStock> findFoodStockByNepaliMonth(String nepaliMonth);

	@Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	List<FoodStock> findExpenseBy1Day(LocalDateTime start, LocalDateTime end);

	@Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	List<FoodStock> findExpenseBy1Week(LocalDateTime start, LocalDateTime end);

	@Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	List<FoodStock> findExpenseByCurrentMonth(LocalDate startOfMonth, LocalDate startOfNextMonth);
}
