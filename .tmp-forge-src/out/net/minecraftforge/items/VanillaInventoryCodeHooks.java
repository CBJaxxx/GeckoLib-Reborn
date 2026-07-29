/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items;

import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VanillaInventoryCodeHooks
{
    /**
     * Copied from TileEntityHopper#captureDroppedItems and added capability support
     * @return Null if we did nothing {no IItemHandler}, True if we moved an item, False if we moved no items
     */
    @Nullable
    public static Boolean extractHook(Level level, Hopper dest)
    {
        return getItemHandler(level, dest, Direction.UP)
                .map(itemHandlerResult -> {
                    IItemHandler handler = itemHandlerResult.getKey();

                    for (int i = 0; i < handler.getSlots(); i++)
                    {
                        ItemStack extractItem = handler.extractItem(i, 1, true);
                        if (!extractItem.m_41619_())
                        {
                            for (int j = 0; j < dest.m_6643_(); j++)
                            {
                                ItemStack destStack = dest.m_8020_(j);
                                if (dest.m_7013_(j, extractItem) && (destStack.m_41619_() || destStack.m_41613_() < destStack.m_41741_() && destStack.m_41613_() < dest.m_6893_() && ItemHandlerHelper.canItemStacksStack(extractItem, destStack)))
                                {
                                    extractItem = handler.extractItem(i, 1, false);
                                    if (destStack.m_41619_())
                                        dest.m_6836_(j, extractItem);
                                    else
                                    {
                                        destStack.m_41769_(1);
                                        dest.m_6836_(j, destStack);
                                    }
                                    dest.m_6596_();
                                    return true;
                                }
                            }
                        }
                    }

                    return false;
                })
                .orElse(null); // TODO bad null
    }

    /**
     * Copied from BlockDropper#dispense and added capability support
     */
    public static boolean dropperInsertHook(Level level, BlockPos pos, DispenserBlockEntity dropper, int slot, @NotNull ItemStack stack)
    {
        Direction enumfacing = level.m_8055_(pos).m_61143_(DropperBlock.f_52659_);
        BlockPos blockpos = pos.m_121945_(enumfacing);
        return getItemHandler(level, (double) blockpos.m_123341_(), (double) blockpos.m_123342_(), (double) blockpos.m_123343_(), enumfacing.m_122424_())
                .map(destinationResult -> {
                    IItemHandler itemHandler = destinationResult.getKey();
                    Object destination = destinationResult.getValue();
                    ItemStack dispensedStack = stack.m_41777_().m_41620_(1);
                    ItemStack remainder = putStackInInventoryAllSlots(dropper, destination, itemHandler, dispensedStack);

                    if (remainder.m_41619_())
                    {
                        remainder = stack.m_41777_();
                        remainder.m_41774_(1);
                    }
                    else
                    {
                        remainder = stack.m_41777_();
                    }

                    dropper.m_6836_(slot, remainder);
                    return false;
                })
                .orElse(true);
    }

    /**
     * Copied from TileEntityHopper#transferItemsOut and added capability support
     */
    public static boolean insertHook(HopperBlockEntity hopper)
    {
        Direction hopperFacing = hopper.m_58900_().m_61143_(HopperBlock.f_54021_);
        return getItemHandler(hopper.m_58904_(), hopper, hopperFacing)
                .map(destinationResult -> {
                    IItemHandler itemHandler = destinationResult.getKey();
                    Object destination = destinationResult.getValue();
                    if (isFull(itemHandler))
                    {
                        return false;
                    }
                    else
                    {
                        for (int i = 0; i < hopper.m_6643_(); ++i)
                        {
                            if (!hopper.m_8020_(i).m_41619_())
                            {
                                ItemStack originalSlotContents = hopper.m_8020_(i).m_41777_();
                                ItemStack insertStack = hopper.m_7407_(i, 1);
                                ItemStack remainder = putStackInInventoryAllSlots(hopper, destination, itemHandler, insertStack);

                                if (remainder.m_41619_())
                                {
                                    return true;
                                }

                                hopper.m_6836_(i, originalSlotContents);
                            }
                        }

                        return false;
                    }
                })
                .orElse(false);
    }

    private static ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack)
    {
        for (int slot = 0; slot < destInventory.getSlots() && !stack.m_41619_(); slot++)
        {
            stack = insertStack(source, destination, destInventory, stack, slot);
        }
        return stack;
    }

    /**
     * Copied from TileEntityHopper#insertStack and added capability support
     */
    private static ItemStack insertStack(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot)
    {
        ItemStack itemstack = destInventory.getStackInSlot(slot);

        if (destInventory.insertItem(slot, stack, true).m_41619_())
        {
            boolean insertedItem = false;
            boolean inventoryWasEmpty = isEmpty(destInventory);

            if (itemstack.m_41619_())
            {
                destInventory.insertItem(slot, stack, false);
                stack = ItemStack.f_41583_;
                insertedItem = true;
            }
            else if (ItemHandlerHelper.canItemStacksStack(itemstack, stack))
            {
                int originalSize = stack.m_41613_();
                stack = destInventory.insertItem(slot, stack, false);
                insertedItem = originalSize < stack.m_41613_();
            }

            if (insertedItem)
            {
                if (inventoryWasEmpty && destination instanceof HopperBlockEntity)
                {
                    HopperBlockEntity destinationHopper = (HopperBlockEntity)destination;

                    if (!destinationHopper.m_59409_())
                    {
                        int k = 0;
                        if (source instanceof HopperBlockEntity)
                        {
                            if (destinationHopper.getLastUpdateTime() >= ((HopperBlockEntity) source).getLastUpdateTime())
                            {
                                k = 1;
                            }
                        }
                        destinationHopper.m_59395_(8 - k);
                    }
                }
            }
        }

        return stack;
    }

    private static Optional<Pair<IItemHandler, Object>> getItemHandler(Level level, Hopper hopper, Direction hopperFacing)
    {
        double x = hopper.m_6343_() + (double) hopperFacing.m_122429_();
        double y = hopper.m_6358_() + (double) hopperFacing.m_122430_();
        double z = hopper.m_6446_() + (double) hopperFacing.m_122431_();
        return getItemHandler(level, x, y, z, hopperFacing.m_122424_());
    }

    private static boolean isFull(IItemHandler itemHandler)
    {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++)
        {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.m_41619_() || stackInSlot.m_41613_() < itemHandler.getSlotLimit(slot))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(IItemHandler itemHandler)
    {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++)
        {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.m_41613_() > 0)
            {
                return false;
            }
        }
        return true;
    }

    public static Optional<Pair<IItemHandler, Object>> getItemHandler(Level worldIn, double x, double y, double z, final Direction side)
    {
        int i = Mth.m_14107_(x);
        int j = Mth.m_14107_(y);
        int k = Mth.m_14107_(z);
        BlockPos blockpos = new BlockPos(i, j, k);
        net.minecraft.world.level.block.state.BlockState state = worldIn.m_8055_(blockpos);

        if (state.m_155947_())
        {
            BlockEntity blockEntity = worldIn.m_7702_(blockpos);
            if (blockEntity != null)
            {
                return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side)
                    .map(capability -> ImmutablePair.<IItemHandler, Object>of(capability, blockEntity));
            }
        }

        return Optional.empty();
    }
}
