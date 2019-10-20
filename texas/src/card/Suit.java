package card;

public enum Suit {
    CLUBS("Clubs"),
    HEARTS("Hearts"),
    SPADES("Spades"),
    DIAMONDS("Diamonds");

    public String name;

    Suit(String display) {
        this.name = display;
    }
}
