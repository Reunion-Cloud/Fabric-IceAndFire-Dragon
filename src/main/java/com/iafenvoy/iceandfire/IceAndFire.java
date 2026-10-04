package com.iafenvoy.iceandfire;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.data.DragonColor;
import com.iafenvoy.iceandfire.data.IafSkullType;
import com.iafenvoy.iceandfire.data.SeaSerpentType;
import com.iafenvoy.iceandfire.data.TrollType;
import com.iafenvoy.iceandfire.event.handler.ServerEvents;
import com.iafenvoy.iceandfire.network.NetworkManager;
import com.iafenvoy.iceandfire.recipe.DragonForgeRecipeSync;
import com.iafenvoy.iceandfire.registry.*;
import com.iafenvoy.iceandfire.world.DangerousGeneration;
import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.ServerConfigManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class IceAndFire {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "iceandfire";
    public static final String VERSION;

    static {
        VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).map(c -> c.getMetadata().getVersion().getFriendlyString()).orElse("UNKNOWN");
    }

    //TODO: IceAndFire::id is a temporary fix to capable with old version, should be removed in later versions
    public static Identifier id(String path) {
        if (path.contains(":")) return Identifier.tryParse(path);
        else return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void init() {
        ConfigManager.getInstance().registerConfigHandler(IafCommonConfig.INSTANCE);
        ServerConfigManager.registerServerConfig(IafCommonConfig.INSTANCE, ServerConfigManager.PermissionChecker.IS_OPERATOR);

        IafBestiaryPages.init();
        IafDragonTypes.init();
        IafDragonColors.init();
        IafHippogryphTypes.init();
        IafSeaSerpentTypes.init();
        IafTrollTypes.init();

        DragonColor.initArmors();
        SeaSerpentType.initArmors();
        IafSkullType.initItems();
        TrollType.initArmors();

        IafAttachments.init();
        IafAttributes.REGISTRY.register();
        IafSounds.REGISTRY.register();
        IafDataComponents.REGISTRY.register();
        IafBlocks.REGISTRY.register();
        IafBlockEntities.REGISTRY.register();
        IafEntities.REGISTRY.register();
        IafItems.REGISTRY.register();
        IafCreativeModeTabs.REGISTRY.register();
        IafLoots.REGISTRY.register();
        IafRecipes.REGISTRY.register();
        IafRecipeSerializers.REGISTRY.register();
        IafParticles.REGISTRY.register();
        IafProcessors.REGISTRY.register();
        IafFeatures.REGISTRY.register();
        IafMenus.REGISTRY.register();
        IafMobEffects.REGISTRY.register();
        IafPotions.REGISTRY.register();
        IafStructurePieces.REGISTRY.register();
        IafStructureTypes.REGISTRY.register();
        //Trade
        IafTrades.POI_REGISTRY.register();
        IafTrades.PROFESSION_REGISTRY.register();

        // Needs entity types registered before spawn/attribute wiring
        IafEntities.init();
        // Vanilla biome spawn/feature data (replaces the neoforge biome_modifier JSONs)
        IceAndFireBiomes.init();
        // Tracks the running server (replaces NeoForge's ServerLifecycleHooks.getCurrentServer)
        DangerousGeneration.init();
        NetworkManager.registerPayloads();
        ServerEvents.init();
        // Former FMLCommonSetupEvent body
        IafTrades.registerPoiStates();
        IafRecipes.init();
        DragonForgeRecipeSync.init();
        IafTiers.init();
    }
}
