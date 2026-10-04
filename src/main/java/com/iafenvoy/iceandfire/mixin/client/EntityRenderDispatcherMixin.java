package com.iafenvoy.iceandfire.mixin.client;

import com.iafenvoy.iceandfire.event.handler.ClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private <S extends EntityRenderState> void iceandfire$disablePlayerRenderWhenNeed(S state, CameraRenderState camera, double x, double y, double z, PoseStack poseStack, SubmitNodeCollector collector, CallbackInfo ci) {
        if (state instanceof AvatarRenderState avatarState && ClientEvents.shouldCancelPlayerRender(avatarState)) {
            ci.cancel();
        }
    }
}
