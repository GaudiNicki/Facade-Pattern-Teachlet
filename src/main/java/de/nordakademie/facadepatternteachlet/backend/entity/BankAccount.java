package de.nordakademie.facadepatternteachlet.backend.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The class <b>BankAccount</b> represents the entity for a bank account. It consists of an id, a customer, a balance
 * and a set of transactions.
 *
 * @author Niklas Witzel
 */
@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.EAGER)
    private List<Transaction> transactions;

    private String customerName;

    protected BankAccount() {}

    public BankAccount(String customerName) {
        this.customerName = customerName;
        this.transactions = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public BigDecimal getBalance() {
        return transactions.stream()
                .map(transaction -> transaction instanceof Withdrawal ? transaction.getAmount().multiply(new BigDecimal(-1))
                        : transaction.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getCustomerName() {
        return customerName;
    }
}
