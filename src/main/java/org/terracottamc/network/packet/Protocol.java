package org.terracottamc.network.packet;

import java.util.Arrays;
import java.util.List;

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
public interface Protocol {

    int CURRENT_PROTOCOL = Protocol.PROTOCOL_v1_17_10;
    int PROTOCOL_v1_17_10 = 448; // 1.17.10 until 1.17.11
    int PROTOCOL_v1_17_0 = 440; // 1.17.0 until 1.17.2

    List<Integer> SUPPORTED_PROTOCOL_VERSIONS = Arrays.asList(Protocol.CURRENT_PROTOCOL, Protocol.PROTOCOL_v1_17_0);

    String MINECRAFT_VERSION = "1.17.11";
    String MINECRAFT_VERSION_v1_17_0 = "1.17.0";

    short BATCH_PACKET = 0xfe;
    byte LOGIN_PACKET = 0x01;
    byte PLAY_STATUS_PACKET = 0x02;
    // byte SERVER_TO_CLIENT_HANDSHAKE_PACKET = 0x03;
    // byte CLIENT_TO_SERVER_HANDSHAKE_PACKET = 0x04;
    byte DISCONNECT_PACKET = 0x05;
    byte RESOURCE_PACKS_INFO_PACKET = 0x06;
    byte RESOURCE_PACK_STACK_PACKET = 0x07;
    byte RESOURCE_PACK_CLIENT_RESPONSE_PACKET = 0x08;
    byte TEXT_PACKET = 0x09;
    byte START_GAME_PACKET = 0x0b;
    byte SET_TIME_PACKET = 0x0a;
    byte TICK_SYNC_PACKET = 0x17;
    byte SET_ENTITY_DATA_PACKET = 0x27;
    byte REQUEST_CHUNK_RADIUS_PACKET = 0x45;
    byte CHUNK_RADIUS_UPDATED_PACKET = 0x46;
    byte RESOURCE_PACK_DATA_INFO_PACKET = 0x52;
    byte RESOURCE_PACK_CHUNK_DATA_PACKET = 0x53;
    byte RESOURCE_PACK_CHUNK_REQUEST_PACKET = 0x54;
    byte SET_LOCAL_PLAYER_AS_INITIALIZED_PACKET = 0x71;
    byte AVAILABLE_ENTITY_IDENTIFIERS_PACKET = 0x77;
    byte BIOME_DEFINITION_LIST_PACKET = 0x7a;
    short CLIENT_CACHE_STATUS_PACKET = 0x81;
    short CREATIVE_CONTENT_PACKET = 0x91;
    short VIOLATION_WARNING_PACKET = 0x9c;
}