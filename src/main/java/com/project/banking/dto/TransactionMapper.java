package com.project.banking.dto;

import com.project.banking.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "sender.user.username" , target = "sender")
    @Mapping(source = "receiver.user.username" , target = "receiver")
    TransactionResponseDTO toTransactionDTO(Transaction transaction);

    List<TransactionResponseDTO> toTransactionDTOList(List<Transaction> transactions);
}
