package br.com.bank.account.service;

import br.com.bank.account.dto.CreateAccountRequestDTO;
import br.com.bank.account.dto.DepositRequestDTO;
import br.com.bank.account.dto.InfoAccountDTO;
import br.com.bank.account.dto.TransferRequestDTO;
import br.com.bank.account.entity.AccountEntity;
import br.com.bank.account.exception.*;
import br.com.bank.account.repository.AccountRepository;
import br.com.bank.account.util.AccountTransactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

    public AccountEntity saveNewAccount(CreateAccountRequestDTO account) {

        if (account.getId() == null) {
            // verificar se conta existe

            // evoluir essa validação para criar conta com nome e cpf da pessoa,
            // numero da conta deve ser gerado automaticamente, sendo unico no banco de dados

            Optional<AccountEntity> userAlreadyExist = getAccountByCPF(account.getCpf());

            if (userAlreadyExist.isPresent()) {
                throw new AccountException("Account already exist");
            }

            // numero nao existe, cria a conta nova

            AccountEntity createAccount = new AccountEntity();

            createAccount.setName(account.getName());
            createAccount.setCpf(account.getCpf());
            createAccount.setNumber(generateUniqueAccountNumber());
            createAccount.setBalance(BigDecimal.valueOf(0.00));

            return accountRepository.save(createAccount);

        } else {
            throw new CreateAccountException("Id account must be null");
        }

    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        Random random = new Random();
        do {
            accountNumber = String.format("%08d", random.nextInt(100000000)); // Gera um número de 8 dígitos
        } while (accountRepository.existsByNumber(accountNumber)); // Verifica se já existe no banco

        return accountNumber;
    }


    public Optional<AccountEntity> getAccountByNameAndNumber(String name, String number) {
        return accountRepository.findByNameAndNumber(name, number);
    }

    public Optional<AccountEntity> getAccountByCPF(String cpf) {
        return accountRepository.findByCpf(cpf);
    }


    public AccountEntity findAccount(InfoAccountDTO account_params) {

        Optional<AccountEntity> account = getAccountByNameAndNumber(
                account_params.getName(),
                account_params.getNumber()
        );

        if (account.isPresent()) {
            return account.get();
        } else {
            throw new AccountNotFoundException(
                    String.format("Account %s not found", account_params.getNumber())
            );
        }

    }

    public AccountEntity executeDeposit(DepositRequestDTO deposit_params) {

        InfoAccountDTO account = deposit_params.getDestination_account();
        BigDecimal deposit_value = deposit_params.getDeposit_value();

        AccountEntity destinationAccount = findAccount(account);

        BigDecimal balance = destinationAccount.getBalance();
        BigDecimal newBalance = transaction.addValue(balance, deposit_value);

        updateBalance(destinationAccount, newBalance);

        return destinationAccount;

    }

    public AccountEntity transferenceBetweenAccounts(TransferRequestDTO transference_params) {

        InfoAccountDTO origin = transference_params.getOrigin_account();
        InfoAccountDTO destination = transference_params.getDestination_account();
        BigDecimal transfer_value = transference_params.getTransfer_value();

        AccountEntity originAccount = findAccount(origin);
        AccountEntity destinationAccount = findAccount(destination);

        BigDecimal balanceOrigin = originAccount.getBalance();
        BigDecimal balanceDestination = destinationAccount.getBalance();

        if (balanceOrigin.compareTo(transfer_value) >= 0) {

            BigDecimal newBalanceOrigin = transaction.subtractValue(balanceOrigin, transfer_value);
            updateBalance(destinationAccount, newBalanceOrigin);

            BigDecimal debit_value = transfer_value.multiply(BigDecimal.valueOf(-1));

            extractService.updateExtract(
                    originAccount.getNumber(),
                    "debit",
                    debit_value,
                    destinationAccount.getNumber(),
                    LocalDateTime.now(),
                    newBalanceOrigin);

            BigDecimal newBalanceDestination = transaction.addValue(balanceDestination, transfer_value);
            updateBalance(destinationAccount, newBalanceDestination);

            extractService.updateExtract(
                    destinationAccount.getNumber(),
                    "credit",
                    transfer_value,
                    originAccount.getNumber(),
                    LocalDateTime.now(),
                    newBalanceDestination);

            return originAccount;

        } else {
            throw new InsufficientFundsException("Insufficient balance to make the transfer.");
        }

    }

    public void updateBalance(AccountEntity account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

}
