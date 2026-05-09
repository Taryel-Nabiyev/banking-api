package com.project.banking.controller;

import com.project.banking.dto.UserMapper;
import com.project.banking.dto.UserResponseDTO;
import com.project.banking.entity.User;
import com.project.banking.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepo userRepo;

    private final UserMapper userMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userMapper.toUserDTOList(userRepo.findAll()));
    }
}
