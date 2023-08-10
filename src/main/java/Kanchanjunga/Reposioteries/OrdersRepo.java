package Kanchanjunga.Reposioteries;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import Kanchanjunga.Entity.Orders;

public interface OrdersRepo extends MongoRepository<Orders, UUID>{
	
	
	   // Custom query to fetch orders within the last 24 hours
    @Query("{ 'createdDate' : { $gte: ?0 } }")
    List<Orders> findOrdersWithinLast24Hours(Date  twentyFourHoursAgoDate);
    
    
    // Custom query to fetch orders within the last 7 days
    @Query("{ 'createdDate' : { $gte: ?0 } }")
    List<Orders> findOrdersWithinLast7Days(Date sevenDaysAgoDate);
    
    
    
    // Custom query to fetch orders within a specific time range
    @Query("{ 'createdDate' : { $gte: ?0, $lt: ?1 } }")
    List<Orders> findOrdersWithinTimeRange(Date startTime, Date endTime);
}
