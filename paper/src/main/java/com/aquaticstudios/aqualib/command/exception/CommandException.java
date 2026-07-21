package com.aquaticstudios.aqualib.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandException extends RuntimeException {

    public CommandException(@NotNull String message) {
        super(message);
    }
}
