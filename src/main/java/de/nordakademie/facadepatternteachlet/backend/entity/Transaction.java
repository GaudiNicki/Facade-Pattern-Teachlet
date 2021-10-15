package de.nordakademie.facadepatternteachlet.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * The class <b>Transaction</b> is the superclass of all transaction entities. It consists of
 * an id, a reason for payment, a transaction date, a transaction amount and a bank account.
 *
 * @author Niklas Witzel
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne
    protected BankAccount bankAccount;

    protected BigDecimal amount;
    protected LocalDateTime dateTime;
    protected String reasonForPayment;

    protected Transaction() {}

    protected Transaction(BankAccount bankAccount, String reasonForPayment, BigDecimal amount, LocalDateTime date) {
        this.bankAccount = bankAccount;
        this.amount = amount;
        this.dateTime = date;
        this.reasonForPayment = reasonForPayment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getReasonForPayment() {
        return reasonForPayment;
    }

    public void setReasonForPayment(String reasonForPayment) {
        this.reasonForPayment = reasonForPayment;
    }
}
