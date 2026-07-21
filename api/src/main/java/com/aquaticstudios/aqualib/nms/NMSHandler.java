package com.aquaticstudios.aqualib.nms;

import com.aquaticstudios.aqualib.nms.version.MinecraftVersion;
import org.jetbrains.annotations.NotNull;

public interface NMSHandler {

    @NotNull
    MinecraftVersion version();
}
