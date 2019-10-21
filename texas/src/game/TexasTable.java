package game;

import cards.Deck;
import cards.TexasHand;

public class TexasTable {

    Deck deck = Deck.getInstance();
    TexasHand tableCards;

    public TexasTable(Player... players) {

        tableCards = deck.pullHand();
        tableCards.IS_TABLE = true;


    }

}
