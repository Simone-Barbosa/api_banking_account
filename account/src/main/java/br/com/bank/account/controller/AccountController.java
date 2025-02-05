package br.com.bank.account.controller;

import br.com.bank.account.dto.*;
import br.com.bank.account.entity.ExtractEntity;
import br.com.bank.account.service.ExtractService;
import br.com.bank.account.util.AccountTransactions;
import br.com.bank.account.entity.AccountEntity;
import br.com.bank.account.service.AccountService;
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

    @Autowired
    private ExtractService extractService;

    AccountTransactions transaction = new AccountTransactions();

    @GetMapping("/all")
    public List<AccountEntity> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping("/search")
    public ResponseEntity<AccountEntity> searchAccount(
            @Valid @RequestBody InfoAccountDTO account_params) {

        AccountEntity account = accountService.findAccount(account_params);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/deposit")
    public ResponseEntity<AccountEntity> depositOnAccount(
            @Valid @RequestBody DepositRequestDTO deposit_params) {

        AccountEntity destinationAccount = accountService.executeDeposit(deposit_params);
        return ResponseEntity.ok(destinationAccount);
    }

    @PutMapping("/transfer")
    public ResponseEntity<AccountEntity> transferBetweenAccounts(
            @Valid @RequestBody TransferRequestDTO transference_params) {

        AccountEntity originAccount = accountService.transferenceBetweenAccounts(transference_params);
        return ResponseEntity.ok(originAccount);
    }

    @PostMapping("/create")
    public ResponseEntity<AccountEntity> createNewAccount(
            @Valid @RequestBody CreateAccountRequestDTO account) {

        AccountEntity accountCreated = accountService.saveNewAccount(account);
        return ResponseEntity.ok(accountCreated);

    }

    @PostMapping("/extract")
    public ResponseEntity<List<ExtractEntity>> searchExtractByNumber(
            @Valid @RequestBody ExtractRequestDTO extractRequest) {

        List<ExtractEntity> extract = extractService.getExtractAccount(extractRequest.getAccount_main());
        return ResponseEntity.ok(extract);
    }
}