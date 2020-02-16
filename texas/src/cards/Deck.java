package cards;

import enums.Face;
import enums.Suit;

import java.util.ArrayList;

public class Deck {

    private static Deck instance = null;

    /**
     * Holds all cards available
     */
    private static ArrayList<Card> deck = new ArrayList<>();

    private Deck() {

    }

    public static Deck getInstance() {
        if(instance == null) {
            instance = new Deck();
            resetDeck();
        }
        return instance;
    }

    /**
     * Populate {@link Deck#deck} with all needed cards.
     */


    public static void resetDeck() {
        for(Suit s : Suit.values()) {
            for(Face v : Face.values()) {
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
     * Gets current size of deck (available cards)
     * @return Size of Deck
     */
    public int size() {
        return deck.size();
    }

    /**
     * Tests whether card is in deck / available
     * @param c Card to check
     * @return True if card is available
     */
    public boolean inDeck(Card c) {
        return deck.contains(c);
    }


}
