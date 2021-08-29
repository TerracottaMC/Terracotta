package org.terracottamc.network.packet.handler;

import org.terracottamc.entity.player.Player;
import org.terracottamc.math.Vector;
import org.terracottamc.network.packet.AvailableEntityIdentifiersPacket;
import org.terracottamc.network.packet.BiomeDefinitionListPacket;
import org.terracottamc.network.packet.CreativeContentPacket;
import org.terracottamc.network.packet.ResourcePackClientResponsePacket;
import org.terracottamc.network.packet.ResourcePackDataInfoPacket;
import org.terracottamc.network.packet.ResourcePackStackPacket;
import org.terracottamc.network.packet.StartGamePacket;
import org.terracottamc.network.packet.type.GamePublishSetting;
import org.terracottamc.network.packet.type.ResourcePackEntry;
import org.terracottamc.network.packet.type.ResourcePackResponseStatus;
import org.terracottamc.resourcepack.ResourcePack;
import org.terracottamc.server.Server;
import org.terracottamc.util.BedrockResourceDataReader;
import org.terracottamc.world.Difficulty;
import org.terracottamc.world.Dimension;
import org.terracottamc.world.gamerule.GameRuleRegistry;
import org.terracottamc.world.generator.GeneratorType;

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
public class ResourcePackClientResponsePacketHandler implements IPacketHandler<ResourcePackClientResponsePacket> {

    @Override
    public void handle(final ResourcePackClientResponsePacket packet, final Player player) {
        final Server server = player.getServer();

        switch (packet.getResponseStatus()) {
            case REFUSED:
                player.disconnect(ResourcePackResponseStatus.REFUSED.name());
                break;
            case SEND_PACKS:
                for (final ResourcePackEntry resourcePackEntry : packet.getResourcePackEntries()) {
                    final ResourcePack resourcePack = server.getResourcePackManager()
                            .retrieveResourcePackById(resourcePackEntry.getUuid());

                    if (resourcePack != null) {
                        final int maxChunkSize = 1048576;

                        final ResourcePackDataInfoPacket resourcePackDataInfoPacket = new ResourcePackDataInfoPacket();
                        resourcePackDataInfoPacket.setResourcePackUuid(resourcePack.getUuid());
                        resourcePackDataInfoPacket.setMaxChunkSize(maxChunkSize);
                        resourcePackDataInfoPacket.setChunkCount((int) (resourcePack.getSize() / maxChunkSize));
                        resourcePackDataInfoPacket.setCompressedResourcePackSize(resourcePack.getSize());
                        resourcePackDataInfoPacket.setResourcePackSha256(resourcePack.getSha256());

                        player.getPlayerNetworkConnection().sendPacket(resourcePackDataInfoPacket);
                    }
                }
                break;
            case HAVE_ALL_PACKS:
                this.sendResourcePackStack(player);
                break;
            case COMPLETED:
                final StartGamePacket startGamePacket = new StartGamePacket();
                startGamePacket.setEntityUniqueId(player.getEntityId());
                startGamePacket.setEntityId(player.getEntityId());
                startGamePacket.setGameMode(server.getDefaultGameMode());
                startGamePacket.setVector(new Vector(0f, 6f, 0f)); // TODO
                startGamePacket.setYaw(0f);
                startGamePacket.setPitch(0f);
                startGamePacket.setWorldSeed(0);
                startGamePacket.setSpawnBiomeType(0);
                startGamePacket.setUserDefinedBiomeName("plains");
                startGamePacket.setDimension(Dimension.WORLD);
                startGamePacket.setGeneratorType(GeneratorType.INFINITE);
                startGamePacket.setDifficulty(Difficulty.NORMAL);
                startGamePacket.setAchievementsDisabled(false);
                startGamePacket.setDayCycleStopTime(-1);
                startGamePacket.setEducationEditionOffer(0);
                startGamePacket.setEducationEditionFeaturesEnabled(false);
                startGamePacket.setEducationEditionProductId("");
                startGamePacket.setRainTime(0); // TODO
                startGamePacket.setThunderTime(0); // TODO
                startGamePacket.setConfirmedPlatformLockedContent(false);
                startGamePacket.setMultiplayerGame(true);
                startGamePacket.setBroadcastToLAN(true);
                startGamePacket.setXblBroadcastIntent(GamePublishSetting.PUBLIC);
                startGamePacket.setPlatformBroadcastIntent(GamePublishSetting.PUBLIC);
                startGamePacket.setCommandsEnabled(true);
                startGamePacket.setTexturePacksRequired(false);
                startGamePacket.setGameRules(GameRuleRegistry.getGameRules());
                startGamePacket.setExperimentAmount(0);
                startGamePacket.setExperimentsPreviouslyToggled(false);
                startGamePacket.setBonusChest(false);
                startGamePacket.setStartWithMapEnabled(false);
                startGamePacket.setPermissionLevel(1);
                startGamePacket.setServerChunkTickRange(4);
                startGamePacket.setLockedBehaviourPack(false);
                startGamePacket.setLockedResourcePack(false);
                startGamePacket.setFromLockedWorldTemplate(false);
                startGamePacket.setUsingMsaGamerTagsOnly(false);
                startGamePacket.setFromWorldTemplate(false);
                startGamePacket.setWorldTemplateOptionLocked(false);
                startGamePacket.setOnlySpawningV1Villagers(false);
                startGamePacket.setLimitedWorldWidth(16);
                startGamePacket.setLimitedWorldHeight(16);
                startGamePacket.setNetherType(false);
                startGamePacket.setExperimentalGameplay(false);
                startGamePacket.setWorldId("");
                startGamePacket.setWorldName(server.getDefaultWorldName());
                startGamePacket.setPremiumWorldTemplateId("");
                startGamePacket.setTrail(false);
                startGamePacket.setMovementServerAuthoritative(false);
                startGamePacket.setRewindHistorySize(0);
                startGamePacket.setServerAuthoritativeBlockBreaking(false);
                startGamePacket.setCurrentTick(0);
                startGamePacket.setEnchantmentSeed(0);
                startGamePacket.setCustomBlocksAmount(0);
                startGamePacket.setItemPalette(BedrockResourceDataReader
                        .retrieveItemPaletteByProtocolVersion(player.getLoginChainData().getProtocolVersion()));
                startGamePacket.setMultiplayerCorrelationId("");
                startGamePacket.setInventoryServerAuthoritative(false);
                startGamePacket.setServerEngine("Terracotta");

                player.getPlayerNetworkConnection().sendPacket(startGamePacket);

                final AvailableEntityIdentifiersPacket availableEntityIdentifiersPacket =
                        new AvailableEntityIdentifiersPacket();
                availableEntityIdentifiersPacket.setEntityIdentifiersData(BedrockResourceDataReader
                        .retrieveEntityIdentifiersDataByProtocolVersion(player.getLoginChainData().getProtocolVersion()));

                player.getPlayerNetworkConnection().sendPacket(availableEntityIdentifiersPacket);

                final BiomeDefinitionListPacket biomeDefinitionListPacket = new BiomeDefinitionListPacket();
                biomeDefinitionListPacket.setBiomeDefintionListData(BedrockResourceDataReader
                        .retrieveBiomeDefinitionsDataByProtocolVersion(player.getLoginChainData().getProtocolVersion()));

                player.getPlayerNetworkConnection().sendPacket(biomeDefinitionListPacket);
                player.getPlayerNetworkConnection().sendPacket(new CreativeContentPacket());
                player.initialize();
                break;
        }
    }

    /**
     * Sends the resource pack stack to the given {@link org.terracottamc.entity.player.Player}
     *
     * @param player who should receive the stack
     */
    private void sendResourcePackStack(final Player player) {
        final ResourcePackStackPacket resourcePackStackPacket = new ResourcePackStackPacket();
        resourcePackStackPacket.setMustAccept(player.getServer().isForceResourcePacks());

        player.getPlayerNetworkConnection().sendPacket(resourcePackStackPacket);
    }
}