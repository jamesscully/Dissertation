package enums;

public enum TexasResults {
        HIGH_CARD(1),
        PAIR(2),
        TWO_PAIR(3),
        THREE_OF_KIND(4),
        STRAIGHT(5),
        FLUSH(6),
        FULL_HOUSE(7),
        FOUR_OF_KIND(8),
        STRAIGHT_FLUSH(9),
        ROYAL_FLUSH(10);

        private final int value;

        TexasResults(int val) {
            this.value = val;
        }
}