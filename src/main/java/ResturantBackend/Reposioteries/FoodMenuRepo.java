package ResturantBackend.Reposioteries;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import ResturantBackend.Entity.FoodMenu;

public interface FoodMenuRepo extends MongoRepository<FoodMenu, UUID>{

}
