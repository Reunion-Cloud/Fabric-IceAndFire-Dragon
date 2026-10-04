package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.registry.tag.IafBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class IceAndFireBiomes {
    private IceAndFireBiomes() {
    }

    public static void init() {
        registerSpawns();
        registerFeatures();
    }

    private static void registerSpawns() {
        if (IafCommonConfig.INSTANCE.hippogryphs.spawn.getValue() && IafCommonConfig.INSTANCE.hippogryphs.spawnWeight.getValue() > 0)
            BiomeModifications.addSpawn(BiomeSelectors.tag(IafBiomeTags.HIPPOGRYPH), MobCategory.CREATURE, IafEntities.HIPPOGRYPH.get(), IafCommonConfig.INSTANCE.hippogryphs.spawnWeight.getValue(), 1, 1);
        if (IafCommonConfig.INSTANCE.lich.spawn.getValue() && IafCommonConfig.INSTANCE.lich.spawnWeight.getValue() > 0)
            BiomeModifications.addSpawn(BiomeSelectors.tag(IafBiomeTags.MAUSOLEUM), MobCategory.MONSTER, IafEntities.DREAD_LICH.get(), IafCommonConfig.INSTANCE.lich.spawnWeight.getValue(), 1, 1);
        if (IafCommonConfig.INSTANCE.cockatrice.spawn.getValue() && IafCommonConfig.INSTANCE.cockatrice.spawnWeight.getValue() > 0)
            BiomeModifications.addSpawn(BiomeSelectors.tag(IafBiomeTags.COCKATRICE), MobCategory.CREATURE, IafEntities.COCKATRICE.get(), IafCommonConfig.INSTANCE.cockatrice.spawnWeight.getValue(), 1, 2);
        if (IafCommonConfig.INSTANCE.amphithere.spawn.getValue() && IafCommonConfig.INSTANCE.amphithere.spawnWeight.getValue() > 0)
            BiomeModifications.addSpawn(BiomeSelectors.tag(IafBiomeTags.AMPHITHERE), MobCategory.CREATURE, IafEntities.AMPHITHERE.get(), IafCommonConfig.INSTANCE.amphithere.spawnWeight.getValue(), 1, 3);
        if (IafCommonConfig.INSTANCE.troll.spawn.getValue() && IafCommonConfig.INSTANCE.troll.spawnWeight.getValue() > 0)
            BiomeModifications.addSpawn(BiomeSelectors.tag(IafBiomeTags.TROLL), MobCategory.MONSTER, IafEntities.TROLL.get(), IafCommonConfig.INSTANCE.troll.spawnWeight.getValue(), 1, 3);
    }

    private static void registerFeatures() {
        addFeatureToBiome(IafBiomeTags.FIRE, IafFeatures.PLACED_FIRE_LILY, GenerationStep.Decoration.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.ICE, IafFeatures.PLACED_FROST_LILY, GenerationStep.Decoration.VEGETAL_DECORATION);
        addFeatureToBiome(IafBiomeTags.LIGHTNING, IafFeatures.PLACED_LIGHTNING_LILY, GenerationStep.Decoration.VEGETAL_DECORATION);

        addFeatureToBiome(IafBiomeTags.SILVER_ORE, IafFeatures.PLACED_SILVER_ORE, GenerationStep.Decoration.UNDERGROUND_ORES);
        addFeatureToBiome(IafBiomeTags.SAPPHIRE_ORE, IafFeatures.PLACED_SAPPHIRE_ORE, GenerationStep.Decoration.UNDERGROUND_ORES);

        addFeatureToBiome(IafBiomeTags.FIRE, IafFeatures.PLACED_SPAWN_DRAGON_SKELETON_F, GenerationStep.Decoration.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.ICE, IafFeatures.PLACED_SPAWN_DRAGON_SKELETON_I, GenerationStep.Decoration.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.LIGHTNING, IafFeatures.PLACED_SPAWN_DRAGON_SKELETON_L, GenerationStep.Decoration.SURFACE_STRUCTURES);

        addFeatureToBiome(IafBiomeTags.DEATHWORM, IafFeatures.PLACED_SPAWN_DEATH_WORM, GenerationStep.Decoration.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.WANDERING_CYCLOPS, IafFeatures.PLACED_SPAWN_WANDERING_CYCLOPS, GenerationStep.Decoration.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.HIPPOCAMPUS, IafFeatures.PLACED_SPAWN_HIPPOCAMPUS, GenerationStep.Decoration.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.SEA_SERPENT, IafFeatures.PLACED_SPAWN_SEA_SERPENT, GenerationStep.Decoration.SURFACE_STRUCTURES);
        addFeatureToBiome(IafBiomeTags.STYMPHALIAN_BIRD, IafFeatures.PLACED_SPAWN_STYMPHALIAN_BIRD, GenerationStep.Decoration.SURFACE_STRUCTURES);
    }

    private static void addFeatureToBiome(TagKey<Biome> biomeTag, ResourceKey<PlacedFeature> featureResource, GenerationStep.Decoration step) {
        BiomeModifications.addFeature(BiomeSelectors.tag(biomeTag), step, featureResource);
    }
}
