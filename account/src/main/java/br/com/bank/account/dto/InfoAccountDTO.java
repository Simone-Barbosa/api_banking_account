package br.com.bank.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoAccountDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Number cannot be blank")
    @Size(max = 45, message = "Number must be at most 45 characters")
    private String number;
}
