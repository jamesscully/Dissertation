package evaluators;

import cards.Card;
import cards.TexasHand;
import enums.Face;
import enums.Suit;
import enums.Rank;
import game.TResult;
import game.TexasTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


public class TexasEvaluator {

    TexasHand player = null;
    TexasHand table  = null;

    TreeMap<Suit, Integer> suitCountMap = new TreeMap<>();
    TreeMap<Face, Integer> cardCountMap = new TreeMap<>();

    ArrayList<Card> cards = new ArrayList<>();


    boolean StraightFlushFlag = false;

    public TexasEvaluator(TexasHand player, TexasTable table) {
        this.player = player;

        this.table  = table.tableCards;

        cards.addAll(player.getCards());
        cards.addAll(this.table.getCards());

        Collections.sort(cards);

        genMaps();
    }

    public TexasEvaluator(String debugStringToHand) {
        if(debugStringToHand.length() != 20)
            System.exit(1);

        String[] strCards = debugStringToHand.split("\\s");

        for(String s : strCards) {
            cards.add(Card.strToCard(s));
        }

        Collections.sort(cards, Collections.reverseOrder());

        genMaps();
    }



    public TResult evaluate() {

        // these conditions must be done in sequence, for order of rankings

        TResult kindOutput = getKinds();

        // this is required so that StraightFlushFlag is set

        TResult isStraight = isStraight();

        // variable to hold each test
        TResult result = null;

        // assignment in if statement is to remove calling method twice
        if( (result = isRoyalFlush()) != null)
            return result;

        if(StraightFlushFlag)
            return isStraight;

        if(kindOutput.rank == Rank.FOUR_OF_KIND)
            return kindOutput;

        if(kindOutput.rank == Rank.FULL_HOUSE)
            return kindOutput;

        if( (result = isFlush()) != null)
            return result;

        if(isStraight != null)
            return isStraight;
        
        if(kindOutput != null)
            return kindOutput;

        // the highest card will always be first as we use a sorted collection
        return new TResult(cards.get(0), Rank.HIGH_CARD);
    }

    /**
     * Determines whether the hand has a flush
     * @return True if all cards are the same suit
     */
    public TResult isFlush() {

        Suit flush = null;

        for(Map.Entry<Suit, Integer> entry : suitCountMap.entrySet()) {
            if(entry.getValue() >= 5) {
                flush = entry.getKey();
            }
        }

        // this will be searching high to low, so we can catch the first instance of the highest suit
        for(Card c : cards) {
            if(c.getSuit() == flush)
                return new TResult(c, Rank.FLUSH);
        }

        return null;
    }

    /**
     * Determines whether the hand is a royal flush
     * @return True if hand is a royal flush
     */
    public TResult isRoyalFlush() {

        // we can only have a royal flush if there is 5> cards of the same suit
        if( !(suitCountMap.containsValue(5) || suitCountMap.containsValue(6) || suitCountMap.containsValue(7)) )
            return null;

        Suit flush = null;

        // get the suit that contains the 5 cards.
        // it's unlikely that there will be 6 or 7 of the same suit, but not impossible!
        for(Map.Entry<Suit,Integer> entry : suitCountMap.entrySet()) {
            if(entry.getValue() >= 5)
                flush = entry.getKey();
        }

        // these must all be true for part of a royal flush to exist
        boolean haveAce   = false,
                haveKing  = false,
                haveQueen = false,
                haveJoker = false,
                haveTen   = false;

        for(Card c : cards) {
            // if it's not the suit that holds 5 of the cards in the hand, continue
            if(c.getSuit() != flush)
                continue;

            // determine which of our booleans should be set
            switch (c.getFace()) {
                case ACE:   haveAce   = true; break;
                case KING:  haveKing  = true; break;
                case QUEEN: haveQueen = true; break;
                case JACK:  haveJoker = true; break;
                case TEN:   haveTen   = true; break;
            }
        }
        // these can only be true if they all belong to the same suit and are present.

        if(haveAce && haveKing && haveQueen && haveJoker && haveTen) {
            // since the ACE will always be highest in royal flushes,
            // we can simply create a new card as such

            Card high = new Card(flush, Face.ACE);
            return new TResult(high, Rank.ROYAL_FLUSH);
        }

        return null;
    }

    /**
     * This determines whether the hand is in numerical order.
     * It also relies on the {@link TexasHand} array, as this should not change the card positions.
     * @return Whether the hand is classed as a straight
     */
    public TResult isStraight() {

        System.out.println("Using cards: " + cards);

        int valStreak = 0;
        int suitStreak = 0;

        // our previous value going in should be the first in the sorted array
        int previousVal = cards.get(0).getValue();
        Suit previousSuit   = cards.get(0).getSuit();

        for(int i = 1; i < 7; i++)  {

            // these are the attributes of card i
            Card card   = cards.get(i);
            Suit suit   = card.getSuit();
            int  value  = card.getValue();

            // if we have a previous card of same value, just skip over.
            if(previousVal == value)
                continue;

            // if the previous card was higher than the current, then add to streak
            // else, reset counter to 0.
            if(previousVal == value + 1) {
                valStreak++;

                if(suit == previousSuit)
                    suitStreak++;
                else
                    suitStreak = 0;

            } else {
                valStreak = 0;
            }

            // if we've already managed a straight, then return true.
            // note that this should return the highest STRAIGHT, as we're descending down.
            // todo make this function return points where there is a straight beginning, to determine straight flushes.
            if(valStreak == 4) {
                // this removes the need for ANOTHER function for St. Flushes.
                if(suitStreak == 4)
                    StraightFlushFlag = true;

                Card high = cards.get(i - valStreak);
                Rank result = StraightFlushFlag ? Rank.STRAIGHT_FLUSH : Rank.STRAIGHT;

                System.out.println("Returning true, high card: " + cards.get(i - valStreak));

                return new TResult(high, result);
            }

            previousVal = value;
            previousSuit = suit;
        }
        return null;
    }

    /**
     * Determines whether there is any kinds in the hand.
     * Note, this must be checked for null - a highcard/straight/flush would return null.
     * @return {@link Rank#FULL_HOUSE}, {@link Rank#FOUR_OF_KIND}, {@link Rank#THREE_OF_KIND}, {@link Rank#TWO_PAIR}, {@link Rank#PAIR} or null if none found.
     */
    public TResult getKinds() {

        // what Face we have and what pair of it
        TreeMap<Face, Rank> pairsMap = new TreeMap<>();

        // used to deteremine a full house;
        // which occurs when we have a three-of-kind and a two pair co-exist
        boolean fhThrKind = false;
        boolean fhTwoPair = false;

        Card hFourKind = null;
        Card hThrKind  = null;
        Card hTwpKind  = null;

        TResult result = null;

        int pairs = 0;

        // we'll order the map in descending order, that way we can see our highest-power face pairs first.
        for(Map.Entry<Face, Integer> e : cardCountMap.descendingMap().entrySet()) {
            if(e.getValue() == 4) {
                pairsMap.put(e.getKey(), Rank.FOUR_OF_KIND);
                hFourKind = new Card(Suit.CLUBS, e.getKey());
                result = new TResult(hFourKind, Rank.FOUR_OF_KIND);
            }
            if(e.getValue() == 3) {
                pairsMap.put(e.getKey(), Rank.THREE_OF_KIND);
                fhThrKind = true;
                hThrKind = new Card(Suit.CLUBS, e.getKey());
            }
            if(e.getValue() == 2) {
                pairsMap.put(e.getKey(), Rank.PAIR);
                fhTwoPair = true;
                hTwpKind = new Card(Suit.CLUBS, e.getKey());

                pairs++;
            }
        }


        // three of kind will always be the highest
        if(fhThrKind && fhTwoPair)
            result = new TResult(hThrKind, Rank.FULL_HOUSE);

        if(pairsMap.size() == 1) {
            Map.Entry<Face, Rank> r = pairsMap.firstEntry();
            // suit does not matter in texas, thus we can just return as clubs
            Card high = new Card(Suit.CLUBS, r.getKey());

            result = new TResult(high, r.getValue());
        }

        if(pairs > 1)
            result = new TResult(hTwpKind, Rank.TWO_PAIR);

        return result;
    }

    private void genMaps() {
        for(Card c : cards) {
            Integer sCount = suitCountMap.get(c.getSuit());
            Integer fCount = cardCountMap.get(c.getFace());

            // for both:
            // if we have no value in the map, set to one,
            // else, increment value for key
            suitCountMap.put(c.getSuit(), sCount == null ? 1 : sCount + 1);
            cardCountMap.put(c.getFace(), fCount == null ? 1 : fCount + 1);
        }
    }
}
