package br.com.bank.account.controller;

import br.com.bank.account.util.AccountTransactions;
import br.com.bank.account.dto.DepositRequestDTO;
import br.com.bank.account.dto.InfoAccountDTO;
import br.com.bank.account.dto.TransferRequestDTO;
import br.com.bank.account.entity.AccountEntity;
import br.com.bank.account.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    AccountService accountService;

    AccountTransactions transaction = new AccountTransactions();

    @GetMapping("/all")
    public List<AccountEntity> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("/search")
    public ResponseEntity<AccountEntity> searchAccount(@Valid @RequestBody InfoAccountDTO account_params) {

        try {
            AccountEntity account = accountService.findAccount(account_params);
            return ResponseEntity.ok(account);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/deposit")
    public ResponseEntity<AccountEntity> depositOnAccount(@Valid @RequestBody DepositRequestDTO deposit_params) {

        try {
            AccountEntity destinationAccount = accountService.findAccountForDeposit(deposit_params);
            return ResponseEntity.ok(destinationAccount);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/transfer")
    public ResponseEntity<AccountEntity> transferBetweenAccounts(@Valid @RequestBody TransferRequestDTO transference_params) {

        AccountEntity originAccount = accountService.transferenceBetweenAccounts(transference_params);
        return ResponseEntity.ok(originAccount);
    }

    @PostMapping("/create")
    public ResponseEntity createNewAccount(@RequestBody AccountEntity account) {

        if (account.getId() == null) {
            accountService.saveNewAccount(account);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}