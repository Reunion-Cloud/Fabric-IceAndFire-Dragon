package com.iafenvoy.iceandfire.recipe;

import com.iafenvoy.iceandfire.registry.IafRecipeSerializers;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;

/**
 * Registers {@code iceandfire:dragonforge} recipes for Fabric client synchronization.
 * The client stores them in {@link DragonForgeRecipeCache}.
 */
public final class DragonForgeRecipeSync {
    private DragonForgeRecipeSync() {
    }

    public static void init() {
        RecipeSynchronization.synchronizeRecipeSerializer(IafRecipeSerializers.DRAGONFORGE_SERIALIZER.get());
    }
}
