package com.iafenvoy.iceandfire.item.block;

import com.iafenvoy.iceandfire.registry.IafFeatures;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;

public class DreadwoodSaplingBlock extends SaplingBlock {
    public DreadwoodSaplingBlock() {
        super(new TreeGrower(
                "dread_wood",
                WeightedList.of(new Weighted<>(IafFeatures.DREADWOOD, 9), new Weighted<>(IafFeatures.DREADWOOD_LARGE, 1)),
                WeightedList.of(),
                WeightedList.of(),
                IafFeatures.DREADWOOD
        ), Properties.ofFullCopy(Blocks.OAK_SAPLING));
    }
}
