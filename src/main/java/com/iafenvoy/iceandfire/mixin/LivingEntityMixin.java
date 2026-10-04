package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.effect.FrozenStatusEffect;
import com.iafenvoy.iceandfire.event.handler.ServerEvents;
import com.iafenvoy.iceandfire.item.ability.BuiltinAbilities;
import com.iafenvoy.iceandfire.registry.IafAttachments;
import com.iafenvoy.iceandfire.registry.tag.IafItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Shadow
    public abstract ItemStack getItemInHand(InteractionHand hand);

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        if (!ServerEvents.onEntityStruckByLightning(this, lightning)) {
            super.thunderHit(level, lightning);
        }
    }

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    private void iceandfire$onEntityDamage(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(ServerEvents.onEntityDamage((LivingEntity) (Object) this, source, cir.getReturnValue()));
    }

    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"))
    private void onSwingHand(InteractionHand hand, boolean updateSelf, CallbackInfo ci) {
        if (this.getItemInHand(hand).is(IafItemTags.SUMMON_GHOST_SWORD) && BuiltinAbilities.SUMMON_GHOST_SWORD.isEnable())
            BuiltinAbilities.SUMMON_GHOST_SWORD.active((LivingEntity) (Object) this);
    }

    @Inject(method = "onEffectsRemoved", at = @At("HEAD"))
    private void handleFrozenEffectRemove(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        for (MobEffectInstance effect : effects) {
            if (effect.getEffect().value() instanceof FrozenStatusEffect frozen)
                frozen.onRemoved((LivingEntity) (Object) this);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onLivingTickTail(CallbackInfo ci) {
        IafAttachments.onLivingTick((LivingEntity) (Object) this);
    }
}
