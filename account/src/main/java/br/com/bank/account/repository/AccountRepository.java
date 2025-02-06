package br.com.bank.account.repository;

import br.com.bank.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,Long> {
    Optional<AccountEntity> findByNameAndNumber(String name, String number);
    Optional<AccountEntity> findByCpf(String cpf);
    boolean existsByNumber(String number);

}
