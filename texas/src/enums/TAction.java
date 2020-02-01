package enums;

public enum TAction {
    CALL,
    RAISE,
    FOLD;

    public int value = -1;

    public static TAction parseTAction(String s) {

        TAction ret = null;

        s = s.trim();

        String[] sArr = s.split("\\s+");
        String keyWord = sArr[0];

        if(keyWord.equals("RAISE")) {
            if(sArr[1] != null) {
                ret = TAction.RAISE;
                ret.value = Integer.parseInt(sArr[1]);
                return ret;
            }
            // we can't have a RAISE without a counter-offer!
            return null;
        }

        if(keyWord.equals("CALL"))
            ret = TAction.CALL;
        if(keyWord.equals("FOLD"))
            ret = TAction.FOLD;

        return ret;
    }


    @Override
    public String toString() {

        if(this.equals(TAction.RAISE)) {
            return String.format("RAISE by %d chips", this.value);
        }

        return super.toString();
    }
}
