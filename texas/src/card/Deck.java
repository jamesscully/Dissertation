package card;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private static Deck instance = null;
    private int cards_removed = 0;

    /**
     * Holds all cards available
     */
    private static ArrayList<Card> deck = new ArrayList<>();

    private Deck() {
        initialize();
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

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
        resetDeck();
    }

    public void resetDeck() {
        cards_removed = 0;
        for(Suit s : Suit.values()) {
            for(Value v : Value.values()) {
                deck.add(new Card(s, v));
            }
        }
    }

    /**
     * Removes card from the deck.
     * @param c Card to remove
     * @return True if remove was successful
     */
    private boolean removeCard(Card c) {
        return deck.remove(c);
    }

    /**
     * Pulls a card from the deck for use in a Hand
     * @return Card retrieved from deck
     */
    public Card pullCard() {
        Card pull = Card.getRandomCard();

        // if removing the card returns false, try again
        while(!removeCard(pull))
            pull = Card.getRandomCard();

        return pull;
    }

    /**
     * Prints the deck as it is currently.
     */
    public void printDeck() {
        int i = 0;
        for(Card c : deck) {
            System.out.printf("%2d: %17s\n", i, c);
            i++;
        }
    }

    public int size() {
        return deck.size();
    }

    public boolean inDeck(Card c) {
        return deck.contains(c);
    }


}
