package com.sweng22g1.spike3.api;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweng22g1.spike3.model.Role;
import com.sweng22g1.spike3.model.User;
import com.sweng22g1.spike3.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserController {

	private final UserService userService;

	@Autowired
	private Environment env;

	@GetMapping("user")
	public ResponseEntity<List<User>> getUsers() {
		System.out.println(env.getProperty("spike3.test-prop"));
		return ResponseEntity.ok().body(userService.getUsers());
	}

	@GetMapping("role")
	public ResponseEntity<List<Role>> getRoles() {
		return ResponseEntity.ok().body(userService.getRoles());
	}

	@PostMapping("user")
	public ResponseEntity<User> saveUser(@RequestBody User thisUser) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(thisUser));
	}

	@PostMapping("role")
	public ResponseEntity<Role> saveRole(@RequestBody Role thisRole) {
//		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/role").toUriString());
//		return ResponseEntity.created(uri).body(userService.saveRole(thisRole));
		System.out.println(thisRole);
		return ResponseEntity.ok().body(userService.saveRole(thisRole));
	}

	@GetMapping("token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authorisationHeader = request.getHeader(AUTHORIZATION);
		if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {
			try {

				String refresh_token = authorisationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				User user = userService.getUser(username);
				String access_token = JWT.create().withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);

//				response.setHeader("access_token", access_token);
//				response.setHeader("refresh_token", refresh_token);

				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);

				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);

			} catch (Exception exception) {
				response.setHeader("error", exception.getMessage());
//				response.sendError(FORBIDDEN.value());
				response.setStatus(FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", exception.getMessage());
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), error);

			}
		} else {
			throw new RuntimeException("Refresh token is missing");
		}
	}

}
