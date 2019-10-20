package card;

import java.util.*;

public class Hand {

    public enum Result {
        HIGH_CARD(1),
        PAIR(2),
        TWO_PAIR(3),
        THREE_OF_KIND(4),
        STRAIGHT(5),
        FLUSH(6),
        FULL_HOUSE(7),
        FOUR_OF_KIND(8),
        STRAIGHT_FLUSH(9),
        ROYAL_FLUSH(10);

        private final int value;

        Result(int val) {
            this.value = val;
        }
    }

    private final static int MAX_CARDS = 5;

    ArrayList<Card> cards = new ArrayList<>();

    Map<Suit, Integer> suitHashMap = new HashMap<>();
    Map<Value, Integer> valueHashMap = new HashMap<>();

    public Hand(Card a, Card b, Card c, Card d, Card e) {

        cards.addAll(Arrays.asList(a, b, c, d, e));

        valMap();
        suitHash();

    }


    public void evaluate() {



    }

    public Card getHighestCard() {

        Card highest = cards.get(0);

        for(Card c : cards) {
            if(c.getValue() > highest.getValue())
                highest = c;
        }

        return highest;
    }

    public boolean isFlush() {
        if (suitHashMap == null) {
            System.err.println("Suit Hashmap was not initialized!");
        }

        // if the entire hand is the same suit, then the map should only be size of 1
        return (suitHashMap.size() == 1);
    }

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
                valueHashMap.containsKey(Value.JOKER) &&
                valueHashMap.containsKey(Value.TEN);

        return neededValues;
    }

    /**
     * This determines whether the hand is in numerical order (ascending)
     *
     * It also relies on the {@link Hand#cards} array, as this should not change the card positions.
     *
     * @// TODO: 10/20/19 Add descending order
     * @return Whether the hand is classed as a straight
     */
    public boolean isStraight() {

        int valPrevious = cards.get(0).value.val;

        for(int i = 1; i < cards.size(); i++) {
            int valCurrent = cards.get(i).value.val;

            if(valCurrent != valPrevious + 1)
                return false;

            valPrevious = valCurrent;
        }

        return true;
    }

    public Result getKinds() {
        // if there's only two values in the map, then we either have 4 / 1 or 2 / 3 split.
        // Check for if we have a value containing 1.
        if( valueHashMap.size() == 2 &&
            ! valueHashMap.containsValue(1))
            return Result.FULL_HOUSE;

        boolean threeKind;
        int pairs = 0;

        for(Map.Entry<Value, Integer> e : valueHashMap.entrySet()) {
            if(e.getValue() == 4)
                return Result.FOUR_OF_KIND;

            if(e.getValue() == 3) {
                return Result.THREE_OF_KIND;
            }

            if(e.getValue() == 2) {
                pairs++;
            }
        }



        if (pairs > 0) {
            if(pairs == 2) return Result.TWO_PAIR;
            if(pairs == 1) return Result.PAIR;
        }

        // todo fix this in algorithm
        return null;
    }

    /**
     * This creates a map of the Card values and counts.
     */
    public void valMap() {
        Map<Value, Integer> map = new HashMap<>();
        for(Card c : cards) {
            Integer curCount = map.get(c.value);
            map.put(c.value, curCount == null ? 1 : curCount + 1);
        }
        valueHashMap = map;
    }

    public void suitHash() {
        Map<Suit, Integer> map = new HashMap<>();
        for(Card c : cards) {
            Integer curCount = map.get(c.suit);
            map.put(c.suit, curCount == null ? 1 : curCount + 1);
        }
        suitHashMap = map;
    }








}
