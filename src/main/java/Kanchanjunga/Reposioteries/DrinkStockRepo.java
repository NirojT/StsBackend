package Kanchanjunga.Reposioteries;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import Kanchanjunga.Entity.DrinkStock;

public interface DrinkStockRepo extends MongoRepository<DrinkStock, UUID> {

}
