package tests;

import cards.Card;
import cards.TexasHand;
import enums.Suit;
import enums.TexasResults;
import enums.Face;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TexasHandTest {

    TexasHand FLUSH_FOK = new TexasHand(
            new Card(Suit.CLUBS, Face.FOUR),
            new Card(Suit.CLUBS, Face.FOUR),
            new Card(Suit.CLUBS, Face.FOUR),
            new Card(Suit.CLUBS, Face.FOUR),
            new Card(Suit.CLUBS, Face.ACE)
    );

    TexasHand STRAIGHT_FLUSH_1 = new TexasHand(
            new Card(Suit.CLUBS, Face.THREE),
            new Card(Suit.CLUBS, Face.FOUR),
            new Card(Suit.CLUBS, Face.FIVE),
            new Card(Suit.CLUBS, Face.SIX),
            new Card(Suit.CLUBS, Face.SEVEN)
    );

    TexasHand STRAIGHT_FLUSH_2 = new TexasHand(
            new Card(Suit.DIAMONDS, Face.NINE),
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.DIAMONDS, Face.JACK),
            new Card(Suit.DIAMONDS, Face.QUEEN),
            new Card(Suit.DIAMONDS, Face.KING)
    );

    TexasHand ROYAL_FLUSH = new TexasHand(
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.DIAMONDS, Face.JACK),
            new Card(Suit.DIAMONDS, Face.QUEEN),
            new Card(Suit.DIAMONDS, Face.KING),
            new Card(Suit.DIAMONDS, Face.ACE)
    );

    TexasHand THREEOFKIND = new TexasHand(
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.DIAMONDS, Face.KING),
            new Card(Suit.DIAMONDS, Face.ACE)
    );

    TexasHand TWOPAIR = new TexasHand(
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.DIAMONDS, Face.KING),
            new Card(Suit.DIAMONDS, Face.KING),
            new Card(Suit.DIAMONDS, Face.ACE)
    );

    TexasHand PAIR = new TexasHand(
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.DIAMONDS, Face.TWO),
            new Card(Suit.DIAMONDS, Face.KING),
            new Card(Suit.DIAMONDS, Face.ACE)
    );



    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void evaluate() {
    }

    @Test
    public void getHighestCard() {
        assertEquals(FLUSH_FOK.getHighestCard(),        new Card(Suit.CLUBS,    Face.ACE));
        assertEquals(STRAIGHT_FLUSH_1.getHighestCard(), new Card(Suit.CLUBS,    Face.SEVEN));
        assertEquals(STRAIGHT_FLUSH_2.getHighestCard(), new Card(Suit.DIAMONDS, Face.KING));
        assertEquals(ROYAL_FLUSH.getHighestCard(),      new Card(Suit.DIAMONDS, Face.ACE));
    }

    @Test
    public void isFlush() {
        assertTrue(FLUSH_FOK.isFlush());
        assertTrue(STRAIGHT_FLUSH_1.isFlush());
        assertTrue(STRAIGHT_FLUSH_2.isFlush());
    }

    @Test
    public void isRoyalFlush() {
        assertTrue(ROYAL_FLUSH.isRoyalFlush());
    }

    @Test
    public void isStraight() {
        assertTrue(STRAIGHT_FLUSH_1.isStraight());
        assertTrue(STRAIGHT_FLUSH_2.isStraight());
        assertFalse(THREEOFKIND.isStraight());
        assertFalse(FLUSH_FOK.isStraight());
    }

    @Test
    public void getKinds() {
        assertEquals(TexasResults.FOUR_OF_KIND, FLUSH_FOK.getKinds());
        assertEquals(TexasResults.THREE_OF_KIND, THREEOFKIND.getKinds());
        assertEquals(TexasResults.TWO_PAIR, TWOPAIR.getKinds());
        assertEquals(TexasResults.PAIR, PAIR.getKinds());
    }

    @Test
    public void valMap() {
    }

    @Test
    public void suitHash() {
    }
}