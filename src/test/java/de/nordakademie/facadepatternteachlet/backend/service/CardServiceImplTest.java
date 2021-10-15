package de.nordakademie.facadepatternteachlet.backend.service;

import de.nordakademie.facadepatternteachlet.backend.entity.Card;
import de.nordakademie.facadepatternteachlet.backend.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test cases for <b>CardServiceImpl</b>.
 *
 * @author Niklas Witzel
 */
class CardServiceImplTest {

    private Card card;
    private Card card2;

    private CardService cardService;

    @BeforeEach
    public void beforeEach() {
        this.card = mock(Card.class);
        this.card2 = mock(Card.class);

        CardRepository cardRepository = mock(CardRepository.class);
        when(cardRepository.findAll()).thenReturn(Arrays.asList(card, card2));

        this.cardService = new CardServiceImpl(cardRepository);
    }

    @Test
    void findAll() {
        assertEquals(Arrays.asList(card, card2), cardService.findAll());
    }
}
