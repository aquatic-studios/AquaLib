package com.aquaticstudios.aqualib.velocity;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class CC {

    private final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .build();

    @NotNull
    public Component set(@NotNull String input) {
        return LEGACY.deserialize(input);
    }
}
