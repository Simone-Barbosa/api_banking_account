package br.com.bank.account.service;

import br.com.bank.account.entity.ExtractEntity;
import br.com.bank.account.exception.ExtractNotFoundException;
import br.com.bank.account.repository.ExtractRepository;
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

        List<ExtractEntity> extract = extractRepository.getExtractByAccountMain(account_main);

        if(extract.isEmpty()){
            throw new ExtractNotFoundException("Extract is empty for account: " + account_main);
        }
        return extract;
    }
}
