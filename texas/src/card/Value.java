package card;

public enum Value {

    TWO   ("Two", 2),
    THREE ("Three", 3),
    FOUR  ("Four", 4),
    FIVE  ("Five", 5),
    SIX   ("Six", 6),
    SEVEN ("Seven", 7),
    EIGHT ("Eight", 8),
    NINE  ("Nine", 9),
    TEN   ("Ten", 10),
    JOKER ("Joker", 11),
    QUEEN ("Queen", 12),
    KING  ("King", 13),
    ACE   ("Ace", 14);

    public final int val;
    public final String str;

    private Value(String display, int value) {
        this.str = display;
        this.val = value;
    }
}
