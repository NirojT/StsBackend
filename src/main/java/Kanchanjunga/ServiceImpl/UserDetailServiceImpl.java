package Kanchanjunga.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import Kanchanjunga.Entity.Users;
import Kanchanjunga.Reposioteries.UserRepo;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Users user = userRepo.findByName(name).orElseThrow(() -> new RuntimeException("user not found.."));
		return user;
	}

}
