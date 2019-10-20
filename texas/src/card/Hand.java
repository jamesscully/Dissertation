package card;

import java.util.ArrayList;
import java.util.Collections;

public class Hand {


    private final static int MAX_CARDS = 5;

    ArrayList<Card> cards = new ArrayList<>();

    public Hand() {
        cards.add(new Card(Suit.CLUBS, Value.ACE));
        cards.add(new Card(Suit.CLUBS, Value.ACE));
        cards.add(new Card(Suit.HEARTS, Value.ACE));
        cards.add(new Card(Suit.CLUBS, Value.ACE));
        cards.add(new Card(Suit.CLUBS, Value.ACE));

        for (Card c : cards) {
            System.out.println(c.hashCode());
            System.out.println(c);
        }

        System.out.println(Collections.frequency(cards, new Card(Suit.CLUBS, Value.ACE)));
    }





}
