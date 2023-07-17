package Kanchanjunga.Reposioteries;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import Kanchanjunga.Entity.FoodMenu;

public interface FoodMenuRepo extends MongoRepository<FoodMenu, UUID>{

}
