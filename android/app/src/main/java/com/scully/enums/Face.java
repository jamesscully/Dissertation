package com.scully.enums;

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

    private final int val;
    private final String str;

    Face(String display, int value) {
        this.str = display;
        this.val = value;
    }

    public int getValue() {
        return val;
    }

    @Override
    public String toString() {
        return str;
    }
}
