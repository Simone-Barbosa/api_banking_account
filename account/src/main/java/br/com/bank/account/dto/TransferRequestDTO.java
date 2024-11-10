package br.com.bank.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class TransferRequestDTO {

    @NotNull(message = "Origin account information is required")
    private final InfoAccountDTO origin_account;

    @NotNull(message = "Destination account information is required")
    private final InfoAccountDTO destination_account;

    @NotNull(message = "Transfer value cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Transfer value must be greater than zero")
    private final BigDecimal transfer_value;

    // constructor
    public TransferRequestDTO(
            InfoAccountDTO origin_account, InfoAccountDTO destination_account, BigDecimal transfer_value) {
        this.origin_account = origin_account;
        this.destination_account = destination_account;
        this.transfer_value = transfer_value;
    }
}

// immutable class, model three