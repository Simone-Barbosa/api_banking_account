package br.com.bank.account.interfaces;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class TransferRequest {

    private InfoAccount origin_account;
    private InfoAccount destination_account;
    private BigDecimal transfer_value;

}
