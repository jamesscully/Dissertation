package cards;

import enums.Suit;
import enums.Face;

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
    public String toString() {
        // Ace of Spades
        // Ten of Hearts
        return "" + face.getName() + " of " + suit.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit &&
                face == card.face;
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
}
