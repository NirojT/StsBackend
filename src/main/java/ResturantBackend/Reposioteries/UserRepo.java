package ResturantBackend.Reposioteries;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ResturantBackend.Entity.Users;

public interface UserRepo extends MongoRepository<Users, UUID> {
    @Query("{name:'?0'}")
    Optional<Users> findByName(String name);

    // Optional<Users> findByName(String name);
}
