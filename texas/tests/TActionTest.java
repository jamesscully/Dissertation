import com.scully.enums.TAction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
// test
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
