package com.aquaticstudios.aqualib.message;

import org.jetbrains.annotations.NotNull;

public final class Placeholder {

    private final String token;
    private final String value;

    private Placeholder(@NotNull String key, @NotNull String value) {
        this.token = "%" + key + "%";
        this.value = value;
    }

    @NotNull
    public static Placeholder of(@NotNull String key, @NotNull Object value) {
        return new Placeholder(key, String.valueOf(value));
    }

    @NotNull
    public String apply(@NotNull String input) {
        return input.replace(token, value);
    }
}
