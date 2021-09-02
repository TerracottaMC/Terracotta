package org.terracottamc.math;

import org.terracottamc.world.Dimension;
import org.terracottamc.world.World;

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
public class Location extends Vector {

    private World world;
    private float yaw;
    private float pitch;

    /**
     * Creates a new {@link org.terracottamc.math.Location} with x, y, z values and yaw and pitch for entities and
     * a {@link org.terracottamc.world.Dimension} that should be given when the {@link org.terracottamc.math.Location}
     * is not located in {@link org.terracottamc.world.Dimension#WORLD}
     *
     * @param world     which represents the {@link org.terracottamc.world.World}
     *                  where this {@link org.terracottamc.math.Location} is in
     * @param x         that represents the value on the x-axis
     * @param y         that represents the value on the y-axis
     * @param z         that represents the value on the z-axis
     * @param yaw       that represents the yaw value which can be very useful on entities
     * @param pitch     that represents the pitch value which can be very useful on entities
     * @param dimension which should be given when this {@link org.terracottamc.math.Location}
     *                  is not in {@link org.terracottamc.world.Dimension#WORLD}
     */
    public Location(final World world, final float x, final float y, final float z, final float yaw, final float pitch, final Dimension dimension) {
        super(x, y, z, dimension);

        this.world = world;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Creates a new {@link org.terracottamc.math.Location} with x, y, z values and yaw and pitch for entities
     *
     * @param world which represents the {@link org.terracottamc.world.World}
     *              where this {@link org.terracottamc.math.Location} is in
     * @param x     that represents the value on the x-axis
     * @param y     that represents the value on the y-axis
     * @param z     that represents the value on the z-axis
     * @param yaw   that represents the yaw value which can be very useful on entities
     * @param pitch that represents the pitch value which can be very useful on entities
     */
    public Location(final World world, final float x, final float y, final float z, final float yaw, final float pitch) {
        this(world, x, y, z, yaw, pitch, Dimension.WORLD);
    }

    /**
     * Creates a new {@link org.terracottamc.math.Location} with x, y, z values
     *
     * @param world which represents the {@link org.terracottamc.world.World}
     *              where this {@link org.terracottamc.math.Location} is in
     * @param x     that represents the value on the x-axis
     * @param y     that represents the value on the y-axis
     * @param z     that represents the value on the z-axis
     */
    public Location(final World world, final float x, final float y, final float z) {
        this(world, x, y, z, 0f, 0f, Dimension.WORLD);
    }

    /**
     * Creates a new {@link org.terracottamc.math.Location} with a {@link org.terracottamc.math.Vector}
     * and yaw and pitch for entities. The location also uses the {@link org.terracottamc.world.Dimension} of the
     * given {@link org.terracottamc.math.Vector}
     *
     * @param world  which represents the {@link org.terracottamc.world.World}
     *               where this {@link org.terracottamc.math.Location} is in
     * @param vector that holds the x-axis value, y-axis value, z-axis value
     *               and the {@link org.terracottamc.world.Dimension}
     * @param yaw    that represents the yaw value which can be very useful on entities
     * @param pitch  that represents the pitch value which can be very useful on entities
     */
    public Location(final World world, final Vector vector, final float yaw, final float pitch) {
        this(world, vector.getX(), vector.getY(), vector.getZ(), yaw, pitch, vector.getDimension());
    }

    /**
     * Creates a new {@link org.terracottamc.math.Location} with a {@link org.terracottamc.math.Vector}.
     * The location also uses the {@link org.terracottamc.world.Dimension} of the
     * given {@link org.terracottamc.math.Vector}
     *
     * @param world  which represents the {@link org.terracottamc.world.World}
     *               where this {@link org.terracottamc.math.Location} is in
     * @param vector that holds the x-axis value, y-axis value, z-axis value
     *               and the {@link org.terracottamc.world.Dimension}
     */
    public Location(final World world, final Vector vector) {
        this(world, vector.getX(), vector.getY(), vector.getZ(), 0f, 0f, vector.getDimension());
    }

    /**
     * Retrieves the yaw value of this {@link org.terracottamc.math.Location}
     *
     * @return a fresh yaw value
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * Retrieves the pitch value of this {@link org.terracottamc.math.Location}
     *
     * @return a fresh pitch value
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Retrieves the {@link org.terracottamc.world.World} of this {@link org.terracottamc.math.Location}
     *
     * @return a fresh {@link org.terracottamc.world.World}
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Updates the yaw value of this {@link org.terracottamc.math.Location}
     *
     * @param yaw which represents the updated value
     */
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    /**
     * Updates the pitch value of this {@link org.terracottamc.math.Location}
     *
     * @param pitch which represents the updated value
     */
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    /**
     * Updates the {@link org.terracottamc.world.World} of this {@link org.terracottamc.math.Location}
     *
     * @param world a fresh new {@link org.terracottamc.world.World}
     */
    public void setWorld(final World world) {
        this.world = world;
    }
}