package org.terracottamc.world;

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
public enum Difficulty {

    PEACEFUL,
    EASY,
    NORMAL,
    HARD;

    /**
     * Retrieves this {@link org.terracottamc.world.Difficulty} by their identifier
     *
     * @param difficultyId which is used to retrieve this {@link org.terracottamc.world.Difficulty}
     *
     * @return a fresh {@link org.terracottamc.world.Difficulty}
     */
    public static Difficulty retrieveDifficultyById(final int difficultyId) {
        for (final Difficulty difficulty : Difficulty.values()) {
            if (difficulty.ordinal() == difficultyId) {
                return difficulty;
            }
        }
        return null;
    }
}