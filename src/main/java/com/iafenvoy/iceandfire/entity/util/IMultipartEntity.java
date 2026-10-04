package com.iafenvoy.iceandfire.entity.util;

import com.iafenvoy.iceandfire.entity.MultipartPartEntity;
import org.jetbrains.annotations.NotNull;

public interface IMultipartEntity {
    default boolean isMultipartEntity() {
        return true;
    }

    MultipartPartEntity<?> @NotNull [] getParts();
}
