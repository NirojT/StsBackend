package ResturantBackend.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import ResturantBackend.Dto.UserDTO;

public interface UsersService {
    String createUser(UserDTO users);

    boolean deleteUser(UUID id);
    boolean isActive(UUID id);

    boolean updateUser(UUID id, String name,
            String role,
            String contactNo,
            String address,
            MultipartFile image,
            String password);

    List<UserDTO> getAllUsers();

    UserDTO getUserByID(UUID id);
}
