package br.com.bank.account.interfaces;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class DepositRequest {
    private InfoAccount destination_account;
    private BigDecimal deposit_value;

}

