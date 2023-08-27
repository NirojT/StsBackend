package Kanchanjunga.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.Dto.UserDTO;
import Kanchanjunga.Entity.Users;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.UserRepo;
import Kanchanjunga.Services.UsersService;
import Kanchanjunga.Utility.FilesHelper;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UserRepo userRepo;
	
	
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	ModelMapper mapper;

	@Autowired
	FilesHelper filesHelper;

	// checks for empty data -> Users data
	public static boolean checkData(Users users) {
		if (users.getAddress().isEmpty())
			return false;
		if (users.getContactNo().isEmpty())
			return false;
		if (users.getName().isEmpty())
			return false;
		if (users.getPassword().isEmpty())
			return false;
		if (users.getRole().isEmpty())
			return false;
		return true;
	}

	@Override
	public String createUser(UserDTO users) {
		try {
			Users user = this.mapper.map(users, Users.class);
			if (checkData(user)) {
				user.setId(UUID.randomUUID());
				user.setCreatedDate(new Date());
				String filename = this.filesHelper.saveFile(users.getImage());
				user.setImage(filename);
				Users savedUser = userRepo.save(user);
				if (savedUser != null) {
					return "saved";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}

	@Override
	public boolean deleteUser(UUID id) {
		try {
			Users user = this.userRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("users", "userId", id));
			this.userRepo.delete(user);
			Optional<Users> isDeleted = this.userRepo.findById(id);
			if (isDeleted.isPresent()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateUser(UUID id, String name, String role, String contactNo, String address, MultipartFile image,
			String password) {
		Users user = this.userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFound("users", "userId", id));
		try {
			user.setName(name);
			user.setAddress(address);
			user.setRole(role);
			user.setContactNo(contactNo);
			user.setPassword(encoder.encode(password));
			
			System.out.println(user.toString());

			if (image == null) {
				user.setImage(user.getImage());
				Users updatedUser = this.userRepo.save(user);
				if (updatedUser != null) {
					return true;
				}
			}
			String filename = this.filesHelper.saveFile(image);
			Boolean isDeleted = this.filesHelper.deleteExistingFile(user.getImage());
			if (isDeleted) {
				user.setImage(filename);
				Users updatedUser = this.userRepo.save(user);
				if (updatedUser != null) {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public List<UserDTO> getAllUsers() {
		try {
			List<Users> users = this.userRepo.findAll();
			// if (users.size() > 0) {

			List<UserDTO> usersDto = users.stream().map((user) -> {
				UserDTO userDTO = this.mapper.map(user, UserDTO.class);
				userDTO.setImageName(user.getImage());
				userDTO.setOrders(user.getOrders());
				userDTO.setPassword(null);
				return userDTO;
			}).collect(Collectors.toList());
			return usersDto;
			// }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public UserDTO getUserByID(UUID id) {
		try {
			Users user = this.userRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFound("users", "userId", id));
			UserDTO userDTO = this.mapper.map(user, UserDTO.class);
			userDTO.setImageName(user.getImage());
			userDTO.setOrders(user.getOrders());
			userDTO.setPassword(null);
			return userDTO;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
