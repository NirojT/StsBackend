package Kanchanjunga.Reposioteries;

import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import Kanchanjunga.Entity.Users;


public interface UserRepo extends MongoRepository<Users, UUID>  {

	Optional<Users> findByName(String name);
}
