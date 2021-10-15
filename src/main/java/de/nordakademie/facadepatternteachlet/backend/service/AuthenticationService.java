package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.Card;
import de.nordakademie.facadepatternteachlet.backend.exception.CardExpiredException;
import de.nordakademie.facadepatternteachlet.backend.exception.WrongPinException;

/**
 * The interface <b>AuthenticationService</b> serves as abstraction towards the facade layer and provides login
 * and logout functionality as well as the ability to get the current authentication status.
 *
 * @author Niklas Witzel
 * @author Anna Engelmann
 */
public interface AuthenticationService {
    /**
     * This method is used to log in / authenticate a customer via a card and a pin
     * @param card the card the customer inserted
     * @param pin the pin the customer entered
     * @throws WrongPinException if the given pin is not the correct pin for the given card
     */
    void login(Card card, String pin) throws WrongPinException, CardExpiredException;

    /**
     * This method is used to log out the customer who is currently authenticated
     */
    void logout();

    /**
     * This method is used to query the current authentication status.
     *
     * @return true if any customer is authenticated else false
     */
    boolean isAuthenticated();
}
