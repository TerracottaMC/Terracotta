package org.terracottamc.world.leveldb;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.terracottamc.math.Location;
import org.terracottamc.server.Server;
import org.terracottamc.taglib.NBTBuilder;
import org.terracottamc.taglib.nbt.io.NBTReader;
import org.terracottamc.taglib.nbt.tag.NBTTagCompound;
import org.terracottamc.world.Difficulty;
import org.terracottamc.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

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
public class LevelDBProvider {

    protected Location worldSpawnLocation;
    protected Difficulty worldDifficulty;

    private final File worldFolder;
    private final File worldFile;

    private DB database;

    /**
     * Creates a new {@link org.terracottamc.world.leveldb.LevelDBProvider}
     *
     * @param worldName which should be bound to this {@link org.terracottamc.world.leveldb.LevelDBProvider}
     */
    public LevelDBProvider(final String worldName) {
        this.worldFolder = new File(System.getProperty("user.dir") + "/worlds/" + worldName);

        if (!this.worldFolder.exists()) {
            this.worldFolder.mkdirs();
        }

        this.worldFile = new File(this.worldFolder.getPath(), "level.dat");
    }

    /**
     * Loads the world file by this {@link org.terracottamc.world.leveldb.LevelDBProvider}
     *
     * @return whether the world file could be loaded successfully or not
     */
    public boolean loadWorldFile() {
        try (final FileInputStream fileInputStream = new FileInputStream(this.worldFile)) {
            fileInputStream.skip(8);

            final byte[] data = new byte[fileInputStream.available()];

            fileInputStream.read(data);

            final ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(data.length);
            buffer.writeBytes(data);

            final NBTReader nbtReader = new NBTBuilder()
                    .withIOBuffer(buffer)
                    .withByteOrder(ByteOrder.LITTLE_ENDIAN)
                    .buildReader();

            final NBTTagCompound nbtTagCompound = nbtReader.createCompound();
            final String worldName = this.worldFile.getName();
            final int spawnX = nbtTagCompound.getInt("SpawnX");
            final int spawnY = nbtTagCompound.getInt("SpawnY");
            final int spawnZ = nbtTagCompound.getInt("SpawnZ");
            final int difficultyId = nbtTagCompound.getInt("Difficulty");

            final World world = Server.getInstance().getWorld(worldName);

            this.worldSpawnLocation = new Location(world, spawnX, spawnY, spawnZ);
            this.worldDifficulty = Difficulty.retrieveDifficultyById(difficultyId);

            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Initializes the {@link org.iq80.leveldb.DB} of this {@link org.terracottamc.world.leveldb.LevelDBProvider}
     *
     * @return whether the database could be initialized successfully
     */
    public boolean initializeDataBase() {
        try {
            this.database = Iq80DBFactory.factory
                    .open(new File(this.worldFolder.getPath(), "db/"), new Options().createIfMissing(true));

            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves data from the database of this {@link org.terracottamc.world.leveldb.LevelDBProvider} with provided data
     *
     * @param data which is used to retrieve the data
     *
     * @return fresh retrieved data
     */
    public byte[] retrieveDataFromDatabase(final byte[] data) {
        return this.database.get(data);
    }
}