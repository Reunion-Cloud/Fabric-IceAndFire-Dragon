package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.event.handler.ClientEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;

public final class IafKeyMappings {
    public static final KeyMapping DRAGON_BREATH = new KeyMapping("key.dragon_fireAttack", InputConstants.KEY_R, KeyMapping.Category.GAMEPLAY);
    public static final KeyMapping DRAGON_STRIKE = new KeyMapping("key.dragon_strike", InputConstants.KEY_G, KeyMapping.Category.GAMEPLAY);
    public static final KeyMapping DRAGON_DOWN = new KeyMapping("key.dragon_down", InputConstants.KEY_X, KeyMapping.Category.GAMEPLAY);
    public static final KeyMapping DRAGON_CHANGE_VIEW = new KeyMapping("key.dragon_change_view", InputConstants.KEY_F7, KeyMapping.Category.GAMEPLAY);

    /**
     * Registers the key mappings and the per-tick handling. Replaces NeoForge's
     * RegisterKeyMappingsEvent handler and the ClientTickEvent.Post subscriber.
     */
    public static void initClient() {
        KeyMappingHelper.registerKeyMapping(DRAGON_BREATH);
        KeyMappingHelper.registerKeyMapping(DRAGON_STRIKE);
        KeyMappingHelper.registerKeyMapping(DRAGON_DOWN);
        KeyMappingHelper.registerKeyMapping(DRAGON_CHANGE_VIEW);

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (DRAGON_CHANGE_VIEW.consumeClick()) {
                if (ClientEvents.currentView + 1 > 3) ClientEvents.currentView = 0;
                else ClientEvents.currentView++;
            }
        });
    }
}
