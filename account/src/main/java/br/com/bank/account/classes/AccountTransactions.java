package br.com.bank.account.classes;

import br.com.bank.account.entities.AccountEntity;
import br.com.bank.account.interfaces.InfoAccount;
import br.com.bank.account.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountTransactions {
    @Autowired
    AccountService accountService;

    public ResponseEntity<AccountEntity> depositValue(InfoAccount destination_account, BigDecimal value) {

        System.out.println("class param = " + destination_account);

        Optional<AccountEntity> account = accountService.getAccountByNameAndNumber(destination_account.getName(),
                destination_account.getNumber());

        if (account.isPresent()) {
            AccountEntity accountUpdated = account.get();
            BigDecimal balance = accountUpdated.getBalance();
            BigDecimal newBalance = balance.add(value);

            accountService.updateBalance(accountUpdated, newBalance);
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
