package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.BankAccount;
import de.nordakademie.facadepatternteachlet.backend.exception.CustomerAlreadyLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.exception.NoCustomerLoggedInException;
import de.nordakademie.facadepatternteachlet.backend.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * The class <b>BankAccountServiceImpl</b> implements the BankAccountService interface and
 * implements its method by using the bank account object functions.
 *
 * @author Merlin Ritsch
 */
@Service
public class BankAccountServiceImpl implements BankAccountService{

    private static final Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final BankAccountRepository bankAccountRepository;

    private Long registeredBankAccountId;

    public BankAccountServiceImpl(@Autowired BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void registerBankAccount(BankAccount bankAccount) throws CustomerAlreadyLoggedInException {
        if (this.registeredBankAccountId != null) {
            throw new CustomerAlreadyLoggedInException();
        }

        this.registeredBankAccountId = bankAccount.getId();
    }

    @Override
    public void unregisterBankAccount() {
        this.registeredBankAccountId = null;
    }

    @Override
    public BankAccount getRegisteredBankAccount() throws NoCustomerLoggedInException {
        Optional<BankAccount> optionalBankAccount = this.bankAccountRepository.findById(registeredBankAccountId);

        if (optionalBankAccount.isEmpty()) {
            throw new NoCustomerLoggedInException();
        }

        return optionalBankAccount.get();
    }

    @Override
    public BigDecimal getCurrentBalance() throws NoCustomerLoggedInException {
        BankAccount bankAccount = this.getRegisteredBankAccount();
        BigDecimal balance = bankAccount.getBalance().setScale(2, RoundingMode.HALF_UP);

        log.info(MessageFormat.format("The balance of the bank account {0} is {1}", bankAccount.getId(), balance));
        return balance;
    }
}
