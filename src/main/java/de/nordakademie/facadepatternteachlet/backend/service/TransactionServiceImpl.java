package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.BankAccount;
import de.nordakademie.facadepatternteachlet.backend.entity.Deposit;
import de.nordakademie.facadepatternteachlet.backend.entity.Transaction;
import de.nordakademie.facadepatternteachlet.backend.entity.Withdrawal;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NotEnoughMoneyInBankAccountException;
import de.nordakademie.facadepatternteachlet.backend.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The class <b>TransactionServiceImpl</b> implements the TransactionService interface and
 * implements its method by using the TransactionRepository.
 *
 * @author Til Zöller
 */
@Service
public class TransactionServiceImpl implements TransactionService{

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final BankAccountService bankAccountService;

    public TransactionServiceImpl(@Autowired TransactionRepository transactionRepository,
                                  @Autowired BankAccountService bankAccountService) {
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
    }

    @Override
    public void deposit(BigDecimal amount) throws NoCustomerLoggedInException {
        BankAccount bankAccount = this.bankAccountService.getRegisteredBankAccount();

        Deposit deposit = new Deposit(bankAccount, "cash deposit", amount, LocalDateTime.now());
        this.transactionRepository.save(deposit);

        log.info(MessageFormat.format("Successfully deposited {0} € from bank account {1}.", deposit.getAmount(), bankAccount.getId()));
    }

    @Override
    public void withdraw(BigDecimal amount) throws NotEnoughMoneyInBankAccountException, NoCustomerLoggedInException {
        BankAccount bankAccount = this.bankAccountService.getRegisteredBankAccount();

        if (bankAccount.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughMoneyInBankAccountException();
        }
        Withdrawal withdraw = new Withdrawal(bankAccount, "withdraw cash", amount, LocalDateTime.now());
        this.transactionRepository.save(withdraw);

        log.info(MessageFormat.format("Successfully withdraw {0} € from bank account {1}.", withdraw.getAmount(), bankAccount.getId()));
    }

    @Override
    public List<Transaction> getAllTransactions() throws NoCustomerLoggedInException {
        BankAccount bankAccount = this.bankAccountService.getRegisteredBankAccount();

        List<Transaction> transactions = this.transactionRepository.findAllByBankAccountId(bankAccount.getId());

        log.info(MessageFormat.format("Successfully received {0} transactions from bank account {1}.", transactions.size(), bankAccount.getId()));
        return transactions;
    }
}
