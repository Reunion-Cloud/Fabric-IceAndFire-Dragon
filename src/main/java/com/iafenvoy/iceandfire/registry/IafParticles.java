package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.particle.*;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import com.iafenvoy.iceandfire.platform.DeferredHolder;
import com.iafenvoy.iceandfire.platform.DeferredRegister;

import java.util.function.Supplier;

public final class IafParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, IceAndFire.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOD = register("blood", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, DragonFlameParticleType> DRAGON_FLAME = register("dragon_flame", DragonFlameParticleType::new);
    public static final DeferredHolder<ParticleType<?>, DragonFrostParticleType> DRAGON_FROST = register("dragon_frost", DragonFrostParticleType::new);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DREAD_TORCH = register("dread_torch", () -> new SimpleParticleType(true));
    //TODO::What is this?
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GHOST_APPEARANCE = register("ghost_appearance", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HYDRA_BREATH = register("hydra_breath", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PIXIE_DUST = register("pixie_dust", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SERPENT_BUBBLE = register("serpent_bubble", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SIREN_MUSIC = register("siren_music", () -> new SimpleParticleType(true));

    private static <T extends ParticleType<?>> DeferredHolder<ParticleType<?>, T> register(String name, Supplier<T> obj) {
        return REGISTRY.register(name, obj);
    }

    /**
     * Registers the client particle providers. Replaces NeoForge's RegisterParticleProvidersEvent
     * handler (fabric-particles-v1 ParticleProviderRegistry). Call after {@link #REGISTRY} has
     * been registered (particle types must exist).
     */
    public static void initClient() {
        ParticleProviderRegistry registry = ParticleProviderRegistry.getInstance();
        registry.register(BLOOD.get(), BloodParticle::factory);
        registry.register(DRAGON_FLAME.get(), DragonFlameParticle::factory);
        registry.register(DRAGON_FROST.get(), DragonFrostParticle::factory);
        registry.register(DREAD_TORCH.get(), DreadTorchParticle::factory);
        registry.register(GHOST_APPEARANCE.get(), GhostAppearanceParticle.factory());
        registry.register(HYDRA_BREATH.get(), HydraBreathParticle::factory);
        registry.register(PIXIE_DUST.get(), PixieDustParticle::factory);
        registry.register(SERPENT_BUBBLE.get(), SerpentBubbleParticle::factory);
        registry.register(SIREN_MUSIC.get(), SirenMusicParticle::factory);
    }
}
