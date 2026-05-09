package com.project.banking.controller;

import com.project.banking.dto.AuthRequest;
import com.project.banking.dto.AuthResponse;
import com.project.banking.dto.UserResponseDTO;
import com.project.banking.entity.User;
import com.project.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody Map<String , String> request){
        UserResponseDTO response = userService.register(
                request.get("username"),
                request.get("email"),
                request.get("password")
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

//    @GetMapping("/me")
//    public ResponseEntity<UserResponseDTO> getCurrentUser(Principal principal) {
//        User user = userRepo.findByUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return ResponseEntity.ok(userMapper.toUserDTO(user));
//    }

    // UserController
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(userService.getCurrentUser(principal.getName()));
    }
}
