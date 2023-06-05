package com.sweng22g1.spike3.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sweng22g1.spike3.model.User;
import com.sweng22g1.spike3.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/")
public class HealthCheck {
	
	@GetMapping("ping")
	public ResponseEntity<String> getUsers() {
		System.out.println("ping pong!");
		return ResponseEntity.ok().body("pong");
	}
	
}
