package card;

import java.util.ArrayList;

public class  Deck {

    private static Deck instance;

    /**
     * Holds all cards available
     */
    ArrayList<Card> deck = new ArrayList<>();

    public Deck() { }

    public static synchronized Deck getInstance() {
        if(instance == null) {
            instance = new Deck();
        }
        return instance;
    }

    /**
     * Populate {@link Deck#deck} with all needed cards.
     */
    public void initialize() {
        for(Suit s : Suit.values()) {
            for(Value v : Value.values()) {
                deck.add(new Card(s, v));
            }
        }

        int i = 1;
        for(Card c : deck) {

            if(i % 13 == 1)
                System.out.println();

            System.out.println(i + " : " + c);



            i++;
        }
    }


}
