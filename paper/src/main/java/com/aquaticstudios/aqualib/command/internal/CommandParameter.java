package com.aquaticstudios.aqualib.command.internal;

import com.aquaticstudios.aqualib.command.annotation.Optional;
import com.aquaticstudios.aqualib.command.annotation.Range;
import com.aquaticstudios.aqualib.command.annotation.Switch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;

public final class CommandParameter {

    private final String name;
    private final Class<?> type;
    private final boolean optional;
    private final String defaultValue;
    private final String switchFlag;
    private final boolean ranged;
    private final double min;
    private final double max;

    private CommandParameter(String name, Class<?> type, boolean optional, String defaultValue,
                             String switchFlag, boolean ranged, double min, double max) {
        this.name = name;
        this.type = type;
        this.optional = optional;
        this.defaultValue = defaultValue;
        this.switchFlag = switchFlag;
        this.ranged = ranged;
        this.min = min;
        this.max = max;
    }

    @NotNull
    public static CommandParameter of(@NotNull Parameter parameter) {
        Optional optional = parameter.getAnnotation(Optional.class);
        Switch switchAnnotation = parameter.getAnnotation(Switch.class);
        Range range = parameter.getAnnotation(Range.class);
        return new CommandParameter(
                parameter.getName().toLowerCase(),
                parameter.getType(),
                optional != null,
                optional == null ? "" : optional.value(),
                switchAnnotation == null ? null : switchAnnotation.value(),
                range != null,
                range == null ? Double.NEGATIVE_INFINITY : range.min(),
                range == null ? Double.POSITIVE_INFINITY : range.max()
        );
    }

    @NotNull
    public String name() {
        return name;
    }

    @NotNull
    public Class<?> type() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }

    @NotNull
    public String defaultValue() {
        return defaultValue;
    }

    public boolean hasDefault() {
        return !defaultValue.isEmpty();
    }

    public boolean isSwitch() {
        return switchFlag != null;
    }

    @Nullable
    public String switchFlag() {
        return switchFlag;
    }

    public boolean isRanged() {
        return ranged;
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }
}
