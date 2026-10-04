package com.iafenvoy.iceandfire.mixin.client;

import com.iafenvoy.iceandfire.event.handler.ClientEvents;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @ModifyVariable(method = "getMaxZoom", at = @At("HEAD"), argsOnly = true)
    private float iceandfire$modifyCameraDistance(float maxZoom) {
        return ClientEvents.onCameraSetup(maxZoom);
    }
}
