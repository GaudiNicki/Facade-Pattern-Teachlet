package de.nordakademie.facadepatternteachlet.backend.facade;


import de.nordakademie.facadepatternteachlet.backend.entity.BankAccount;
import de.nordakademie.facadepatternteachlet.backend.entity.Card;
import de.nordakademie.facadepatternteachlet.backend.entity.Deposit;
import de.nordakademie.facadepatternteachlet.backend.entity.Transaction;
import de.nordakademie.facadepatternteachlet.backend.entity.Withdrawal;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases for <b>ATMFacadeImpl</b>.
 *
 * @author Merlin Ritsch
 * @author Niklas Witzel
 * @author Til Zöller
 * @author Anna Engelmann
 */
class ATMFacadeImplTest {

    private BankAccount bankAccount;

    private Card card;
    private Card card2;
    private String pin;

    private AuthenticationService authenticationService;
    private TransactionService transactionService;
    private BankAccountService bankAccountService;

    private Transaction deposit;
    private Transaction withdrawal;

    private ATMFacade facade;

    private List<TypedBanknoteStack> typedBanknoteStacks;

    @BeforeEach
    public void beforeEach() throws NoCustomerLoggedInException {
        this.bankAccount = mock(BankAccount.class);

        this.card = mock(Card.class);
        this.card2 = mock(Card.class);

        when(this.card.getPin()).thenReturn("1234");
        when(this.card.getValidUntil()).thenReturn(LocalDate.of(2022,6,30));
        when(this.card.getAccount()).thenReturn(this.bankAccount);

        this.pin = "1234";

        this.authenticationService = mock(AuthenticationService.class);
        this.bankAccountService = mock(BankAccountService.class);
        this.transactionService = mock(TransactionService.class);
        CardService cardService = mock(CardService.class);

        when(authenticationService.isAuthenticated()).thenReturn(true);
        when(cardService.findAll()).thenReturn(Arrays.asList(card, card2));

        when(bankAccountService.getCurrentBalance()).thenReturn(BigDecimal.valueOf(420));

        this.deposit = mock(Deposit.class);
        when(deposit.getAmount()).thenReturn(BigDecimal.valueOf(600));
        this.withdrawal = mock(Withdrawal.class);
        when(withdrawal.getAmount()).thenReturn(BigDecimal.valueOf(180));

        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(deposit, withdrawal));

        TypedBanknoteStack fiveHundredEuroStack = mock(TypedBanknoteStack.class);
        when(fiveHundredEuroStack.getCount()).thenReturn(1);
        when(fiveHundredEuroStack.getBanknote()).thenReturn(Banknote.FIVE_HUNDRED_EURO);

        TypedBanknoteStack twoHundredEuroStack = mock(TypedBanknoteStack.class);
        when(twoHundredEuroStack.getCount()).thenReturn(1);
        when(twoHundredEuroStack.getBanknote()).thenReturn(Banknote.TWO_HUNDRED_EURO);

        TypedBanknoteStack oneHundredEuroStack = mock(TypedBanknoteStack.class);
        when(oneHundredEuroStack.getCount()).thenReturn(1);
        when(oneHundredEuroStack.getBanknote()).thenReturn(Banknote.ONE_HUNDRED_EURO);

        TypedBanknoteStack fiftyEuroStack = mock(TypedBanknoteStack.class);
        when(fiftyEuroStack.getCount()).thenReturn(1);
        when(fiftyEuroStack.getBanknote()).thenReturn(Banknote.FIFTY_EURO);

        TypedBanknoteStack twentyEuroStack = mock(TypedBanknoteStack.class);
        when(twentyEuroStack.getCount()).thenReturn(1);
        when(twentyEuroStack.getBanknote()).thenReturn(Banknote.TWENTY_EURO);

        TypedBanknoteStack tenEuroStack = mock(TypedBanknoteStack.class);
        when(tenEuroStack.getCount()).thenReturn(1);
        when(tenEuroStack.getBanknote()).thenReturn(Banknote.TEN_EURO);

        TypedBanknoteStack fiveEuroStack = mock(TypedBanknoteStack.class);
        when(fiveEuroStack.getCount()).thenReturn(1);
        when(fiveEuroStack.getBanknote()).thenReturn(Banknote.FIVE_EURO);

        this.typedBanknoteStacks = new ArrayList<>(Arrays.asList(
                fiveHundredEuroStack,
                twoHundredEuroStack,
                oneHundredEuroStack,
                fiftyEuroStack,
                twentyEuroStack,
                tenEuroStack,
                fiveEuroStack
        ));

        this.facade = new ATMFacadeImpl(authenticationService, cardService, transactionService, bankAccountService);
    }

    @Test
    void customerLoginWithoutCardSelected() {
        card = null;

        assertThrows(NoCardSelectedException.class, () -> facade.login(card, pin));
    }

    @Test
    void customerLoginWithoutPinEntered() {
        pin = null;

        assertThrows(WrongPinException.class, () -> facade.login(card, pin));
    }

    @Test
    void customerLoginWithoutAuthenticatedCustomer() {
        assertThrows(CustomerAlreadyLoggedInException.class, () -> facade.login(card, pin));
    }

    @Test
    void customerLoginWhenCardIsExpired() throws CardExpiredException, WrongPinException {
        when(authenticationService.isAuthenticated()).thenReturn(false);
        doThrow(CardExpiredException.class).when(authenticationService).login(card, pin);

        assertThrows(CardExpiredException.class, () -> facade.login(card, pin));
    }

    @Test
    void customerLoginWithSelectedCardAndInvalidPin() throws WrongPinException, CardExpiredException {
        pin = "AB34";

        when(authenticationService.isAuthenticated()).thenReturn(false);
        doThrow(new WrongPinException()).when(authenticationService).login(card, pin);

        assertThrows(WrongPinException.class, () -> facade.login(card, pin));
    }

    @Test
    void customerLoginWithSelectedCardAndIncorrectPin() throws WrongPinException, CardExpiredException {
        pin = "4589";

        when(authenticationService.isAuthenticated()).thenReturn(false);
        doThrow(new WrongPinException()).when(authenticationService).login(card, pin);

        assertThrows(WrongPinException.class, () -> facade.login(card, pin));
    }

    @Test
    void customerLoginWithSelectedCardAndCorrectPin() throws NoCardSelectedException, WrongPinException, CustomerAlreadyLoggedInException, CardExpiredException {
        when(authenticationService.isAuthenticated()).thenReturn(false);

        facade.login(card, pin);

        verify(authenticationService).login(card, pin);
        verify(bankAccountService).registerBankAccount(bankAccount);
    }

    @Test
    void logoutWithNoAuthenticatedCustomer() {
        when(authenticationService.isAuthenticated()).thenReturn(false);

        assertThrows(NoCustomerLoggedInException.class, () -> facade.logout());
    }

    @Test
    void customerLogout() throws NoCustomerLoggedInException {
        facade.logout();

        verify(authenticationService).logout();
        verify(bankAccountService).unregisterBankAccount();
    }

    @Test
    void getAllAvailableCards() {
        assertEquals(Arrays.asList(card, card2), facade.getAllCards());
    }

    @Test
    void depositWithNoAuthenticatedCustomer() {
        when(this.authenticationService.isAuthenticated()).thenReturn(false);

        assertThrows(NoCustomerLoggedInException.class, () -> facade.deposit(typedBanknoteStacks));
    }

    @Test
    void depositNoCash() {
        typedBanknoteStacks = new ArrayList<>();

        assertThrows(NoAmountGivenException.class, () -> facade.deposit(typedBanknoteStacks));
    }

    @Test
    void depositCashWithAuthenticatedCustomer() throws NoAmountGivenException, NoCustomerLoggedInException {
        assertEquals(facade.deposit(typedBanknoteStacks), BigDecimal.valueOf(885));
        verify(transactionService).deposit(BigDecimal.valueOf(885));
    }

    @Test
    void withdrawWithNoAuthenticatedCustomer() {
        when(this.authenticationService.isAuthenticated()).thenReturn(false);

        assertThrows(NoCustomerLoggedInException.class, () -> facade.withdraw(BigDecimal.valueOf(45)));
    }

    @Test
    void withdrawNoCash() {
        assertThrows(NoAmountGivenException.class, () -> facade.withdraw(BigDecimal.ZERO));
    }

    @Test
    void withdrawNegativeAmount() {
        assertThrows(NoAmountGivenException.class, () -> facade.withdraw(BigDecimal.valueOf(-1)));
    }

    @Test
    void withdrawNotEnoughMoneyInBankAccount() throws NoCustomerLoggedInException, NotEnoughMoneyInBankAccountException {
        doThrow(NotEnoughMoneyInBankAccountException.class).when(transactionService).withdraw(any(BigDecimal.class));

        assertThrows(NotEnoughMoneyInBankAccountException.class, () -> facade.withdraw(BigDecimal.valueOf(5000)));
    }

    @Test
    void withdrawInvalidAmountOfCash() {
        assertThrows(NoValidReturnOfBanknotesPossibleException.class, () -> facade.withdraw(BigDecimal.valueOf(42)));
    }

    @Test
    void withdraw1385EuroWithAuthenticatedCustomer() throws NoAmountGivenException, NoCustomerLoggedInException, NoValidReturnOfBanknotesPossibleException, NotEnoughMoneyInBankAccountException {
        List<TypedBanknoteStack> listOfTypedBanknoteStacks = Arrays.asList(
                new TypedBanknoteStack(Banknote.FIVE_HUNDRED_EURO, 2),
                new TypedBanknoteStack(Banknote.TWO_HUNDRED_EURO, 1),
                new TypedBanknoteStack(Banknote.ONE_HUNDRED_EURO, 1),
                new TypedBanknoteStack(Banknote.FIFTY_EURO, 1),
                new TypedBanknoteStack(Banknote.TWENTY_EURO, 1),
                new TypedBanknoteStack(Banknote.TEN_EURO, 1),
                new TypedBanknoteStack(Banknote.FIVE_EURO, 1)
        );

        assertEquals(listOfTypedBanknoteStacks, facade.withdraw(BigDecimal.valueOf(1385)));
        verify(transactionService).withdraw(BigDecimal.valueOf(1385));
    }

    @Test
    void getCurrentBalanceWithNoAuthenticatedCustomer() {
        when(authenticationService.isAuthenticated()).thenReturn(false);

        assertThrows(NoCustomerLoggedInException.class, () -> facade.getCurrentBalance());
    }

    @Test
    void getCurrentBalanceWithAuthenticatedCustomer() throws NoCustomerLoggedInException {
        assertEquals(BigDecimal.valueOf(420), facade.getCurrentBalance());

        verify(bankAccountService).getCurrentBalance();
    }

    @Test
    void getTransactionsWithNoAuthenticatedCustomer() {
        when(authenticationService.isAuthenticated()).thenReturn(false);

        assertThrows(NoCustomerLoggedInException.class, () -> facade.getTransactions());
    }

    @Test
    void getTransactionsWithAuthenticatedCustomer() throws NoCustomerLoggedInException {
        assertEquals(Arrays.asList(deposit, withdrawal), facade.getTransactions());

        verify(this.transactionService).getAllTransactions();
    }




}
