package br.com.bank.account.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Value
public class InfoAccountDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be at most 100 characters")
    String name;

    @NotBlank(message = "Number cannot be blank")
    @Size(max = 45, message = "Number must be at most 45 characters")
    String number;

    @JsonCreator
    public InfoAccountDTO(
            @JsonProperty("name") String name,
            @JsonProperty("number") String number) {
        this.name = name;
        this.number = number;
    }
}

// immutable class, model two