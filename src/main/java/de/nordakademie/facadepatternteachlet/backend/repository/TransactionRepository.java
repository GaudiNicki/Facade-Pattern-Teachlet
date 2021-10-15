package de.nordakademie.facadepatternteachlet.backend.repository;

import de.nordakademie.facadepatternteachlet.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface <b>TransactionRepository</b> extends JpaRepository and therefore provides an interface for saving,
 * reading, updating and deleting bank transactions.
 *
 * @author Til Zöller
 * @author Merlin Ritsch
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByBankAccountId(Long id);
}
