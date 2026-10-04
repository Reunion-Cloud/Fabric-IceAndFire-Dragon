package com.iafenvoy.iceandfire.platform;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public final class DeferredBlock<T extends Block> extends DeferredHolder<Block, T> {
    DeferredBlock(Identifier id, Function<Identifier, ? extends T> factory) {
        super(Registries.BLOCK, id, factory);
    }
}
