package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.CyclopsEntity;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.world.DangerousGeneration;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

public class WanderingCyclopsSpawnFeature implements Feature, DangerousGeneration {
    public static final MapCodec<WanderingCyclopsSpawnFeature> CODEC = MapCodec.unit(WanderingCyclopsSpawnFeature::new);

    public WanderingCyclopsSpawnFeature() {
    }

    @Override
    public MapCodec<WanderingCyclopsSpawnFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockPos pos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin.offset(8, 0, 8));
        if (this.isFarEnoughFromSpawn(world, pos) && random.nextDouble() < IafCommonConfig.INSTANCE.cyclops.spawnWanderingChance.getValue() && random.nextInt(12) == 0) {
            CyclopsEntity cyclops = IafEntities.CYCLOPS.get().create(world.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
            assert cyclops != null;
            cyclops.setPos(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F);
            cyclops.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.SPAWNER, null);
            world.addFreshEntity(cyclops);
            for (int i = 0; i < 3 + random.nextInt(3); i++) {
                Sheep sheep = EntityTypes.SHEEP.create(world.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
                assert sheep != null;
                sheep.setPos(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F);
                sheep.setColor(Sheep.getRandomSheepColor(world, pos));
                world.addFreshEntity(sheep);
            }
        }
        return true;
    }
}
