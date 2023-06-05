package com.sweng22g1.spike3.service;

import java.util.List;

import com.sweng22g1.spike3.model.Role;
import com.sweng22g1.spike3.model.User;

public interface UserService {

	User saveUser(User user);

	Role saveRole(Role role);

	void addRoleToUser(String username, String roleName);

	User getUser(String username);

	List<User> getUsers();
	
	List<Role> getRoles();

}
