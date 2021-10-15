package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.Card;
import de.nordakademie.facadepatternteachlet.backend.exception.CardExpiredException;
import de.nordakademie.facadepatternteachlet.backend.exception.WrongPinException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

/**
 * The class <b>AuthenticationServiceImpl</b> implements the AuthenticationService interface and implements
 * its methods by querying or modifying the authentication status.
 *
 * @author Niklas Witzel
 * @author Anna Engelmann
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private boolean isAuthenticated;

    @Autowired
    public AuthenticationServiceImpl() {
        this.isAuthenticated = false;
    }

    @Override
    public void login(Card card, String pin) throws WrongPinException, CardExpiredException {
        if(card.getValidUntil().isBefore(LocalDate.now())) throw new CardExpiredException();
        if(!Objects.equals(card.getPin(), pin)) throw new WrongPinException();

        isAuthenticated = true;
        log.info("Customer successfully logged in!");
    }

    @Override
    public void logout() {
        isAuthenticated = false;
        log.info("Customer successfully logged out!");
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
