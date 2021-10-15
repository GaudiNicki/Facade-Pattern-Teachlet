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
import de.nordakademie.facadepatternteachlet.backend.service.AuthenticationService;
import de.nordakademie.facadepatternteachlet.backend.service.BankAccountService;
import de.nordakademie.facadepatternteachlet.backend.service.CardService;
import de.nordakademie.facadepatternteachlet.backend.service.TransactionService;
import de.nordakademie.facadepatternteachlet.ui.enums.Banknote;
import de.nordakademie.facadepatternteachlet.ui.model.TypedBanknoteStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * The class <b>ATMFacadeImpl</b> implements the ATMFacade interface and implements its methods by calling different
 * services inside the service layer.
 *
 * @author Anna Engelmann
 * @author Merlin Ritsch
 * @author Niklas Witzel
 * @author Til Zöller
 */
public class ATMFacadeImpl implements ATMFacade {

    private final AuthenticationService authenticationService;
    private final CardService cardService;
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;

    public ATMFacadeImpl(AuthenticationService authenticationService, CardService cardService, TransactionService transactionService, BankAccountService bankAccountService) {
        this.authenticationService = authenticationService;
        this.cardService = cardService;
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
    }

    @Override
    public void login(Card card, String pin) throws NoCardSelectedException, WrongPinException, CustomerAlreadyLoggedInException, CardExpiredException {
        if (card == null) throw new NoCardSelectedException();
        if (pin == null) throw new WrongPinException();
        if (this.authenticationService.isAuthenticated()) throw new CustomerAlreadyLoggedInException();

        authenticationService.login(card, pin);
        bankAccountService.registerBankAccount(card.getAccount());
    }

    @Override
    public void logout() throws NoCustomerLoggedInException {
        if (!this.authenticationService.isAuthenticated()) throw new NoCustomerLoggedInException();

        authenticationService.logout();
        bankAccountService.unregisterBankAccount();
    }

    @Override
    public List<Card> getAllCards() {
        return cardService.findAll();
    }

    @Override
    public BigDecimal deposit(List<TypedBanknoteStack> typedBanknoteStacks) throws NoCustomerLoggedInException, NoAmountGivenException {
        if (!this.authenticationService.isAuthenticated()) throw new NoCustomerLoggedInException();

        BigDecimal amount = typedBanknoteStacks.stream()
                .map(typedBanknoteStack -> typedBanknoteStack.getBanknote().getValue().multiply(new BigDecimal(typedBanknoteStack.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (amount.compareTo(BigDecimal.ONE) < 0) throw new NoAmountGivenException();

        this.transactionService.deposit(amount);
        return amount;
    }

    @Override
    public List<TypedBanknoteStack> withdraw(BigDecimal withdrawAmount) throws NoCustomerLoggedInException, NoAmountGivenException, NotEnoughMoneyInBankAccountException, NoValidReturnOfBanknotesPossibleException {

        if (!this.authenticationService.isAuthenticated()) throw new NoCustomerLoggedInException();
        if (withdrawAmount.compareTo(BigDecimal.ONE) < 0) throw new NoAmountGivenException();
        if (withdrawAmount.remainder(new BigDecimal(5)).compareTo(BigDecimal.ZERO) != 0)
            throw new NoValidReturnOfBanknotesPossibleException();

        this.transactionService.withdraw(withdrawAmount);

        return calculateBanknoteDistribution(withdrawAmount);
    }

    private List<TypedBanknoteStack> calculateBanknoteDistribution(BigDecimal amount) {
        List<TypedBanknoteStack> banknoteDistribution = new ArrayList<>();

        for (Banknote banknote : Banknote.values()) {
            banknoteDistribution.add(new TypedBanknoteStack(banknote, amount.divide(banknote.getValue(), RoundingMode.FLOOR).intValue()));
            amount = amount.subtract(amount.divide(banknote.getValue(), RoundingMode.FLOOR).multiply(banknote.getValue()));
        }

        return banknoteDistribution;
    }

    @Override
    public BigDecimal getCurrentBalance() throws NoCustomerLoggedInException {
        if (!this.authenticationService.isAuthenticated()) throw new NoCustomerLoggedInException();

        return this.bankAccountService.getCurrentBalance();
    }

    @Override
    public List<Transaction> getTransactions() throws NoCustomerLoggedInException {
        if (!this.authenticationService.isAuthenticated()) throw new NoCustomerLoggedInException();

        return this.transactionService.getAllTransactions();
    }
}
