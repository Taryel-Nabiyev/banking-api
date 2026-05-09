package com.project.banking.service;

import com.project.banking.dto.*;
import com.project.banking.entity.Account;
import com.project.banking.entity.Transaction;
import com.project.banking.entity.User;
import com.project.banking.repository.AccountRepository;
import com.project.banking.repository.TransactionRepo;
import com.project.banking.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepo userRepo;
    private final AccountMapper accountMapper;
    private final TransactionRepo transactionRepository;
    private final TransactionMapper transactionMapper;

    public AccountResponseDTO createAccount(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (accountRepository.existsByUser(user)) {
            throw new RuntimeException("User already has an account");
        }

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(0.0)
                .user(user)
                .build();

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toAccountDTO(savedAccount);
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }




    @Transactional
    public String transfer(String username, TransferRequestDTO request) {
        // 5. Check amount is positive
        if (request.getAmount() <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        // 1. Find sender's account
        User sender = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account senderAccount = accountRepository.findByUser(sender)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));


        // 3. Check not sending to yourself
        if (senderAccount.getAccountNumber().equals(request.getToAccountNumber())) {
            throw new RuntimeException("Cannot transfer to your own account");
        }

        // 4. Check sufficient balance
        if (senderAccount.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }


        // 2. Find receiver's account
        Account receiverAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        // 6. Update balances
        senderAccount.setBalance(senderAccount.getBalance() - request.getAmount());
        receiverAccount.setBalance(receiverAccount.getBalance() + request.getAmount());

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // 7. Record transaction
        Transaction transaction = Transaction.builder()
                .sender(senderAccount)
                .receiver(receiverAccount)
                .amount(request.getAmount())
                .build();

        transactionRepository.save(transaction);

        return "Transfer successful";
    }


    public List<TransactionResponseDTO> getTransactionHistory(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<Transaction> transactions = transactionRepository
                .findBySenderOrReceiverOrderByCreatedAtDesc(account, account);

        return transactionMapper.toTransactionDTOList(transactions);
    }
}
