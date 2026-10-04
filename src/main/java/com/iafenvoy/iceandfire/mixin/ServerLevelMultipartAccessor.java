package com.iafenvoy.iceandfire.mixin;

import com.iafenvoy.iceandfire.entity.MultipartPartEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerLevel.class)
public interface ServerLevelMultipartAccessor {
    @Accessor("dragonParts")
    Int2ObjectMap<MultipartPartEntity<?>> iceandfire$getDragonParts();
}
