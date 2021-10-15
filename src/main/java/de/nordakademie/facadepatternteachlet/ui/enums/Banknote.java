package de.nordakademie.facadepatternteachlet.ui.enums;

import java.math.BigDecimal;

/**
 * The enum <b>Banknote</b> represents a type of banknote.
 * It defines which type of banknote is worth how many euros.
 *
 * @author Til Zöller
 * @author Merlin Ritsch
 */
public enum Banknote {
    FIVE_HUNDRED_EURO(500),
    TWO_HUNDRED_EURO(200),
    ONE_HUNDRED_EURO(100),
    FIFTY_EURO(50),
    TWENTY_EURO(20),
    TEN_EURO(10),
    FIVE_EURO(5);

    private final BigDecimal value;

    Banknote(int value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return this.value;
    }
}
