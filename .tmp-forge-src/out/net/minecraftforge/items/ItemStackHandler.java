/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items;

import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

public class ItemStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected NonNullList<ItemStack> stacks;

    public ItemStackHandler() {
        this(1);
    }

    public ItemStackHandler(int size) {
        stacks = NonNullList.m_122780_(size, ItemStack.f_41583_);
    }

    public ItemStackHandler(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public void setSize(int size) {
        stacks = NonNullList.m_122780_(size, ItemStack.f_41583_);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        validateSlotIndex(slot);
        this.stacks.set(slot, stack);
        onContentsChanged(slot);
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.m_41619_())
            return ItemStack.f_41583_;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.m_41619_()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.m_41613_();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.m_41613_() > limit;

        if (!simulate) {
            if (existing.m_41619_()) {
                this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.m_41769_(reachedLimit ? limit : stack.m_41613_());
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.m_41613_()- limit) : ItemStack.f_41583_;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.f_41583_;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        if (existing.m_41619_())
            return ItemStack.f_41583_;

        int toExtract = Math.min(amount, existing.m_41741_());

        if (existing.m_41613_() <= toExtract) {
            if (!simulate) {
                this.stacks.set(slot, ItemStack.f_41583_);
                onContentsChanged(slot);
                return existing;
            } else {
                return existing.m_41777_();
            }
        } else {
            if (!simulate) {
                this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.m_41613_() - toExtract));
                onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.m_41741_());
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider lookup) {
        try (var problems = new ProblemReporter.ScopedCollector(LOGGER)) {
            var output = TagValueOutput.m_402504_(problems, lookup);
            ContainerHelper.m_18976_(output, this.stacks);
            return output.m_404289_();
        }
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider lookup, CompoundTag nbt) {
        try (var problems = new ProblemReporter.ScopedCollector(LOGGER)) {
            ContainerHelper.m_18980_(TagValueInput.m_401579_(problems, lookup, nbt), stacks);
        }
        onLoad();
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }

    protected void onLoad() {
    }

    protected void onContentsChanged(int slot) {
    }
}
