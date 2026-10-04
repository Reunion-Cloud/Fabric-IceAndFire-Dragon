package com.iafenvoy.iceandfire.platform;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Fabric replacement for NeoForge's DeferredHolder: a lazy holder that
 * resolves (and registers) its value when {@link #registerInto} is invoked
 * by the owning DeferredRegister.
 * <p>
 * In 26.3 {@link Holder} is sealed (permits Direct and Reference), so this class
 * extends the non-sealed {@link Holder.Reference} instead of implementing Holder.
 * Tag / component / serialization queries are delegated to the real registry
 * holder obtained at registration time, so they stay correct after tags load.
 */
public class DeferredHolder<T, I extends T> extends Holder.Reference<T> implements Supplier<T> {
    private final Identifier id;
    private final ResourceKey<T> valueKey;
    private final Function<Identifier, ? extends T> factory;
    private Holder.Reference<T> backing;
    private boolean resolved = false;

    DeferredHolder(ResourceKey<Registry<T>> registryKey, Identifier id, Function<Identifier, ? extends T> factory) {
        super(Holder.Reference.Type.STAND_ALONE, owner(), ResourceKey.create(registryKey, id), null);
        this.id = id;
        this.valueKey = ResourceKey.create(registryKey, id);
        this.factory = factory;
    }

    private static <T> HolderOwner<T> owner() {
        return new HolderOwner<>() {
        };
    }

    static <T, I extends T> DeferredHolder<T, I> create(ResourceKey<Registry<T>> registryKey, Identifier id, Function<Identifier, ? extends T> factory) {
        return new DeferredHolder<>(registryKey, id, factory);
    }

    void registerInto(Registry<T> registry) {
        if (this.resolved) return;
        T created = this.factory.apply(this.id);
        this.backing = Registry.registerForHolder(registry, this.valueKey, created);
        this.bindValue(created);
        this.resolved = true;
    }

    @SuppressWarnings("unchecked")
    public I get() {
        if (!this.resolved) throw new IllegalStateException("Registry object not present yet: " + this.id);
        return (I) this.value();
    }

    public Identifier getId() {
        return this.id;
    }

    public ResourceKey<T> getKey() {
        return this.valueKey;
    }

    @Override
    public boolean is(TagKey<T> tag) {
        return this.backing != null && this.backing.is(tag);
    }

    @Override
    public Stream<TagKey<T>> tags() {
        return this.backing != null ? this.backing.tags() : Stream.empty();
    }

    @Override
    public boolean areComponentsBound() {
        return this.backing != null && this.backing.areComponentsBound();
    }

    @Override
    public DataComponentMap components() {
        return this.backing != null && this.backing.areComponentsBound() ? this.backing.components() : DataComponentMap.EMPTY;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> owner) {
        return this.backing == null || this.backing.canSerializeIn(owner);
    }
}
