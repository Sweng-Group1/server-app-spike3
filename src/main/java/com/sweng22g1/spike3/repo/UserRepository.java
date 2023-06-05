package com.sweng22g1.spike3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweng22g1.spike3.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}
