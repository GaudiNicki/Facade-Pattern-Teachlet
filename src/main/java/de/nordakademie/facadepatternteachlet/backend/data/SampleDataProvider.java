package de.nordakademie.facadepatternteachlet.backend.data;

import de.nordakademie.facadepatternteachlet.backend.entity.BankAccount;
import de.nordakademie.facadepatternteachlet.backend.entity.Card;
import de.nordakademie.facadepatternteachlet.backend.entity.Deposit;
import de.nordakademie.facadepatternteachlet.backend.entity.Transaction;
import de.nordakademie.facadepatternteachlet.backend.entity.Withdrawal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class <b>SampleDataProvider</b> is used to create and provide sample card and sample transaction data for
 * showcasing and testing purposes.
 *
 * @author Niklas Witzel
 */
public class SampleDataProvider {

    private final List<BankAccount> bankAccounts;
    private final List<Card> cards;
    private final List<Transaction> transactions;

    public SampleDataProvider() {
        bankAccounts = provideSampleBankAccounts();
        cards = provideSampleCards();
        transactions = provideSampleTransactions();
    }

    private List<BankAccount> provideSampleBankAccounts() {
        BankAccount accountNiklas = new BankAccount("Niklas Witzel");
        BankAccount accountMerlin = new BankAccount("Merlin Ritsch");

        return new ArrayList<>(Arrays.asList(accountNiklas, accountMerlin));
    }

    private List<Transaction> provideSampleTransactions() {
        Deposit transaction1 = new Deposit(bankAccounts.get(1), "Loan", new BigDecimal(1200), LocalDateTime.of(2021,8,1, 1, 9, 0));
        Withdrawal transaction2 = new Withdrawal(bankAccounts.get(1),  "macbook", new BigDecimal(900), LocalDateTime.of(2021,8,10, 12, 30, 7));
        Withdrawal transaction3 = new Withdrawal(bankAccounts.get(1), "mydealz", new BigDecimal(42), LocalDateTime.of(2021,8,12, 14, 24, 59));
        Deposit transaction4 = new Deposit(bankAccounts.get(1), "ebay sale", BigDecimal.valueOf(20.69), LocalDateTime.of(2021,8,18, 18, 42, 42));

        return new ArrayList<>(Arrays.asList(transaction1, transaction2, transaction3, transaction4));
    }

    private List<Card> provideSampleCards() {
        Card giroCardNiklas = new Card(
                "135792468",
                "1234",
                LocalDate.of(2022,6,30),
                bankAccounts.get(0)
        );

        Card giroCardNiklasOld = new Card(
                "135792468",
                "1234",
                LocalDate.of(2020,6,30),
                bankAccounts.get(0)
        );

        Card giroCardMerlin = new Card(
                "246813579",
                "4321",
                LocalDate.of(2021,12,31),
                bankAccounts.get(1)
        );

        return new ArrayList<>(Arrays.asList(giroCardNiklas, giroCardNiklasOld, giroCardMerlin));
    }

    public List<Card> getSampleCards() {
        return cards;
    }

    public List<Transaction> getSampleTransactions() {
        return transactions;
    }
}
