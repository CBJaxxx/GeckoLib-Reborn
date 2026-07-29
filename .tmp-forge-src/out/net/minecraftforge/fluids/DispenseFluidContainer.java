/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fluids;

import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

/**
 * Fills or drains a fluid container item using a Dispenser.
 */
public class DispenseFluidContainer extends DefaultDispenseItemBehavior {
    private static final DispenseFluidContainer INSTANCE = new DispenseFluidContainer();

    public static DispenseFluidContainer getInstance() {
        return INSTANCE;
    }

    private DispenseFluidContainer() {}

    private final DefaultDispenseItemBehavior dispenseBehavior = new DefaultDispenseItemBehavior();

    @Override
    @NotNull
    public ItemStack m_7498_(@NotNull BlockSource source, @NotNull ItemStack stack) {
        if (FluidUtil.getFluidContained(stack).isPresent())
            return dumpContainer(source, stack);
        else
            return fillContainer(source, stack);
    }

    /**
     * Picks up fluid in front of a Dispenser and fills a container with it.
     */
    @NotNull
    private ItemStack fillContainer(@NotNull BlockSource source, @NotNull ItemStack stack) {
        Level level = source.f_301782_();
        Direction dispenserFacing = source.f_301783_().m_61143_(DispenserBlock.f_52659_);
        BlockPos blockpos = source.f_301784_().m_121945_(dispenserFacing);

        FluidActionResult actionResult = FluidUtil.tryPickUpFluid(stack, null, level, blockpos, dispenserFacing.m_122424_());
        ItemStack resultStack = actionResult.getResult();

        if (!actionResult.isSuccess() || resultStack.m_41619_())
            return super.m_7498_(source, stack);

        if (stack.m_41613_() == 1)
            return resultStack;

        source.f_301785_().m_338820_(resultStack);

        if (!resultStack.m_41619_())
            this.dispenseBehavior.m_6115_(source, resultStack);

        ItemStack stackCopy = stack.m_41777_();
        stackCopy.m_41774_(1);
        return stackCopy;
    }

    /**
     * Drains a filled container and places the fluid in front of the Dispenser.
     */
    @NotNull
    private ItemStack dumpContainer(BlockSource source, @NotNull ItemStack stack) {
        ItemStack singleStack = stack.m_41777_();
        singleStack.m_41764_(1);
        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(singleStack).orElse(null);
        if (fluidHandler == null)
            return super.m_7498_(source, stack);

        FluidStack fluidStack = fluidHandler.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
        Direction dispenserFacing = source.f_301783_().m_61143_(DispenserBlock.f_52659_);
        BlockPos blockpos = source.f_301784_().m_121945_(dispenserFacing);
        FluidActionResult result = FluidUtil.tryPlaceFluid(null, source.f_301782_(), InteractionHand.MAIN_HAND, blockpos, stack, fluidStack);

        if (result.isSuccess()) {
            ItemStack drainedStack = result.getResult();

            if (drainedStack.m_41613_() == 1)
                return drainedStack;

            if (!drainedStack.m_41619_()) {
                source.f_301785_().m_338820_(drainedStack);
                this.dispenseBehavior.m_6115_(source, drainedStack);
            }

            ItemStack stackCopy = drainedStack.m_41777_();
            stackCopy.m_41774_(1);
            return stackCopy;
        } else {
            return this.dispenseBehavior.m_6115_(source, stack);
        }
    }
}
