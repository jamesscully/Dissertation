package card;

public enum Value {

    TWO   ("Two", 1),
    THREE ("Three", 2),
    FOUR  ("Four", 3),
    FIVE  ("Five", 4),
    SIX   ("Six", 5),
    SEVEN ("Seven", 6),
    EIGHT ("Eight", 7),
    NINE  ("Nine", 8),
    TEN   ("Ten", 9),
    JOKER ("Joker", 10),
    QUEEN ("Queen", 11),
    KING  ("King", 12),
    ACE   ("Ace", 13);

    public final int val;
    public final String str;

    private Value(String display, int value) {
        this.str = display;
        this.val = value;
    }
}
