package com.iafenvoy.iceandfire.world.feature;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import com.iafenvoy.uranus.util.RandomHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

public class DragonSkeletonSpawnFeature implements Feature {
    private final EntityType<? extends DragonBaseEntity> dragonType;
    private final MapCodec<DragonSkeletonSpawnFeature> codec;

    public DragonSkeletonSpawnFeature(EntityType<? extends DragonBaseEntity> dragonType) {
        this.dragonType = dragonType;
        this.codec = MapCodec.unit(this);
    }

    public static MapCodec<DragonSkeletonSpawnFeature> codecFor(EntityType<? extends DragonBaseEntity> type) {
        return new DragonSkeletonSpawnFeature(type).codec();
    }

    @Override
    public MapCodec<DragonSkeletonSpawnFeature> codec() {
        return this.codec;
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockPos pos = world.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, origin.offset(8, 0, 8));
        if (IafCommonConfig.INSTANCE.dragon.generateSkeletons.getValue() && random.nextDouble() < IafCommonConfig.INSTANCE.dragon.generateSkeletonChance.getValue()) {
            DragonBaseEntity dragon = this.dragonType.create(world.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
            assert dragon != null;
            dragon.setPos(pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F);
            int age = 10 + random.nextInt(100);
            dragon.growDragon(age);
            dragon.modelDeadProgress = 20;
            dragon.setModelDead(true);
            dragon.setDeathStage(age / 10);
            dragon.setYRot(random.nextInt(360));
            dragon.setVariant(RandomHelper.randomOne(dragon.dragonType.colors()).getName());
            world.addFreshEntity(dragon);
        }
        return true;
    }
}
