package com.iafenvoy.iceandfire.network;

import com.iafenvoy.iceandfire.config.IafClientConfig;
import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import com.iafenvoy.iceandfire.entity.util.ISyncMount;
import com.iafenvoy.iceandfire.event.handler.ClientEvents;
import com.iafenvoy.iceandfire.item.block.entity.JarBlockEntity;
import com.iafenvoy.iceandfire.item.block.entity.PixieHouseBlockEntity;
import com.iafenvoy.iceandfire.item.block.entity.PodiumBlockEntity;
import com.iafenvoy.iceandfire.network.payload.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class ClientNetworkHandlers {
    private static CameraType prev = CameraType.FIRST_PERSON;

    private ClientNetworkHandlers() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(DragonSetBurnBlockS2CPayload.ID, ClientNetworkHandlers::handleDragonSetBurnBlock);
        ClientPlayNetworking.registerGlobalReceiver(LightningBoltS2CPayload.ID, ClientNetworkHandlers::handleLightningBolt);
        ClientPlayNetworking.registerGlobalReceiver(UpdatePixieHouseS2CPayload.ID, ClientNetworkHandlers::handleUpdatePixieHouse);
        ClientPlayNetworking.registerGlobalReceiver(UpdatePixieJarS2CPayload.ID, ClientNetworkHandlers::handleUpdatePixieJar);
        ClientPlayNetworking.registerGlobalReceiver(UpdatePodiumS2CPayload.ID, ClientNetworkHandlers::handleUpdatePodium);
        ClientPlayNetworking.registerGlobalReceiver(StartRidingMobPayload.ID, ClientNetworkHandlers::handleStartRidingMob);
    }

    public static void handleDragonSetBurnBlock(DragonSetBurnBlockS2CPayload payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> {
            Player player = ctx.player();
            if (player == null) return;
            Entity entity = player.level().getEntity(payload.entityId());
            if (entity instanceof DragonBaseEntity dragon) {
                dragon.setBreathingFire(payload.breathing());
                dragon.burningTarget = payload.target();
            }
        });
    }

    public static void handleLightningBolt(LightningBoltS2CPayload payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> ClientEvents.LIGHTNINGS.addAll(payload.lightnings()));
    }

    public static void handleStartRidingMob(StartRidingMobPayload payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> {
            Player player = ctx.player();
            if (player == null) return;
            Options options = Minecraft.getInstance().options;
            Entity entity = player.level().getEntity(payload.dragonId());
            if (entity instanceof ISyncMount && entity instanceof TamableAnimal tamable && tamable.isOwnedBy(player) && tamable.distanceTo(player) < 14) {
                if (payload.ride()) {
                    if (payload.baby()) tamable.startRiding(player, true, true);
                    else {
                        player.startRiding(tamable, true, true);
                        if (IafClientConfig.INSTANCE.dragonAuto3rdPerson.getValue()) {
                            prev = options.getCameraType();
                            options.setCameraType(CameraType.THIRD_PERSON_BACK);
                        }
                    }
                } else {
                    if (payload.baby()) tamable.stopRiding();
                    else {
                        player.stopRiding();
                        if (IafClientConfig.INSTANCE.dragonAuto3rdPerson.getValue())
                            options.setCameraType(prev);
                    }
                }
            }
        });
    }

    public static void handleUpdatePixieHouse(UpdatePixieHouseS2CPayload payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> {
            Player player = ctx.player();
            if (player == null) return;
            BlockEntity blockEntity = player.level().getBlockEntity(payload.blockPos());
            if (blockEntity instanceof PixieHouseBlockEntity house) {
                house.hasPixie = payload.hasPixie();
                house.pixieType = payload.pixieType();
            } else if (blockEntity instanceof JarBlockEntity jar) {
                jar.hasPixie = payload.hasPixie();
                jar.pixieType = payload.pixieType();
            }
        });
    }

    public static void handleUpdatePixieJar(UpdatePixieJarS2CPayload payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> {
            Player player = ctx.player();
            if (player == null) return;
            if (player.level().getBlockEntity(payload.blockPos()) instanceof JarBlockEntity jar)
                jar.hasProduced = payload.isProducing();
        });
    }

    public static void handleUpdatePodium(UpdatePodiumS2CPayload payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> {
            Player player = ctx.player();
            if (player == null) return;
            if (player.level().getBlockEntity(payload.blockPos()) instanceof PodiumBlockEntity podium)
                podium.setItem(0, payload.heldStack());
        });
    }
}
