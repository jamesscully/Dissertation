package com.scully.enums;

/**
 * Represents the players intention sent over the network
 */
public enum TAction {
    CALL,
    RAISE,
    FOLD,
    QUIT;

    /**
     *  If we are raising the pot, we set this
     */
    public int value = -1;

    /**
     * Parses a string containing the players intent
     * @param s The string retrieved over the network
     * @return A TAction instance
     */
    public static TAction parseTAction(String s) {
        s = s.trim();
        s = s.toUpperCase();

        // split the message, i.e. RAISE 500 = (RAISE, 500)
        String[] sArr = s.split("\\s+");
        String keyWord = sArr[0];

        if(keyWord.equals("RAISE")) {
            TAction ret = null;

            if(sArr.length != 2)
                return null;

            // set the return type
            ret = TAction.RAISE;

            try {
                // set our raise value
                ret.value = Integer.parseInt(sArr[1]);
            } catch (NumberFormatException e) {
                System.err.println("TAction: error parsing - " + sArr[1]);
                return null;
            }

            // return with value set
            return ret;
        }

        switch (keyWord) {
            case "C":
            case "CALL": return CALL;
            case "F":
            case "FOLD": return FOLD;
            case "Q":
            case "QUIT": return QUIT;

            default: return null;
        }
    }


    @Override
    public String toString() {
        if(this.equals(TAction.RAISE)) {
            return String.format("RAISE by %d chips", this.value);
        }
        return super.toString();
    }
}
