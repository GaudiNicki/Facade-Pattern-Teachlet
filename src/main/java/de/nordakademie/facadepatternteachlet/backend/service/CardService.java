package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.Card;

import java.util.List;

/**
 * The interface <b>CardService</b> serves as abstraction towards the facade layer and provides access
 * to all cards available inside the application.
 *
 * @author Niklas Witzel
 */
public interface CardService {
    /**
     * This method is used to get all cards available inside the application
     *
     * @return a list of all cards
     */
    List<Card> findAll();
}
