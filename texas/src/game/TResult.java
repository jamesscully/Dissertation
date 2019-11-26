package game;

import cards.Card;
import enums.Rank;

public class TResult {

    public Card highest;
    public Rank rank;

    public TResult(Card highest, Rank rank) {
        this.highest = highest;
        this.rank = rank;
    }

}
