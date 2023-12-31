package Kanchanjunga.Controller;

import java.util.HashMap;

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
import org.springframework.web.bind.annotation.GetMapping;
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
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/",
		"http://192.168.0.128:5173" }, allowCredentials = "true")
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

	@GetMapping("check-auth")
	private ResponseEntity<?> checkAuthentication(HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<>();
		try {

			String requestHeader = request.getHeader("Authorization");
			String token = null;
			if (requestHeader != null && requestHeader.startsWith("Bearer")) {
				token = requestHeader.substring(7);

				Boolean isValid = jwtHelper.validateLoginToken(token);
				// String username = jwtHelper.extractUsername(token);
				response.put("status", isValid ? 200 : 400);
				response.put("message",
						isValid ? "valid user token" : "invalid user token");
				return ResponseEntity.status(200).body(response);
			}
			return ResponseEntity.status(200).body("fail");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong...");
			return ResponseEntity.status(200).body(response);
		}
	}

}
