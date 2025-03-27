package com.binwatcher.securityservice.service;

import com.binwatcher.securityservice.entity.User;
import com.binwatcher.securityservice.exception.UserAlreadyExistsException;
import com.binwatcher.securityservice.helper.JWTUtil;
import com.binwatcher.securityservice.model.*;
import com.binwatcher.securityservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    public String register(RegisterRequest userParams) throws UserAlreadyExistsException, Exception {
        LOG.info("Attempting to register user with email: {}", userParams.getLogin());

        if (userRepository.findByEmail(userParams.getLogin()).isPresent()) {
            LOG.error("User with email {} already exists", userParams.getLogin());
            throw new UserAlreadyExistsException("Email : " + userParams.getLogin() + " already exists");
        }

        User user = new User();
        user.setEmail(userParams.getLogin());
        user.setName(userParams.getName());
        user.setPassword(passwordEncoder.encode(userParams.getPassword()));
        user.setRoles(userParams.getRoles().stream()
                .map(Authority::new)
                .collect(Collectors.toList())
        );
        user = userRepository.save(user);
        LOG.info("User registered successfully with id: {}", user.getId());
        return user.getId();
    }

    public LoginResponse login(LoginRequest request) {
        LOG.info("User attempting to login with email: {}", request.getLogin());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(),
                            request.getPassword()
                    )
            );

            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", roles);

            String token = jwtUtil.generateToken(claims, request.getLogin());
            LOG.info("User login successful, token generated for: {}", request.getLogin());

            return LoginResponse
                    .builder()
                    .token(token)
                    .build();

        } catch (Exception e) {
            LOG.error("Login attempt failed for user {}: {}", request.getLogin(), e.getMessage());
            throw new RuntimeException("Login failed due to incorrect credentials or other error.");
        }
    }

    public List<UserDTO> getAll() {
        LOG.info("Fetching all users from the database.");
        return userRepository.findAll().stream()
                .map(user -> new UserDTO
                        (
                            user.getId(),
                            user.getEmail(),
                            user.getName(),
                                user.getRoles()
                                        .stream()
                                        .map(Authority::getAuthority)
                                        .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getById(String id) {
        LOG.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(user -> new UserDTO
                        (
                            user.getId(),
                            user.getEmail(),
                            user.getName(),
                            user.getRoles()
                                    .stream()
                                    .map(Authority::getAuthority)
                                    .collect(Collectors.toList()
                        )
                )
        );
    }
}
