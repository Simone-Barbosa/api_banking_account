package br.com.bank.account.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account", nullable = false, unique = true)
    private Long id;

    @Column(name = "owner_account", nullable = false)
    private String name;

    @Column(name = "number_account", nullable = false)
    private String number;

    @Column(name = "balance_account", nullable = false)
    private BigDecimal balance;

}

