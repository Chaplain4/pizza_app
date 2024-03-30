package com.example.demo.service.impl;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.abs.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository ar;

    @Override
    public List<Account> getAllAccounts() {
        return ar.findAll();
    }

    @Override
    public Account getAccountById(int id) {
        return ar.getReferenceById(id);
    }

    @Transactional
    @Override
    public boolean saveOrUpdateAccount(Account account) {
        try {
            ar.save(account);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean createAccount(Account account) {
        try {
            ar.save(account);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAccount(int id) {
        try {
            ar.deleteById(id);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}
