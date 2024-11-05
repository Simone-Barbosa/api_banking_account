package br.com.bank.account.util;
import java.math.BigDecimal;

public class AccountTransactions {

    public BigDecimal addValue(BigDecimal balance, BigDecimal value){
        return balance.add(value);
    }

    public BigDecimal subtractValue(BigDecimal balance, BigDecimal value){
        return balance.subtract(value);
    }
}
