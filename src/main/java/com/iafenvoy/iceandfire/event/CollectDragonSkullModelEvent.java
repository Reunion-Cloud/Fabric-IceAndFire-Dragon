package com.iafenvoy.iceandfire.event;

import com.iafenvoy.iceandfire.data.DragonType;
import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import com.iafenvoy.uranus.client.model.ITabulaModelAnimator;
import com.iafenvoy.uranus.util.function.MemorizeSupplier;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CollectDragonSkullModelEvent {
    public static final Event<Consumer<CollectDragonSkullModelEvent>> EVENT = EventFactory.createArrayBacked(Consumer.class, listeners -> event -> {
        for (Consumer<CollectDragonSkullModelEvent> listener : listeners) {
            listener.accept(event);
        }
    });

    private final Map<DragonType, Pair<Identifier, MemorizeSupplier<ITabulaModelAnimator<? extends DragonBaseEntity>>>> modelMap;

    public CollectDragonSkullModelEvent(Map<DragonType, Pair<Identifier, MemorizeSupplier<ITabulaModelAnimator<? extends DragonBaseEntity>>>> modelMap) {
        this.modelMap = modelMap;
    }

    public <T extends DragonBaseEntity> void register(DragonType type, Identifier tabulaModel, Supplier<ITabulaModelAnimator<T>> animator) {
        this.modelMap.put(type, Pair.of(tabulaModel, new MemorizeSupplier<>(animator::get)));
    }

    public void post() {
        EVENT.invoker().accept(this);
    }
}
