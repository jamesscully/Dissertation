package tests;

import cards.Card;
import cards.TexasHand;
import enums.Suit;
import enums.TexasResults;
import enums.Face;
import evaluators.TexasEvaluator;
import game.Table;
import game.TexasTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class TexasHandTest {

    TexasHand tableHand = new TexasHand(
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.SPADES, Face.TEN),
            new Card(Suit.CLUBS, Face.TWO),
            new Card(Suit.DIAMONDS, Face.KING),
            new Card(Suit.DIAMONDS, Face.ACE)
    );





    TexasTable TABLE = new TexasTable(tableHand);

    TexasHand THOFKIND = new TexasHand(
            new Card(Suit.HEARTS, Face.TWO),
            new Card(Suit.SPADES, Face.TWO)
    );

    TexasHand FROFKIND = new TexasHand (
            new Card(Suit.HEARTS, Face.TEN),
            new Card(Suit.HEARTS, Face.TEN)
    );

    TexasHand FLUSH = new TexasHand (
            new Card(Suit.DIAMONDS, Face.ACE),
            new Card(Suit.DIAMONDS, Face.ACE)
    );
    
    TexasHand TWOFKIND = new TexasHand (
            new Card(Suit.HEARTS, Face.TWO),
            new Card(Suit.HEARTS, Face.ACE)
    );

    TexasHand STRAIGHT = new TexasHand (
            new Card(Suit.HEARTS, Face.QUEEN),
            new Card(Suit.HEARTS, Face.JACK)
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

    }

    @Test
    public void isFlush() {

    }

    @Test
    public void isRoyalFlush() {
        assertTrue(new TexasEvaluator("AD KD JD QD 0D QS JC").isRoyalFlush());
        assertTrue(new TexasEvaluator("4D AD KD QD JD 2D 0D").isRoyalFlush());
        assertTrue(new TexasEvaluator("3D 8D AD KD QD JD 0D").isRoyalFlush());
        assertTrue(new TexasEvaluator("7D 0D KD QD AD JD 3D").isRoyalFlush());

        assertFalse(new TexasEvaluator("2D 3D 4D 5D 6D 7D 8D").isRoyalFlush());
        assertFalse(new TexasEvaluator("8D 4D 2D 7D 8D 0D AD").isRoyalFlush());


    }

    @Test
    public void isStraight() {

        assertTrue(new TexasEvaluator("0D 0D 2D KD AD QD JD").isStraight());
        assertTrue(new TexasEvaluator("AD KD QD JD 0D QD JD").isStraight());
        assertTrue(new TexasEvaluator("0D 2D 0D 9D 8D 7D 6D").isStraight());
        assertTrue(new TexasEvaluator("2D 3D 4D 5D 6D QD JD").isStraight());
        assertTrue(new TexasEvaluator("0D 0D 2D KD AD QD JD").isStraight());

        // Test that multiple of the same card does not affect streak
        assertTrue(new TexasEvaluator("KD QD JD JD JD 0D 9D").isStraight());

        /* Falses */

        assertFalse(new TexasEvaluator("0D 0D 0D 0D AD QD JD").isStraight());
        assertFalse(new TexasEvaluator("2D 5D 9D JD 0D QD JD").isStraight());
        assertFalse(new TexasEvaluator("kD 2D 0D 4D 8D 7D 6D").isStraight());
        assertFalse(new TexasEvaluator("2D 3D 9D 3D 4D QD JD").isStraight());


    }

    @Test
    public void getKinds() {
        assertEquals(TexasResults.FOUR_OF_KIND,  new TexasEvaluator("0D 0D 0D 0D AD QD JD").getKinds());
        assertEquals(TexasResults.THREE_OF_KIND, new TexasEvaluator("0D 0D 0D 4D AD QD KD").getKinds());
        assertEquals(TexasResults.TWO_PAIR,      new TexasEvaluator("0D 0D KD KD AD QD JD").getKinds());
        assertEquals(TexasResults.TWO_PAIR,      new TexasEvaluator("0D 0D QD QD AD 2D JD").getKinds());
    }
}