package de.nordakademie.facadepatternteachlet.backend.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;

/**
 * The class <b>Card</b> represents the entity for a card. It consists of an id, a card number, a PIN, a period of validity
 * and a bank account.
 *
 * @author Niklas Witzel
 */
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cardNumber;
    private String pin;
    private LocalDate validUntil;

    @OneToOne(cascade = CascadeType.ALL)
    private BankAccount account;

    protected Card() {}

    public Card(String cardNumber, String pin, LocalDate validUntil, BankAccount account) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.validUntil = validUntil;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", pin=" + pin +
                ", validUntil=" + validUntil +
                ", account=" + account +
                '}';
    }
}
