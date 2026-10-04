package com.iafenvoy.iceandfire.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Recreates exploding-block launch behavior within Minecraft 26.3's server explosion pipeline.
 */
public final class BlockLaunchExplosion {
    private static final ThreadLocal<Deque<LaunchRequest>> ACTIVE_REQUESTS = ThreadLocal.withInitial(ArrayDeque::new);

    private BlockLaunchExplosion() {
    }

    public static void explode(Level level, Mob source, double x, double y, double z, float radius) {
        explode(level, source, null, x, y, z, radius, true);
    }

    public static void explode(Level level, Mob source, @Nullable DamageSource damageSource, double x, double y, double z, float radius, boolean destroyBlocks) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        LaunchRequest request = new LaunchRequest(source, x, y, z, radius, destroyBlocks);
        Deque<LaunchRequest> requests = ACTIVE_REQUESTS.get();
        requests.push(request);
        try {
            // Preserve vanilla damage, sounds, particles, and client synchronization.
            serverLevel.explode(source, damageSource, null, x, y, z, radius, false, Level.ExplosionInteraction.MOB);
        } finally {
            requests.pop();
            if (requests.isEmpty()) ACTIVE_REQUESTS.remove();
        }
    }

    /**
     * Called at the head of {@link ServerExplosion}'s block-interaction method by
     * {@code ServerExplosionMixin}. Returning true means vanilla block interaction must be skipped.
     */
    public static boolean handleExplosionBlocks(ServerExplosion explosion, List<BlockPos> affectedBlocks) {
        Deque<LaunchRequest> requests = ACTIVE_REQUESTS.get();
        LaunchRequest request = requests.peek();
        if (request == null || !request.matches(explosion)) return false;

        if (!request.destroyBlocks) {
            affectedBlocks.clear();
            return true;
        }

        ServerLevel level = explosion.level();
        List<BlockPos> blocksToLaunch = new ArrayList<>(affectedBlocks);
        affectedBlocks.clear();
        for (BlockPos pos : blocksToLaunch) {
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;

            FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, pos, state);
            Vec3 offset = fallingBlock.getEyePosition().subtract(request.center());
            double normalizedDistance = Math.sqrt(offset.lengthSqr()) / (request.radius * 2.0F);
            double exposure = ServerExplosion.getSeenPercent(request.center(), fallingBlock);
            double force = Math.max(0.0, 1.0 - normalizedDistance) * exposure;
            fallingBlock.setDeltaMovement(fallingBlock.getDeltaMovement().add(offset.scale(force)));
        }
        return true;
    }

    private record LaunchRequest(Entity source, double x, double y, double z, float radius, boolean destroyBlocks) {
        private Vec3 center() {
            return new Vec3(this.x, this.y, this.z);
        }

        private boolean matches(ServerExplosion explosion) {
            return explosion.getDirectSourceEntity() == this.source
                    && explosion.radius() == this.radius
                    && explosion.center().equals(this.center());
        }
    }
}
