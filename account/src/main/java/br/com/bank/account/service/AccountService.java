package br.com.bank.account.service;

import br.com.bank.account.entity.AccountEntity;
import br.com.bank.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public List<AccountEntity> getAllAccounts(){
        return accountRepository.findAll();
    }

    public void saveNewAccount(AccountEntity account){
        accountRepository.save(account);
    }

    public Optional<AccountEntity> getAccountByNameAndNumber(String name, String number) {
        return accountRepository.findByNameAndNumber(name, number);
    }

    public void updateBalance(AccountEntity account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
