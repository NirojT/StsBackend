package Kanchanjunga.Reposioteries;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import Kanchanjunga.Entity.Users;

public interface UserRepo extends MongoRepository<Users, UUID> {
    @Query("{name:'?0'}")
    Users findByName(String name);

}
