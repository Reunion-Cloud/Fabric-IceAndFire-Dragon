package com.iafenvoy.iceandfire.fabric;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafClientConfig;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.event.handler.ClientEvents;
import com.iafenvoy.iceandfire.network.ClientNetworkHandlers;
import com.iafenvoy.iceandfire.recipe.DragonForgeRecipeCache;
import com.iafenvoy.iceandfire.registry.IafKeyMappings;
import com.iafenvoy.iceandfire.registry.IafMenus;
import com.iafenvoy.iceandfire.registry.IafParticles;
import com.iafenvoy.iceandfire.registry.IafRenderers;
import com.iafenvoy.iceandfire.render.SirenShaderRenderHelper;
import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.render.screen.ConfigSelectScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class IceAndFireFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().registerConfigHandler(IafClientConfig.INSTANCE);

        IafRenderers.initClient();
        IafKeyMappings.initClient();
        IafMenus.initClient();
        IafParticles.initClient();
        ClientNetworkHandlers.init();
        DragonForgeRecipeCache.initClient();
        ClientEvents.init();
        SirenShaderRenderHelper.initClient();

        // Built-in "legacy" resource pack (replaces NeoForge's AddPackFindersEvent).
        // Resolves the resourcepacks/iaf_legacy folder inside the mod jar.
        FabricLoader.getInstance().getModContainer(IceAndFire.MOD_ID).ifPresent(container ->
                ResourceLoader.registerBuiltinPack(
                        Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "iaf_legacy"),
                        container,
                        Component.translatable("resourcePack.iceandfire.legacy.name"),
                        PackActivationType.NORMAL));
    }

    /**
     * Config screen factory shared with the Mod Menu entrypoint
     * (replaces NeoForge's IConfigScreenFactory extension point).
     */
    public static Screen createConfigScreen(Screen parent) {
        return ConfigSelectScreen.builder(Component.translatable("config.iceandfire.title"), parent)
                .server(IafCommonConfig.INSTANCE)
                .client(IafClientConfig.INSTANCE)
                .build();
    }
}
