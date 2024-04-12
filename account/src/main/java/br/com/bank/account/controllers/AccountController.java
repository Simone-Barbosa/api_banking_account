package br.com.bank.account.controllers;

import br.com.bank.account.classes.AccountTransactions;
import br.com.bank.account.entities.AccountEntity;
import br.com.bank.account.interfaces.DepositRequest;
import br.com.bank.account.interfaces.InfoAccount;
import br.com.bank.account.interfaces.TransferRequest;
import br.com.bank.account.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    AccountService accountService;

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
//        return account.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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

            Optional<AccountEntity> originAccount = accountService.getAccountByNameAndNumber(origin.getName(),
                    origin.getNumber());

            Optional<AccountEntity> destinationAccount = accountService.getAccountByNameAndNumber(destination.getName(),
                    destination.getNumber());

            if (originAccount.isPresent() && destinationAccount.isPresent()) {
                AccountEntity originAccountUpdated = originAccount.get();
                AccountEntity destinationAccountUpdated = destinationAccount.get();

                BigDecimal balanceOrigin = originAccountUpdated.getBalance();
                BigDecimal balanceDestination = destinationAccountUpdated.getBalance();

                if(balanceOrigin.compareTo(transfer_value) >= 0){

                    BigDecimal newBalanceOrigin = transaction.subtractValue(balanceOrigin, transfer_value);
                    accountService.updateBalance(originAccountUpdated, newBalanceOrigin);

                    BigDecimal newBalanceDestination = transaction.addValue(balanceDestination, transfer_value);
                    accountService.updateBalance(destinationAccountUpdated, newBalanceDestination);

                    return ResponseEntity.ok(originAccount.get()); // esse get é só pra mostrar o saldo atualizado

                } else{
                    throw new IllegalArgumentException("Insufficient balance to make the transfer");
                }

            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build(); // erro 400
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // erro 500
        }
    }

    @PostMapping
    public ResponseEntity saveNewAccount(@RequestBody AccountEntity account){
        if(account.getId() == null){
            accountService.saveNewAccount(account);
            return ResponseEntity.ok().build(); // status 200
        }else{
            return ResponseEntity.internalServerError().build(); // status 500
        }
    }

}
