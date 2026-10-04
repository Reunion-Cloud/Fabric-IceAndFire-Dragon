package com.iafenvoy.iceandfire.render;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafClientConfig;
import com.iafenvoy.iceandfire.registry.IafMobEffects;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SirenShaderRenderHelper {
    private static final Identifier SIREN_SHADER = Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "shaders/post/siren.json");

    /**
     * Replaces the former NeoForge ClientTickEvent.Post subscriber.
     */
    public static void initClient() {
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> tick());
    }

    private static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        if (IafClientConfig.INSTANCE.sirenShader.getValue() && player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(IafMobEffects.SIREN_CHARM.get())))
            enableShader(player);
        else disableShader(player);
    }

    private static boolean enabled(LocalPlayer player) {
        return player.getActivePostEffects().contains(SIREN_SHADER);
    }

    private static void enableShader(LocalPlayer player) {
        if (enabled(player)) return;
        List<Identifier> effects = new ArrayList<>(player.getActivePostEffects());
        effects.add(SIREN_SHADER);
        player.setActivePostEffects(effects);
    }

    private static void disableShader(LocalPlayer player) {
        if (!enabled(player)) return;
        List<Identifier> effects = new ArrayList<>(player.getActivePostEffects());
        effects.remove(SIREN_SHADER);
        player.setActivePostEffects(effects);
    }
}
