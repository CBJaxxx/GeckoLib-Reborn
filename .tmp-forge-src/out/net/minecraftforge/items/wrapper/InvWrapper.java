/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items.wrapper;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class InvWrapper implements IItemHandlerModifiable
{
    private final Container inv;

    public InvWrapper(Container inv)
    {
        this.inv = inv;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        InvWrapper that = (InvWrapper) o;

        return getInv().equals(that.getInv());

    }

    @Override
    public int hashCode()
    {
        return getInv().hashCode();
    }

    @Override
    public int getSlots()
    {
        return getInv().m_6643_();
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot)
    {
        return getInv().m_8020_(slot);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
    {
        if (stack.m_41619_())
            return ItemStack.f_41583_;

        ItemStack stackInSlot = getInv().m_8020_(slot);

        int m;
        if (!stackInSlot.m_41619_())
        {
            if (stackInSlot.m_41613_() >= Math.min(stackInSlot.m_41741_(), getSlotLimit(slot)))
                return stack;

            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
                return stack;

            if (!getInv().m_7013_(slot, stack))
                return stack;

            m = Math.min(stack.m_41741_(), getSlotLimit(slot)) - stackInSlot.m_41613_();

            if (stack.m_41613_() <= m)
            {
                if (!simulate)
                {
                    ItemStack copy = stack.m_41777_();
                    copy.m_41769_(stackInSlot.m_41613_());
                    getInv().m_6836_(slot, copy);
                    getInv().m_6596_();
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
                    getInv().m_6836_(slot, copy);
                    getInv().m_6596_();
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
            if (!getInv().m_7013_(slot, stack))
                return stack;

            m = Math.min(stack.m_41741_(), getSlotLimit(slot));
            if (m < stack.m_41613_())
            {
                // copy the stack to not modify the original one
                stack = stack.m_41777_();
                if (!simulate)
                {
                    getInv().m_6836_(slot, stack.m_41620_(m));
                    getInv().m_6596_();
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
                {
                    getInv().m_6836_(slot, stack);
                    getInv().m_6596_();
                }
                return ItemStack.f_41583_;
            }
        }

    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (amount == 0)
            return ItemStack.f_41583_;

        ItemStack stackInSlot = getInv().m_8020_(slot);

        if (stackInSlot.m_41619_())
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

            ItemStack decrStackSize = getInv().m_7407_(slot, m);
            getInv().m_6596_();
            return decrStackSize;
        }
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack)
    {
        getInv().m_6836_(slot, stack);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return getInv().m_6893_();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack)
    {
        return getInv().m_7013_(slot, stack);
    }

    public Container getInv()
    {
        return inv;
    }
}
