package br.com.bank.account.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity(name = "extract_transactions")
public class ExtractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction", nullable = false, unique = true)
    private Long id;

    @Column(name = "account_main", nullable = false)
    private String account_main;

    @Column(name = "type_operation", nullable = false)
    private String type_operation;

    @Column(name = "value_transaction", nullable = false)
    private BigDecimal value_transaction;

    @Column(name = "related_account", nullable = false)
    private String related_account;

    @Column(name = "date_transaction", nullable = false)
    private LocalDateTime date_transaction;

    @Column(name = "balance_account_main")
    private BigDecimal balance_account_main;

}
