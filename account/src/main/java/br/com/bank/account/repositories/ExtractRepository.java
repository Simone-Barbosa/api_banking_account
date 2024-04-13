package br.com.bank.account.repositories;

import br.com.bank.account.entities.ExtractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExtractRepository extends JpaRepository<ExtractEntity, Long> {
    @Query("SELECT e FROM extract_transactions e WHERE e.account_main = :account_main")
    List<ExtractEntity> getExtractByAccountMain(@Param("account_main") String account_main);
}

