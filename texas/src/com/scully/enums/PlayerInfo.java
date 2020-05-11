package com.scully.enums;

import com.scully.cards.Card;

import java.io.Serializable;

/**
 * Data class transferred over the network to inform the player of their data
 */
public class PlayerInfo implements Serializable {
    /**
     * Players ID
     */
    public int id = 0;
    /**
     * Chip count
     */
    public int chips = 0;

    /**
     * What cards they can see
     */
    public Card[] visibleCards;

    /**
     * Current round
     */
    public Round round;

    /**
     * If they have folded
     */
    public boolean folded = false;

    public PlayerInfo(int id, int chips, Card[] cards, Round round) {
        this.id = id;
        this.chips = chips;
        this.round = round;
        this.visibleCards = cards;
    }
}
