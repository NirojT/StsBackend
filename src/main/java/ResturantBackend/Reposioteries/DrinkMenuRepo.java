package ResturantBackend.Reposioteries;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import ResturantBackend.Entity.DrinkMenu;

public interface DrinkMenuRepo extends MongoRepository<DrinkMenu, UUID>{

}
