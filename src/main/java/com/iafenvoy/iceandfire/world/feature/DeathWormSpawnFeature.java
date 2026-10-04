package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.DeathWormEntity;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.world.DangerousGeneration;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

public class DeathWormSpawnFeature implements Feature, DangerousGeneration {
    public static final MapCodec<DeathWormSpawnFeature> CODEC = MapCodec.unit(DeathWormSpawnFeature::new);

    public DeathWormSpawnFeature() {
    }

    @Override
    public MapCodec<DeathWormSpawnFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockPos pos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin.offset(8, 0, 8));
        if (this.isFarEnoughFromSpawn(world, pos) && random.nextDouble() < IafCommonConfig.INSTANCE.deathworm.spawnChance.getValue()) {
            DeathWormEntity deathWorm = IafEntities.DEATH_WORM.get().create(world.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
            assert deathWorm != null;
            deathWorm.setPos(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F);
            deathWorm.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.CHUNK_GENERATION, null);
            world.addFreshEntity(deathWorm);
        }
        return true;
    }
}
