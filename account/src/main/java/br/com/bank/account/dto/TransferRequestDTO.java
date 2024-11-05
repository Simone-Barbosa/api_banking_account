package br.com.bank.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class TransferRequestDTO {

    @NotNull(message = "Origin account information is required")
    private InfoAccountDTO origin_account;

    @NotNull(message = "Destination account information is required")
    private InfoAccountDTO destination_account;

    @NotNull(message = "Transfer value cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Transfer value must be greater than zero")
    private BigDecimal transfer_value;

}
