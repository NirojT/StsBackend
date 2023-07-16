package Kanchanjunga.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Entity.Staffs;
import Kanchanjunga.Entity.Users;
import Kanchanjunga.Reposioteries.StaffsRepo;
import Kanchanjunga.Reposioteries.UserRepo;

@RestController
@RequestMapping("/user")
public class Controller {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private StaffsRepo staffRepo;
	
	
	@GetMapping("/")
	public ResponseEntity<?> getusers(){
		 List<Users> allUsers = this.userRepo.findAll();
		 return ResponseEntity.ok(allUsers);
	}
	@PostMapping("/c")
	public ResponseEntity<?> createUser(@RequestBody Users users){
		Users user = this.userRepo.save(users);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getStaff(){
		List<Staffs> allUsers = this.staffRepo.findAll();
		return ResponseEntity.ok(allUsers);
	}
	@PostMapping("/create")
	public ResponseEntity<?> createStaff(@RequestBody Staffs users){
		
		String randomUUID =UUID.randomUUID().toString();
		users.setId(randomUUID);	
		Staffs user = this.staffRepo.save(users);
		return ResponseEntity.ok(user);
	}
}
