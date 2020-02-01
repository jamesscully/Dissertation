package cards;

import java.util.Collections;

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

    public void addCard(Card c) {
        this.cards.add(c);
    }

}
