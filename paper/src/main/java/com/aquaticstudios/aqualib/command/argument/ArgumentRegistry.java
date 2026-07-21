package com.aquaticstudios.aqualib.command.argument;

import com.aquaticstudios.aqualib.command.argument.parser.BooleanParser;
import com.aquaticstudios.aqualib.command.argument.parser.DoubleParser;
import com.aquaticstudios.aqualib.command.argument.parser.IntegerParser;
import com.aquaticstudios.aqualib.command.argument.parser.LongParser;
import com.aquaticstudios.aqualib.command.argument.parser.OfflinePlayerParser;
import com.aquaticstudios.aqualib.command.argument.parser.PlayerParser;
import com.aquaticstudios.aqualib.command.argument.parser.StringParser;
import com.aquaticstudios.aqualib.command.argument.parser.WorldParser;
import com.aquaticstudios.aqualib.command.exception.CommandException;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ArgumentRegistry {

    private final Map<Class<?>, ArgumentParser<?>> parsers = new HashMap<>();

    public ArgumentRegistry() {
        registerDefaults();
    }

    public <T> void register(@NotNull Class<T> type, @NotNull ArgumentParser<T> parser) {
        parsers.put(type, parser);
    }

    public boolean has(@NotNull Class<?> type) {
        return parsers.containsKey(type);
    }

    @NotNull
    public Object parse(@NotNull Class<?> type, @NotNull CommandSender sender, @NotNull String input) throws CommandException {
        ArgumentParser<?> parser = parsers.get(type);
        if (parser == null) {
            throw new CommandException("&cUnsupported argument type: " + type.getSimpleName());
        }
        return parser.parse(sender, input);
    }

    private void registerDefaults() {
        StringParser string = new StringParser();
        IntegerParser integer = new IntegerParser();
        LongParser longParser = new LongParser();
        DoubleParser doubleParser = new DoubleParser();
        BooleanParser bool = new BooleanParser();

        register(String.class, string);
        register(int.class, integer);
        register(Integer.class, integer);
        register(long.class, longParser);
        register(Long.class, longParser);
        register(double.class, doubleParser);
        register(Double.class, doubleParser);
        register(boolean.class, bool);
        register(Boolean.class, bool);
        register(Player.class, new PlayerParser());
        register(OfflinePlayer.class, new OfflinePlayerParser());
        register(World.class, new WorldParser());
    }
}
