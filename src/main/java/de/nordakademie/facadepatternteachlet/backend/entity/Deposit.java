package de.nordakademie.facadepatternteachlet.backend.entity;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The class <b>Deposit</b> is a subclass of Transaction and represents the entity for a deposit. It consists of an id,
 * a reason for payment, a transaction date, a transaction amount and a bank account.
 *
 * @author Niklas Witzel
 */
@Entity
public class Deposit extends Transaction {
    protected Deposit() {}

    public Deposit(BankAccount bankAccount, String reasonForPayment, BigDecimal amount, LocalDateTime dateTime) {
        super(bankAccount, reasonForPayment, amount, dateTime);
    }
}
