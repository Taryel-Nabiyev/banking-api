package com.project.banking;


import com.project.banking.dto.UserMapper;
import com.project.banking.dto.UserResponseDTO;
import com.project.banking.entity.User;
import com.project.banking.repository.UserRepo;
import com.project.banking.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Test
    void register_userExistByName_throwsException(){
        String username = "Jamie";
        String email = "Jamie@gmail.com";
        String password = "Fake_Password";

        when(userRepo.existsByUsername(username)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.register(username, email , password)
        );

        assertEquals("Username already exist", exception.getMessage());
    }

    @Test
    void register_userExistByEmail_throwsException(){
        String username = "Jamie";
        String email = "Jamie@gmail.com";
        String password = "Fake_Password";

        when(userRepo.existsByUsername(username)).thenReturn(false);
        when(userRepo.existsByEmail(email)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.register(username, email , password)
        );

        assertEquals("Email already exist", exception.getMessage());
    }


    @Test
    void register_success_returnsUserResponseDTO() {
        String username = "Jamie";
        String email = "Jamie@gmail.com";
        String password = "Fake_Password";
        String encodedPassword = "encoded_password";

        User savedUser = User.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .build();

        UserResponseDTO expectedDTO = UserResponseDTO.builder()
                .username(username)
                .email(email)
                .build();

        when(userRepo.existsByUsername(username)).thenReturn(false);
        when(userRepo.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepo.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toUserDTO(savedUser)).thenReturn(expectedDTO);

        UserResponseDTO result = userService.register(username, email, password);

        assertEquals(expectedDTO, result);
        // Make sure password was encoded and never saved as plain text
        verify(passwordEncoder).encode(password);
        verify(userRepo).save(any(User.class));
    }



}
