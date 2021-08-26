package org.terracottamc.network.packet.handler;

import io.netty.channel.Channel;
import org.terracottamc.entity.player.LoginChainData;
import org.terracottamc.entity.player.Player;
import org.terracottamc.entity.player.PlayerNetworkConnection;
import org.terracottamc.entity.player.info.DeviceInfo;
import org.terracottamc.network.packet.LoginPacket;
import org.terracottamc.network.packet.Protocol;
import org.terracottamc.network.packet.ResourcePacksInfoPacket;
import org.terracottamc.network.packet.type.PlayStatus;
import org.terracottamc.server.Server;

import java.util.UUID;

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
public class LoginPacketHandler implements IPacketHandler<LoginPacket> {

    @Override
    public void handle(final LoginPacket packet, final Player player) {

    }

    /**
     * Handles the {@link org.terracottamc.network.packet.LoginPacket}
     *
     * @param packet        which should be handled
     * @param rakNetSession which represents the session of the client
     */
    public void handleLogin(final LoginPacket packet, final Channel rakNetSession) {
        final Server server = Server.getInstance();

        final int protocolVersion = packet.getProtocolVersion();
        final String username = packet.getUsername();
        final String xboxId = packet.getXboxId();
        final UUID uuid = packet.getUuid();
        final String languageCode = packet.getLanguageCode();
        final String gameVersion = packet.getGameVersion();
        final String platformOfflineId = packet.getPlatformOfflineId();
        final String platformOnlineId = packet.getPlatformOnlineId();
        final String selfSignedId = packet.getSelfSignedId();
        final String serverAddress = packet.getServerAddress();
        final String thirdPartyName = packet.getThirdPartyName();
        final boolean thirdPartyNameOnly = packet.isThirdPartyNameOnly();
        final DeviceInfo deviceInfo = packet.getDeviceInfo();

        final LoginChainData loginChainData = new LoginChainData(protocolVersion, username, xboxId, uuid, languageCode,
                gameVersion, platformOfflineId, platformOnlineId, selfSignedId, serverAddress, thirdPartyName,
                thirdPartyNameOnly, deviceInfo);
        final Player player = new Player(new PlayerNetworkConnection(server, rakNetSession), loginChainData);
        player.setUuid(uuid);
        player.setSkin(packet.getSkin());
        player.setNameTag(username);
        player.setNameTagVisible(true);
        player.setNameTagAlwaysVisible(true);

        PlayStatus playStatus = PlayStatus.LOGIN_SUCCESS;

        if (!Protocol.SUPPORTED_PROTOCOL_VERSIONS.contains(protocolVersion)) {
            if (protocolVersion < Protocol.PROTOCOL_v1_17_0) {
                playStatus = PlayStatus.LOGIN_FAILED_CLIENT_OUTDATED;
            }

            if (protocolVersion > Protocol.CURRENT_PROTOCOL) {
                playStatus = PlayStatus.LOGIN_FAILED_SERVER_OUTDATED;
            }
        }

        if (server.getPlayers().size() >= server.getMaxPlayers()) {
            playStatus = PlayStatus.LOGIN_FAILED_SERVER_FULL;
        }

        player.sendPlayStatus(playStatus);

        if (!playStatus.equals(PlayStatus.LOGIN_SUCCESS)) {
            return;
        }

        server.addPlayer(player);

        final ResourcePacksInfoPacket resourcePacksInfoPacket = new ResourcePacksInfoPacket();
        resourcePacksInfoPacket.setForceAccept(Server.getInstance().isForceResourcePacks());
        resourcePacksInfoPacket.setScripting(false);
        resourcePacksInfoPacket.setForceServerPacks(false);

        player.getPlayerNetworkConnection().sendPacket(resourcePacksInfoPacket);
    }
}