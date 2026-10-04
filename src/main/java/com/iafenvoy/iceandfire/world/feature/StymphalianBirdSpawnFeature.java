package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.StymphalianBirdEntity;
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

public class StymphalianBirdSpawnFeature implements Feature, DangerousGeneration {
    public static final MapCodec<StymphalianBirdSpawnFeature> CODEC = MapCodec.unit(StymphalianBirdSpawnFeature::new);

    public StymphalianBirdSpawnFeature() {
    }

    @Override
    public MapCodec<StymphalianBirdSpawnFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockPos pos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin.offset(8, 0, 8));
        if (this.isFarEnoughFromSpawn(world, pos) && random.nextDouble() < IafCommonConfig.INSTANCE.stymphalianBird.spawnChance.getValue())
            for (int i = 0; i < 4 + random.nextInt(4); i++) {
                BlockPos spawnPos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos.offset(random.nextInt(10) - 5, 0, random.nextInt(10) - 5));
                if (world.getBlockState(spawnPos.below()).canOcclude()) {
                    StymphalianBirdEntity bird = IafEntities.STYMPHALIAN_BIRD.get().create(world.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
                    assert bird != null;
                    bird.snapTo(spawnPos.getX() + 0.5F, spawnPos.getY() + 1.5F, spawnPos.getZ() + 0.5F, 0, 0);
                    world.addFreshEntity(bird);
                }
            }
        return true;
    }
}
