package org.terracottamc.entity.player;

import org.terracottamc.entity.EntityHuman;
import org.terracottamc.network.packet.ChunkRadiusUpdatedPacket;
import org.terracottamc.network.packet.DisconnectPacket;
import org.terracottamc.network.packet.PlayStatusPacket;
import org.terracottamc.network.packet.type.PlayStatus;
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
public class Player extends EntityHuman {

    private final PlayerNetworkConnection playerNetworkConnection;
    private final Server server;
    private final LoginChainData loginChainData;
    private final String name;
    private final String xboxId;

    private GameMode gameMode;
    private boolean spawned;
    private int viewDistance;

    /**
     * Creates a new {@link org.terracottamc.entity.player.Player} with given {@link org.terracottamc.server.Server}
     * and {@link java.net.InetSocketAddress}
     *
     * @param playerNetworkConnection which represents the client connection to the {@link org.terracottamc.server.Server}
     *                                of this {@link org.terracottamc.entity.player.Player}
     * @param loginChainData          which is the read chain data of the
     *                                {@link org.terracottamc.network.packet.LoginPacket}
     */
    public Player(final PlayerNetworkConnection playerNetworkConnection, final LoginChainData loginChainData) {
        this.loginChainData = loginChainData;
        this.name = this.loginChainData.getUsername();
        this.xboxId = this.loginChainData.getXboxId();
        this.playerNetworkConnection = playerNetworkConnection;
        this.server = this.playerNetworkConnection.getServer();
    }

    /**
     * Retrieves the {@link org.terracottamc.entity.player.PlayerNetworkConnection}
     * of this {@link org.terracottamc.entity.player.Player}
     *
     * @return a fresh {@link org.terracottamc.entity.player.PlayerNetworkConnection}
     */
    public PlayerNetworkConnection getPlayerNetworkConnection() {
        return this.playerNetworkConnection;
    }

    /**
     * Retrieves the {@link org.terracottamc.server.Server} this {@link org.terracottamc.entity.player.Player}
     * is connected to
     *
     * @return a fresh {@link org.terracottamc.server.Server}
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * Retrieves the {@link org.terracottamc.entity.player.LoginChainData}
     * of this {@link org.terracottamc.entity.player.Player}
     *
     * @return a fresh {@link org.terracottamc.entity.player.LoginChainData}
     */
    public LoginChainData getLoginChainData() {
        return this.loginChainData;
    }

    /**
     * Returns the xbox account identifier of this {@link org.terracottamc.entity.player.Player}
     *
     * @return a fresh {@link java.lang.String}
     */
    public String getXboxId() {
        return this.xboxId;
    }

    /**
     * Retrieves the name of this {@link org.terracottamc.entity.player.Player}
     *
     * @return a fresh {@link java.lang.String}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the current {@link org.terracottamc.entity.player.GameMode} of this
     * {@link org.terracottamc.entity.player.Player}
     *
     * @return a fresh {@link org.terracottamc.entity.player.GameMode}
     */
    public GameMode getGameMode() {
        return this.gameMode;
    }

    /**
     * Proofs whether this {@link org.terracottamc.entity.player.Player}
     * is spawned on the {@link org.terracottamc.server.Server}
     *
     * @return whether this {@link org.terracottamc.entity.player.Player} is spawned
     */
    public boolean isSpawned() {
        return this.spawned;
    }

    /**
     * Retrieves the view distance in chunks of this {@link org.terracottamc.entity.player.Player}
     *
     * @return a fresh view distance
     */
    public int getViewDistance() {
        return this.viewDistance;
    }

    /**
     * Updates the view distance of this {@link org.terracottamc.entity.player.Player}
     * by sending the {@link org.terracottamc.network.packet.ChunkRadiusUpdatedPacket}
     *
     * @param viewDistance which represents the updated value
     */
    public void setViewDistance(final int viewDistance) {
        final ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setUpdatedChunkRadius(viewDistance);

        this.playerNetworkConnection.sendPacket(chunkRadiusUpdatedPacket);

        this.viewDistance = viewDistance;
    }

    /**
     * Sends the given {@link org.terracottamc.network.packet.type.PlayStatus}
     * to this {@link org.terracottamc.entity.player.Player}
     *
     * @param playStatus which is used to build the {@link org.terracottamc.network.packet.PlayStatusPacket}
     *                   that should be sent
     */
    public void sendPlayStatus(final PlayStatus playStatus) {
        final PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setPlayStatus(playStatus);

        this.playerNetworkConnection.sendPacket(playStatusPacket);
    }

    /**
     * Initializes this {@link org.terracottamc.entity.player.Player} after the loading screen has been passed
     */
    public void initialize() {
        this.sendPlayStatus(PlayStatus.PLAYER_SPAWN);

        this.spawned = true;
    }

    /**
     * Disconnects this {@link org.terracottamc.entity.player.Player} from its {@link org.terracottamc.server.Server}
     * without hiding the disconnection screen
     *
     * @param disconnectMessage which will be displayed in the disconnection screen
     */
    public void disconnect(final String disconnectMessage) {
        this.disconnect(disconnectMessage, false);
    }

    /**
     * Disconnects this {@link org.terracottamc.entity.player.Player} from its {@link org.terracottamc.server.Server}
     *
     * @param disconnectMessage    which will be displayed in the disconnection screen
     * @param hideDisconnectScreen that hides the disconnect screen
     */
    public void disconnect(final String disconnectMessage, final boolean hideDisconnectScreen) {
        final DisconnectPacket disconnectPacket = new DisconnectPacket();
        disconnectPacket.setDisconnectMessage(disconnectMessage);
        disconnectPacket.setHideDisconnectScreen(hideDisconnectScreen);

        this.playerNetworkConnection.sendPacket(disconnectPacket);

        Server.getInstance().getLogger().info("The player " + this.name + " disconnected with reason: " +
                disconnectMessage);

        this.server.removePlayerByAddress((InetSocketAddress) this.getPlayerNetworkConnection().getRakNetSession()
                .remoteAddress());

        try {
            this.playerNetworkConnection.getRakNetSession().close().sync();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}