/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items.wrapper;

import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntUnaryOperator;

public class SidedInvWrapper implements IItemHandlerModifiable
{
    protected final WorldlyContainer inv;
    @Nullable
    protected final Direction side;

    private final IntUnaryOperator slotLimit;
    private final InsertLimit newStackInsertLimit;
    private interface InsertLimit
    {
        int limitInsert(int wrapperSlot, int invSlot, ItemStack stack);
    }

    @SuppressWarnings("unchecked")
    public static LazyOptional<IItemHandlerModifiable>[] create(WorldlyContainer inv, Direction... sides) {
        LazyOptional<IItemHandlerModifiable>[] ret = new LazyOptional[sides.length];
        for (int x = 0; x < sides.length; x++) {
            final Direction side = sides[x];
            ret[x] = LazyOptional.of(() -> new SidedInvWrapper(inv, side));
        }
        return ret;
    }

    public SidedInvWrapper(WorldlyContainer inv, @Nullable Direction side)
    {
        this.inv = inv;
        this.side = side;

        // A few special cases to account for canPlaceItem implementations attempting to limit specific inputs to 1,
        // by returning false if there's already a contained item. This doesn't work with modded inserted sizes > 1.
        // - Limit buckets to 1 in furnace fuel inputs.
        // - Limit brewing stand "bottle" inputs to 1.
        // Done using lambdas to avoid the overhead of instanceof checks in hot code.
        if (inv instanceof BrewingStandBlockEntity)
            this.slotLimit = wrapperSlot -> getSlot(inv, wrapperSlot, side) < 3 ? 1 : inv.m_6893_();
        else
            this.slotLimit = wrapperSlot -> inv.m_6893_();
        if (inv instanceof AbstractFurnaceBlockEntity)
            this.newStackInsertLimit = (wrapperSlot, invSlot, stack) -> invSlot == 1 && stack.m_150930_(Items.f_42446_) ? 1 : Math.min(stack.m_41741_(), getSlotLimit(wrapperSlot));
        else
            this.newStackInsertLimit = (wrapperSlot, invSlot, stack) -> Math.min(stack.m_41741_(), getSlotLimit(wrapperSlot));
    }

    public static int getSlot(WorldlyContainer inv, int slot, @Nullable Direction side)
    {
        int[] slots = inv.m_7071_(side);
        if (slot < slots.length)
            return slots[slot];
        return -1;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SidedInvWrapper that = (SidedInvWrapper) o;

        return inv.equals(that.inv) && side == that.side;
    }

    @Override
    public int hashCode()
    {
        int result = inv.hashCode();
        result = 31 * result + (side == null ? 0 : side.hashCode());
        return result;
    }

    @Override
    public int getSlots()
    {
        return inv.m_7071_(side).length;
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot)
    {
        int i = getSlot(inv, slot, side);
        return i == -1 ? ItemStack.f_41583_ : inv.m_8020_(i);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (stack.m_41619_())
            return ItemStack.f_41583_;

        int slot1 = getSlot(inv, slot, side);

        if (slot1 == -1)
            return stack;

        ItemStack stackInSlot = inv.m_8020_(slot1);

        int m;
        if (!stackInSlot.m_41619_())
        {
            if (stackInSlot.m_41613_() >= Math.min(stackInSlot.m_41741_(), getSlotLimit(slot)))
                return stack;

            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
                return stack;

            if (!inv.m_7155_(slot1, stack, side) || !inv.m_7013_(slot1, stack))
                return stack;

            m = Math.min(stack.m_41741_(), getSlotLimit(slot)) - stackInSlot.m_41613_();

            if (stack.m_41613_() <= m)
            {
                if (!simulate)
                {
                    ItemStack copy = stack.m_41777_();
                    copy.m_41769_(stackInSlot.m_41613_());
                    setInventorySlotContents(slot1, copy);
                }

                return ItemStack.f_41583_;
            }
            else
            {
                // copy the stack to not modify the original one
                stack = stack.m_41777_();
                if (!simulate)
                {
                    ItemStack copy = stack.m_41620_(m);
                    copy.m_41769_(stackInSlot.m_41613_());
                    setInventorySlotContents(slot1, copy);
                    return stack;
                }
                else
                {
                    stack.m_41774_(m);
                    return stack;
                }
            }
        }
        else
        {
            if (!inv.m_7155_(slot1, stack, side) || !inv.m_7013_(slot1, stack))
                return stack;

            m = newStackInsertLimit.limitInsert(slot, slot1, stack);

            if (m < stack.m_41613_())
            {
                // copy the stack to not modify the original one
                stack = stack.m_41777_();
                if (!simulate)
                {
                    setInventorySlotContents(slot1, stack.m_41620_(m));
                    return stack;
                }
                else
                {
                    stack.m_41774_(m);
                    return stack;
                }
            }
            else
            {
                if (!simulate)
                    setInventorySlotContents(slot1, stack);
                return ItemStack.f_41583_;
            }
        }

    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack)
    {
        int slot1 = getSlot(inv, slot, side);

        if (slot1 != -1)
            setInventorySlotContents(slot1, stack);
    }

    private void setInventorySlotContents(int slot, ItemStack stack) {
      inv.m_6596_(); //Notify vanilla of updates, We change the handler to be responsible for this instead of the caller. So mimic vanilla behavior
      inv.m_6836_(slot, stack);
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (amount == 0)
            return ItemStack.f_41583_;

        int slot1 = getSlot(inv, slot, side);

        if (slot1 == -1)
            return ItemStack.f_41583_;

        ItemStack stackInSlot = inv.m_8020_(slot1);

        if (stackInSlot.m_41619_())
            return ItemStack.f_41583_;

        if (!inv.m_7157_(slot1, stackInSlot, side))
            return ItemStack.f_41583_;

        if (simulate)
        {
            if (stackInSlot.m_41613_() < amount)
            {
                return stackInSlot.m_41777_();
            }
            else
            {
                ItemStack copy = stackInSlot.m_41777_();
                copy.m_41764_(amount);
                return copy;
            }
        }
        else
        {
            int m = Math.min(stackInSlot.m_41613_(), amount);
            ItemStack ret = inv.m_7407_(slot1, m);
            inv.m_6596_();
            return ret;
        }
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return slotLimit.applyAsInt(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        int slot1 = getSlot(inv, slot, side);
        return slot1 == -1 ? false : inv.m_7013_(slot1, stack);
    }
}
