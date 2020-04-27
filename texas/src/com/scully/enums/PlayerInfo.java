package com.scully.enums;

import com.scully.cards.Card;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    public int id = 0;
    public int chips = 0;

    public Card[] visibleCards;

    public PlayerInfo(int id, int chips) {
        this.id = id;
        this.chips = chips;
    }
}
