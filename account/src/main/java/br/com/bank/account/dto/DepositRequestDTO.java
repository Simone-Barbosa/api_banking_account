package br.com.bank.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DepositRequestDTO {

    @NotNull(message = "Destination account information is required")
    private final InfoAccountDTO destination_account;

    @NotNull(message = "Deposit value cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Deposit value must be greater than zero")
    private final BigDecimal deposit_value;

}

// immutable class, model one