package tests;

import com.scully.enums.Face;
import com.scully.enums.Rank;
import com.scully.enums.TAction;
import com.scully.evaluators.TexasEvaluator;
import com.scully.game.TResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TexasHandTest {
    @BeforeAll
    static void setUp() throws Exception {

    }

    @AfterAll
    static void tearDown() throws Exception {
    }

    @Test
    public void evaluate() {
        assertEquals(new TResult(Face.ACE, Rank.THREE_OF_KIND),
                     new TexasEvaluator("AD AD AD 2S 5S JC 0S").evaluate());

        assertEquals(new TResult(Face.ACE, Rank.STRAIGHT),
                     new TexasEvaluator("AD KS QH JS 0D JC 3S").evaluate());

        assertEquals(new TResult(Face.EIGHT, Rank.STRAIGHT),
                     new TexasEvaluator("AD KS 8H 7S 6D 5C 4S").evaluate());

        assertEquals(new TResult(Face.ACE, Rank.STRAIGHT),
                     new TexasEvaluator("AD KS QH JS 0D JC 3S").evaluate());

        assertEquals(new TResult(Face.ACE, Rank.THREE_OF_KIND),
                     new TexasEvaluator("AS AD AH 2S 5S JC 3S").evaluate());

        assertEquals(new TResult(Face.KING, Rank.STRAIGHT_FLUSH),
                     new TexasEvaluator("KD QD JD 0D 9D JC 3S").evaluate());

//        assertEquals(new TexasEvaluator("").evaluate(),  );
    }

    @Test
    public void getHighestCard() {
        assertEquals(new TResult(Face.ACE, Rank.HIGH_CARD), new TexasEvaluator("AH JC KH 2H 3C 5D 8D").evaluate());
        assertEquals(new TResult(Face.KING, Rank.HIGH_CARD), new TexasEvaluator("4H JC KH 2H 3C 5D 8D").evaluate());
        assertEquals(new TResult(Face.TEN, Rank.HIGH_CARD), new TexasEvaluator("6H 9C 0H 2H 3C 8D 5D").evaluate());
        assertEquals(new TResult(Face.JACK, Rank.HIGH_CARD), new TexasEvaluator("2H JC 3H 4H 5C 8D 0D").evaluate());
    }

    @Test
    public void isFlush() {
        assertNotNull(new TexasEvaluator("AD AD AD AD AD JC JC").isFlush());
        assertNotNull(new TexasEvaluator("AH AH AD AH AD JH JH").isFlush());
        assertNotNull(new TexasEvaluator("AD AD AD AD AD JC JC").isFlush());
        assertNotNull(new TexasEvaluator("AD AD AD AD AD JC JC").isFlush());


        assertNull(new TexasEvaluator("AD AD AC AD AD JC JC").isFlush());
        assertNull(new TexasEvaluator("AH AC AD AH AD QH JH").isFlush());
        assertNull(new TexasEvaluator("JD AS 0D AD KD JC JC").isFlush());
        assertNull(new TexasEvaluator("5D Ac 0D AD 9D JC JC").isFlush());
    }

    @Test
    public void isRoyalFlush() {
        assertNotNull(new TexasEvaluator("AD KD JD QD 0D QS JC").isRoyalFlush());
        assertNotNull(new TexasEvaluator("4D AD KD QD JD 2D 0D").isRoyalFlush());
        assertNotNull(new TexasEvaluator("3D 8D AD KD QD JD 0D").isRoyalFlush());
        assertNotNull(new TexasEvaluator("7D 0D KD QD AD JD 3D").isRoyalFlush());

        assertNull(new TexasEvaluator("2D 3D 4D 5D 6D 7D 8D").isRoyalFlush());
        assertNull(new TexasEvaluator("8D 4D 2D 7D 8D 0D AD").isRoyalFlush());
        assertNull(new TexasEvaluator("0D kD qD jh aD 0D AD").isRoyalFlush());
        assertNull(new TexasEvaluator("8D 4D 2D 7D 8D 0D AD").isRoyalFlush());
        assertNull(new TexasEvaluator("8D 4D 2D 7D 8D 0D AD").isRoyalFlush());
    }



    @Test
    public void isStraight() {

        assertNotNull(new TexasEvaluator("0D 0D 2D KD AD QD JD").isStraight());
        assertNotNull(new TexasEvaluator("AD KH QD JD 0D QD JD").isStraight());
        assertNotNull(new TexasEvaluator("0D 2D 0D 9D 8D 7D 6D").isStraight());
        assertNotNull(new TexasEvaluator("2D 3D 4D 5D 6D QD JD").isStraight());
        assertNotNull(new TexasEvaluator("0D 0D 2D KD AD QD JD").isStraight());
        assertNotNull(new TexasEvaluator("KD QD JD JD JD 0D 9D").isStraight());

        /* Falses */

        assertNull(new TexasEvaluator("0D 0D 0D 0D AD QD JD").isStraight());
        assertNull(new TexasEvaluator("2D 5D 9D JD 0D QD JD").isStraight());
        assertNull(new TexasEvaluator("kD 2D 0D 4D 8D 7D 6D").isStraight());
        assertNull(new TexasEvaluator("2D 3D 9D 3D 4D QD JD").isStraight());

    }



    @Test
    public void getKinds() {
        assertEquals(new TResult(Face.TEN, Rank.FOUR_OF_KIND),
                     new TexasEvaluator("0D 0D 0D 0D AD QD JD").getKinds());

        assertEquals(new TResult(Face.TEN, Rank.THREE_OF_KIND),
                     new TexasEvaluator("0D 0D 0D 4D AD QD KD").getKinds());

        assertEquals(new TResult(Face.KING, Rank.TWO_PAIR),
                     new TexasEvaluator("0D 0D KD KD AD QD JD").getKinds());

        assertEquals(new TResult(Face.QUEEN, Rank.TWO_PAIR),
                     new TexasEvaluator("0D 0D QD QD AD 2D JD").getKinds());

        assertEquals(new TResult(Face.QUEEN, Rank.FULL_HOUSE),
                     new TexasEvaluator("0D 0D QD QD QD 2D JD").getKinds());
    }
}