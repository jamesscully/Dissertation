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
                        case 1: return "High Card"; break;
                        case 2: return "One Pair"; break;
                        case 3: return "Two Pair"; break;
                        case 4: return "Three of a Kind"; break;
                        case 5: return "Straight"; break;
                        case 6: return "Flush"; break;
                        case 7: return "Full House"; break;
                        case 8: return "Four of a Kind"; break;
                        case 9: return "Straight Flsuh"; break;
                        case 10: return "Royal Flush"; break;
                }
        }

        private final int value;

        Rank(int val) {
            this.value = val;
        }
}