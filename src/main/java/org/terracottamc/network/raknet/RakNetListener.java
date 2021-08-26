package org.terracottamc.network.raknet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.unix.UnixChannelOption;
import network.ycc.raknet.RakNet;
import network.ycc.raknet.pipeline.UserDataCodec;
import network.ycc.raknet.server.channel.RakNetServerChannel;
import org.terracottamc.network.packet.Protocol;
import org.terracottamc.network.raknet.compression.PacketCompressor;
import org.terracottamc.network.raknet.compression.PacketDecompressor;
import org.terracottamc.network.raknet.handler.ConnectionTimeoutHandler;
import org.terracottamc.network.raknet.handler.UnconnectedPingHandler;
import org.terracottamc.network.raknet.protocol.ProtocolDecoder;
import org.terracottamc.network.raknet.protocol.ProtocolEncoder;
import org.terracottamc.network.raknet.protocol.ProtocolHandler;
import org.terracottamc.server.Server;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

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
public class RakNetListener {

    private final long serverId;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;

    /**
     * Creates a new {@link org.terracottamc.network.raknet.RakNetListener} which is used to let the networking work
     *
     * @param serverId which represents the unique number of the {@link org.terracottamc.server.Server}
     */
    public RakNetListener(final long serverId) {
        this.serverId = serverId;
    }

    /**
     * Binds this {@link org.terracottamc.network.raknet.RakNetListener}
     */
    public void bind() {
        final Server server = Server.getInstance();
        final InetSocketAddress address = new InetSocketAddress(server.getAddress(), server.getPort());

        try {
            this.bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
            this.workerGroup = new DefaultEventLoopGroup();

            final ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(this.bossGroup, this.workerGroup)
                    .channelFactory(() -> new RakNetServerChannel(() ->
                            Epoll.isAvailable() ? new EpollDatagramChannel() : new NioDatagramChannel()))
                    .option(RakNet.SERVER_ID, serverId)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(final Channel channel) {
                            channel.pipeline().addLast(new UnconnectedPingHandler());
                        }
                    })
                    .childHandler(new ChannelInitializer<Channel>() {
                        protected void initChannel(final Channel channel) {
                            final ChannelConfig channelConfig = channel.config();
                            try {
                                channelConfig.setOption(ChannelOption.IP_TOS, 0x18);
                            } catch (final ChannelException ignored) {

                            }
                            channelConfig.setAllocator(PooledByteBufAllocator.DEFAULT);

                            final RakNet.Config rakNetConfig = (RakNet.Config) channelConfig;
                            rakNetConfig.setMaxQueuedBytes(8 * 1024 * 1024);

                            channel.pipeline().addFirst(ConnectionTimeoutHandler.NAME,
                                    new ConnectionTimeoutHandler(15, TimeUnit.SECONDS));
                            channel.pipeline().addLast(UserDataCodec.NAME, new UserDataCodec(Protocol.BATCH_PACKET));
                            channel.pipeline().addLast(PacketCompressor.NAME, new PacketCompressor());
                            channel.pipeline().addLast(PacketDecompressor.NAME, new PacketDecompressor());
                            channel.pipeline().addLast(ProtocolDecoder.NAME, new ProtocolDecoder());
                            channel.pipeline().addLast(ProtocolEncoder.NAME, new ProtocolEncoder());
                            channel.pipeline().addLast(ProtocolHandler.NAME, new ProtocolHandler());
                        }
                    });

            final ChannelFutureListener channelFutureListener = channelFuture ->
                    Server.getInstance().getLogger().info("Started Terracotta on " + address.getHostName() + ":" +
                            address.getPort() + " (Bedrock Edition v" + Protocol.MINECRAFT_VERSION
                            .replace(Protocol.MINECRAFT_VERSION.split("\\.")[2], "x") + ")");


            if (this.bossGroup.getClass().equals(EpollEventLoopGroup.class)) {
                serverBootstrap.option(UnixChannelOption.SO_REUSEPORT, true);
                serverBootstrap.option(EpollChannelOption.MAX_DATAGRAM_PAYLOAD_SIZE, 4 * 1024);
            } else {
                serverBootstrap.option(ChannelOption.RCVBUF_ALLOCATOR,
                        new AdaptiveRecvByteBufAllocator(4 * 1024, 64 * 1024, 256 * 1024));
            }

            this.channelFuture = serverBootstrap.bind(address).addListener(channelFutureListener).sync();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes this {@link org.terracottamc.network.raknet.RakNetListener}
     */
    public void close() {
        try {
            this.channelFuture.channel().closeFuture().sync();
        } catch (final InterruptedException ignored) {

        } finally {
            this.workerGroup.shutdownGracefully();
            this.bossGroup.shutdownGracefully();
        }
    }
}