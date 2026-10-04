package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.platform.DeferredHolder;
import com.iafenvoy.iceandfire.platform.DeferredRegister;
import com.iafenvoy.iceandfire.world.feature.DeathWormSpawnFeature;
import com.iafenvoy.iceandfire.world.feature.DragonSkeletonSpawnFeature;
import com.iafenvoy.iceandfire.world.feature.HippocampusSpawnFeature;
import com.iafenvoy.iceandfire.world.feature.SeaSerpentSpawnFeature;
import com.iafenvoy.iceandfire.world.feature.StymphalianBirdSpawnFeature;
import com.iafenvoy.iceandfire.world.feature.WanderingCyclopsSpawnFeature;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class IafFeatures {
    public static final DeferredRegister<MapCodec<? extends Feature>> REGISTRY = DeferredRegister.create(Registries.FEATURE_TYPE, IceAndFire.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<DeathWormSpawnFeature>> SPAWN_DEATH_WORM = feature("spawn_death_worm", () -> DeathWormSpawnFeature.CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<DragonSkeletonSpawnFeature>> SPAWN_DRAGON_SKELETON_L = feature("spawn_dragon_skeleton_lightning", () -> DragonSkeletonSpawnFeature.codecFor(IafEntities.LIGHTNING_DRAGON.get()));
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<DragonSkeletonSpawnFeature>> SPAWN_DRAGON_SKELETON_F = feature("spawn_dragon_skeleton_fire", () -> DragonSkeletonSpawnFeature.codecFor(IafEntities.FIRE_DRAGON.get()));
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<DragonSkeletonSpawnFeature>> SPAWN_DRAGON_SKELETON_I = feature("spawn_dragon_skeleton_ice", () -> DragonSkeletonSpawnFeature.codecFor(IafEntities.ICE_DRAGON.get()));
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<HippocampusSpawnFeature>> SPAWN_HIPPOCAMPUS = feature("spawn_hippocampus", () -> HippocampusSpawnFeature.CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<SeaSerpentSpawnFeature>> SPAWN_SEA_SERPENT = feature("spawn_sea_serpent", () -> SeaSerpentSpawnFeature.CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<StymphalianBirdSpawnFeature>> SPAWN_STYMPHALIAN_BIRD = feature("spawn_stymphalian_bird", () -> StymphalianBirdSpawnFeature.CODEC);
    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<WanderingCyclopsSpawnFeature>> SPAWN_WANDERING_CYCLOPS = feature("spawn_wandering_cyclops", () -> WanderingCyclopsSpawnFeature.CODEC);

    private static <F extends Feature> DeferredHolder<MapCodec<? extends Feature>, MapCodec<F>> feature(String name, Supplier<MapCodec<F>> codec) {
        return REGISTRY.register(name, codec);
    }

    public static final ResourceKey<Feature> DREADWOOD = featureKey("dreadwood");
    public static final ResourceKey<Feature> DREADWOOD_LARGE = featureKey("dreadwood_large");

    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_DEATH_WORM = placeFeature("spawn_death_worm");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_L = placeFeature("spawn_dragon_skeleton_lightning");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_F = placeFeature("spawn_dragon_skeleton_fire");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_DRAGON_SKELETON_I = placeFeature("spawn_dragon_skeleton_ice");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_HIPPOCAMPUS = placeFeature("spawn_hippocampus");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_SEA_SERPENT = placeFeature("spawn_sea_serpent");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_STYMPHALIAN_BIRD = placeFeature("spawn_stymphalian_bird");
    public static final ResourceKey<PlacedFeature> PLACED_SPAWN_WANDERING_CYCLOPS = placeFeature("spawn_wandering_cyclops");
    public static final ResourceKey<PlacedFeature> PLACED_SILVER_ORE = placeFeature("silver_ore");
    public static final ResourceKey<PlacedFeature> PLACED_SAPPHIRE_ORE = placeFeature("sapphire_ore");
    public static final ResourceKey<PlacedFeature> PLACED_FIRE_LILY = placeFeature("fire_lily");
    public static final ResourceKey<PlacedFeature> PLACED_LIGHTNING_LILY = placeFeature("lightning_lily");
    public static final ResourceKey<PlacedFeature> PLACED_FROST_LILY = placeFeature("frost_lily");

    private static ResourceKey<Feature> featureKey(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, name));
    }

    public static ResourceKey<PlacedFeature> placeFeature(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, name));
    }
}
