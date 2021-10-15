package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.BankAccount;
import de.nordakademie.facadepatternteachlet.backend.entity.Deposit;
import de.nordakademie.facadepatternteachlet.backend.entity.Transaction;
import de.nordakademie.facadepatternteachlet.backend.entity.Withdrawal;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NotEnoughMoneyInBankAccountException;
import de.nordakademie.facadepatternteachlet.backend.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases for <b>TransactionServiceImpl</b>.
 *
 * @author Til Zöller
 * @author Merlin Ritsch
 */
class TransactionServiceImplTest {

    private BankAccountService bankAccountService;

    private TransactionRepository transactionRepository;

    private BankAccount bankAccount;

    private Transaction transaction;
    private Transaction transaction2;

    private TransactionService transactionService;

    @BeforeEach
    public void beforeEach() {
        this.bankAccountService = mock(BankAccountService.class);
        this.transactionRepository = mock(TransactionRepository.class);
        this.bankAccount = mock(BankAccount.class);

        this.transaction = mock(Transaction.class);
        this.transaction2 = mock(Transaction.class);

        when(transactionRepository.findAllByBankAccountId(1L)).thenReturn(Arrays.asList(transaction, transaction2));

        this.transactionService = new TransactionServiceImpl(transactionRepository, bankAccountService);
    }

    @Test
    void deposit() throws NoCustomerLoggedInException {
        when(bankAccountService.getRegisteredBankAccount()).thenReturn(bankAccount);
        ArgumentCaptor<Deposit> depositArgument = ArgumentCaptor.forClass(Deposit.class);

        transactionService.deposit(BigDecimal.valueOf(100));

        verify(transactionRepository).save(depositArgument.capture());

        assertEquals("cash deposit", depositArgument.getValue().getReasonForPayment());
        assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP), depositArgument.getValue().getAmount());
        assertEquals(LocalDate.now(), depositArgument.getValue().getDateTime().toLocalDate());
        assertEquals(LocalDateTime.now().getHour(), depositArgument.getValue().getDateTime().getHour());
        assertEquals(LocalDateTime.now().getMinute(), depositArgument.getValue().getDateTime().getMinute());
    }

    @Test
    void withdrawalWithNotEnoughMoneyInBankAccount() throws NoCustomerLoggedInException {
        when(bankAccountService.getRegisteredBankAccount()).thenReturn(bankAccount);
        when(bankAccount.getBalance()).thenReturn(BigDecimal.valueOf(99));

        assertThrows(NotEnoughMoneyInBankAccountException.class, () -> this.transactionService.withdraw(BigDecimal.valueOf(100)));
    }

    @Test
    void withdrawalWithEnoughMoneyInBankAccount() throws NoCustomerLoggedInException, NotEnoughMoneyInBankAccountException {
        when(bankAccountService.getRegisteredBankAccount()).thenReturn(bankAccount);
        when(bankAccount.getBalance()).thenReturn(BigDecimal.valueOf(150));

        ArgumentCaptor<Withdrawal> withdrawalArgument = ArgumentCaptor.forClass(Withdrawal.class);

        transactionService.withdraw(BigDecimal.valueOf(100));

        verify(transactionRepository).save(withdrawalArgument.capture());

        assertEquals("withdraw cash", withdrawalArgument.getValue().getReasonForPayment());
        assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP), withdrawalArgument.getValue().getAmount());
        assertEquals(LocalDate.now(), withdrawalArgument.getValue().getDateTime().toLocalDate());
        assertEquals(LocalDateTime.now().getHour(), withdrawalArgument.getValue().getDateTime().getHour());
        assertEquals(LocalDateTime.now().getMinute(), withdrawalArgument.getValue().getDateTime().getMinute());
    }

    @Test
    void getAllTransactions() throws NoCustomerLoggedInException {
        when(bankAccountService.getRegisteredBankAccount()).thenReturn(bankAccount);
        when(bankAccount.getId()).thenReturn(1L);

        assertEquals(Arrays.asList(transaction, transaction2), transactionService.getAllTransactions());
    }
}
