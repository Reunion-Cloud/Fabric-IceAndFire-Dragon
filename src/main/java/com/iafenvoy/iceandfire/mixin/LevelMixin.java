package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.entity.MultipartPartEntity;
import com.iafenvoy.iceandfire.entity.util.IMultipartEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Shadow
    public abstract boolean isClientSide();

    @Shadow
    public abstract Collection<EnderDragonPart> dragonParts();

    @Shadow
    protected abstract LevelEntityGetter<Entity> getEntities();

    @Redirect(
            method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;dragonParts()Ljava/util/Collection;")
    )
    private Collection<EnderDragonPart> iceandfire$skipVanillaDragonPartsLoop(Level instance) {
        return Collections.emptyList();
    }

    @Inject(
            method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
            at = @At("RETURN")
    )
    private void iceandfire$collectMultipartEntities(@Nullable Entity except, AABB box, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cir) {
        List<Entity> list = cir.getReturnValue();
        for (Object obj : (Collection<?>) this.dragonParts()) {
            if (obj instanceof EnderDragonPart part) {
                if (part != except && part.parentMob != except && predicate.test(part) && box.intersects(part.getBoundingBox())) {
                    list.add(part);
                }
            } else if (obj instanceof MultipartPartEntity<?> part) {
                if (!part.isRemoved() && !part.getParent().isRemoved() && part != except && part.getParent() != except && predicate.test(part) && box.intersects(part.getBoundingBox())) {
                    list.add(part);
                }
            }
        }
        if (this.isClientSide()) {
            for (Entity entity : this.getEntities().getAll()) {
                if (entity instanceof IMultipartEntity multipart && multipart.isMultipartEntity()) {
                    for (MultipartPartEntity<?> part : multipart.getParts()) {
                        if (part != null && !part.isRemoved() && part != except && part.getParent() != except && predicate.test(part) && box.intersects(part.getBoundingBox())) {
                            list.add(part);
                        }
                    }
                }
            }
        }
    }
}
