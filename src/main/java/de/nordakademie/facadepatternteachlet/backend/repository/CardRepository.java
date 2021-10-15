package de.nordakademie.facadepatternteachlet.backend.repository;

import de.nordakademie.facadepatternteachlet.backend.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface <b>CardRepository</b> extends JpaRepository and therefore provides an interface for saving,
 * reading, updating and deleting cards.
 *
 * @author Niklas Witzel
 */
public interface CardRepository extends JpaRepository<Card, Long> {}
