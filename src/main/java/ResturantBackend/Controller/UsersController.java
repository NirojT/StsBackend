package ResturantBackend.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ResturantBackend.Dto.UserDTO;
import ResturantBackend.Services.UsersService;

@RestController
@RequestMapping("/api/user/")

@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/",
		"https://64f1a1ae3172de413ab9674b--cute-taiyaki-355152.netlify.app/",
		"http://192.168.0.102:5173/" }, allowCredentials = "true")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("create")
    public ResponseEntity<?> createUser(@ModelAttribute UserDTO users) {
        String result = this.usersService.createUser(users);
        HashMap<String, Object> response = new HashMap<>();
        if (result.equalsIgnoreCase("saved")) {
            response.put("status", 200);
            response.put("message", "user created successfully");
            return ResponseEntity.status(200).body(response);
        }
        response.put("status", 400);
        response.put("message", "user creation failed");
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") UUID userId) {
        boolean result = this.usersService.deleteUser(userId);
        HashMap<String, Object> response = new HashMap<>();
        if (result) {
            response.put("status", 200);
            response.put("message", "user deleted successfully");
            return ResponseEntity.status(200).body(response);
        }
        response.put("status", 400);
        response.put("message", "user deletion failed");
        return ResponseEntity.status(200).body(response);

    }
    @DeleteMapping("fake-delete/{id}")
    public ResponseEntity<?> fakeDeleteUser(@PathVariable("id") UUID userId) {
    	boolean result = this.usersService.fakeDeleteUser(userId);
    	HashMap<String, Object> response = new HashMap<>();
    	if (result) {
    		response.put("status", 200);
    		response.put("message", "user deleted successfully");
    		return ResponseEntity.status(200).body(response);
    	}
    	response.put("status", 400);
    	response.put("message", "user deletion failed");
    	return ResponseEntity.status(200).body(response);
    	
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllUsers() {
        HashMap<String, Object> response = new HashMap<>();
        List<UserDTO> users = this.usersService.getAllUsers();
        System.out.println(users);
        if (users.size() > 0 && users != null) {
            response.put("status", 200);
            response.put("message", "users fetched successfully");
            response.put("users", users);
            return ResponseEntity.status(200).body(response);
        }
        response.put("status", 400);
        response.put("message", "no users found");
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") UUID userId) {
        HashMap<String, Object> response = new HashMap<>();
        UserDTO user = this.usersService.getUserByID(userId);
        if (user != null) {
            response.put("status", 200);
            response.put("message", "user fetched successfully");
            response.put("user", user);
            return ResponseEntity.status(200).body(response);
        }
        response.put("status", 400);
        response.put("message", "user not found");
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, 
    		@RequestParam (required=false ) String name,
    		@RequestParam (required=false ) String role, 
    		@RequestParam (required=false ) String contactNo,
    		@RequestParam (required=false ) String address,
    		@RequestParam (required=false ) MultipartFile image) {
        HashMap<String, Object> response = new HashMap<>();
        Boolean isUpdated = this.usersService.updateUser(id, name, role, contactNo, address, image, address);
        response.put("status", isUpdated ? 200 : 400);
        response.put("message", isUpdated ? "user updated successfully" : "user details update fail");
        return ResponseEntity.status(200).body(response);
    }
}
