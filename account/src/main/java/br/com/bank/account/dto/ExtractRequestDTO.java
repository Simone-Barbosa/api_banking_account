package br.com.bank.account.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtractRequestDTO {

    @NotBlank(message = "Account main cannot be blank")
    private String account_main;
}
