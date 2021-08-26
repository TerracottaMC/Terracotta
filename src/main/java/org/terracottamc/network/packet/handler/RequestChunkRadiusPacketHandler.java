package org.terracottamc.network.packet.handler;

import org.terracottamc.entity.player.Player;
import org.terracottamc.network.packet.RequestChunkRadiusPacket;

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
public class RequestChunkRadiusPacketHandler implements IPacketHandler<RequestChunkRadiusPacket> {

    @Override
    public void handle(final RequestChunkRadiusPacket packet, final Player player) {
        final int viewDistance = Math.min(packet.getChunkRadius(), player.getServer().getViewDistance());

        player.setViewDistance(viewDistance);
    }
}