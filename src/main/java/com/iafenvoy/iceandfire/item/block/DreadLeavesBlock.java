package com.iafenvoy.iceandfire.item.block;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class DreadLeavesBlock extends LeavesBlock {

    public DreadLeavesBlock(BlockBehaviour.Properties properties) {
        super(AmbientLeavesBlockSoundPlayer.noAmbientSound(), properties);
    }
}
