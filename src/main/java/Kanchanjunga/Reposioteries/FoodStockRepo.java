package Kanchanjunga.Reposioteries;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import Kanchanjunga.Entity.FoodStock;

public interface FoodStockRepo extends MongoRepository<FoodStock, UUID>{

}
