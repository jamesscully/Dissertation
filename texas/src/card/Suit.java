package card;

public enum Suit {
    CLUBS("Clubs"),
    HEARTS("Hearts"),
    SPADES("Spades"),
    DIAMONDS("Diamonds");

    public String name;
    public int id;

    Suit(String display) {
        this.name = display;


        // assigning an id to each suit can assist in clearing them from the deck;
        // we would just need to perform the formula:
        // Position = ( (card.value * card.suit.id) + 13 % card.value ) - cardsRemoved

        switch (display) {
            case "Clubs":    this.id = 1; break;
            case "Hearts":   this.id = 2; break;
            case "Spades":   this.id = 3; break;
            case "Diamonds": this.id = 4; break;
        }
    }
}
