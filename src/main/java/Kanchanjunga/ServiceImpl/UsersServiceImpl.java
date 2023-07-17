package Kanchanjunga.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Kanchanjunga.Entity.Users;
import Kanchanjunga.ErrorHandlers.ResourceNotFound;
import Kanchanjunga.Reposioteries.UserRepo;
import Kanchanjunga.Services.UsersService;

@Service
public class UsersServiceImpl implements UsersService {

	  @Autowired
	    private UserRepo userRepo;

	    @Override
	    public String createUser(Users users) {
	        Users userExist = userRepo.findByName(users.getName());
	        if (userExist != null) {
	            return "exist";
	        }
	        try {

	            if (checkData(users)) {
	                users.setId(UUID.randomUUID());
	                Users savedUser = userRepo.save(users);
	                if (savedUser != null) {
	                    return "saved";
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "fail";
	    }

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
	    public boolean deleteUser(UUID id) {
	        try {
	            Users user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFound("users", "userId", id));
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
	    public boolean updateUser(Users users, UUID id) {
	        Users user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFound("users", "userId", id));
	        try {
	            if (checkData(users)) {
	                // update the user object
	                user.setName(users.getName());
	                user.setAddress(users.getAddress());
	                user.setRole(users.getRole());
	                user.setContactNo(users.getContactNo());
	                // save the updated user object
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
	    public List<Users> getAllUsers() {
	        try {
	            List<Users> users = this.userRepo.findAll();
	            if (users.size() > 0) {
	                return users;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	        return null;

	    }

	    @Override
	    public Users getUserByID(UUID id) {
	        try {
	            Users user = this.userRepo.findById(id).orElseThrow(() -> new ResourceNotFound("users", "userId", id));
	            return user;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }


}
