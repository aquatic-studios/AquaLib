package com.aquaticstudios.aqualib.platform;

public enum Platform {

    SPIGOT,

    PAPER,

    FOLIA;

    public boolean isPaper() {
        return ordinal() >= PAPER.ordinal();
    }

    public boolean isFolia() {
        return this == FOLIA;
    }
}
