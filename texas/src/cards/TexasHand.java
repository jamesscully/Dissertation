package cards;

import enums.Face;
import enums.Result;

import java.util.Collections;
import java.util.Map;

public class TexasHand extends Hand {

    public boolean IS_TABLE = false;

    public TexasHand(Card a, Card b) {
        cards.add(a);
        cards.add(b);
    }

    public TexasHand(Card... a) {
        IS_TABLE = true;
        Collections.addAll(cards, a);
    }
    
}
