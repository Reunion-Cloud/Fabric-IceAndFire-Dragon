package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.data.component.ChainData;
import com.iafenvoy.iceandfire.data.component.MiscData;
import com.iafenvoy.iceandfire.util.attachment.IafEntityAttachment;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("UnstableApiUsage")
public final class IafAttachments {
    public static final AttachmentType<ChainData> CHAIN_DATA = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "chain_data"),
            builder -> builder.initializer(ChainData::new)
                    .persistent(ChainData.CODEC)
                    .syncWith(ChainData.PACKET_CODEC, AttachmentSyncPredicate.all())
                    .copyOnDeath()
    );
    public static final AttachmentType<MiscData> MISC_DATA = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(IceAndFire.MOD_ID, "misc_data"),
            builder -> builder.initializer(MiscData::new)
                    .persistent(MiscData.CODEC)
                    .syncWith(MiscData.PACKET_CODEC, AttachmentSyncPredicate.all())
                    .copyOnDeath()
    );

    private IafAttachments() {
    }

    public static void init() {
    }

    public static void onLivingTick(LivingEntity living) {
        tickAndSync(CHAIN_DATA, living);
        tickAndSync(MISC_DATA, living);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity, A extends IafEntityAttachment<T>> void tickAndSync(AttachmentType<A> type, T entity) {
        A attachment = entity.getAttached(type);
        if (attachment == null) return;
        attachment.tick(entity);
        if (attachment.isDirty() && !entity.level().isClientSide())
            entity.setAttached(type, (A) attachment.copy());
    }
}
