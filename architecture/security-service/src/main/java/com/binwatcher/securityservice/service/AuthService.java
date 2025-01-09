package com.binwatcher.securityservice.service;

import com.binwatcher.securityservice.entity.User;
import com.binwatcher.securityservice.helper.JWTUtil;
import com.binwatcher.securityservice.model.Authority;
import com.binwatcher.securityservice.model.LoginRequest;
import com.binwatcher.securityservice.model.LoginResponse;
import com.binwatcher.securityservice.model.RegisterRequest;
import com.binwatcher.securityservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;
    public User register(RegisterRequest userParams) throws IllegalArgumentException, Exception {
        if (userRepository.findByEmail(userParams.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Email : " + userParams.getLogin() + " already exists");
        }

        User user = new User();
        user.setEmail(userParams.getLogin());
        user.setName(userParams.getName());
        user.setPassword(passwordEncoder.encode(userParams.getPassword()));
        user.setRoles(userParams.getRoles().stream()
                .map(Authority::new)
                .collect(Collectors.toList())
        );
        userRepository.save(user);
        return user;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public LoginResponse login(LoginRequest request) {
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

        return LoginResponse
                .builder()
                .token(token)
                .build();

    }
}
