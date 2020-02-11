package game;

import cards.Deck;
import cards.TexasHand;

public class TexasTable {

    Deck deck = Deck.getInstance();
    public TexasHand tableCards;

    public TexasTable() {
        tableCards = new TexasHand();
        tableCards.IS_TABLE = true;
    }

    public void pullFlop() {
        tableCards.addCard(deck.pullCard());
        tableCards.addCard(deck.pullCard());
        tableCards.addCard(deck.pullCard());
    }

    public void pullTurn() {
        tableCards.addCard(deck.pullCard());
    }

    public void pullRiver() {
        tableCards.addCard(deck.pullCard());
    }




}
