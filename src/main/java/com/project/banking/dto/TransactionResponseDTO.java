package com.project.banking.dto;

import com.project.banking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private String sender;
    private String receiver;
    private Double amount;
    private LocalDateTime createdAt;
}
