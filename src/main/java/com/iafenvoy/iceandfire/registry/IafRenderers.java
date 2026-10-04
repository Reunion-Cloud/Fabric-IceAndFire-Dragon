package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.data.DragonColor;
import com.iafenvoy.iceandfire.data.SeaSerpentType;
import com.iafenvoy.iceandfire.data.TrollType;
import com.iafenvoy.iceandfire.item.DragonHornItem;
import com.iafenvoy.iceandfire.item.SummoningCrystalItem;
import com.iafenvoy.iceandfire.render.block.*;
import com.iafenvoy.iceandfire.render.entity.*;
import com.iafenvoy.iceandfire.render.item.*;
import com.iafenvoy.iceandfire.render.item.armor.BasicArmorRenderer;
import com.iafenvoy.iceandfire.render.item.armor.ScaleArmorRenderer;
import com.iafenvoy.iceandfire.render.model.animator.FireDragonTabulaModelAnimator;
import com.iafenvoy.iceandfire.render.model.animator.IceDragonTabulaModelAnimator;
import com.iafenvoy.iceandfire.render.model.animator.LightningTabulaDragonAnimator;
import com.iafenvoy.iceandfire.render.model.armor.*;
import com.iafenvoy.uranus.client.model.util.TabulaModelHandlerHelper;
import com.iafenvoy.uranus.client.render.DynamicItemRenderer;
import com.iafenvoy.uranus.client.render.armor.IArmorRendererBase;
import com.iafenvoy.uranus.util.function.MemorizeSupplier;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class IafRenderers {
    // Uranus prefixes every Tabula model lookup with models/tabula/.
    public static final Identifier FIRE_DRAGON = Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "firedragon/firedragon_ground");
    public static final Identifier ICE_DRAGON = Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "icedragon/icedragon_ground");
    public static final Identifier LIGHTNING_DRAGON = Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "lightningdragon/lightningdragon_ground");
    public static final Identifier SEA_SERPENT = Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "seaserpent/seaserpent_base");

    /**
     * Client bootstrap: entity/BE renderers, armor renderers, dynamic item renderers and
     * model predicates. Replaces NeoForge's EntityRenderersEvent.RegisterRenderers and
     * RegisterClientExtensionsEvent handlers. Call after entity/block/item registries have
     * been registered.
     */
    public static void initClient() {
        registerEntityRenderers();
        registerBlockEntityRenderers();
        registerArmorRenderers();
        registerItemRenderers();
        registerModelPredicates();
    }

    private static void registerEntityRenderers() {
        EntityRenderers.register(IafEntities.FIRE_DRAGON.get(), x -> new DragonBaseEntityRenderer<>(x, () -> TabulaModelHandlerHelper.getModel(FIRE_DRAGON, new MemorizeSupplier<>(FireDragonTabulaModelAnimator::new))));
        EntityRenderers.register(IafEntities.ICE_DRAGON.get(), manager -> new DragonBaseEntityRenderer<>(manager, () -> TabulaModelHandlerHelper.getModel(ICE_DRAGON, new MemorizeSupplier<>(IceDragonTabulaModelAnimator::new))));
        EntityRenderers.register(IafEntities.LIGHTNING_DRAGON.get(), manager -> new LightningDragonEntityRenderer(manager, () -> TabulaModelHandlerHelper.getModel(LIGHTNING_DRAGON, new MemorizeSupplier<>(LightningTabulaDragonAnimator::new))));
        EntityRenderers.register(IafEntities.DRAGON_EGG.get(), DragonEggEntityRenderer::new);
        EntityRenderers.register(IafEntities.DRAGON_ARROW.get(), DragonArrowEntityRenderer::new);
        EntityRenderers.register(IafEntities.DRAGON_SKULL.get(), DragonSkullEntityRenderer::new);
        EntityRenderers.register(IafEntities.FIRE_DRAGON_CHARGE.get(), manager -> new DragonChargeEntityRenderer(manager, true));
        EntityRenderers.register(IafEntities.ICE_DRAGON_CHARGE.get(), manager -> new DragonChargeEntityRenderer(manager, false));
        EntityRenderers.register(IafEntities.LIGHTNING_DRAGON_CHARGE.get(), LightningDragonChargeEntityRenderer::new);
        EntityRenderers.register(IafEntities.HIPPOGRYPH_EGG.get(), ThrownItemRenderer::new);
        EntityRenderers.register(IafEntities.HIPPOGRYPH.get(), HippogryphEntityRenderer::new);
        EntityRenderers.register(IafEntities.STONE_STATUE.get(), StoneStatueEntityRenderer::new);
        EntityRenderers.register(IafEntities.GORGON.get(), GorgonEntityRenderer::new);
        EntityRenderers.register(IafEntities.PIXIE.get(), PixieEntityRenderer::new);
        EntityRenderers.register(IafEntities.CYCLOPS.get(), CyclopsEntityRenderer::new);
        EntityRenderers.register(IafEntities.SIREN.get(), SirenEntityRenderer::new);
        EntityRenderers.register(IafEntities.HIPPOCAMPUS.get(), HippocampusEntityRenderer::new);
        EntityRenderers.register(IafEntities.DEATH_WORM.get(), DeathWormEntityRenderer::new);
        EntityRenderers.register(IafEntities.DEATH_WORM_EGG.get(), ThrownItemRenderer::new);
        EntityRenderers.register(IafEntities.COCKATRICE.get(), CockatriceEntityRenderer::new);
        EntityRenderers.register(IafEntities.COCKATRICE_EGG.get(), ThrownItemRenderer::new);
        EntityRenderers.register(IafEntities.STYMPHALIAN_BIRD.get(), StymphalianBirdEntityRenderer::new);
        EntityRenderers.register(IafEntities.STYMPHALIAN_FEATHER.get(), StymphalianFeatherEntityRenderer::new);
        EntityRenderers.register(IafEntities.STYMPHALIAN_ARROW.get(), StymphalianArrowEntityRenderer::new);
        EntityRenderers.register(IafEntities.TROLL.get(), TrollEntityRenderer::new);
        EntityRenderers.register(IafEntities.AMPHITHERE.get(), AmphithereEntityRenderer::new);
        EntityRenderers.register(IafEntities.AMPHITHERE_ARROW.get(), AmphithereArrowEntityRenderer::new);
        EntityRenderers.register(IafEntities.SEA_SERPENT.get(), SeaSerpentEntityRenderer::new);
        EntityRenderers.register(IafEntities.SEA_SERPENT_BUBBLES.get(), NothingEntityRenderer::new);
        EntityRenderers.register(IafEntities.SEA_SERPENT_ARROW.get(), SeaSerpentArrowEntityRenderer::new);
        EntityRenderers.register(IafEntities.CHAIN_TIE.get(), ChainTieEntityRenderer::new);
        EntityRenderers.register(IafEntities.PIXIE_CHARGE.get(), NothingEntityRenderer::new);
        EntityRenderers.register(IafEntities.TIDE_TRIDENT.get(), TideTridentEntityRenderer::new);
        EntityRenderers.register(IafEntities.MOB_SKULL.get(), MobSkullEntityRenderer::new);
        EntityRenderers.register(IafEntities.DREAD_SCUTTLER.get(), DreadScuttlerEntityRenderer::new);
        EntityRenderers.register(IafEntities.DREAD_GHOUL.get(), DreadGhoulEntityRenderer::new);
        EntityRenderers.register(IafEntities.DREAD_BEAST.get(), DreadBeastEntityRenderer::new);
        EntityRenderers.register(IafEntities.DREAD_SCUTTLER.get(), DreadScuttlerEntityRenderer::new);
        EntityRenderers.register(IafEntities.DREAD_THRALL.get(), DreadThrallEntityRenderer::new);
        EntityRenderers.register(IafEntities.DREAD_LICH.get(), DreadLichEntityRenderer::new);
        EntityRenderers.register(IafEntities.DREAD_LICH_SKULL.get(), DreadLichSkullEntityRenderer::new);
        EntityRenderers.register(IafEntities.DREAD_KNIGHT.get(), DreadKnightEntityRenderer::new);
        EntityRenderers.register(IafEntities.DREAD_HORSE.get(), DreadHorseEntityRenderer::new);
        EntityRenderers.register(IafEntities.HYDRA.get(), HydraEntityRenderer::new);
        EntityRenderers.register(IafEntities.HYDRA_BREATH.get(), NothingEntityRenderer::new);
        EntityRenderers.register(IafEntities.HYDRA_ARROW.get(), HydraArrowEntityRenderer::new);
        EntityRenderers.register(IafEntities.GHOST.get(), GhostEntityRenderer::new);
        EntityRenderers.register(IafEntities.GHOST_SWORD.get(), GhostSwordEntityRenderer::new);
    }

    // Particle providers live in IafParticles.initClient() (fabric-particles-v1).

    private static void registerBlockEntityRenderers() {
        BlockEntityRenderers.register(IafBlockEntities.PODIUM.get(), PodiumBlockEntityRenderer::new);
        BlockEntityRenderers.register(IafBlockEntities.IAF_LECTERN.get(), LecternBlockEntityRenderer::new);
        BlockEntityRenderers.register(IafBlockEntities.EGG_IN_ICE.get(), EggInIceBlockEntityRenderer::new);
        BlockEntityRenderers.register(IafBlockEntities.PIXIE_HOUSE.get(), PixieHouseBlockEntityRenderer::new);
        BlockEntityRenderers.register(IafBlockEntities.PIXIE_JAR.get(), JarBlockEntityRenderer::new);
        BlockEntityRenderers.register(IafBlockEntities.DREAD_SPAWNER.get(), DreadSpawnerBlockEntityRenderer::new);
        BlockEntityRenderers.register(IafBlockEntities.GHOST_CHEST.get(), ChestRenderer::new);
    }

    public static void registerArmorRenderers() {
        // Armor textures are now provided by the 26.3 data-driven assets/iceandfire/equipment/*.json files.
        // The old Uranus armor renderer API accepted Minecraft Model/LayerType instances and is not used by the current Fabric API.
    }

    public static void registerItemRenderers() {
        DynamicItemRenderer.RENDERERS.put(IafItems.DEATHWORM_GAUNTLET_RED.get(), new DeathwormGauntletRenderer());
        DynamicItemRenderer.RENDERERS.put(IafItems.DEATHWORM_GAUNTLET_YELLOW.get(), new DeathwormGauntletRenderer());
        DynamicItemRenderer.RENDERERS.put(IafItems.DEATHWORM_GAUNTLET_WHITE.get(), new DeathwormGauntletRenderer());
        DynamicItemRenderer.RENDERERS.put(IafItems.GORGON_HEAD.get(), new GorgonHeadRenderer());
        DynamicItemRenderer.RENDERERS.put(IafItems.TIDE_TRIDENT.get(), new TideTridentItemRenderer());
        DynamicItemRenderer.RENDERERS.put(IafBlocks.PIXIE_HOUSE_BIRCH.get().asItem(), new MiscItemRenderer());
        DynamicItemRenderer.RENDERERS.put(IafBlocks.PIXIE_HOUSE_OAK.get().asItem(), new MiscItemRenderer());
        DynamicItemRenderer.RENDERERS.put(IafBlocks.PIXIE_HOUSE_DARK_OAK.get().asItem(), new MiscItemRenderer());
        DynamicItemRenderer.RENDERERS.put(IafBlocks.PIXIE_HOUSE_SPRUCE.get().asItem(), new MiscItemRenderer());
        DynamicItemRenderer.RENDERERS.put(IafBlocks.PIXIE_HOUSE_MUSHROOM_RED.get().asItem(), new MiscItemRenderer());
        DynamicItemRenderer.RENDERERS.put(IafBlocks.PIXIE_HOUSE_MUSHROOM_BROWN.get().asItem(), new MiscItemRenderer());
        for (TrollType.BuiltinWeapon weapon : TrollType.BuiltinWeapon.values())
            DynamicItemRenderer.RENDERERS.put(weapon.getItem(), new TrollWeaponRenderer());
    }


    public static void registerModelPredicates() {
        ConditionalItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "has_dragon"), HasDragonProperty.MAP_CODEC);
        RangeSelectItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "iceorfire"), DragonHornTypeProperty.MAP_CODEC);
    }

    public record HasDragonProperty() implements ConditionalItemModelProperty {
        public static final MapCodec<HasDragonProperty> MAP_CODEC = MapCodec.unit(new HasDragonProperty());

        @Override
        public boolean get(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, ItemDisplayContext context) {
            return SummoningCrystalItem.hasDragon(stack);
        }

        @Override
        public MapCodec<HasDragonProperty> type() {
            return MAP_CODEC;
        }
    }

    public record DragonHornTypeProperty() implements RangeSelectItemModelProperty {
        public static final MapCodec<DragonHornTypeProperty> MAP_CODEC = MapCodec.unit(new DragonHornTypeProperty());

        @Override
        public float get(ItemStack stack, ClientLevel level, ItemOwner owner, int seed) {
            return DragonHornItem.getDragonType(stack) * 0.25F;
        }

        @Override
        public MapCodec<DragonHornTypeProperty> type() {
            return MAP_CODEC;
        }
    }
}
