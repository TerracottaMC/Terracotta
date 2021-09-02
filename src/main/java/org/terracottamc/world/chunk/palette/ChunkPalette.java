package org.terracottamc.world.chunk.palette;

import org.terracottamc.util.BinaryStream;

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
public class ChunkPalette {

    private final BinaryStream chunkBinaryStream;
    private final short[] indices = new short[4096];

    private ChunkPaletteVersion chunkPaletteVersion;
    private int bits = 0;
    private int writtenWords = 0;

    /**
     * Creates a new {@link org.terracottamc.world.chunk.palette.ChunkPalette} which is used to write the chunks
     *
     * @param chunkBinaryStream which is the stream that is used to write the chunks
     * @param paletteVersion    which represents the version identifier
     *                          of this {@link org.terracottamc.world.chunk.palette.ChunkPalette}
     * @param read              whether the chunks are being read from the
     *                          directory of its {@link org.terracottamc.world.World}
     */
    public ChunkPalette(final BinaryStream chunkBinaryStream, final int paletteVersion, final boolean read) {
        this.chunkBinaryStream = chunkBinaryStream;

        for (final ChunkPaletteVersion chunkPaletteVersion : ChunkPaletteVersion.values()) {
            if ((!read && chunkPaletteVersion.getAmountOfWords() <= paletteVersion &&
                    chunkPaletteVersion.getAmountOfPadding() == 0) || read &&
                    chunkPaletteVersion.getPaletteVersionId() == paletteVersion) {
                this.chunkPaletteVersion = chunkPaletteVersion;

                break;
            }
        }
    }

    /**
     * Adds the identifiers of the indices to this {@link org.terracottamc.world.chunk.palette.ChunkPalette}
     *
     * @param indexIdentifiers that should be added to the {@link org.terracottamc.world.chunk.palette.ChunkPalette}
     */
    public void addIndexIdentifiers(final int[] indexIdentifiers) {
        final int versionId = this.chunkPaletteVersion.getPaletteVersionId();

        int leftShiftValue;

        switch (versionId) {
            case 1:
            case 2:
            case 4:
                leftShiftValue = (versionId / 2);
                break;
            case 8:
                leftShiftValue = 3;
                break;
            case 16:
                leftShiftValue = 4;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + versionId);
        }

        for (final int identifier : indexIdentifiers) {
            if (this.writtenWords == this.chunkPaletteVersion.getAmountOfWords()) {
                this.finish();
                this.writtenWords = 0;
            }

            this.bits |= identifier << (this.writtenWords << leftShiftValue);

            this.writtenWords++;
        }
    }

    /**
     * Retrieves the indices from this {@link org.terracottamc.world.chunk.palette.ChunkPalette} by reading the data
     *
     * @return a fresh read indices output data
     */
    public short[] getIndices() {
        final int amountOfWords = this.chunkPaletteVersion.getAmountOfWords();
        final int amountOfIterations = (int) Math.ceil(4096 / (float) amountOfWords);

        for (int i = 0; i < amountOfIterations; i++) {
            int currentData = this.chunkBinaryStream.readIntLE();
            int index = 0;

            for (byte wordsAmount = 0; wordsAmount < amountOfWords; wordsAmount++) {
                short indexValue = 0;
                int innerShiftIndex = 0;

                for (int j = 0; j < this.chunkPaletteVersion.getPaletteVersionId(); j++) {
                    if ((currentData & (1 << index++)) != 0) {
                        indexValue ^= 1 << innerShiftIndex;
                    }

                    innerShiftIndex++;
                }

                int updatedIndex = (i * this.chunkPaletteVersion.getAmountOfWords()) + wordsAmount;

                if (updatedIndex < 4096) {
                    this.indices[updatedIndex] = indexValue;
                }
            }
        }
        return this.indices;
    }

    /**
     * Writes the bits and sets them to 0
     */
    public void finish() {
        this.chunkBinaryStream.writeIntLE(this.bits);

        this.bits = 0;
    }

    /**
     * Returns the {@link org.terracottamc.util.BinaryStream} that is used for reading and writing
     * by this {@link org.terracottamc.world.chunk.palette.ChunkPalette}
     *
     * @return a fresh {@link org.terracottamc.util.BinaryStream}
     */
    public BinaryStream getChunkBinaryStream() {
        return this.chunkBinaryStream;
    }

    /**
     * Returns the {@link org.terracottamc.world.chunk.palette.ChunkPaletteVersion}
     * of this {@link org.terracottamc.world.chunk.palette.ChunkPalette}
     *
     * @return a fresh {@link org.terracottamc.world.chunk.palette.ChunkPaletteVersion}
     */
    public ChunkPaletteVersion getChunkPaletteVersion() {
        return this.chunkPaletteVersion;
    }
}