package com.aquaticstudios.aqualib.config.exception;

import org.jetbrains.annotations.NotNull;

public class ConfigLoadException extends RuntimeException {

    public ConfigLoadException(@NotNull String message) {
        super(message, null, false, false);
    }
}
