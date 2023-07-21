package Kanchanjunga.Reposioteries;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import Kanchanjunga.Entity.DrinkMenu;

public interface DrinkMenuRepo extends MongoRepository<DrinkMenu, UUID>{

}
