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

    public BigDecimal addValue(BigDecimal balance, BigDecimal value){

        return balance.add(value);
    }

    public BigDecimal subtractValue(BigDecimal balance, BigDecimal value){
        return balance.subtract(value);
    }


}
