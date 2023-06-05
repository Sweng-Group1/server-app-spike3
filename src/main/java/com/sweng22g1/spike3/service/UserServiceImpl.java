package com.sweng22g1.spike3.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sweng22g1.spike3.model.Role;
import com.sweng22g1.spike3.model.User;
import com.sweng22g1.spike3.repo.RoleRepository;
import com.sweng22g1.spike3.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User thisUser = userRepo.findByUsername(username);
		if (thisUser == null) {
			log.error("User not found in the database");
			throw new UsernameNotFoundException("User not found in the database");
		} else {
			log.info("User found in the database: {}", username);
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		thisUser.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(thisUser.getUsername(), thisUser.getPassword(),
				authorities);
	}

	@Override
	public User saveUser(User user) {
		log.info("Saving User {} to db", user.getUsername());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		log.info("Saving Role {} to db", role.getName());
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		log.info("Adding Role {} to User {}", roleName, username);
		User thisUser = userRepo.findByUsername(username);
		Role thisRole = roleRepo.findByName(roleName);
		thisUser.getRoles().add(thisRole);
	}

	@Override
	public User getUser(String username) {
		log.info("Fetching User {}", username);
		return userRepo.findByUsername(username);
	}

	@Override
	public List<User> getUsers() {
		log.info("Fetching all users");
		return userRepo.findAll();
	}

	@Override
	public List<Role> getRoles() {
		log.info("Fetching all roles");
		return roleRepo.findAll();
	}

}
