package com.scully.enums;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    public int id = 0;
    public int chips = 0;

    public PlayerInfo(int id, int chips) {
        this.id = id;
        this.chips = chips;
    }
}
