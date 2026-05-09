package com.project.banking;

import com.project.banking.dto.TransferRequestDTO;
import com.project.banking.entity.Account;
import com.project.banking.entity.User;
import com.project.banking.repository.AccountRepository;
import com.project.banking.repository.TransactionRepo;
import com.project.banking.repository.UserRepo;
import com.project.banking.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import com.project.banking.entity.Transaction;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepo userRepo;

    @Mock
    private TransactionRepo transactionRepository;

    @InjectMocks
    private AccountService accountService;


    @Test
    void transfer_insufficientBalance_throwsException() {
        // 1. SETUP — prepare fake data
        String username = "john";

        User sender = new User();
        sender.setUsername(username);

        Account senderAccount = new Account();
        senderAccount.setAccountNumber("ACC123");
        senderAccount.setBalance(50.0); // only has 50

        TransferRequestDTO request = new TransferRequestDTO();
        request.setToAccountNumber("ACC456");
        request.setAmount(100.0); // trying to send 100

        // 2. MOCK — fake the DB calls
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUser(sender)).thenReturn(Optional.of(senderAccount));


        // 3. ACT + ASSERT — call method and expect exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                accountService.transfer(username, request)
        );

        assertEquals("Insufficient balance", exception.getMessage());
    }


    @Test
    void transfer_ownAccount_throwsException(){
        String username = "jamie";

        User sender = new User();
        sender.setUsername(username);

        Account senderAccount = new Account();
        senderAccount.setAccountNumber("ACC123");

        TransferRequestDTO request = new TransferRequestDTO();
        request.setToAccountNumber("ACC123"); //same acc number with senderAcc
        request.setAmount(100.0);

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUser(sender)).thenReturn(Optional.of(senderAccount));


        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                accountService.transfer(username, request)
        );

        assertEquals("Cannot transfer to your own account", exception.getMessage());

    }

    @Test
    void transfer_negativeAmount_throwsException(){
        String username = "Jamie";

        TransferRequestDTO request = new TransferRequestDTO();
        request.setAmount(-100.0);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                accountService.transfer(username, request)
        );

        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    @Test
    void transfer_success_balancesUpdated(){
        // 1. SETUP — prepare fake data
        String username = "john";

        User sender = new User();
        sender.setUsername(username);

        Account senderAccount = new Account();
        senderAccount.setAccountNumber("ACC123");
        senderAccount.setBalance(200.0); //

        TransferRequestDTO request = new TransferRequestDTO();
        request.setToAccountNumber("ACC456");
        request.setAmount(100.0); //

        // 2. MOCK — fake the DB calls
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUser(sender)).thenReturn(Optional.of(senderAccount));

        Account receiverAccount = new Account();
        receiverAccount.setAccountNumber("ACC456");
        receiverAccount.setBalance(0.0);
        when(accountRepository.findByAccountNumber("ACC456")).thenReturn(Optional.of(receiverAccount));


        // just call the method
        String result = accountService.transfer(username, request);

        //assert
        assertEquals("Transfer successful", result);
        assertEquals(100.0, senderAccount.getBalance()); // 200 - 100
        assertEquals(100.0, receiverAccount.getBalance()); // 0 + 100
        verify(transactionRepository).save(org.mockito.ArgumentMatchers.any(Transaction.class)); // transaction was saved
    }
}
