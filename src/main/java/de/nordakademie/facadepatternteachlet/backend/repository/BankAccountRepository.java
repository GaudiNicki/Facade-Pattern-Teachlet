package de.nordakademie.facadepatternteachlet.backend.repository;

import de.nordakademie.facadepatternteachlet.backend.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface <b>BankAccountRepository</b> extends JpaRepository and therefore provides an interface for saving,
 * reading, updating and deleting bank accounts.
 *
 * @author Til Zöller
 * @author Merlin Ritsch
 *
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {}
