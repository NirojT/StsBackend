package Kanchanjunga.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Entity.Users;
import Kanchanjunga.Services.UsersService;

@RestController
@RequestMapping("/api/user/")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("create")
    public ResponseEntity<?> createUser(@RequestBody Users users) {
        String result = this.usersService.createUser(users);
        HashMap<String, Object> response = new HashMap<>();
        if (result.equalsIgnoreCase("exist")) {
            response.put("status", 200);
            response.put("message", "user already exist");
            return ResponseEntity.status(200).body(response);
        }
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

    @GetMapping("get-all")
    public ResponseEntity<?> getAllUsers() {
        HashMap<String, Object> response = new HashMap<>();
        List<Users> users = this.usersService.getAllUsers();
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
        Users user = this.usersService.getUserByID(userId);
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
}
