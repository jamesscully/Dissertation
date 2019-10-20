package cards;

import java.util.Objects;
import java.util.Random;

public class Card {

    public final Suit suit;
    public final Value value;

    public Card(Suit s, Value v) {
        this.suit = s;
        this.value = v;
    }

    @Override
    public String toString() {
        // Ace of Spades
        // Ten of Hearts
        return "" + value.str + " of " + suit.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit &&
                value == card.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, value);
    }

    public int getValue() {
        // Texas Hold'em doesn't hold a value for suits - however some other gametypes might.
        // this will be where we could modify it's value based on suit
        return value.val;
    }

    /**
     * Used to fuzz-test functions, i.e. {@link Hand#getKinds()} where a null value could be returned
     * @return a randomly generated card
     */
    public static Card getRandomCard() {
        Random rand = new Random();

        Suit s = Suit.values()[rand.nextInt(Suit.values().length)];
        Value v = Value.values()[rand.nextInt(Value.values().length)];

        return new Card(s, v);
    }
}
