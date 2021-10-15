package de.nordakademie.facadepatternteachlet.backend.service;


import de.nordakademie.facadepatternteachlet.backend.entity.Card;
import de.nordakademie.facadepatternteachlet.backend.exception.CardExpiredException;
import de.nordakademie.facadepatternteachlet.backend.exception.WrongPinException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test cases for <b>AuthenticationServiceImpl</b>.
 *
 * @author Niklas Witzel
 * @author Anna Engelmann
 */
class AuthenticationServiceImplTest {

    private Card card;
    private String pin;

    private AuthenticationService authenticationService;

    @BeforeEach
    public void beforeEach() {
        card = mock(Card.class);
        when(card.getPin()).thenReturn("1234");
        when(card.getValidUntil()).thenReturn(LocalDate.of(2022,6,30));

        pin = "1234";

        authenticationService = new AuthenticationServiceImpl();
    }

    @Test
    void noCustomerAuthenticatedOnStartup() {
        assertFalse(authenticationService.isAuthenticated());
    }

    @Test
    void customerLoginWithCorrectPin() throws WrongPinException, CardExpiredException {
        authenticationService.login(card, pin);

        assertTrue(authenticationService.isAuthenticated());
    }

    @Test
    void customerLoginWithExpiredCard() {
        when(card.getValidUntil()).thenReturn(LocalDate.of(2020,6,30));

        assertThrows(CardExpiredException.class, () -> authenticationService.login(card, pin));
    }

    @Test
    void customerLoginWithInvalidPin() {
        pin = "AB34";
        assertThrows(WrongPinException.class, () -> authenticationService.login(card, pin));
    }

    @Test
    void customerLoginWithIncorrectPin() {
        pin = "4589";
        assertThrows(WrongPinException.class, () -> authenticationService.login(card, pin));
    }

    @Test
    void customerLogout() {
        authenticationService.logout();

        assertFalse(authenticationService.isAuthenticated());
    }
}
