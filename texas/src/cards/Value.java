package cards;

public enum Value {

    TWO   ("Two", 0),
    THREE ("Three", 1),
    FOUR  ("Four", 2),
    FIVE  ("Five", 3),
    SIX   ("Six", 4),
    SEVEN ("Seven", 5),
    EIGHT ("Eight", 6),
    NINE  ("Nine", 7),
    TEN   ("Ten", 8),
    JACK  ("Jack", 9),
    QUEEN ("Queen", 10),
    KING  ("King", 11),
    ACE   ("Ace", 12);

    public final int val;
    public final String str;

    private Value(String display, int value) {
        this.str = display;
        this.val = value;
    }
}
