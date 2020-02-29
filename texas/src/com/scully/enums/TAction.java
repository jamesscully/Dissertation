package com.scully.enums;

public enum TAction {
    CALL,
    RAISE,
    FOLD,
    QUIT;

    public int value = -1;

    public static TAction parseTAction(String s) {

        TAction ret = null;

        s = s.trim();
        s = s.toUpperCase();

        String[] sArr = s.split("\\s+");
        String keyWord = sArr[0];

        if(keyWord.equals("RAISE")) {
            if(sArr.length != 2)
                return null;

            ret = TAction.RAISE;

            try {
                ret.value = Integer.parseInt(sArr[1]);
            } catch (NumberFormatException e) {
                System.err.println("TAction: error parsing - " + sArr[1]);
                return null;
            }
            return ret;
        }

        switch (keyWord) {
            case "CALL": return CALL;
            case "FOLD": return FOLD;
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
