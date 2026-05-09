package com.project.banking.repository;

import com.project.banking.entity.Account;
import com.project.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

    @Repository
    public interface AccountRepository extends JpaRepository<Account, Long> {
        Optional<Account> findByUser(User user);
        Optional<Account> findByAccountNumber(String accountNumber);
        Boolean existsByUser(User user);
    }
