package com.iafenvoy.iceandfire.event;

import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class DragonFireDamageWorldEvent {
    public static final Event<Consumer<DragonFireDamageWorldEvent>> EVENT = EventFactory.createArrayBacked(Consumer.class, listeners -> event -> {
        for (Consumer<DragonFireDamageWorldEvent> listener : listeners) {
            listener.accept(event);
        }
    });

    private final DragonBaseEntity dragon;
    private double targetX;
    private double targetY;
    private double targetZ;
    private boolean canceled;

    public DragonFireDamageWorldEvent(DragonBaseEntity dragon, double targetX, double targetY, double targetZ) {
        this.dragon = dragon;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public LivingEntity getEntity() {
        return this.dragon;
    }

    public DragonBaseEntity getDragon() {
        return this.dragon;
    }

    public double getTargetX() {
        return this.targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public double getTargetY() {
        return this.targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    public double getTargetZ() {
        return this.targetZ;
    }

    public void setTargetZ(double targetZ) {
        this.targetZ = targetZ;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean post() {
        EVENT.invoker().accept(this);
        return this.canceled;
    }
}
