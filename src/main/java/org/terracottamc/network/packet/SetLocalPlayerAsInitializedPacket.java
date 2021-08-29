package org.terracottamc.network.packet;

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
public class SetLocalPlayerAsInitializedPacket extends Packet {

    private long playerEntityId;

    @Override
    public int getPacketId() {
        return Protocol.SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET;
    }

    @Override
    public void deserialize() {
        super.deserialize();

        this.playerEntityId = this.readUnsignedVarLong();
    }

    /**
     * Retrieves the entity identifier of the player who is ready to be initialized
     *
     * @return a fresh player entity id
     */
    public long getPlayerEntityId() {
        return this.playerEntityId;
    }
}