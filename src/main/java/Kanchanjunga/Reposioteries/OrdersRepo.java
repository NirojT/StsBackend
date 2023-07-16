package Kanchanjunga.Reposioteries;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import Kanchanjunga.Entity.Orders;

public interface OrdersRepo extends MongoRepository<Orders, UUID>{

}
