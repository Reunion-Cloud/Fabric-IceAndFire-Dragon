package com.iafenvoy.iceandfire.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Mod Menu integration: surfaces the Ice and Fire config screen
 * (declaratively wired as the "modmenu" entrypoint in fabric.mod.json).
 * Mirrors com.iafenvoy.jupiter._loader.fabric.compat.ModMenu.
 */
public final class IceAndFireModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return IceAndFireFabricClient::createConfigScreen;
    }
}
