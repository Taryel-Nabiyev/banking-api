package com.project.banking.controller;

import com.project.banking.dto.AccountResponseDTO;
import com.project.banking.dto.TransactionResponseDTO;
import com.project.banking.dto.TransferRequestDTO;
import com.project.banking.entity.Account;
import com.project.banking.entity.Transaction;
import com.project.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponseDTO> createAccount(Principal principal) {
        return ResponseEntity.ok(accountService.createAccount(principal.getName()));
    }


    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(Principal principal, @RequestBody TransferRequestDTO request) {
        return ResponseEntity.ok(accountService.transfer(principal.getName(), request));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(Principal principal) {
        return ResponseEntity.ok(accountService.getTransactionHistory(principal.getName()));
    }
}
