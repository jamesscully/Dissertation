package com.scully.enums;

public enum Suit {
    CLUBS("Clubs"),
    HEARTS("Hearts"),
    SPADES("Spades"),
    DIAMONDS("Diamonds");

    public String name;
    public int id;

    Suit(String display) {
        this.name = display;

        switch (display) {
            case "Clubs":    this.id = 0; break;
            case "Hearts":   this.id = 1; break;
            case "Spades":   this.id = 2; break;
            case "Diamonds": this.id = 3; break;
        }
    }
}
