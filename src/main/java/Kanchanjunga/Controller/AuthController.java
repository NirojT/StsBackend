package Kanchanjunga.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kanchanjunga.Entity.Users;
import Kanchanjunga.JWT.JwtHelper;
import Kanchanjunga.JWT.JwtRequest;
import Kanchanjunga.JWT.JwtResponse;
import Kanchanjunga.Services.UsersService;

@RestController
@RequestMapping("/api/user/")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UsersService usersService;

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
	private ResponseEntity<?> createUser(@RequestBody Users user) {
		user.setPassword(encoder.encode(user.getPassword()));
		String createUser = usersService.createUser(user);
		return ResponseEntity.status(200).body(createUser);
	}

}