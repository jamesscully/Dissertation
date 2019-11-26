package tests;

import enums.Rank;
import evaluators.TexasEvaluator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TexasHandTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void evaluate() {


        assertEquals(Rank.THREE_OF_KIND, new TexasEvaluator("AD AD AD 2S 5S JC 0S").evaluate());
        assertEquals(Rank.STRAIGHT,      new TexasEvaluator("AD KS QH JS 0D JC 3S").evaluate());
        assertEquals(Rank.THREE_OF_KIND, new TexasEvaluator("AS AD AH 2S 5S JC 3S").evaluate());
        assertEquals(Rank.STRAIGHT_FLUSH, new TexasEvaluator("KD QD JD 0D 9D JC 3S").evaluate());

//        assertEquals(new TexasEvaluator("").evaluate(),  );
//        assertEquals(new TexasEvaluator("").evaluate(),  );
//        assertEquals(new TexasEvaluator("").evaluate(),  );
//        assertEquals(new TexasEvaluator("").evaluate(),  );
//        assertEquals(new TexasEvaluator("").evaluate(),  );
//        assertEquals(new TexasEvaluator("").evaluate(),  );

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
        assertFalse(new TexasEvaluator("0D kD qD jh aD 0D AD").isRoyalFlush());
        assertFalse(new TexasEvaluator("8D 4D 2D 7D 8D 0D AD").isRoyalFlush());
        assertFalse(new TexasEvaluator("8D 4D 2D 7D 8D 0D AD").isRoyalFlush());
    }

    @Test
    public void isStraight() {

        assertTrue(new TexasEvaluator("0D 0D 2D KD AD QD JD").isStraight());
        assertTrue(new TexasEvaluator("AD KH QD JD 0D QD JD").isStraight());
        assertTrue(new TexasEvaluator("0D 2D 0D 9D 8D 7D 6D").isStraight());
        assertTrue(new TexasEvaluator("2D 3D 4D 5D 6D QD JD").isStraight());
        assertTrue(new TexasEvaluator("0D 0D 2D KD AD QD JD").isStraight());
        assertTrue(new TexasEvaluator("KD QD JD JD JD 0D 9D").isStraight());

        /* Falses */

        assertFalse(new TexasEvaluator("0D 0D 0D 0D AD QD JD").isStraight());
        assertFalse(new TexasEvaluator("2D 5D 9D JD 0D QD JD").isStraight());
        assertFalse(new TexasEvaluator("kD 2D 0D 4D 8D 7D 6D").isStraight());
        assertFalse(new TexasEvaluator("2D 3D 9D 3D 4D QD JD").isStraight());

    }

    @Test
    public void getKinds() {
        assertEquals(Rank.FOUR_OF_KIND,  new TexasEvaluator("0D 0D 0D 0D AD QD JD").getKinds());
        assertEquals(Rank.THREE_OF_KIND, new TexasEvaluator("0D 0D 0D 4D AD QD KD").getKinds());
        assertEquals(Rank.TWO_PAIR,      new TexasEvaluator("0D 0D KD KD AD QD JD").getKinds());
        assertEquals(Rank.TWO_PAIR,      new TexasEvaluator("0D 0D QD QD AD 2D JD").getKinds());
        assertEquals(Rank.FULL_HOUSE,      new TexasEvaluator("0D 0D QD QD QD 2D JD").getKinds());
    }
}