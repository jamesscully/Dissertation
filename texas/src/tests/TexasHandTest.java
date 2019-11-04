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

    }

    TexasHand tblHandStr2 = new TexasHand(
            new Card(Suit.DIAMONDS, Face.TEN),
            new Card(Suit.SPADES, Face.EIGHT),
            new Card(Suit.CLUBS, Face.NINE),
            new Card(Suit.DIAMONDS, Face.NINE),
            new Card(Suit.DIAMONDS, Face.ACE)
    );

    TexasHand STRAIGHT2 = new TexasHand (
            new Card(Suit.HEARTS, Face.SEVEN),
            new Card(Suit.HEARTS, Face.SIX)
    );





    @Test
    public void isStraight() {

        TexasEvaluator str = new TexasEvaluator("0D 0S 2C KD AD QH JH");
        assertTrue(str.isStraight());

    }

    @Test
    public void getKinds() {

    }
}