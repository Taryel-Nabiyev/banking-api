package com.project.banking.service;

import com.project.banking.dto.AuthRequest;
import com.project.banking.dto.AuthResponse;
import com.project.banking.dto.UserMapper;
import com.project.banking.dto.UserResponseDTO;
import com.project.banking.entity.Role;
import com.project.banking.entity.User;
import com.project.banking.repository.UserRepo;
import com.project.banking.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO register(String username , String email , String password){
        if(userRepo.existsByUsername(username)){
            throw new RuntimeException("Username already exist");
        }
        if(userRepo.existsByEmail(email)){
            throw new RuntimeException("Email already exist");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();

        User savedUser = userRepo.save(user);

        return userMapper.toUserDTO(savedUser);

    }


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(request.getUsername());
        return new AuthResponse(token);
    }

    // UserService
    public UserResponseDTO getCurrentUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserDTO(user);
    }



}
