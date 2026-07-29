/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotItemHandler extends Slot
{
    private static Container emptyInventory = new SimpleContainer(0);
    private final IItemHandler itemHandler;
    private final int index;

    public SlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(emptyInventory, index, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.index = index;
    }

    @Override
    public boolean m_5857_(@NotNull ItemStack stack)
    {
        if (stack.m_41619_())
            return false;
        return itemHandler.isItemValid(index, stack);
    }

    @Override
    @NotNull
    public ItemStack m_7993_()
    {
        return this.getItemHandler().getStackInSlot(index);
    }

    // Override if your IItemHandler does not implement IItemHandlerModifiable
    @Override
    public void m_5852_(@NotNull ItemStack stack)
    {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, stack);
        this.m_6654_();
    }

    // Override if your IItemHandler does not implement IItemHandlerModifiable
    // @Override
    public void initialize(ItemStack stack)
    {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, stack);
        this.m_6654_();
    }

    @Override
    public void m_40234_(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn)
    {

    }

    @Override
    public int m_6641_()
    {
        return this.itemHandler.getSlotLimit(this.index);
    }

    @Override
    public int m_5866_(@NotNull ItemStack stack)
    {
        ItemStack maxAdd = stack.m_41777_();
        int maxInput = stack.m_41741_();
        maxAdd.m_41764_(maxInput);

        IItemHandler handler = this.getItemHandler();
        ItemStack currentStack = handler.getStackInSlot(index);
        if (handler instanceof IItemHandlerModifiable) {
            IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;

            handlerModifiable.setStackInSlot(index, ItemStack.f_41583_);

            ItemStack remainder = handlerModifiable.insertItem(index, maxAdd, true);

            handlerModifiable.setStackInSlot(index, currentStack);

            return maxInput - remainder.m_41613_();
        }
        else
        {
            ItemStack remainder = handler.insertItem(index, maxAdd, true);

            int current = currentStack.m_41613_();
            int added = maxInput - remainder.m_41613_();
            return current + added;
        }
    }

    @Override
    public boolean m_8010_(Player playerIn)
    {
        return !this.getItemHandler().extractItem(index, 1, true).m_41619_();
    }

    @Override
    @NotNull
    public ItemStack m_6201_(int amount)
    {
        return this.getItemHandler().extractItem(index, amount, false);
    }

    public IItemHandler getItemHandler()
    {
        return itemHandler;
    }
/* TODO Slot patches
    @Override
    public boolean isSameInventory(Slot other)
    {
        return other instanceof SlotItemHandler && ((SlotItemHandler) other).getItemHandler() == this.itemHandler;
    }*/
}
