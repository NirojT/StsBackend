package Kanchanjunga.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Dto.UserDTO;
import Kanchanjunga.JWT.JwtHelper;
import Kanchanjunga.JWT.JwtRequest;
import Kanchanjunga.JWT.JwtResponse;
import Kanchanjunga.Services.UsersService;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/" }, allowCredentials = "true")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UsersService usersService;

	@Autowired
	private PasswordEncoder encoder;

	@PostMapping("login")
	public ResponseEntity<?> loginUser(@RequestBody JwtRequest jwtRequest) {
		doAuthenticate(jwtRequest.getName(), jwtRequest.getPassword());
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getName());
		String token = this.jwtHelper.generateToken(jwtRequest.getName());

		JwtResponse jwtResponse = JwtResponse.builder().token(token).username(userDetails.getUsername()).build();
		return new ResponseEntity<>(jwtResponse, HttpStatus.OK);

		// try {
		// OrdersDto ordersByID = this.ordersService.getOrdersByID(ids);
		// if (ordersByID != null) {
		// response.put("status", 200);
		// response.put("Order", ordersByID);
		// return ResponseEntity.status(200).body(response);

		// }
		// response.put("status", 400);
		// response.put("message", "order is empty 🤢🤢");
		// return ResponseEntity.status(200).body(response);

		// } catch (Exception e) {
		// e.printStackTrace();
		// return null;
		// }
	}

	public void doAuthenticate(String name, String password) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				name, password);
		try {
			authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(" Invalid Username or Password  !!");

		}
	}

	@ExceptionHandler(BadCredentialsException.class)
	public String badCredentialEXcHandler() {
		return ("credential invalid");

	}

	@PostMapping("register")
	private ResponseEntity<?> createUser(@ModelAttribute UserDTO user) {
		user.setPassword(encoder.encode(user.getPassword()));
		String createUser = usersService.createUser(user);
		HashMap<String, Object> response = new HashMap<>();
		if (createUser.equalsIgnoreCase("saved")) {
			response.put("status", 200);
			response.put("message", "user created successfully");
			return ResponseEntity.status(200).body(response);
		}
		response.put("status", 400);
		response.put("message", "user creation failed");
		return ResponseEntity.status(200).body(response);

	}

}
