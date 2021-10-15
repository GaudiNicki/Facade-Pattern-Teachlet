package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.BankAccount;
import de.nordakademie.facadepatternteachlet.backend.exception.CustomerAlreadyLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;

import java.math.BigDecimal;

/**
 * The interface <b>BankAccountService</b> serves as abstraction towards the facade layer and provides
 * a functionality to receive the current balance of a bank account.
 *
 * @author Merlin Ritsch
 */
public interface BankAccountService {

    /**
     * This method is used to register a bank account in this service
     *
     * @param bankAccount to be registered
     * @throws CustomerAlreadyLoggedInException if a bank account is already registered in this service
     */
    void registerBankAccount(BankAccount bankAccount) throws CustomerAlreadyLoggedInException;

    /**
     * This method is used to unregister a bank account in this service
     */
    void unregisterBankAccount();

    /**
     * This method is used to unregister a bank account in this service
     *
     * @return registered bank account
     * @throws NoCustomerLoggedInException if there is no bank account registered
     */
    BankAccount getRegisteredBankAccount() throws NoCustomerLoggedInException;

    /**
     * This method is used to get the current balance of a bank account.
     *
     * @return current account balance
     */
    BigDecimal getCurrentBalance() throws NoCustomerLoggedInException;
}
