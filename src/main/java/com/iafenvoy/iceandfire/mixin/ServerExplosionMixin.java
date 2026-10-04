package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.entity.util.BlockLaunchExplosion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin {
    @Inject(method = "interactWithBlocks", at = @At("HEAD"), cancellable = true)
    private void iceandfire$launchExplodedBlocks(List<BlockPos> affectedBlocks, CallbackInfo ci) {
        if (BlockLaunchExplosion.handleExplosionBlocks((ServerExplosion) (Object) this, affectedBlocks)) ci.cancel();
    }
}
