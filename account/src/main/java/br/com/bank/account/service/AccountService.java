package br.com.bank.account.service;

import br.com.bank.account.dto.DepositRequestDTO;
import br.com.bank.account.dto.InfoAccountDTO;
import br.com.bank.account.dto.TransferRequestDTO;
import br.com.bank.account.entity.AccountEntity;
import br.com.bank.account.exception.AccountNotFoundException;
import br.com.bank.account.exception.InsufficientFundsException;
import br.com.bank.account.repository.AccountRepository;
import br.com.bank.account.util.AccountTransactions;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ExtractService extractService;

    AccountTransactions transaction = new AccountTransactions();

    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void saveNewAccount(AccountEntity account) {
        accountRepository.save(account);
    }

    public Optional<AccountEntity> getAccountByNameAndNumber(String name, String number) {
        return accountRepository.findByNameAndNumber(name, number);
    }

    public AccountEntity findAccount(InfoAccountDTO account_params) {

        String name = account_params.getName();
        String number = account_params.getNumber();

        Optional<AccountEntity> account = getAccountByNameAndNumber(name, number);

        if (account.isPresent()) {
            return account.get();
        } else {
            throw new EntityNotFoundException("Account not found with name: " + name + " and number: " + number);
        }
    }

    public AccountEntity findAccountForDeposit(DepositRequestDTO deposit_params) {

        InfoAccountDTO destination_account = deposit_params.getDestination_account();
        BigDecimal deposit_value = deposit_params.getDeposit_value();

        Optional<AccountEntity> account = getAccountByNameAndNumber(
                destination_account.getName(),
                destination_account.getNumber()
        );

        if (account.isPresent()) {
            AccountEntity accountUpdated = account.get();
            BigDecimal balance = accountUpdated.getBalance();

            BigDecimal newBalance = transaction.addValue(balance, deposit_value);
            updateBalance(accountUpdated, newBalance);

            return account.get();
        } else {
            throw new EntityNotFoundException("Account not found: " + destination_account);
        }

    }

    public AccountEntity transferenceBetweenAccounts(TransferRequestDTO transference_params) {

        InfoAccountDTO origin = transference_params.getOrigin_account();
        InfoAccountDTO destination = transference_params.getDestination_account();
        BigDecimal transfer_value = transference_params.getTransfer_value();

        Optional<AccountEntity> originAccount = getAccountByNameAndNumber(
                origin.getName(),
                origin.getNumber());

        Optional<AccountEntity> destinationAccount = getAccountByNameAndNumber(
                destination.getName(),
                destination.getNumber());

        if (originAccount.isPresent() && destinationAccount.isPresent()) {
            AccountEntity originAccountUpdated = originAccount.get();
            AccountEntity destinationAccountUpdated = destinationAccount.get();

            BigDecimal balanceOrigin = originAccountUpdated.getBalance();
            BigDecimal balanceDestination = destinationAccountUpdated.getBalance();

            if (balanceOrigin.compareTo(transfer_value) >= 0) {

                BigDecimal newBalanceOrigin = transaction.subtractValue(balanceOrigin, transfer_value);
                updateBalance(originAccountUpdated, newBalanceOrigin);

                BigDecimal transfer_value_negative = transfer_value.multiply(BigDecimal.valueOf(-1));

                extractService.updateExtract(
                        originAccountUpdated.getNumber(),
                        "debit",
                        transfer_value_negative,
                        destinationAccountUpdated.getNumber(),
                        LocalDateTime.now(),
                        newBalanceOrigin);

                BigDecimal newBalanceDestination = transaction.addValue(balanceDestination, transfer_value);
                updateBalance(destinationAccountUpdated, newBalanceDestination);

                extractService.updateExtract(
                        destinationAccountUpdated.getNumber(),
                        "credit",
                        transfer_value,
                        originAccountUpdated.getNumber(),
                        LocalDateTime.now(),
                        newBalanceDestination);

                return originAccount.get();

            } else {
                throw new InsufficientFundsException("Insufficient balance to make the transfer.");
            }

        } else {
            throw new AccountNotFoundException("Accounts not found: " + "origin: " + originAccount + "destination: " + destinationAccount);
        }

    }

    public void updateBalance(AccountEntity account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

}
