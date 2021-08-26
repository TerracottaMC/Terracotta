package org.terracottamc.network.raknet.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import network.ycc.raknet.RakNet;
import network.ycc.raknet.packet.UnconnectedPing;
import network.ycc.raknet.packet.UnconnectedPong;
import network.ycc.raknet.server.pipeline.UdpPacketHandler;
import org.terracottamc.network.raknet.type.ServerInfo;
import org.terracottamc.entity.player.GameMode;
import org.terracottamc.server.Server;

import java.net.InetSocketAddress;

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
public class UnconnectedPingHandler extends UdpPacketHandler<UnconnectedPing> {

    public UnconnectedPingHandler() {
        super(UnconnectedPing.class);
    }

    @Override
    protected void handle(final ChannelHandlerContext ctx, final InetSocketAddress socketAddress, final UnconnectedPing unconnectedPing) {
        final Channel channel = ctx.channel();
        final RakNet.Config rakNetConfig = (RakNet.Config) channel.config();
        final Server server = Server.getInstance();

        final ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerId(server.getServerId());
        serverInfo.setMotd(server.getMotd());
        serverInfo.setSubmotd(server.getSubMotd());
        serverInfo.setMaxPlayers(server.getMaxPlayers());
        serverInfo.setDefaultGameMode(server.getDefaultGameMode());

        final UnconnectedPong unconnectedPong = new UnconnectedPong();
        unconnectedPong.setClientTime(unconnectedPing.getClientTime());
        unconnectedPong.setServerId(rakNetConfig.getServerId());
        unconnectedPong.setMagic(rakNetConfig.getMagic());
        unconnectedPong.setInfo(serverInfo.toString());

        final ByteBuf buffer = ctx.alloc().directBuffer(unconnectedPong.sizeHint());

        try {
            rakNetConfig.getCodec().encode(unconnectedPong, buffer);

            for (int i = 0; i < 3; i++) {
                channel.writeAndFlush(new DatagramPacket(buffer.retainedSlice(), socketAddress))
                        .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
        } finally {
            ReferenceCountUtil.safeRelease(unconnectedPong);
            buffer.release();
        }
    }
}