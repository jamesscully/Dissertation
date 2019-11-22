package evaluators;

import cards.Card;
import cards.TexasHand;
import enums.Face;
import enums.Suit;
import enums.TexasResults;
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

    public TexasEvaluator(TexasHand player, TexasTable table) {
        this.player = player;

        this.table  = table.tableCards;


        cards.addAll(player.getCards());
        cards.addAll(this.table.getCards());

        Collections.sort(cards);

        getCardSuits();
        getCardValues();
    }

    public TexasEvaluator(String debugStringToHand) {
        if(debugStringToHand.length() != 20)
            System.exit(1);

        String[] strCards = debugStringToHand.split("\\s");

        for(String s : strCards) {
            s = s.toUpperCase();

            Face face = null;
            Suit suit = null;

            switch (s.charAt(0)) {
                case 'A': face = Face.ACE;   break;
                case 'K': face = Face.KING;  break;
                case 'Q': face = Face.QUEEN; break;
                case 'J': face = Face.JACK;  break;
                case '0': face = Face.TEN;   break;
                case '9': face = Face.NINE;  break;
                case '8': face = Face.EIGHT; break;
                case '7': face = Face.SEVEN; break;
                case '6': face = Face.SIX;   break;
                case '5': face = Face.FIVE;  break;
                case '4': face = Face.FOUR;  break;
                case '3': face = Face.THREE; break;
                case '2': face = Face.TWO;   break;

                default:
                    System.err.println(s.charAt(0) + " is not a valid face!");
                    System.exit(1);
                    break;
            }

            switch (s.charAt(1)) {
                case 'D': suit = Suit.DIAMONDS; break;
                case 'S': suit = Suit.SPADES;   break;
                case 'C': suit = Suit.CLUBS;    break;
                case 'H': suit = Suit.HEARTS;   break;

                default:
                    System.err.println(s.charAt(1) + " is not a valid suit!");
                    System.exit(1);
                    break;
            }

            cards.add(new Card(suit, face));
        }

        Collections.sort(cards, Collections.reverseOrder());

        getCardSuits();
        getCardValues();
    }



    public TexasResults evaluate() {

        TexasResults kindOutput = getKinds();

        if(isRoyalFlush())
            return TexasResults.ROYAL_FLUSH;

        // straight flush goes here

        if(kindOutput == TexasResults.FOUR_OF_KIND)
            return kindOutput;

        if(kindOutput == TexasResults.FULL_HOUSE)
            return kindOutput;

        if(isFlush())
            return TexasResults.FLUSH;

        if(isStraight())
            return TexasResults.STRAIGHT;

        // todo this could be reduced to returning if not null, we'll do that later

        if(kindOutput == TexasResults.THREE_OF_KIND)
            return kindOutput;

        if(kindOutput == TexasResults.TWO_PAIR)
            return kindOutput;

        if(kindOutput == TexasResults.PAIR)
            return kindOutput;

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

    //todo rework
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

        // we can only have a royal flush if there is 5> cards of the same suit
        if( !(suitCountMap.containsValue(5) || suitCountMap.containsValue(6) || suitCountMap.containsValue(7)) )
            return false;

        Suit flush = null;

        // get the suit that contains the 5 cards.
        // it's unlikely that there will be 6 or 7 of the same suit, but not impossible!
        for(Map.Entry<Suit,Integer> entry : suitCountMap.entrySet()) {
            if(entry.getValue() >= 5)
                flush = entry.getKey();
        }

        boolean haveAce   = false,
                haveKing  = false,
                haveQueen = false,
                haveJoker = false,
                haveTen   = false;

        for(Card c : cards) {
            // if it's not the suit that holds 5 of the cards in the hand, continue
            if(c.getSuit() != flush)
                continue;

            switch (c.getFace()) {
                case ACE:
                    haveAce = true;
                    break;
                case KING:
                    haveKing = true;
                    break;
                case QUEEN:
                    haveQueen = true;
                    break;
                case JACK:
                    haveJoker = true;
                    break;
                case TEN:
                    haveTen = true;
                    break;
            }
        }
        // these can only be true if they all belong to the same suit and are present.
        return (haveAce && haveKing && haveQueen && haveJoker && haveTen);
    }

    /**
     * This determines whether the hand is in numerical order.
     * It also relies on the {@link TexasHand} array, as this should not change the card positions.
     * @return Whether the hand is classed as a straight
     */
    public boolean isStraight() {

        int streak = 0;

        // our previous value going in should be the first in the sorted array
        int previousVal = cards.get(0).getValue();

        for(int i = 1; i < 7; i++)  {
            Card cCard = cards.get(i);
            int value  = cCard.getValue();

            // if we have a previous card of same value, just skip over.
            if(previousVal == value)
                continue;

            // if the previous card was higher than the current, then add to streak
            if(previousVal == value + 1) {
                streak++;
            } else {
                streak = 0;
            }

            // if we've already managed a straight, then return true.
            // note that this should return the highest STRAIGHT, as we're descending down.
            // todo make this function return points where there is a straight beginning, to determine straight flushes.
            if(streak == 4)
                return true;

            previousVal = value;
        }
        return false;
    }

    /**
     * Determines whether there is any kinds in the hand.
     * Note, this must be checked for null - a highcard/straight/flush would return null.
     * @return {@link TexasResults#FULL_HOUSE}, {@link TexasResults#FOUR_OF_KIND}, {@link TexasResults#THREE_OF_KIND}, {@link TexasResults#TWO_PAIR}, {@link TexasResults#PAIR} or null if none found.
     */
    public TexasResults getKinds() {

        TreeMap<Face, TexasResults> pairs = new TreeMap<>();

        //todo make this return the Kind and the high card associated with it

        // used to deteremine a full house;
        // which occurs when we have a three-of-kind and a two pair.
        boolean fullHouse3OK = false;
        Face THOFKIND_HIGH = null;
        boolean fullHousePR = false;
        Face PAIR_HIGH = null;

        int iPairs = 0;

        // we'll order the map in descending order, that way we can see our highest-power face pairs first.
        for(Map.Entry<Face, Integer> e : cardCountMap.descendingMap().entrySet()) {
            if(e.getValue() == 4)
                pairs.put(e.getKey(), TexasResults.FOUR_OF_KIND);
            if(e.getValue() == 3) {
                pairs.put(e.getKey(), TexasResults.THREE_OF_KIND);
                fullHouse3OK = true;
            }
            if(e.getValue() == 2) {
                pairs.put(e.getKey(), TexasResults.PAIR);
                fullHousePR = true;
                iPairs++;
            }
        }

        TexasResults result = null;

        System.out.println(pairs);

        if(pairs.size() == 1)
            result = pairs.firstEntry().getValue();

        if(iPairs > 1)
            result = TexasResults.TWO_PAIR;
        
        if(fullHouse3OK && fullHousePR)
            result = TexasResults.FULL_HOUSE;

        return result;
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
