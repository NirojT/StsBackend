package Kanchanjunga.Reposioteries;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import Kanchanjunga.Entity.Users;


public interface UserRepo extends MongoRepository<Users, ObjectId>  {

}
