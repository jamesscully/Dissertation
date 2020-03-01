package com.scully.game;

import com.scully.cards.Card;
import com.scully.cards.Deck;

public class TexasTable {

    Deck deck = Deck.getInstance();

    Card first, second, third;
    Card turn;
    Card river;

    public TexasTable() {

    }

    public Card[] getCards() {
        return new Card[] { first, second, third, turn, river };
    }

    public void pullFlop() {
        first = deck.pullCard();
        second = deck.pullCard();
        third = deck.pullCard();
    }

    public Card[] getFlop() {
        return new Card[] {first, second, third};
    }

    public void pullTurn() {
        turn = deck.pullCard();
    }

    public Card getTurn() {
        return turn;
    }

    public void pullRiver() {
        river = deck.pullCard();
    }

    public Card getRiver() {
        return river;
    }
}
