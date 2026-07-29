/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.extensions;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public interface IForgeResourceKey<T> {
    private ResourceKey<T> self() {
        return (ResourceKey<T>) this;
    }

    default Holder<T> getOrThrow(BlockEntity blockEntity) {
        return getOrThrow(blockEntity.m_58904_());
    }

    default Holder<T> getOrThrow(Entity entity) {
        return getOrThrow(entity.m_321891_());
    }

    default Holder<T> getOrThrow(Level level) {
        return getOrThrow(level.m_9598_());
    }

    /**
     * @param registryAccess of {@link RegistryAccess}
     * @return {@link Holder<T>} of the Object that this {@link ResourceKey<T>} is registered to
     */
    default Holder<T> getOrThrow(RegistryAccess registryAccess) {
        Objects.requireNonNull(registryAccess, "registryAccess was null");
        return registryAccess.m_254974_(self().m_323747_()).m_255043_(self());
    }
}
