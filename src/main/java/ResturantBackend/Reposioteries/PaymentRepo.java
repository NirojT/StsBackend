package ResturantBackend.Reposioteries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ResturantBackend.Entity.Payment;

public interface PaymentRepo extends MongoRepository<Payment, UUID> {
	
	@Query("{'createdNepDate':{ $regex: ?0}}")
	List<Payment> findPaymentsByNepaliMonth(String nepaliMonth);
	
	 @Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	    List<Payment> findPaymentBy1Day(LocalDateTime start, LocalDateTime end);
	 
	 @Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	 List<Payment> findPaymentBy1Week(LocalDateTime start, LocalDateTime end);

	    
		 @Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	    List<Payment> findPaymentByCurrentMonth(LocalDate startOfMonth, LocalDate startOfNextMonth);
}
