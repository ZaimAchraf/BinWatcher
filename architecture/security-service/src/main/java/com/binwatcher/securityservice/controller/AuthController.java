package com.binwatcher.securityservice.controller;

import com.binwatcher.securityservice.entity.User;
import com.binwatcher.securityservice.exception.UserAlreadyExistsException;
import com.binwatcher.securityservice.model.LoginRequest;
import com.binwatcher.securityservice.model.LoginResponse;
import com.binwatcher.securityservice.model.RegisterRequest;
import com.binwatcher.securityservice.model.UserDTO;
import com.binwatcher.securityservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerParams) {
        try {
            String registeredUserId = authService.register(registerParams);
            return ResponseEntity.ok(registeredUserId);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOG.error("Unexpected error occurred during registration for email: {}: {}", registerParams.getLogin(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginParams) {
        try {
            LoginResponse response = authService.login(loginParams);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            LOG.warn("Login failed for user with email: {} - Reason: {}", loginParams.getLogin(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOG.error("Unexpected error occurred during login for email: {}: {}", loginParams.getLogin(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getEmailById(@PathVariable String id) {
        LOG.info("Fetching email for user with ID: {}", id);

        return authService.getById(id).map(user -> {
            LOG.info("User found with ID: {} and email: {}", id, user.getEmail());
            return new ResponseEntity<>(user.getEmail(), HttpStatus.OK);
        }).orElseGet(() -> {
            LOG.warn("User not found with ID: {}", id);
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        });    }

    @GetMapping("/all")
    public List<UserDTO> getAll() {
        List<UserDTO> users = authService.getAll();
        LOG.info("Fetched {} users", users.size());
        return users;
    }


}
