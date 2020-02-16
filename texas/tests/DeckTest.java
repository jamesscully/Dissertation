package tests;

import com.scully.cards.Card;
import com.scully.cards.Deck;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DeckTest {


    Deck deck = Deck.getInstance();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void resetDeck() {
    }

    @Test
    public void TestPullCard() {
        Card c = deck.pullCard();

        assertEquals(51, deck.size());
        assertFalse(deck.inDeck(c));

        HashSet<Card> pulled = new HashSet<>();

        for(int i = 0; i < 50; i++) {
            Card pull = deck.pullCard();

            System.out.println(pull);

            pulled.add(pull);
        }

        assertEquals(1, deck.size());
        assertEquals(50, pulled.size());

    }

    @Test
    public void TestNoDuplicatePullCards() {
        // hashsets cannot contain duplicate values,
        // thus we should have a size of 52
        HashSet<Card> pulled = new HashSet<>();
        for(int i = 0; i < 52; i++) {
            pulled.add(deck.pullCard());
        }
        assertEquals(52, pulled.size());
    }

    @Test
    public void printDeck() {
    }
}