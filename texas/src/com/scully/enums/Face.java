package com.scully.enums;

/**
 * Represents the 'face' of the card, e.g. queen, king, two
 */
public enum Face {

    TWO   ("Two", 2),
    THREE ("Three", 3),
    FOUR  ("Four", 4),
    FIVE  ("Five", 5),
    SIX   ("Six", 6),
    SEVEN ("Seven", 7),
    EIGHT ("Eight", 8),
    NINE  ("Nine", 9),
    TEN   ("Ten", 10),
    JACK  ("Jack", 11),
    QUEEN ("Queen", 12),
    KING  ("King", 13),
    ACE   ("Ace", 14);

    /**
     * Integer value of the face
     */
    private final int val;

    /**
     * Name of the face
     */
    private final String str;

    Face(String display, int value) {
        this.str = display;
        this.val = value;
    }

    /** @return The integer value of the face */
    public int getValue() {
        return val;
    }

    @Override
    public String toString() {
        return str;
    }
}
