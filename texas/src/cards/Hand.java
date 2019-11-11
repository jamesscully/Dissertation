package cards;

import enums.Face;
import enums.Suit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Hand {

    protected ArrayList<Card> cards = new ArrayList<>();

    protected Map<Suit, Integer> suitCountMap = new HashMap<>();
    protected Map<Face, Integer> cardCountMap = new HashMap<>();;

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

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Map<Suit, Integer> getSuitCountMap() {
        getCardSuits();
        return suitCountMap;
    }

    public Map<Face, Integer> getCardCountMap(){
        getCardValues();
        return cardCountMap;
    }


}
