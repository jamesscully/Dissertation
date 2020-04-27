package com.scully.enums;

import com.scully.cards.Card;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    public int id = 0;
    public int chips = 0;

    public Card[] visibleCards;
    public Round round;

    public boolean folded = false;

    public PlayerInfo(int id, int chips, Card[] cards, Round round) {
        this.id = id;
        this.chips = chips;
        this.round = round;
        this.visibleCards = cards;
    }
}
