package com.aquaticstudios.aqualib.config;

import com.aquaticstudios.aqualib.config.exception.ConfigLoadException;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.Block;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class Config {

    private final File file;
    private final YamlDocument document;

    public Config(@NotNull File file, @Nullable InputStream resource,
                  @NotNull GeneralSettings general, @NotNull LoaderSettings loader,
                  @NotNull DumperSettings dumper, @NotNull UpdaterSettings updater) {
        this.file = file;
        try {
            this.document = YamlDocument.create(file, resource, general, loader, dumper, updater);
        } catch (Exception exception) {
            throw new ConfigLoadException(YamlErrors.describe(file.getName(), exception));
        }
    }

    @NotNull
    public YamlDocument document() {
        return document;
    }

    @NotNull
    public File file() {
        return file;
    }

    @Nullable
    public Object get(@NotNull String path) {
        return document.get(path);
    }

    public Object get(@NotNull String path, @Nullable Object def) {
        return document.get(path, def);
    }

    @NotNull
    public Optional<Object> getOptional(@NotNull String path) {
        return document.getOptional(path);
    }

    @Nullable
    public <T> T getAs(@NotNull String path, @NotNull Class<T> type) {
        return document.getAs(path, type);
    }

    public <T> T getAs(@NotNull String path, @NotNull Class<T> type, @Nullable T def) {
        return document.getAs(path, type, def);
    }

    @NotNull
    public <T> Optional<T> getAsOptional(@NotNull String path, @NotNull Class<T> type) {
        return document.getAsOptional(path, type);
    }

    public <T> boolean is(@NotNull String path, @NotNull Class<T> type) {
        return document.is(path, type);
    }

    @Nullable
    public String getString(@NotNull String path) {
        return document.getString(path);
    }

    public String getString(@NotNull String path, @Nullable String def) {
        return document.getString(path, def);
    }

    @NotNull
    public Optional<String> getOptionalString(@NotNull String path) {
        return document.getOptionalString(path);
    }

    @Nullable
    public Integer getInt(@NotNull String path) {
        return document.getInt(path);
    }

    public Integer getInt(@NotNull String path, @Nullable Integer def) {
        return document.getInt(path, def);
    }

    @NotNull
    public Optional<Integer> getOptionalInt(@NotNull String path) {
        return document.getOptionalInt(path);
    }

    @Nullable
    public Long getLong(@NotNull String path) {
        return document.getLong(path);
    }

    public Long getLong(@NotNull String path, @Nullable Long def) {
        return document.getLong(path, def);
    }

    @NotNull
    public Optional<Long> getOptionalLong(@NotNull String path) {
        return document.getOptionalLong(path);
    }

    @Nullable
    public Short getShort(@NotNull String path) {
        return document.getShort(path);
    }

    public Short getShort(@NotNull String path, @Nullable Short def) {
        return document.getShort(path, def);
    }

    @NotNull
    public Optional<Short> getOptionalShort(@NotNull String path) {
        return document.getOptionalShort(path);
    }

    @Nullable
    public Byte getByte(@NotNull String path) {
        return document.getByte(path);
    }

    public Byte getByte(@NotNull String path, @Nullable Byte def) {
        return document.getByte(path, def);
    }

    @NotNull
    public Optional<Byte> getOptionalByte(@NotNull String path) {
        return document.getOptionalByte(path);
    }

    @Nullable
    public Float getFloat(@NotNull String path) {
        return document.getFloat(path);
    }

    public Float getFloat(@NotNull String path, @Nullable Float def) {
        return document.getFloat(path, def);
    }

    @NotNull
    public Optional<Float> getOptionalFloat(@NotNull String path) {
        return document.getOptionalFloat(path);
    }

    @Nullable
    public Double getDouble(@NotNull String path) {
        return document.getDouble(path);
    }

    public Double getDouble(@NotNull String path, @Nullable Double def) {
        return document.getDouble(path, def);
    }

    @NotNull
    public Optional<Double> getOptionalDouble(@NotNull String path) {
        return document.getOptionalDouble(path);
    }

    @Nullable
    public Boolean getBoolean(@NotNull String path) {
        return document.getBoolean(path);
    }

    public Boolean getBoolean(@NotNull String path, @Nullable Boolean def) {
        return document.getBoolean(path, def);
    }

    @NotNull
    public Optional<Boolean> getOptionalBoolean(@NotNull String path) {
        return document.getOptionalBoolean(path);
    }

    @Nullable
    public Character getChar(@NotNull String path) {
        return document.getChar(path);
    }

    public Character getChar(@NotNull String path, @Nullable Character def) {
        return document.getChar(path, def);
    }

    @NotNull
    public Optional<Character> getOptionalChar(@NotNull String path) {
        return document.getOptionalChar(path);
    }

    @Nullable
    public Number getNumber(@NotNull String path) {
        return document.getNumber(path);
    }

    public Number getNumber(@NotNull String path, @Nullable Number def) {
        return document.getNumber(path, def);
    }

    @NotNull
    public Optional<Number> getOptionalNumber(@NotNull String path) {
        return document.getOptionalNumber(path);
    }

    @Nullable
    public BigInteger getBigInt(@NotNull String path) {
        return document.getBigInt(path);
    }

    public BigInteger getBigInt(@NotNull String path, @Nullable BigInteger def) {
        return document.getBigInt(path, def);
    }

    @NotNull
    public Optional<BigInteger> getOptionalBigInt(@NotNull String path) {
        return document.getOptionalBigInt(path);
    }

    @Nullable
    public <T extends Enum<T>> T getEnum(@NotNull String path, @NotNull Class<T> type) {
        return document.getEnum(path, type);
    }

    public <T extends Enum<T>> T getEnum(@NotNull String path, @NotNull Class<T> type, @Nullable T def) {
        return document.getEnum(path, type, def);
    }

    @NotNull
    public <T extends Enum<T>> Optional<T> getOptionalEnum(@NotNull String path, @NotNull Class<T> type) {
        return document.getOptionalEnum(path, type);
    }

    @Nullable
    public List<?> getList(@NotNull String path) {
        return document.getList(path);
    }

    public List<?> getList(@NotNull String path, @Nullable List<?> def) {
        return document.getList(path, def);
    }

    @NotNull
    public Optional<List<?>> getOptionalList(@NotNull String path) {
        return document.getOptionalList(path);
    }

    @Nullable
    public List<String> getStringList(@NotNull String path) {
        return document.getStringList(path);
    }

    public List<String> getStringList(@NotNull String path, @Nullable List<String> def) {
        return document.getStringList(path, def);
    }

    @NotNull
    public Optional<List<String>> getOptionalStringList(@NotNull String path) {
        return document.getOptionalStringList(path);
    }

    @Nullable
    public List<Integer> getIntList(@NotNull String path) {
        return document.getIntList(path);
    }

    public List<Integer> getIntList(@NotNull String path, @Nullable List<Integer> def) {
        return document.getIntList(path, def);
    }

    @NotNull
    public Optional<List<Integer>> getOptionalIntList(@NotNull String path) {
        return document.getOptionalIntList(path);
    }

    @Nullable
    public List<Long> getLongList(@NotNull String path) {
        return document.getLongList(path);
    }

    public List<Long> getLongList(@NotNull String path, @Nullable List<Long> def) {
        return document.getLongList(path, def);
    }

    @NotNull
    public Optional<List<Long>> getOptionalLongList(@NotNull String path) {
        return document.getOptionalLongList(path);
    }

    @Nullable
    public List<Short> getShortList(@NotNull String path) {
        return document.getShortList(path);
    }

    public List<Short> getShortList(@NotNull String path, @Nullable List<Short> def) {
        return document.getShortList(path, def);
    }

    @NotNull
    public Optional<List<Short>> getOptionalShortList(@NotNull String path) {
        return document.getOptionalShortList(path);
    }

    @Nullable
    public List<Byte> getByteList(@NotNull String path) {
        return document.getByteList(path);
    }

    public List<Byte> getByteList(@NotNull String path, @Nullable List<Byte> def) {
        return document.getByteList(path, def);
    }

    @NotNull
    public Optional<List<Byte>> getOptionalByteList(@NotNull String path) {
        return document.getOptionalByteList(path);
    }

    @Nullable
    public List<Float> getFloatList(@NotNull String path) {
        return document.getFloatList(path);
    }

    public List<Float> getFloatList(@NotNull String path, @Nullable List<Float> def) {
        return document.getFloatList(path, def);
    }

    @NotNull
    public Optional<List<Float>> getOptionalFloatList(@NotNull String path) {
        return document.getOptionalFloatList(path);
    }

    @Nullable
    public List<Double> getDoubleList(@NotNull String path) {
        return document.getDoubleList(path);
    }

    public List<Double> getDoubleList(@NotNull String path, @Nullable List<Double> def) {
        return document.getDoubleList(path, def);
    }

    @NotNull
    public Optional<List<Double>> getOptionalDoubleList(@NotNull String path) {
        return document.getOptionalDoubleList(path);
    }

    @Nullable
    public List<BigInteger> getBigIntList(@NotNull String path) {
        return document.getBigIntList(path);
    }

    public List<BigInteger> getBigIntList(@NotNull String path, @Nullable List<BigInteger> def) {
        return document.getBigIntList(path, def);
    }

    @NotNull
    public Optional<List<BigInteger>> getOptionalBigIntList(@NotNull String path) {
        return document.getOptionalBigIntList(path);
    }

    @Nullable
    public List<Map<?, ?>> getMapList(@NotNull String path) {
        return document.getMapList(path);
    }

    public List<Map<?, ?>> getMapList(@NotNull String path, @Nullable List<Map<?, ?>> def) {
        return document.getMapList(path, def);
    }

    @NotNull
    public Optional<List<Map<?, ?>>> getOptionalMapList(@NotNull String path) {
        return document.getOptionalMapList(path);
    }

    @Nullable
    public Section getSection(@NotNull String path) {
        return document.getSection(path);
    }

    public Section getSection(@NotNull String path, @Nullable Section def) {
        return document.getSection(path, def);
    }

    @NotNull
    public Optional<Section> getOptionalSection(@NotNull String path) {
        return document.getOptionalSection(path);
    }

    @NotNull
    public Section createSection(@NotNull String path) {
        return document.createSection(path);
    }

    @Nullable
    public Block<?> getBlock(@NotNull String path) {
        return document.getBlock(path);
    }

    @NotNull
    public Optional<Block<?>> getOptionalBlock(@NotNull String path) {
        return document.getOptionalBlock(path);
    }

    @NotNull
    public Optional<Section> getParent(@NotNull String path) {
        return document.getParent(path);
    }

    @NotNull
    public Set<String> getKeys(boolean deep) {
        return document.getRoutesAsStrings(deep);
    }

    public boolean contains(@NotNull String path) {
        return document.contains(path);
    }

    public boolean isSection(@NotNull String path) {
        return document.isSection(path);
    }

    public boolean isList(@NotNull String path) {
        return document.isList(path);
    }

    public boolean isString(@NotNull String path) {
        return document.isString(path);
    }

    public boolean isInt(@NotNull String path) {
        return document.isInt(path);
    }

    public boolean isLong(@NotNull String path) {
        return document.isLong(path);
    }

    public boolean isShort(@NotNull String path) {
        return document.isShort(path);
    }

    public boolean isByte(@NotNull String path) {
        return document.isByte(path);
    }

    public boolean isFloat(@NotNull String path) {
        return document.isFloat(path);
    }

    public boolean isDouble(@NotNull String path) {
        return document.isDouble(path);
    }

    public boolean isBoolean(@NotNull String path) {
        return document.isBoolean(path);
    }

    public boolean isChar(@NotNull String path) {
        return document.isChar(path);
    }

    public boolean isNumber(@NotNull String path) {
        return document.isNumber(path);
    }

    public boolean isDecimal(@NotNull String path) {
        return document.isDecimal(path);
    }

    public boolean isBigInt(@NotNull String path) {
        return document.isBigInt(path);
    }

    public <T extends Enum<T>> boolean isEnum(@NotNull String path, @NotNull Class<T> type) {
        return document.isEnum(path, type);
    }

    public void set(@NotNull String path, @Nullable Object value) {
        document.set(path, value);
    }

    public boolean remove(@NotNull String path) {
        return document.remove(path);
    }

    public boolean reload() {
        try {
            document.reload();
            return true;
        } catch (Exception exception) {
            throw new ConfigLoadException(YamlErrors.describe(file.getName(), exception));
        }
    }

    public boolean save() {
        try {
            document.save();
            return true;
        } catch (IOException exception) {
            throw new ConfigLoadException(file.getName() + ": could not be saved - " + exception.getMessage());
        }
    }
}
