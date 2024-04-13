package br.com.bank.account.services;

import br.com.bank.account.entities.ExtractEntity;
import br.com.bank.account.repositories.ExtractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExtractService {
    @Autowired
    ExtractRepository extractRepository;

    public void updateExtract(String account_main,
                              String type_operation,
                              BigDecimal value_transaction,
                              String related_account,
                              LocalDateTime date_transaction,
                              BigDecimal balance_account_main) {

        ExtractEntity extract = new ExtractEntity();

        extract.setAccount_main(account_main);
        extract.setType_operation(type_operation);
        extract.setValue_transaction(value_transaction);
        extract.setRelated_account(related_account);
        extract.setDate_transaction(date_transaction);
        extract.setBalance_account_main(balance_account_main);

        extractRepository.save(extract);
    }

    public List<ExtractEntity> getExtractAccount(String account_main) {
        return extractRepository.getExtractByAccountMain(account_main);
    }
}
