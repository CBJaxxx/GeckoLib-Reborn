/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.util;

import java.lang.ref.WeakReference;
import java.util.Objects;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

/**
 * Represents a captured snapshot of a block which will not change
 * automatically.
 * <p>
 * Unlike Block, which only one object can exist per coordinate, BlockSnapshot
 * can exist multiple times for any given Block.
 */
public class BlockSnapshot {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("forge.debugBlockSnapshot", "false"));

    private final ResourceKey<Level> dim;
    private final BlockPos pos;
    private final int flags;
    private final BlockState block;
    @Nullable
    private final CompoundTag nbt;

    @Nullable
    private WeakReference<LevelAccessor> level;
    private String toString = null;

    private BlockSnapshot(ResourceKey<Level> dim, LevelAccessor level, BlockPos pos, BlockState state, @Nullable CompoundTag nbt, int flags) {
        this.dim = dim;
        this.pos = pos.m_7949_();
        this.block = state;
        this.flags = flags;
        this.nbt = nbt;

        this.level = new WeakReference<>(level);

        if (DEBUG)
            System.out.println("Created " + this.toString());
    }

    public static BlockSnapshot create(ResourceKey<Level> dim, LevelAccessor world, BlockPos pos) {
        return create(dim, world, pos, 3);
    }

    public static BlockSnapshot create(ResourceKey<Level> dim, LevelAccessor world, BlockPos pos, int flag) {
        return new BlockSnapshot(dim, world, pos, world.m_8055_(pos), getBlockEntityTag(world.m_7702_(pos), world.m_9598_()), flag);
    }

    @Nullable
    private static CompoundTag getBlockEntityTag(@Nullable BlockEntity te, HolderLookup.Provider lookup) {
        return te == null ? null : te.m_187480_(lookup);
    }

    public BlockState getCurrentBlock() {
        LevelAccessor world = getLevel();
        return world == null ? Blocks.f_50016_.m_49966_() : world.m_8055_(this.pos);
    }

    @Nullable
    public LevelAccessor getLevel() {
        LevelAccessor world = this.level != null ? this.level.get() : null;
        if (world == null) {
            world = ServerLifecycleHooks.getCurrentServer().m_129880_(this.dim);
            this.level = new WeakReference<LevelAccessor>(world);
        }
        return world;
    }

    public BlockState getReplacedBlock() {
        return this.block;
    }

    /*
    @Nullable
    public BlockEntity getBlockEntity() {
        return getTag() != null ? BlockEntity.loadStatic(getPos(), getReplacedBlock(), getTag()) : null;
    }
    */

    public boolean restore() {
        return restore(false);
    }

    public boolean restore(boolean force) {
        return restore(force, true);
    }

    public boolean restore(boolean force, boolean notifyNeighbors) {
        return restoreToLocation(getLevel(), getPos(), force, notifyNeighbors);
    }

    public boolean restoreToLocation(LevelAccessor world, BlockPos pos, boolean force, boolean notifyNeighbors) {
        BlockState current = getCurrentBlock();
        BlockState replaced = getReplacedBlock();

        int flags = notifyNeighbors ? Block.f_152402_ : Block.f_152394_;

        if (current != replaced) {
            if (force)
                world.m_7731_(pos, replaced, flags);
            else
                return false;
        }

        world.m_7731_(pos, replaced, flags);
        if (world instanceof Level)
            ((Level)world).m_7260_(pos, current, replaced, flags);

        BlockEntity te = null;
        if (getTag() != null) {
            te = world.m_7702_(pos);
            if (te != null) {
                try (var problems = new ProblemReporter.ScopedCollector(te.m_401685_(), LOGGER)) {
                    te.m_320998_(TagValueInput.m_401579_(problems, world.m_9598_(), getTag()));
                }

                te.m_6596_();
            }
        }

        if (DEBUG)
            System.out.println("Restored " + this.toString());
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        final BlockSnapshot other = (BlockSnapshot) obj;
        return this.dim.equals(other.dim) &&
            this.pos.equals(other.pos) &&
            this.block == other.block &&
            this.flags == other.flags &&
            Objects.equals(this.nbt, other.nbt);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.dim.hashCode();
        hash = 73 * hash + this.pos.hashCode();
        hash = 73 * hash + this.block.hashCode();
        hash = 73 * hash + this.flags;
        hash = 73 * hash + Objects.hashCode(this.getTag());
        return hash;
    }

    @Override
    public String toString() {
        if (toString == null) {
            this.toString =
                "BlockSnapshot[" +
                "World:" + this.dim.m_135782_() + ',' +
                "Pos: " + this.pos + ',' +
                "State: " + this.block + ',' +
                "Flags: " + this.flags + ',' +
                "NBT: " + (this.nbt == null ? "null" : this.nbt.toString()) +
                ']';
        }
        return this.toString;
    }

    public BlockPos getPos() { return pos; }


    public int getFlag() { return flags; }

    @Nullable
    public CompoundTag getTag() { return nbt; }

}
