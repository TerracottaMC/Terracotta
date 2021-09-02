package org.terracottamc.math;

import org.apache.commons.math3.util.FastMath;
import org.terracottamc.world.Dimension;

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
public class Vector {

    private float x;
    private float y;
    private float z;
    private Dimension dimension;

    /**
     * Creates a new {@link org.terracottamc.math.Vector}
     *
     * @param x         which represents the value on the x-axis
     * @param y         which represents the value on the y-axis
     * @param z         which represents the value on the z-axis
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *                  of this {@link org.terracottamc.math.Vector}
     */
    public Vector(final float x, final float y, final float z, final Dimension dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    /**
     * Creates a new {@link org.terracottamc.math.Vector} specified for blocks
     *
     * @param x         which represents the value on the x-axis
     * @param y         which represents the value on the y-axis
     * @param z         which represents the value on the z-axis
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *                  of this {@link org.terracottamc.math.Vector}
     */
    public Vector(final int x, final int y, final int z, final Dimension dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    /**
     * Creates a new {@link org.terracottamc.math.Vector}
     *
     * @param x which represents the value on the x-axis
     * @param y which represents the value on the y-axis
     * @param z which represents the value on the z-axis
     */
    public Vector(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = Dimension.WORLD;
    }

    /**
     * Creates a new {@link org.terracottamc.math.Vector} specified for blocks
     *
     * @param x which represents the value on the x-axis
     * @param y which represents the value on the y-axis
     * @param z which represents the value on the z-axis
     */
    public Vector(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = Dimension.WORLD;
    }

    /**
     * Retrieves the upper {@link org.terracottamc.math.Vector}
     *
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh {@link org.terracottamc.math.Vector}
     */
    public static Vector up(final Dimension dimension) {
        return new Vector(0, 1, 0, dimension);
    }

    /**
     * Retrieves the lower {@link org.terracottamc.math.Vector}
     *
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh {@link org.terracottamc.math.Vector}
     */
    public static Vector down(final Dimension dimension) {
        return new Vector(0, -1, 0, dimension);
    }

    /**
     * Retrieves the north direction {@link org.terracottamc.math.Vector}
     *
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh {@link org.terracottamc.math.Vector}
     */
    public static Vector north(final Dimension dimension) {
        return new Vector(0, 0, -1, dimension);
    }

    /**
     * Retrieves the east direction {@link org.terracottamc.math.Vector}
     *
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh {@link org.terracottamc.math.Vector}
     */
    public static Vector east(final Dimension dimension) {
        return new Vector(1, 0, 0, dimension);
    }

    /**
     * Retrieves the south direction {@link org.terracottamc.math.Vector}
     *
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh {@link org.terracottamc.math.Vector}
     */
    public static Vector south(final Dimension dimension) {
        return new Vector(0, 0, 1, dimension);
    }

    /**
     * Retrieves the west direction {@link org.terracottamc.math.Vector}
     *
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh {@link org.terracottamc.math.Vector}
     */
    public static Vector west(final Dimension dimension) {
        return new Vector(-1, 0, 0, dimension);
    }

    /**
     * Adds new coordinates to this {@link org.terracottamc.math.Vector}
     *
     * @param x         coordinate that should be added
     * @param y         coordinate that should be added
     * @param z         coordinate that should be added
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh calculated {@link org.terracottamc.math.Vector}
     */
    public Vector add(final float x, final float y, final float z, final Dimension dimension) {
        return new Vector(this.x + x, this.y + y, this.z + z, dimension);
    }

    /**
     * Subtracts new coordinates from this {@link org.terracottamc.math.Vector}
     *
     * @param x         coordinate that should be subtracted
     * @param y         coordinate that should be subtracted
     * @param z         coordinate that should be subtracted
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh calculated {@link org.terracottamc.math.Vector}
     */
    public Vector subtract(final float x, final float y, final float z, final Dimension dimension) {
        return new Vector(this.x - x, this.y - y, this.z - z, dimension);
    }

    /**
     * Multiplies new coordinates to this {@link org.terracottamc.math.Vector}
     *
     * @param x         coordinate that should be multiplied
     * @param y         coordinate that should be multiplied
     * @param z         coordinate that should be multiplied
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh calculated {@link org.terracottamc.math.Vector}
     */
    public Vector multiply(final float x, final float y, final float z, final Dimension dimension) {
        return new Vector(this.x * x, this.y * y, this.z * z, dimension);
    }

    /**
     * Divides new coordinates of this {@link org.terracottamc.math.Vector}
     *
     * @param x         coordinate that should be divided
     * @param y         coordinate that should be divided
     * @param z         coordinate that should be divided
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh calculated {@link org.terracottamc.math.Vector}
     */
    public Vector divide(final float x, final float y, final float z, final Dimension dimension) {
        return new Vector(this.x / x, this.y / y, this.z / z, dimension);
    }

    /**
     * Creates the square root from the coordinates of this {@link org.terracottamc.math.Vector}
     *
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh calculated {@link org.terracottamc.math.Vector}
     */
    public Vector squareRoot(final Dimension dimension) {
        return new Vector((float) FastMath.sqrt(this.x), (float) FastMath.sqrt(this.y), (float) FastMath.sqrt(this.z), dimension);
    }

    /**
     * Creates the cubic root from the coordinates of this {@link org.terracottamc.math.Vector}
     *
     * @return a fresh calculated {@link org.terracottamc.math.Vector}
     */
    public Vector cubicRoot(final Dimension dimension) {
        return new Vector((float) FastMath.cbrt(this.x), (float) FastMath.cbrt(this.y), (float) FastMath.cbrt(this.z), dimension);
    }

    /**
     * Normalizes this {@link org.terracottamc.math.Vector} by dividing its values through the
     * square root of the sum of its squared values
     *
     * @param dimension which represents the {@link org.terracottamc.world.Dimension}
     *
     * @return a fresh normalized {@link org.terracottamc.math.Vector}
     */
    public Vector normalize(final Dimension dimension) {
        final float squaredLength = this.squaredLength();

        return this.divide(squaredLength, squaredLength, squaredLength, dimension);
    }

    /**
     * Calculates the distance between this and a given {@link org.terracottamc.math.Vector}
     *
     * @param vector which represents the endpoint that is used in the calculation
     *
     * @return a fresh calculated distance float
     */
    public float distance(final Vector vector) {
        return (float) FastMath.sqrt(this.squaredDistance(vector));
    }

    /**
     * Calculates the distance between this and a given {@link org.terracottamc.math.Vector} and squares its values
     *
     * @param vector which is used to execute the calculation
     *
     * @return a fresh calculated and squared distance
     */
    public float squaredDistance(final Vector vector) {
        return (float) (FastMath.pow((this.x - vector.getX()), 2) + FastMath.pow((this.y - vector.getY()), 2) +
                FastMath.pow((this.z - vector.getZ()), 2));
    }

    /**
     * Calculates the square root of the sum of the squared values from this {@link org.terracottamc.math.Vector}
     *
     * @return a fresh squared length
     */
    public float squaredLength() {
        return (float) (FastMath.sqrt(this.x * this.x + this.y * this.y + this.z * this.z));
    }

    /**
     * Retrieves the x value of this {@link org.terracottamc.math.Vector}
     *
     * @return a fresh float value
     */
    public float getX() {
        return this.x;
    }

    /**
     * Retrieves the y value of this {@link org.terracottamc.math.Vector}
     *
     * @return a fresh float value
     */
    public float getY() {
        return this.y;
    }

    /**
     * Retrieves the z value of this {@link org.terracottamc.math.Vector}
     *
     * @return a fresh float value
     */
    public float getZ() {
        return this.z;
    }

    /**
     * Retrieves the x value of this {@link org.terracottamc.math.Vector} specified for blocks
     *
     * @return a fresh float value
     */
    public int getBlockX() {
        return (int) this.x;
    }

    /**
     * Retrieves the y value of this {@link org.terracottamc.math.Vector} specified for blocks
     *
     * @return a fresh float value
     */
    public int getBlockY() {
        return (int) this.y;
    }

    /**
     * Retrieves the z value of this {@link org.terracottamc.math.Vector} specified for blocks
     *
     * @return a fresh float value
     */
    public int getBlockZ() {
        return (int) this.z;
    }

    /**
     * Retrieves the {@link org.terracottamc.world.Dimension} of this {@link org.terracottamc.math.Vector}
     *
     * @return a fresh {@link org.terracottamc.world.Dimension}
     */
    public Dimension getDimension() {
        return this.dimension;
    }

    /**
     * Updates the x value of this {@link org.terracottamc.math.Vector}
     *
     * @param x that represents the updated value
     */
    public void setX(final float x) {
        this.x = x;
    }

    /**
     * Updates the y value of this {@link org.terracottamc.math.Vector}
     *
     * @param y that represents the updated value
     */
    public void setY(final float y) {
        this.y = y;
    }

    /**
     * Updates the z value of this {@link org.terracottamc.math.Vector}
     *
     * @param z that represents the updated value
     */
    public void setZ(final float z) {
        this.z = z;
    }

    /**
     * Updates the x value of this {@link org.terracottamc.math.Vector} specified for blocks
     *
     * @param blockX that represents the updated value
     */
    public void setBlockX(final int blockX) {
        this.x = blockX;
    }

    /**
     * Updates the y value of this {@link org.terracottamc.math.Vector} specified for blocks
     *
     * @param blockY that represents the updated value
     */
    public void setBlockY(final int blockY) {
        this.y = blockY;
    }

    /**
     * Updates the z value of this {@link org.terracottamc.math.Vector} specified for blocks
     *
     * @param blockZ that represents the updated value
     */
    public void setBlockZ(final int blockZ) {
        this.z = blockZ;
    }

    /**
     * Updates the {@link org.terracottamc.world.Dimension} of this {@link org.terracottamc.math.Vector}
     *
     * @param dimension that represents the updated value
     */
    public void setDimension(final Dimension dimension) {
        this.dimension = dimension;
    }
}