package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.Transaction;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NotEnoughMoneyInBankAccountException;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface <b>TransactionService</b> serves as abstraction towards the facade layer and provides
 * a deposit and withdraw functionality as well as the ability to receive all transactions of a bank account.
 *
 * @author Til Zöller
 */
public interface TransactionService {
    /**
     * This method is used to deposit an amount into registered bank account.
     *
     */
    void deposit(BigDecimal amount) throws NoCustomerLoggedInException;

    /**
     * This method is used to withdraw an amount of registered bank account.
     *
     * @throws NotEnoughMoneyInBankAccountException if there is not enough money on the bank account
     */
    void withdraw(BigDecimal amount) throws NotEnoughMoneyInBankAccountException, NoCustomerLoggedInException;

    /**
     * This method is used to get all transactions of registered bank account.
     *
     * @return resulting bank account transactions
     */
    List<Transaction> getAllTransactions() throws NoCustomerLoggedInException;
}
