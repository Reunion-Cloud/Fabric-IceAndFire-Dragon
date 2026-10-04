package com.iafenvoy.iceandfire.render.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;

/**
 * Carries the live entity and frame interpolation value to Uranus models while vanilla uses render states.
 */
public class LegacyEntityRenderState<T extends Entity> extends LivingEntityRenderState {
    public T entity;
    public float partialTick;
}
