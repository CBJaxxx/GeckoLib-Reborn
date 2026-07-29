/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.*;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.jetbrains.annotations.Nullable;

public interface IForgeBlockGetter
{
    private BlockGetter self() { return (BlockGetter) this; }

    /**
     * Get the {@link BlockEntity} at the given position if it exists.
     * <p>
     * {@link Level#getBlockEntity(BlockPos)} would create a new {@link BlockEntity} if the
     * {@link net.minecraft.world.level.block.Block} has one, but it has not been placed in the world yet
     * (This can happen on world load).
     * @return The BlockEntity at the given position or null if it doesn't exist
     */
    @Nullable
    default BlockEntity getExistingBlockEntity(BlockPos pos)
    {
        if (this instanceof Level level)
        {
            if (!level.m_7232_(SectionPos.m_123171_(pos.m_123341_()), SectionPos.m_123171_(pos.m_123343_())))
            {
                return null;
            }

            return level.m_46865_(pos).getExistingBlockEntity(pos);
        }
        else if (this instanceof LevelChunk chunk)
        {
            return chunk.m_62954_().get(pos);
        }
        else if (this instanceof ImposterProtoChunk chunk)
        {
            return chunk.m_62768_().getExistingBlockEntity(pos);
        }
        return self().m_7702_(pos);
    }

    /**
     * Retrieves the model data manager for this level.
     * This will be {@code null} on a server level.
     */
    @Nullable
    default ModelDataManager getModelDataManager()
    {
        return null;
    }
}
