package com.project.banking.dto;

import com.project.banking.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toUserDTO(User user);

    List<UserResponseDTO> toUserDTOList(List<User> users);
}
