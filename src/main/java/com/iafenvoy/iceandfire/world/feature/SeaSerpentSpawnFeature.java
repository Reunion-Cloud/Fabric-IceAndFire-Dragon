package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.SeaSerpentEntity;
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
import net.minecraft.world.level.material.Fluids;

public class SeaSerpentSpawnFeature implements Feature, DangerousGeneration {
    public static final MapCodec<SeaSerpentSpawnFeature> CODEC = MapCodec.unit(SeaSerpentSpawnFeature::new);

    public SeaSerpentSpawnFeature() {
    }

    @Override
    public MapCodec<SeaSerpentSpawnFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockPos pos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin.offset(8, 0, 8));
        BlockPos oceanPos = world.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, pos.offset(8, 0, 8));
        if (this.isFarEnoughFromSpawn(world, pos) && random.nextDouble() < IafCommonConfig.INSTANCE.seaSerpent.spawnChance.getValue()) {
            BlockPos spawnPos = oceanPos.offset(random.nextInt(10) - 5, random.nextInt(30), random.nextInt(10) - 5);
            if (world.getFluidState(spawnPos).getType() == Fluids.WATER) {
                SeaSerpentEntity serpent = IafEntities.SEA_SERPENT.get().create(world.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
                assert serpent != null;
                serpent.onWorldSpawn(random);
                serpent.snapTo(spawnPos.getX() + 0.5F, spawnPos.getY() + 0.5F, spawnPos.getZ() + 0.5F, 0, 0);
                world.addFreshEntity(serpent);
            }
        }
        return true;
    }
}
