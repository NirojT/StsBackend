package Kanchanjunga.Services;

import java.util.List;
import java.util.UUID;

import Kanchanjunga.Entity.Users;

public interface UsersService {
    String createUser(Users users);

    boolean deleteUser(UUID id);

    boolean updateUser(Users users, UUID id);

    List<Users> getAllUsers();

    Users getUserByID(UUID id);
}
