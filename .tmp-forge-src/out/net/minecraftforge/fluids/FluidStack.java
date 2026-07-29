/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fluids;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * ItemStack substitute for Fluids.
 *
 * NOTE: Equality is based on the Fluid, not the amount. Use
 * {@link #isFluidStackIdentical(FluidStack)} to determine if FluidID, Amount and NBT Tag are all
 * equal.
 *
 */
public class FluidStack {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final FluidStack EMPTY = new FluidStack(Fluids.f_76191_, 0);

    public static final Codec<FluidStack> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BuiltInRegistries.f_257020_.m_194605_().fieldOf("FluidName").forGetter(FluidStack::getFluid),
                    Codec.INT.fieldOf("Amount").forGetter(FluidStack::getAmount),
                    CompoundTag.f_128325_.optionalFieldOf("Tag").forGetter(stack -> Optional.ofNullable(stack.getTag()))
            ).apply(instance, (fluid, amount, tag) -> {
                FluidStack stack = new FluidStack(fluid, amount);
                tag.ifPresent(stack::setTag);
                return stack;
            })
    );

    private boolean isEmpty;
    private int amount;
    private @Nullable CompoundTag tag;
    private final Holder.Reference<Fluid> fluidDelegate;

    public FluidStack(Fluid fluid, int amount) {
        if (fluid == null) {
            LOGGER.fatal("Null fluid supplied to fluidstack. Did you try and create a stack for an unregistered fluid?");
            throw new IllegalArgumentException("Cannot create a fluidstack from a null fluid");
        } else if (ForgeRegistries.FLUIDS.getKey(fluid) == null) {
            LOGGER.fatal("Failed attempt to create a FluidStack for an unregistered Fluid {} (type {})", ForgeRegistries.FLUIDS.getKey(fluid), fluid.getClass().getName());
            throw new IllegalArgumentException("Cannot create a fluidstack from an unregistered fluid");
        }
        this.fluidDelegate = ForgeRegistries.FLUIDS.getDelegateOrThrow(fluid);
        this.amount = amount;

        updateEmpty();
    }

    public FluidStack(Fluid fluid, int amount, CompoundTag nbt) {
        this(fluid, amount);

        if (nbt != null)
            tag = nbt.m_6426_();
    }

    public FluidStack(FluidStack stack, int amount) {
        this(stack.getFluid(), amount, stack.tag);
    }

    /**
     * This provides a safe method for retrieving a FluidStack - if the Fluid is invalid, the stack
     * will return as null.
     */
    public static FluidStack loadFluidStackFromNBT(CompoundTag nbt) {
        if (nbt == null)
            return EMPTY;

        var fluidNameString = nbt.m_128461_("FluidName");
        if (fluidNameString.isEmpty())
            return EMPTY;

        ResourceLocation fluidName = ResourceLocation.m_338530_(fluidNameString.get());
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
        if (fluid == null)
            return EMPTY;

        FluidStack stack = new FluidStack(fluid, nbt.m_385147_("Amount", 0));

        nbt.m_128469_("Tag").ifPresent(tag -> stack.tag = tag);
        return stack;
    }

    public static FluidStack loadFluidStackFrom(ValueInput input) {
        if (input == null)
            return EMPTY;
        input.m_401629_("FluidName", null);
        var fluidNameString = input.m_401530_("FluidName");
        if (fluidNameString.isEmpty())
            return EMPTY;

        ResourceLocation fluidName = ResourceLocation.m_338530_(fluidNameString.get());
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
        if (fluid == null)
            return EMPTY;

        FluidStack stack = new FluidStack(fluid, input.m_402779_("Amount", 0));

        input.m_402757_("Tag", CompoundTag.f_128325_).ifPresent(tag -> stack.tag = tag);
        return stack;
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt.m_128359_("FluidName", ForgeRegistries.FLUIDS.getKey(getFluid()).toString());
        nbt.m_128405_("Amount", amount);

        if (tag != null)
            nbt.m_128365_("Tag", tag);
        return nbt;
    }

    public void writeTo(ValueOutput output) {
        output.m_402368_("FluidName", ForgeRegistries.FLUIDS.getKey(getFluid()).toString());
        output.m_402823_("Amount", amount);

        if (tag != null)
            output.m_404013_("Tag", CompoundTag.f_128325_, tag);
    }

    public final Fluid getFluid() {
        return isEmpty ? Fluids.f_76191_ : fluidDelegate.get();
    }

    public final Fluid getRawFluid() {
        return fluidDelegate.get();
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    protected void updateEmpty() {
        isEmpty = getRawFluid() == Fluids.f_76191_ || amount <= 0;
    }

    public int getAmount() {
        return isEmpty ? 0 : amount ;
    }

    public void setAmount(int amount) {
        if (getRawFluid() == Fluids.f_76191_) throw new IllegalStateException("Can't modify the empty stack.");
        this.amount = amount;
        updateEmpty();
    }

    public void grow(int amount) {
        setAmount(this.amount + amount);
    }

    public void shrink(int amount) {
        setAmount(this.amount - amount);
    }

    public boolean hasTag() {
        return tag != null;
    }

    public CompoundTag getTag() {
        return tag;
    }

    public void setTag(CompoundTag tag) {
        if (getRawFluid() == Fluids.f_76191_) throw new IllegalStateException("Can't modify the empty stack.");
        this.tag = tag;
    }

    public CompoundTag getOrCreateTag() {
        if (tag == null)
            setTag(new CompoundTag());
        return tag;
    }

    public @Nullable CompoundTag getChildTag(String childName) {
        if (tag == null)
            return null;
        return tag.m_384634_(childName);
    }

    public CompoundTag getOrCreateChildTag(String childName) {
        getOrCreateTag();
        var optional = tag.m_128469_(childName);
        if (optional.isEmpty()) {
            var child = new CompoundTag();
            tag.m_128365_(childName, child);
            return child;
        }

        return optional.get();
    }

    public void removeChildTag(String childName) {
        if (tag != null)
            tag.m_128473_(childName);
    }

    public Component getDisplayName() {
        return this.getFluid().getFluidType().getDescription(this);
    }

    public String getTranslationKey() {
        return this.getFluid().getFluidType().getDescriptionId(this);
    }

    /**
     * @return A copy of this FluidStack
     */
    public FluidStack copy() {
        return new FluidStack(getFluid(), amount, tag);
    }

    /**
     * Determines if the FluidIDs and NBT Tags are equal. This does not check amounts.
     *
     * @param other
     *            The FluidStack for comparison
     * @return true if the Fluids (IDs and NBT Tags) are the same
     */
    public boolean isFluidEqual(@NotNull FluidStack other) {
        return getFluid() == other.getFluid() && isFluidStackTagEqual(other);
    }

    private boolean isFluidStackTagEqual(FluidStack other) {
        return tag == null ? other.tag == null : other.tag != null && tag.equals(other.tag);
    }

    /**
     * Determines if the NBT Tags are equal. Useful if the FluidIDs are known to be equal.
     */
    public static boolean areFluidStackTagsEqual(@NotNull FluidStack stack1, @NotNull FluidStack stack2) {
        return stack1.isFluidStackTagEqual(stack2);
    }

    /**
     * Determines if the Fluids are equal and this stack is larger.
     *
     * @return true if this FluidStack contains the other FluidStack (same fluid and >= amount)
     */
    public boolean containsFluid(@NotNull FluidStack other) {
        return isFluidEqual(other) && amount >= other.amount;
    }

    /**
     * Determines if the FluidIDs, Amounts, and NBT Tags are all equal.
     *
     * @param other
     *            - the FluidStack for comparison
     * @return true if the two FluidStacks are exactly the same
     */
    public boolean isFluidStackIdentical(FluidStack other) {
        return isFluidEqual(other) && amount == other.amount;
    }

    /**
     * Determines if the FluidIDs and NBT Tags are equal compared to a registered container
     * ItemStack. This does not check amounts.
     *
     * @param other
     *            The ItemStack for comparison
     * @return true if the Fluids (IDs and NBT Tags) are the same
     */
    public boolean isFluidEqual(@NotNull ItemStack other) {
        return FluidUtil.getFluidContained(other).map(this::isFluidEqual).orElse(false);
    }

    @Override
    public final int hashCode() {
        int code = 1;
        code = 31*code + getFluid().hashCode();
        if (tag != null)
            code = 31*code + tag.hashCode();
        return code;
    }

    /**
     * Default equality comparison for a FluidStack. Same functionality as isFluidEqual().
     *
     * This is included for use in data structures.
     */
    @Override
    public final boolean equals(Object o) {
        return o instanceof FluidStack stack && isFluidEqual(stack);
    }
}
