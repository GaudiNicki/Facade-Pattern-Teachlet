package de.nordakademie.facadepatternteachlet.ui.model;

import de.nordakademie.facadepatternteachlet.ui.enums.Banknote;

import java.util.Objects;

/**
 * The class <b>TypedBankNoteStack</b> represents a stack of one type banknotes.
 * It describes one type of banknote and the number of banknotes of that type.
 *
 * @author Til Zöller
 * @author Merlin Ritsch
 */
public class TypedBanknoteStack {

    private final Banknote banknote;
    private final int count;

    public TypedBanknoteStack(Banknote banknote, int count) {
        this.banknote = banknote;
        this.count = count;
    }

    public Banknote getBanknote() {
        return this.banknote;
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypedBanknoteStack that = (TypedBanknoteStack) o;
        return count == that.count && banknote == that.banknote;
    }

    @Override
    public int hashCode() {
        return Objects.hash(banknote, count);
    }
}
