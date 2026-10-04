package com.iafenvoy.iceandfire.platform;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public final class DeferredItem<I extends Item> extends DeferredHolder<Item, I> {
    DeferredItem(Identifier id, Function<Identifier, ? extends I> factory) {
        super(Registries.ITEM, id, factory);
    }
}
