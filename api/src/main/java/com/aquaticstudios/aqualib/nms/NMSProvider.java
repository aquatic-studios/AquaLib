package com.aquaticstudios.aqualib.nms;

import com.aquaticstudios.aqualib.nms.version.MinecraftVersion;
import org.jetbrains.annotations.Nullable;

public final class NMSProvider {

    private static boolean resolved;
    private static NMSHandler handler;

    @Nullable
    public static NMSHandler handler() {
        if (!resolved) {
            handler = resolve();
            resolved = true;
        }
        return handler;
    }

    public static boolean isSupported() {
        return handler() != null;
    }

    @Nullable
    private static NMSHandler resolve() {
        MinecraftVersion version = MinecraftVersion.current();
        if (version == null) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(version.handlerClassName());
            return (NMSHandler) clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException exception) {
            return null;
        }
    }
}
