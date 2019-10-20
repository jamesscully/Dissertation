package tests;

import card.Card;
import card.Hand;
import card.Suit;
import card.Value;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HandTest {

    Hand FLUSH_FOK = new Hand(
            new Card(Suit.CLUBS, Value.FOUR),
            new Card(Suit.CLUBS, Value.FOUR),
            new Card(Suit.CLUBS, Value.FOUR),
            new Card(Suit.CLUBS, Value.FOUR),
            new Card(Suit.CLUBS, Value.ACE)
    );

    Hand STRAIGHT_FLUSH_1 = new Hand(
            new Card(Suit.CLUBS, Value.THREE),
            new Card(Suit.CLUBS, Value.FOUR),
            new Card(Suit.CLUBS, Value.FIVE),
            new Card(Suit.CLUBS, Value.SIX),
            new Card(Suit.CLUBS, Value.SEVEN)
    );

    Hand STRAIGHT_FLUSH_2 = new Hand(
            new Card(Suit.DIAMONDS, Value.NINE),
            new Card(Suit.DIAMONDS, Value.TEN),
            new Card(Suit.DIAMONDS, Value.JOKER),
            new Card(Suit.DIAMONDS, Value.QUEEN),
            new Card(Suit.DIAMONDS, Value.KING)
    );

    Hand ROYAL_FLUSH = new Hand(
            new Card(Suit.DIAMONDS, Value.TEN),
            new Card(Suit.DIAMONDS, Value.JOKER),
            new Card(Suit.DIAMONDS, Value.QUEEN),
            new Card(Suit.DIAMONDS, Value.KING),
            new Card(Suit.DIAMONDS, Value.ACE)
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
    public void isHighCard() {
        assertEquals(FLUSH_FOK.getHighestCard(),        new Card(Suit.CLUBS,    Value.ACE));
        assertEquals(STRAIGHT_FLUSH_1.getHighestCard(), new Card(Suit.CLUBS,    Value.SEVEN));
        assertEquals(STRAIGHT_FLUSH_2.getHighestCard(), new Card(Suit.DIAMONDS, Value.KING));
        assertEquals(ROYAL_FLUSH.getHighestCard(),      new Card(Suit.DIAMONDS, Value.ACE));
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
    }

    @Test
    public void getKinds() {
        assertEquals(Hand.Result.FOUR_OF_KIND, FLUSH_FOK.getKinds());
    }

    @Test
    public void valMap() {
    }

    @Test
    public void suitHash() {
    }
}