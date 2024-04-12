package br.com.bank.account.repositories;

import br.com.bank.account.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,Long> {
    Optional<AccountEntity> findByNameAndNumber(String name, String number);


}
