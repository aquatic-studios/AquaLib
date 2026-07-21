package com.aquaticstudios.aqualib.nms.version;

import com.aquaticstudios.aqualib.platform.PlatformProvider;
import com.aquaticstudios.aqualib.platform.ServerVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum MinecraftVersion {

    v1_16_R3(16, 3),
    v1_17_R1(17, 1),
    v1_18_R2(18, 2),
    v1_19_R3(19, 3),
    v1_20_R4(20, 4),
    v1_21_R1(21, 1);

    private final int minor;
    private final int mappingRevision;

    MinecraftVersion(int minor, int mappingRevision) {
        this.minor = minor;
        this.mappingRevision = mappingRevision;
    }

    public int minor() {
        return minor;
    }

    public int mappingRevision() {
        return mappingRevision;
    }

    @Nullable
    public static MinecraftVersion current() {
        ServerVersion version = PlatformProvider.version();
        for (MinecraftVersion candidate : values()) {
            if (candidate.minor == version.minor()) {
                return candidate;
            }
        }
        return null;
    }

    @NotNull
    public String handlerClassName() {
        return "com.aquaticstudios.aqualib.nms." + name() + ".Handler";
    }
}
