package de.nordakademie.facadepatternteachlet.backend.facade;

import de.nordakademie.facadepatternteachlet.backend.entity.Card;
import de.nordakademie.facadepatternteachlet.backend.entity.Transaction;
import de.nordakademie.facadepatternteachlet.backend.exception.CardExpiredException;
import de.nordakademie.facadepatternteachlet.backend.exception.CustomerAlreadyLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoAmountGivenException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCardSelectedException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoValidReturnOfBanknotesPossibleException;
import de.nordakademie.facadepatternteachlet.backend.exception.NotEnoughMoneyInBankAccountException;
import de.nordakademie.facadepatternteachlet.backend.exception.WrongPinException;
import de.nordakademie.facadepatternteachlet.ui.model.TypedBanknoteStack;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface <b>ATMFacade</b> serves as an abstraction towards the UI layer to encapsulate all complex ATM subsystems
 * and their sophisticated functionality and to provide a unified interface.
 *
 * @author Anna Engelmann
 * @author Merlin Ritsch
 * @author Niklas Witzel
 * @author Til Zöller
 */
public interface ATMFacade {
    /**
     * This method is used to log in a customer with a card and a pin.
     *
     * @param card the card which was inserted by the customer
     * @param pin the pin which was entered by the customer
     * @throws NoCardSelectedException if card is null
     * @throws WrongPinException if pin is null or incorrect
     * @throws CustomerAlreadyLoggedInException if there is already an authenticated customer
     */
    void login(Card card, String pin) throws NoCardSelectedException, WrongPinException, CustomerAlreadyLoggedInException, CardExpiredException;

    /**
     * This method is used to log out a customer
     *
     * @throws NoCustomerLoggedInException if there is no authenticated customer
     */
    void logout() throws NoCustomerLoggedInException;

    /**
     * This method is used to get all available cards.
     *
     * @return a list of all cards found in the database
     */
    List<Card> getAllCards();

    /**
     * This method is used to deposit banknotes to customer bank account
     *
     * @param typedBanknoteStacks is a list of {@link TypedBanknoteStack}
     *                            and represents the different banknotes to be deposited
     *
     * @return the deposited amount
     * @throws NoCustomerLoggedInException if there is no authenticated customer
     * @throws NoAmountGivenException if the amount to deposit is negative or zero
     */
    BigDecimal deposit(List<TypedBanknoteStack> typedBanknoteStacks) throws NoCustomerLoggedInException, NoAmountGivenException;

    /**
     * This method is used to withdraw banknotes to a customer from his bank account
     * @param withdrawAmount is an BigDecimal that indicates the amount of money to withdraw
     *
     * @return list of banknotes to withdraw to customer
     * @throws NoCustomerLoggedInException if there is no authenticated customer
     * @throws NoAmountGivenException if the amount to deposit is negative or zero
     * @throws NoValidReturnOfBanknotesPossibleException if it is not possible to return banknotes for given withdraw amount
     */
    List<TypedBanknoteStack> withdraw(BigDecimal withdrawAmount) throws NoCustomerLoggedInException, NoAmountGivenException, NotEnoughMoneyInBankAccountException, NoValidReturnOfBanknotesPossibleException;

    /**
     * This method is used to receive the current balance of the authenticated user bank account.
     *
     * @return the current balance
     * @throws NoCustomerLoggedInException if there is no authenticated customer
     */
    BigDecimal getCurrentBalance() throws NoCustomerLoggedInException;

    /**
     * This method is used to get a list of all transactions of the authenticated user bank account.
     *
     * @return a list of all transactions found in the database
     * @throws NoCustomerLoggedInException if there is no authenticated customer
     */
    List<Transaction> getTransactions() throws NoCustomerLoggedInException;
}
