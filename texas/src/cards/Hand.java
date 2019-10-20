package cards;

import enums.Suit;
import enums.TexasResults;
import enums.Value;

import java.util.*;

public class Hand {

    private final static int MAX_CARDS = 5;

    ArrayList<Card> cards = new ArrayList<>();

    Map<Suit, Integer>  suitHashMap  = new HashMap<>();
    Map<Value, Integer> valueHashMap = new HashMap<>();

    public Hand(Card a, Card b, Card c, Card d, Card e) {

        cards.addAll(Arrays.asList(a, b, c, d, e));

        valMap();
        suitHash();

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
        if (suitHashMap == null) {
            System.err.println("Suit Hashmap was not initialized!");
            return false;
        }

        // if the entire hand is the same suit, then the map should only be size of 1
        return (suitHashMap.size() == 1);
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
                valueHashMap.containsKey(Value.ACE)   &&
                valueHashMap.containsKey(Value.KING)  &&
                valueHashMap.containsKey(Value.QUEEN) &&
                valueHashMap.containsKey(Value.JACK) &&
                valueHashMap.containsKey(Value.TEN);

        return neededValues;
    }

    /**
     * This determines whether the hand is in numerical order.
     * It also relies on the {@link Hand#cards} array, as this should not change the card positions.
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
        if( valueHashMap.size() == 2 &&
            ! valueHashMap.containsValue(1))
            return TexasResults.FULL_HOUSE;

        boolean threeKind;
        int pairs = 0;

        for(Map.Entry<Value, Integer> e : valueHashMap.entrySet()) {
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

    /**
     * Creates a map of the Card values and how many there are.
     */
    private void valMap() {
        Map<Value, Integer> map = new HashMap<>();
        for(Card c : cards) {
            Integer curCount = map.get(c.value);
            map.put(c.value, curCount == null ? 1 : curCount + 1);
        }
        valueHashMap = map;
    }

    /**
     * Creates a map containing the suits and how many there are.
     */
    private void suitHash() {
        Map<Suit, Integer> map = new HashMap<>();
        for(Card c : cards) {
            Integer curCount = map.get(c.getSuit());
            map.put(c.getSuit(), curCount == null ? 1 : curCount + 1);
        }
        suitHashMap = map;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                '}';
    }
}
