package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.BankAccount;
import de.nordakademie.facadepatternteachlet.backend.exception.CustomerAlreadyLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test cases for <b>BankAccountServiceImpl</b>.
 *
 * @author Til Zöller
 * @author Merlin Ritsch
 */
class BankAccountServiceImplTest {

    private BankAccount bankAccount;
    private BankAccountRepository bankAccountRepository;

    private BankAccountService bankAccountService;

    @BeforeEach
    public void beforeEach() {
        this.bankAccount = mock(BankAccount.class);
        this.bankAccountRepository = mock(BankAccountRepository.class);

        this.bankAccountService = new BankAccountServiceImpl(bankAccountRepository);
    }

    @Test
    void registerBankAccount() throws CustomerAlreadyLoggedInException, NoCustomerLoggedInException {
        when(bankAccount.getId()).thenReturn(1L);
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(bankAccount));

        bankAccountService.registerBankAccount(bankAccount);

        assertEquals(bankAccount, bankAccountService.getRegisteredBankAccount());
    }

    @Test
    void registerBankAccountWithRegisteredBankAccount() throws CustomerAlreadyLoggedInException {
        bankAccountService.registerBankAccount(bankAccount);
        assertThrows(CustomerAlreadyLoggedInException.class, () -> bankAccountService.registerBankAccount(bankAccount));
    }

    @Test
    void unregisterBankAccountWithRegisteredBankAccount() throws CustomerAlreadyLoggedInException {
        when(bankAccount.getId()).thenReturn(1L);

        bankAccountService.registerBankAccount(bankAccount);
        bankAccountService.unregisterBankAccount();

        assertThrows(NoCustomerLoggedInException.class, () -> bankAccountService.getRegisteredBankAccount());
    }

    @Test
    void unregisterBankAccountWithNoRegisteredBankAccount() {
        bankAccountService.unregisterBankAccount();

        assertThrows(NoCustomerLoggedInException.class, () -> bankAccountService.getRegisteredBankAccount());
    }

    @Test
    void getRegisteredBankAccountWithNoRegisteredBankAccount() {
        assertThrows(NoCustomerLoggedInException.class, () -> this.bankAccountService.getRegisteredBankAccount());
    }

    @Test
    void getRegisteredBankAccountWithRegisteredBankAccount() throws CustomerAlreadyLoggedInException, NoCustomerLoggedInException {
        when(this.bankAccount.getId()).thenReturn(1L);
        when(this.bankAccountRepository.findById(1L)).thenReturn(Optional.of(bankAccount));

        bankAccountService.registerBankAccount(bankAccount);

        assertEquals(bankAccount, this.bankAccountService.getRegisteredBankAccount());
    }

    @Test
    void getCurrentBalance() throws CustomerAlreadyLoggedInException, NoCustomerLoggedInException {
        when(this.bankAccount.getId()).thenReturn(1L);
        when(this.bankAccount.getBalance()).thenReturn(BigDecimal.valueOf(500));
        when(this.bankAccountRepository.findById(1L)).thenReturn(Optional.of(bankAccount));

        this.bankAccountService.registerBankAccount(bankAccount);

        assertEquals(BigDecimal.valueOf(500.00).setScale(2, RoundingMode.HALF_UP), bankAccountService.getCurrentBalance());
    }

    @Test
    void getCurrentBalanceWithRounding() throws CustomerAlreadyLoggedInException, NoCustomerLoggedInException {
        when(this.bankAccount.getId()).thenReturn(1L);
        when(this.bankAccount.getBalance()).thenReturn(BigDecimal.valueOf(500.9887));
        when(this.bankAccountRepository.findById(1L)).thenReturn(Optional.of(bankAccount));

        this.bankAccountService.registerBankAccount(bankAccount);

        assertEquals(BigDecimal.valueOf(500.99), this.bankAccountService.getCurrentBalance());
    }
}
