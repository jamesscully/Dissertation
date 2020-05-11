package com.scully.enums;

public enum  Round {
    PREFLOP,
    FLOP,
    TURN,
    RIVER,
    RESULT,
    ERROR;

    public static Round nextRound(Round round) {
        switch (round) {
            case PREFLOP: return FLOP;
            case FLOP:    return TURN;
            case TURN:    return RIVER;
            case RIVER:   return RESULT;

            default:
                System.err.println("Round.java: Attempted to get next round on the final round");
                System.exit(1);
        }
        return ERROR;
    }
}
