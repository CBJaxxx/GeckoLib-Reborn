/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

/**
 * Provides helper functions replacing those in {@link ItemBlockRenderTypes}.
 */
public final class RenderTypeHelper {
    /**
     * Provides a {@link RenderType} using {@link DefaultVertexFormat#NEW_ENTITY} for the given {@link DefaultVertexFormat#BLOCK} format.
     * This should be called for each {@link RenderType} returned by {@link BakedModel#getRenderTypes(BlockState, RandomSource, ModelData)}.
     * <p>
     * Mimics the behavior of vanilla's {@link ItemBlockRenderTypes#getRenderType(BlockState)}.
     */
    @NotNull
    public static RenderType getEntityRenderType(ChunkSectionLayer layer) {
        return layer == ChunkSectionLayer.TRANSLUCENT ? Sheets.m_110791_() : Sheets.m_110790_();
    }

    /**
     * Provides a {@link RenderType} fit for rendering moving blocks given the specified chunk render type.
     * This should be called for each {@link RenderType} returned by {@link BakedModel#getRenderTypes(BlockState, RandomSource, ModelData)}.
     * <p>
     * Mimics the behavior of vanilla's {@link ItemBlockRenderTypes#getMovingBlockRenderType(BlockState)}.
     */
    @NotNull
    public static RenderType getMovingBlockRenderType(ChunkSectionLayer layer) {
        if (layer == null)
            return RenderType.m_110451_();

        return switch (layer) {
            case SOLID -> RenderType.m_110451_();
            case CUTOUT_MIPPED -> RenderType.m_110457_();
            case CUTOUT -> RenderType.m_110463_();
            case TRANSLUCENT -> RenderType.m_110469_();
            case TRIPWIRE -> RenderType.m_110503_();
        };
    }

    /**
     * Provides a fallback {@link RenderType} for the given {@link ItemStack} in the case that none is explicitly specified.
     * <p>
     * Mimics the behavior of vanilla's {@link ItemBlockRenderTypes#getRenderType(ItemStack, boolean)}
     * but removes the need to query the model again if the item is a {@link BlockItem}.
     */
    @NotNull
    public static RenderType getFallbackItemRenderType(ItemStack stack, BlockStateModel model) {
        if (stack.m_41720_() instanceof BlockItem blockItem) {
            var renderTypes = model.getRenderTypes(blockItem.m_40614_().m_49966_(), RandomSource.m_216335_(42), ModelData.EMPTY);
            if (renderTypes.contains(ChunkSectionLayer.TRANSLUCENT))
                return getEntityRenderType(ChunkSectionLayer.TRANSLUCENT);
            return Sheets.m_110790_();
        }
        return Sheets.m_110791_();
    }

    private RenderTypeHelper() {}
}
