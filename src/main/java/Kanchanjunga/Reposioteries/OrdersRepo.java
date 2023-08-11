package Kanchanjunga.Reposioteries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import Kanchanjunga.Entity.Orders;
import Kanchanjunga.Entity.Payment;

public interface OrdersRepo extends MongoRepository<Orders, UUID>{
	
   
    // Custom query to fetch orders within a specific time range


	 @Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
    List<Orders> findOrdersBy1Day(LocalDateTime start, LocalDateTime end);

	 
	 @Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
	 List<Payment> findOrderNoBy1Week(LocalDateTime start, LocalDateTime end);

    
	 @Query("{ 'createdDate' : { $gte: ?0, $lte: ?1 } }")
    List<Orders> findOrdersByCurrentMonth(LocalDate startOfMonth, LocalDate startOfNextMonth);


}
