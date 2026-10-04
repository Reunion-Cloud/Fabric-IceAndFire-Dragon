package com.iafenvoy.iceandfire.platform;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Fabric replacement for NeoForge's DeferredRegister. Entries are captured
 * lazily at class-load time and written into the vanilla registry when
 * {@link #register()} runs from the mod entrypoint.
 */
public class DeferredRegister<T> {
    private final ResourceKey<Registry<T>> key;
    protected final String modId;
    private final List<DeferredHolder<T, ?>> pending = new ArrayList<>();

    protected DeferredRegister(ResourceKey<Registry<T>> key, String modId) {
        this.key = key;
        this.modId = modId;
    }

    public static <T> DeferredRegister<T> create(ResourceKey<Registry<T>> key, String modId) {
        return new DeferredRegister<>(key, modId);
    }

    public static DeferredRegister.Blocks createBlocks(String modId) {
        return new Blocks(modId);
    }

    public static DeferredRegister.Items createItems(String modId) {
        return new Items(modId);
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> factory) {
        return this.register(name, id -> factory.get());
    }

    @SuppressWarnings("unchecked")
    public <I extends T> DeferredHolder<T, I> register(String name, Function<Identifier, ? extends I> factory) {
        DeferredHolder<T, I> holder = DeferredHolder.create(this.key, Identifier.fromNamespaceAndPath(this.modId, name), (Function<Identifier, ? extends T>) factory);
        this.pending.add(holder);
        return holder;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void register() {
        Registry<T> registry = (Registry<T>) ((Registry) BuiltInRegistries.REGISTRY).getValue(this.key);
        if (registry == null) throw new IllegalStateException("Unknown registry: " + this.key);
        for (DeferredHolder<T, ?> holder : List.copyOf(this.pending))
            holder.registerInto(registry);
        this.pending.clear();
    }

    public static final class Blocks extends DeferredRegister<Block> {
        Blocks(String modId) {
            super(Registries.BLOCK, modId);
        }

        public <B extends Block> DeferredBlock<B> register(String name, Supplier<? extends B> factory) {
            return this.register(name, id -> factory.get());
        }

        public <B extends Block> DeferredBlock<B> register(String name, Function<Identifier, ? extends B> factory) {
            DeferredBlock<B> holder = new DeferredBlock<>(Identifier.fromNamespaceAndPath(this.modId, name), factory);
            this.pendingInternal(holder);
            return holder;
        }
    }

    public static final class Items extends DeferredRegister<Item> {
        Items(String modId) {
            super(Registries.ITEM, modId);
        }

        public <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> factory) {
            return this.register(name, id -> factory.get());
        }

        public <I extends Item> DeferredItem<I> register(String name, Function<Identifier, ? extends I> factory) {
            DeferredItem<I> holder = new DeferredItem<>(Identifier.fromNamespaceAndPath(this.modId, name), factory);
            this.pendingInternal(holder);
            return holder;
        }
    }

    @SuppressWarnings("unchecked")
    protected void pendingInternal(DeferredHolder<? extends T, ? extends T> holder) {
        this.pending.add((DeferredHolder<T, ?>) holder);
    }
}
