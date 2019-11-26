package cards;

import enums.Face;
import enums.Suit;

import java.util.Objects;
import java.util.Random;

public class Card implements Comparable<Card> {

    private final Suit suit;
    public final Face face;

    public Card(Suit s, Face v) {
        this.suit = s;
        this.face = v;
    }


    @Override
    public int hashCode() {
        return Objects.hash(suit, face);
    }

    public Face getFace() {
        return face;
    }

    public int getValue() {
        return face.getValue();
    }

    public Integer getValueInteger() {
        return face.getValue();
    }

    public Suit getSuit() {
        return suit;
    }

    /**
     * Used to fuzz-test functions, i.e. {@link TexasHand#getKinds()} where a null value could be returned
     * @return a randomly generated card
     */
    public static Card getRandomCard() {
        Random rand = new Random();

        Suit s = Suit.values()[rand.nextInt(Suit.values().length)];
        Face v = Face.values()[rand.nextInt(Face.values().length)];

        return new Card(s, v);
    }



    @Override
    public int compareTo(Card card) {

        return getValueInteger().compareTo(card.getValueInteger());
    }

    public static Card strToCard(String s) {

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

        return new Card(suit, face);
    }

}
