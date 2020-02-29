import com.scully.enums.Face;
import com.scully.enums.Rank;
import com.scully.enums.TAction;
import com.scully.evaluators.TexasEvaluator;
import com.scully.game.TResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TActionTest {

    @BeforeAll
    static void setUp() throws Exception {

    }

    @AfterAll
    static void tearDown() throws Exception {
    }

    @Test
    public void factory() {

        assertEquals(TAction.CALL, TAction.parseTAction("CALL"));
        assertEquals(TAction.CALL, TAction.parseTAction("call"));
        assertEquals(TAction.CALL, TAction.parseTAction(" call "));

        assertEquals(TAction.RAISE, TAction.parseTAction("RAISE 500"));
        assertEquals(500, TAction.parseTAction("RAISE 500").value);

        assertEquals(TAction.RAISE, TAction.parseTAction("RAISE 231213"));
        assertEquals(111, TAction.parseTAction("RAISE      111      ").value);

        assertNull(TAction.parseTAction("RAISE"));

    }

}
