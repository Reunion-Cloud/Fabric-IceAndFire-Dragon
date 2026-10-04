package com.iafenvoy.iceandfire.world;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;

import java.util.Optional;

public interface DangerousGeneration {
    default boolean isFarEnoughFromSpawn(LevelAccessor world, BlockPos pos) {
        return !this.getOrigin(world, pos).closerThan(pos, this.getDangerousRadius());
    }

    default boolean isFarEnoughFromSpawn(BlockPos pos) {
        return Optional.ofNullable(ServerTracker.currentServer).map(server -> this.isFarEnoughFromSpawn(server.overworld(), pos)).orElse(true);
    }

    default float getDangerousRadius() {
        return IafCommonConfig.INSTANCE.worldGen.dangerousDistanceLimit.getValue().floatValue();
    }

    default BlockPos getOrigin(LevelAccessor world, BlockPos pos) {
        BlockPos spawn = world.getLevelData().getRespawnData().pos();
        return new BlockPos(spawn.getX(), pos.getY(), spawn.getZ());
    }

    /**
     * Tracks the running server. Replaces NeoForge's ServerLifecycleHooks.getCurrentServer(),
     * which has no Fabric API equivalent. Call once from the mod entrypoint.
     */
    static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerTracker.currentServer = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            if (ServerTracker.currentServer == server) ServerTracker.currentServer = null;
        });
    }

    final class ServerTracker {
        public static volatile MinecraftServer currentServer;

        private ServerTracker() {
        }
    }
}
