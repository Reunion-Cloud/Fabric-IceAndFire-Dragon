package com.iafenvoy.iceandfire.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;

/**
 * Shared helpers for structure generation that replace vanilla APIs removed between NeoForge 26.1.2 and 26.3.
 */
public final class StructureUtil {
    private StructureUtil() {
    }

    /**
     * Replacement for the removed {@code Structure#getLowestYIn5by5BoxOffset7Blocks(GenerationContext, Rotation)}.
     * Finds the lowest first-occupied height (WORLD_SURFACE_WG) at the corners of a 5x5 block box
     * offset 7 blocks into the chunk, with the box offset shifted according to the structure rotation.
     * This reproduces the exact 26.1.2 vanilla algorithm.
     */
    public static BlockPos getLowestYIn5by5BoxOffset7Blocks(Structure.GenerationContext context, Rotation rotation) {
        int offsetX = 5;
        int offsetZ = 5;
        if (rotation == Rotation.CLOCKWISE_90) {
            offsetX = -5;
        } else if (rotation == Rotation.CLOCKWISE_180) {
            offsetX = -5;
            offsetZ = -5;
        } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            offsetZ = -5;
        }

        ChunkPos chunkPos = context.chunkPos();
        int blockX = chunkPos.getBlockX(7);
        int blockZ = chunkPos.getBlockZ(7);
        return new BlockPos(blockX, getLowestY(context, blockX, blockZ, offsetX, offsetZ), blockZ);
    }

    /**
     * Copy of vanilla {@code Structure#getLowestY(GenerationContext, int, int, int, int)} (protected there),
     * sampling the four corners of the given box with the WORLD_SURFACE_WG heightmap.
     */
    public static int getLowestY(Structure.GenerationContext context, int minX, int minZ, int sizeX, int sizeZ) {
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        LevelHeightAccessor heightAccessor = context.heightAccessor();
        RandomState randomState = context.randomState();
        int h1 = chunkGenerator.getFirstOccupiedHeight(minX, minZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
        int h2 = chunkGenerator.getFirstOccupiedHeight(minX, minZ + sizeZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
        int h3 = chunkGenerator.getFirstOccupiedHeight(minX + sizeX, minZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
        int h4 = chunkGenerator.getFirstOccupiedHeight(minX + sizeX, minZ + sizeZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
        return Math.min(Math.min(h1, h2), Math.min(h3, h4));
    }
}
