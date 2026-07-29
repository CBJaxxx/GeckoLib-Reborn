/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fluids.capability.wrappers;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class BucketPickupHandlerWrapper implements IFluidHandler
{
    private static final Logger LOGGER = LogManager.getLogger();

    protected final BucketPickup bucketPickupHandler;
    protected final Level world;
    protected final BlockPos blockPos;

    public BucketPickupHandlerWrapper(BucketPickup bucketPickupHandler, Level world, BlockPos blockPos)
    {
        this.bucketPickupHandler = bucketPickupHandler;
        this.world = world;
        this.blockPos = blockPos;
    }

    @Override
    public int getTanks()
    {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        if (tank == 0)
        {
            //Best guess at stored fluid
            FluidState fluidState = world.m_6425_(blockPos);
            if (!fluidState.m_76178_())
            {
                return new FluidStack(fluidState.m_76152_(), FluidType.BUCKET_VOLUME);
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return FluidType.BUCKET_VOLUME;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack)
    {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        return 0;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)
    {
        if (!resource.isEmpty() && FluidType.BUCKET_VOLUME <= resource.getAmount())
        {
            FluidState fluidState = world.m_6425_(blockPos);
            if (!fluidState.m_76178_() && resource.getFluid() == fluidState.m_76152_())
            {
                if (action.execute())
                {
                    ItemStack itemStack = bucketPickupHandler.m_142598_(null, world, blockPos, world.m_8055_(blockPos));
                    if (itemStack != ItemStack.f_41583_ && itemStack.m_41720_() instanceof BucketItem bucket)
                    {
                        FluidStack extracted = new FluidStack(bucket.getFluid(), FluidType.BUCKET_VOLUME);
                        if (!resource.isFluidEqual(extracted))
                        {
                            //Be loud if something went wrong
                            LOGGER.error("Fluid removed without successfully being picked up. Fluid {} at {} in {} matched requested type, but after performing pickup was {}.",
                                    ForgeRegistries.FLUIDS.getKey(fluidState.m_76152_()), blockPos, world.m_46472_().m_135782_(), ForgeRegistries.FLUIDS.getKey(bucket.getFluid()));
                            return FluidStack.EMPTY;
                        }
                        return extracted;
                    }
                }
                else
                {
                    FluidStack extracted = new FluidStack(fluidState.m_76152_(), FluidType.BUCKET_VOLUME);
                    if (resource.isFluidEqual(extracted))
                    {
                        //Validate NBT matches
                        return extracted;
                    }
                }
            }
        }
        return FluidStack.EMPTY;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        if (FluidType.BUCKET_VOLUME <= maxDrain)
        {
            FluidState fluidState = world.m_6425_(blockPos);
            if (!fluidState.m_76178_())
            {
                if (action.simulate())
                {
                    return new FluidStack(fluidState.m_76152_(), FluidType.BUCKET_VOLUME);
                }
                ItemStack itemStack = bucketPickupHandler.m_142598_(null, world, blockPos, world.m_8055_(blockPos));
                if (itemStack != ItemStack.f_41583_ && itemStack.m_41720_() instanceof BucketItem bucket)
                {
                    return new FluidStack(bucket.getFluid(), FluidType.BUCKET_VOLUME);
                }
            }
        }
        return FluidStack.EMPTY;
    }
}
