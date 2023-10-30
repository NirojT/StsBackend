package ResturantBackend.Reposioteries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ResturantBackend.Entity.DrinkStock;
import ResturantBackend.Entity.Payment;

public interface DrinkStockRepo extends MongoRepository<DrinkStock, UUID> {

	@Query("{'createdNepDate':{ $regex: ?0}}")
	List<DrinkStock> findDrinkStockNepaliMonth(String nepaliMonth);
	
	 @Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	    List<DrinkStock> findExpenseBy1Day(LocalDateTime start, LocalDateTime end);
	 
	 @Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	 List<DrinkStock> findExpenseBy1Week(LocalDateTime start, LocalDateTime end);

	
	 @Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	    List<DrinkStock> findExpenseByCurrentMonth(LocalDate startOfMonth, LocalDate startOfNextMonth);
}
