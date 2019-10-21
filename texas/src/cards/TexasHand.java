package cards;

import enums.Suit;
import enums.TexasResults;
import enums.Face;

import java.util.*;

public class TexasHand extends Hand {

    private final static int MAX_CARDS = 5;

    public boolean IS_TABLE = false;

    public TexasHand(Card ... a) {
        cards.addAll(Arrays.asList(a));
    }

    public void init() {
        getCardCountMap();
        getSuitCountMap();
    }

    public void evaluate() {

    }

    /**
     * Gets the highest valued card in the hand
     * @return {@link Card} of highest value in hand
     */
    public Card getHighestCard() {

        Card highest = cards.get(0);

        for(Card c : cards) {
            if(c.getValue() > highest.getValue())
                highest = c;
        }

        return highest;
    }

    /**
     * Determines whether the hand is all of the same suit
     * @return True if all cards are the same suit
     */
    public boolean isFlush() {
        if (suitCountMap == null) {
            System.err.println("Suit Hashmap was not initialized!");
            return false;
        }

        // if the entire hand is the same suit, then the map should only be size of 1
        return (suitCountMap.size() == 1);
    }

    /**
     * Determines whether the hand is a royal flush
     * @return True if hand is a royal flush
     */
    public boolean isRoyalFlush() {
        boolean sameSuit = isFlush();

        // ensure we're working with the same suit
        if(!sameSuit)
            return false;

        // although this looks inefficient,
        // AND operators will cut off computing the rest of the statement if one is false.
        boolean neededValues =
                cardCountMap.containsKey(Face.ACE)   &&
                cardCountMap.containsKey(Face.KING)  &&
                cardCountMap.containsKey(Face.QUEEN) &&
                cardCountMap.containsKey(Face.JACK) &&
                cardCountMap.containsKey(Face.TEN);

        return neededValues;
    }

    /**
     * This determines whether the hand is in numerical order.
     * It also relies on the {@link TexasHand#cards} array, as this should not change the card positions.
     * @return Whether the hand is classed as a straight
     */
    public boolean isStraight() {

        int valPrevious = cards.get(0).getValue();

        for(int i = 1; i < cards.size(); i++) {
            int valCurrent = cards.get(i).getValue();

            if(valCurrent != valPrevious + 1)
                return false;

            valPrevious = valCurrent;
        }

        return true;
    }

    /**
     * Determines whether there is any kinds in the hand.
     * Note, this must be checked for null - a highcard/straight/flush would return null.
     * @return {@link TexasResults#FULL_HOUSE}, {@link TexasResults#FOUR_OF_KIND}, {@link TexasResults#THREE_OF_KIND}, {@link TexasResults#TWO_PAIR}, {@link TexasResults#PAIR} or null if none found.
     *
     */
    public TexasResults getKinds() {
        // if there's only two values in the map, then we either have 4 / 1 or 2 / 3 split.
        // Check for if we have a value containing 1.
        if( cardCountMap.size() == 2 &&
            ! cardCountMap.containsValue(1))
            return TexasResults.FULL_HOUSE;

        int pairs = 0;

        for(Map.Entry<Face, Integer> e : cardCountMap.entrySet()) {
            if(e.getValue() == 4)
                return TexasResults.FOUR_OF_KIND;

            if(e.getValue() == 3) {
                return TexasResults.THREE_OF_KIND;
            }

            if(e.getValue() == 2) {
                pairs++;
            }
        }

        if (pairs > 0) {
            if(pairs == 2) return TexasResults.TWO_PAIR;
            if(pairs == 1) return TexasResults.PAIR;
        }

        // todo fix this in algorithm
        return null;
    }


    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                '}';
    }
}
