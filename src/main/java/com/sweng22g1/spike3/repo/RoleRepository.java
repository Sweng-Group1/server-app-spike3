package com.sweng22g1.spike3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweng22g1.spike3.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);

}
