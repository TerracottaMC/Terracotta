package org.terracottamc.world.chunk.palette;

/**
 * Copyright (c) 2021, TerracottaMC
 * All rights reserved.
 *
 * <p>
 * This project is licensed under the BSD 3-Clause License which
 * can be found in the root directory of this source tree
 *
 * @author Kaooot
 * @version 1.0
 */
public enum ChunkPaletteVersion {

    VERSION_1(1, 32),
    VERSION_2(2, 16),
    VERSION_3(3, 10, 2),
    VERSION_4(4, 8),
    VERSION_5(5, 6, 2),
    VERSION_6(6, 5, 2),
    VERSION_8(8, 4),
    VERSION_16(16, 2);

    private final byte paletteVersionId;
    private final byte amountOfWords;
    private final byte amountOfPadding;

    /**
     * Creates a new {@link ChunkPaletteVersion}
     *
     * @param paletteVersionId that stands for the identifier
     *                         of the {@link ChunkPaletteVersion}
     * @param amountOfWords    that represents the amount of words
     *                         this {@link ChunkPaletteVersion} has
     * @param amountOfPadding  the amount of padding of this {@link ChunkPaletteVersion}
     */
    ChunkPaletteVersion(final int paletteVersionId, final int amountOfWords, final int amountOfPadding) {
        this.paletteVersionId = (byte) paletteVersionId;
        this.amountOfWords = (byte) amountOfWords;
        this.amountOfPadding = (byte) amountOfPadding;
    }

    ChunkPaletteVersion(final int paletteVersionId, final int amountOfWords) {
        this(paletteVersionId, amountOfWords, (byte) 0);
    }

    /**
     * Retrieves the version identifier from this {@link ChunkPaletteVersion}
     *
     * @return a fresh version id
     */
    public byte getPaletteVersionId() {
        return this.paletteVersionId;
    }

    /**
     * Retrieves the amount of words from this {@link ChunkPaletteVersion}
     *
     * @return a fresh amount of words
     */
    public byte getAmountOfWords() {
        return this.amountOfWords;
    }

    /**
     * Retrieves the amount of padding from this {@link ChunkPaletteVersion}
     *
     * @return a fresh amount of padding
     */
    public byte getAmountOfPadding() {
        return this.amountOfPadding;
    }
}