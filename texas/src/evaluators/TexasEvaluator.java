package evaluators;

import cards.Card;
import cards.TexasHand;
import enums.Face;
import enums.Suit;
import enums.TexasResults;

import java.util.*;

public class TexasEvaluator {

    TexasHand player = null;
    TexasHand table  = null;

    TexasHand overall = null;

    ArrayList<Card> cards = new ArrayList<>();

    Map<Suit, Integer> suitCountMap = new HashMap<>();
    Map<Face, Integer> cardCountMap = new HashMap<>();;


    public TexasEvaluator(TexasHand player, TexasHand table) {
        this.player = player;

        if(!table.IS_TABLE)
            throw new IllegalArgumentException("TexasEvaluator: second argument must be have IS_TABLE flag enabled.");

        this.table  = table;


        cards.addAll(player.getCards());
        cards.addAll(table.getCards());

        Collections.sort(cards);

        getCardSuits();
        getCardValues();

    }

    public TexasResults evaluate() {

        return TexasResults.HIGH_CARD;
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
     * Determines whether the hand has a flush
     * @return True if all cards are the same suit
     */
    public boolean isFlush() {
        for(Map.Entry<Suit, Integer> entry : suitCountMap.entrySet()) {
            if(entry.getValue() == 5)
                return true;
        }
        return false;
    }

    /**
     * Determines whether the hand is a royal flush
     * @return True if hand is a royal flush
     */
    public boolean isRoyalFlush() {

        return false;
    }

    /**
     * This determines whether the hand is in numerical order.
     * It also relies on the {@link TexasHand} array, as this should not change the card positions.
     * @return Whether the hand is classed as a straight
     */
    public boolean isStraight() {

        System.out.println("Sorted :   " + cards);
        System.out.println("Value map: " + cardCountMap);

        ArrayList<Card> sorted = new ArrayList<>(cards);
        Collections.sort(sorted, Collections.reverseOrder());

        System.out.println(sorted);

        int streak = 0;

        int previousVal = 0;
        for(int i = 0; i < 7; i++)  {
            Card cCard = sorted.get(i);
            int value = cCard.getValue();

            if(previousVal == value - 1) {
                previousVal = value;
                streak++;
            }



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

        return null;
    }

    private void getCardSuits() {
        for(Card c : cards) {
            Integer count = suitCountMap.get(c.getSuit());
            suitCountMap.put(c.getSuit(), count == null ? 1 : count + 1);
        }
    }

    private void getCardValues() {
        for(Card c : cards) {
            Integer count = cardCountMap.get(c.getFace());
            cardCountMap.put(c.getFace(), count == null ? 1 : count + 1);
        }
    }





}
