package com.iafenvoy.iceandfire.recipe;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.IafRecipes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

/**
 * Client copy of dragon-forge recipes delivered by
 * {@link net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization}.
 */
public final class DragonForgeRecipeCache {
    private static volatile List<DragonForgeRecipe> RECIPES = List.of();

    private DragonForgeRecipeCache() {
    }

    public static List<DragonForgeRecipe> get() {
        return RECIPES;
    }

    public static void initClient() {
        ClientRecipeSynchronizedEvent.EVENT.register((client, synchronizedRecipes) -> {
            RECIPES = synchronizedRecipes.getAllOfType(IafRecipes.DRAGON_FORGE_TYPE.get()).stream().map(RecipeHolder::value).toList();
            IceAndFire.LOGGER.info("Received {} dragon forge recipes from Fabric sync", RECIPES.size());
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> RECIPES = List.of());
    }
}
