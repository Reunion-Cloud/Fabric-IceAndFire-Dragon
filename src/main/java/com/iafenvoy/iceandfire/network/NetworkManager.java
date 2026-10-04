package com.iafenvoy.iceandfire.network;

import com.iafenvoy.iceandfire.network.payload.*;
import com.iafenvoy.iceandfire.world.DangerousGeneration;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class NetworkManager {
    private NetworkManager() {
    }

    public static void registerPayloads() {
        PayloadTypeRegistry.clientboundPlay().register(DragonSetBurnBlockS2CPayload.ID, DragonSetBurnBlockS2CPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LightningBoltS2CPayload.ID, LightningBoltS2CPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(UpdatePixieHouseS2CPayload.ID, UpdatePixieHouseS2CPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(UpdatePixieJarS2CPayload.ID, UpdatePixieJarS2CPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(UpdatePodiumS2CPayload.ID, UpdatePodiumS2CPayload.CODEC);

        PayloadTypeRegistry.serverboundPlay().register(DragonControlC2SPayload.ID, DragonControlC2SPayload.CODEC);

        PayloadTypeRegistry.clientboundPlay().register(StartRidingMobPayload.ID, StartRidingMobPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(StartRidingMobPayload.ID, StartRidingMobPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(DragonControlC2SPayload.ID, ServerNetworkHandlers::handleDragonControl);
        ServerPlayNetworking.registerGlobalReceiver(StartRidingMobPayload.ID, ServerNetworkHandlers::handleStartRidingMob);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static void sendToAllPlayers(CustomPacketPayload payload) {
        MinecraftServer server = DangerousGeneration.ServerTracker.currentServer;
        if (server != null) {
            for (ServerPlayer player : PlayerLookup.all(server)) {
                ServerPlayNetworking.send(player, payload);
            }
        }
    }
}
