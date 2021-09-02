package org.terracottamc.world;

import org.terracottamc.world.leveldb.LevelDBProvider;

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
public class World {

    private final String worldName;
    private final LevelDBProvider levelDBProvider;

    /**
     * Creates a new {@link org.terracottamc.world.World} with given name
     *
     * @param worldName which is used to create the {@link org.terracottamc.world.World}
     */
    public World(final String worldName) {
        this.worldName = worldName;
        this.levelDBProvider = new LevelDBProvider(worldName);
    }

    /**
     * Retrieves the {@link org.terracottamc.world.leveldb.LevelDBProvider} of this {@link org.terracottamc.world.World}
     *
     * @return a fresh {@link org.terracottamc.world.leveldb.LevelDBProvider}
     */
    public LevelDBProvider getLevelDBProvider() {
        return this.levelDBProvider;
    }

    /**
     * Retrieves the name of this {@link org.terracottamc.world.World}
     *
     * @return a fresh {@link java.lang.String}
     */
    public String getWorldName() {
        return this.worldName;
    }
}