package com.example.demo.service.abs;

import com.example.demo.model.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();
    Account getAccountById(int id);
    boolean saveOrUpdateAccount(Account account);
    boolean createAccount(Account account);
}
