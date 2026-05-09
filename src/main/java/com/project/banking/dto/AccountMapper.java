package com.project.banking.dto;

import com.project.banking.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
    public interface AccountMapper {
        @Mapping(source = "user.username" , target = "username")
        AccountResponseDTO toAccountDTO(Account account);
    }

