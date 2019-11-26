package enums;

public enum Rank {
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

        @Override
        public String toString() {
                switch (value) {
                        case 1: return "High Card";
                        case 2: return "One Pair";
                        case 3: return "Two Pair";
                        case 4: return "Three of a Kind";
                        case 5: return "Straight";
                        case 6: return "Flush";
                        case 7: return "Full House";
                        case 8: return "Four of a Kind";
                        case 9: return "Straight Flsuh";
                        case 10: return "Royal Flush";

                        default:
                                return "Unknown";
                }
        }

        private final int value;

        Rank(int val) {
            this.value = val;
        }
}