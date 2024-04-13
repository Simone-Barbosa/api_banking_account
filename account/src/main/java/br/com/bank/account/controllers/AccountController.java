package br.com.bank.account.controllers;

import br.com.bank.account.classes.AccountTransactions;
import br.com.bank.account.entities.AccountEntity;
import br.com.bank.account.interfaces.DepositRequest;
import br.com.bank.account.interfaces.InfoAccount;
import br.com.bank.account.interfaces.TransferRequest;
import br.com.bank.account.services.AccountService;
import br.com.bank.account.services.ExtractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    ExtractService extractService;

    AccountTransactions transaction = new AccountTransactions();

    @GetMapping("/all")
    public List<AccountEntity> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @PostMapping("/search")
    public ResponseEntity<AccountEntity> searchAccountByNameAndNumber(@RequestBody Map<String, String> params) {
        String name = params.get("name");
        String number = params.get("number");

        Optional<AccountEntity> account = accountService.getAccountByNameAndNumber(name, number);
        if (account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/deposit")
    public ResponseEntity<AccountEntity> depositOnAccount(@RequestBody DepositRequest params) {

        InfoAccount destination = params.getDestination_account();
        BigDecimal deposit_value = params.getDeposit_value();

        Optional<AccountEntity> account = accountService.getAccountByNameAndNumber(destination.getName(),
                destination.getNumber());

        if (account.isPresent()) {
            AccountEntity accountUpdated = account.get();
            BigDecimal balance = accountUpdated.getBalance();

            BigDecimal newBalance = transaction.addValue(balance, deposit_value);
            accountService.updateBalance(accountUpdated, newBalance);

            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/transfer")
    public ResponseEntity<AccountEntity> transferBetweenAccounts(@RequestBody TransferRequest params) {

        try {
            InfoAccount origin = params.getOrigin_account();
            InfoAccount destination = params.getDestination_account();
            BigDecimal transfer_value = params.getTransfer_value();

            Optional<AccountEntity> originAccount = accountService.getAccountByNameAndNumber(
                    origin.getName(),
                    origin.getNumber());

            Optional<AccountEntity> destinationAccount = accountService.getAccountByNameAndNumber(
                    destination.getName(),
                    destination.getNumber());

            if (originAccount.isPresent() && destinationAccount.isPresent()) {
                AccountEntity originAccountUpdated = originAccount.get();
                AccountEntity destinationAccountUpdated = destinationAccount.get();

                BigDecimal balanceOrigin = originAccountUpdated.getBalance();
                BigDecimal balanceDestination = destinationAccountUpdated.getBalance();

                if(balanceOrigin.compareTo(transfer_value) >= 0){

                    BigDecimal newBalanceOrigin = transaction.subtractValue(balanceOrigin, transfer_value);
                    accountService.updateBalance(originAccountUpdated, newBalanceOrigin);

                    BigDecimal transfer_value_negative = transfer_value.multiply(BigDecimal.valueOf(-1));

                    extractService.updateExtract(originAccountUpdated.getNumber(),
                            "debit",
                            transfer_value_negative,
                            destinationAccountUpdated.getNumber(),
                            LocalDateTime.now(),
                            newBalanceOrigin);

                    BigDecimal newBalanceDestination = transaction.addValue(balanceDestination, transfer_value);
                    accountService.updateBalance(destinationAccountUpdated, newBalanceDestination);

                    extractService.updateExtract(destinationAccountUpdated.getNumber(),
                            "credit",
                            transfer_value,
                            originAccountUpdated.getNumber(),
                            LocalDateTime.now(),
                            newBalanceDestination);

                    return ResponseEntity.ok(originAccount.get());

                } else{
                    throw new IllegalArgumentException("Insufficient balance to make the transfer");
                }

            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity saveNewAccount(@RequestBody AccountEntity account){
        if(account.getId() == null){
            accountService.saveNewAccount(account);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }
}