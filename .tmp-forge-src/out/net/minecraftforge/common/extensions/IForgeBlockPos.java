/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;

public interface IForgeBlockPos {
    private BlockPos self() { return (BlockPos) this; }

    default ListTag toListTag() {
        var tag = new ListTag();
        tag.add(IntTag.m_128679_(self().m_123341_()));
        tag.add(IntTag.m_128679_(self().m_123342_()));
        tag.add(IntTag.m_128679_(self().m_123343_()));
        return tag;
    }

    default CompoundTag toCompoundTag() {
        return CompoundTag.builder()
            .putInt("x", self().m_123341_())
            .putInt("y", self().m_123342_())
            .putInt("z", self().m_123343_())
            .build();
    }
}
