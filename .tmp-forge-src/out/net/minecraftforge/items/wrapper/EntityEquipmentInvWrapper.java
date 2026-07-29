/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items.wrapper;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the armor or hands inventory of an {@link LivingEntity} as an {@link IItemHandler} using {@link LivingEntity#getItemBySlot(EquipmentSlot)} and
 * {@link LivingEntity#setItemSlot(EquipmentSlot, ItemStack)}.
 */
public abstract class EntityEquipmentInvWrapper implements IItemHandlerModifiable {
    /**
     * The entity.
     */
    protected final LivingEntity entity;

    /**
     * The slots exposed by this wrapper, with {@link EquipmentSlot#getIndex()} as the index.
     */
    protected final List<EquipmentSlot> slots;

    /**
     * @param entity   The entity.
     * @param slotType The slot type to expose.
     */
    public EntityEquipmentInvWrapper(final LivingEntity entity, final EquipmentSlot.Type mainType) {
        this(entity, mainType, (EquipmentSlot.Type[])null);
    }

    public EntityEquipmentInvWrapper(final LivingEntity entity, final EquipmentSlot.Type mainType, final EquipmentSlot.Type... types) {
        this.entity = entity;

        var slots = new ArrayList<EquipmentSlot>();

        for (var slot : EquipmentSlot.values()) {
            if (slot.m_20743_() == mainType)
                slots.add(slot);
            else if (types != null && types.length > 0) {
                for (var type : types) {
                    if (slot.m_20743_() == type) {
                        slots.add(slot);
                        break;
                    }
                }
            }
        }

        this.slots = ImmutableList.copyOf(slots);
    }

    @Override
    public int getSlots() {
        return slots.size();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(final int slot) {
        return entity.m_6844_(validateSlotIndex(slot));
    }

    @NotNull
    @Override
    public ItemStack insertItem(final int slot, @NotNull final ItemStack stack, final boolean simulate) {
        if (stack.m_41619_())
            return ItemStack.f_41583_;

        final EquipmentSlot equipmentSlot = validateSlotIndex(slot);

        final ItemStack existing = entity.m_6844_(equipmentSlot);

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
            if (existing.m_41619_())
                entity.m_21035_(equipmentSlot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            else
                existing.m_41769_(reachedLimit ? limit : stack.m_41613_());
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.m_41613_() - limit) : ItemStack.f_41583_;
    }

    @NotNull
    @Override
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
        if (amount == 0)
            return ItemStack.f_41583_;

        final EquipmentSlot equipmentSlot = validateSlotIndex(slot);

        final ItemStack existing = entity.m_6844_(equipmentSlot);

        if (existing.m_41619_())
            return ItemStack.f_41583_;

        final int toExtract = Math.min(amount, existing.m_41741_());

        if (existing.m_41613_() <= toExtract) {
            if (!simulate)
                entity.m_21035_(equipmentSlot, ItemStack.f_41583_);

            return existing;
        } else {
            if (!simulate)
                entity.m_21035_(equipmentSlot, ItemHandlerHelper.copyStackWithSize(existing, existing.m_41613_() - toExtract));

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(final int slot) {
        final EquipmentSlot equipmentSlot = validateSlotIndex(slot);
        return (equipmentSlot.m_20743_() == EquipmentSlot.Type.HUMANOID_ARMOR || equipmentSlot.m_20743_() == EquipmentSlot.Type.ANIMAL_ARMOR) ? 1 : 64;
    }

    protected int getStackLimit(final int slot, @NotNull final ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.m_41741_());
    }

    @Override
    public void setStackInSlot(final int slot, @NotNull final ItemStack stack) {
        final EquipmentSlot equipmentSlot = validateSlotIndex(slot);
        if (ItemStack.m_41728_(entity.m_6844_(equipmentSlot), stack))
            return;
        entity.m_21035_(equipmentSlot, stack);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    protected EquipmentSlot validateSlotIndex(final int slot) {
        if (slot < 0 || slot >= slots.size())
            throw new IllegalArgumentException("Slot " + slot + " not in valid range - [0," + slots.size() + ")");

        return slots.get(slot);
    }

    public static LazyOptional<IItemHandlerModifiable>[] create(LivingEntity entity) {
        @SuppressWarnings("unchecked")
        LazyOptional<IItemHandlerModifiable>[] ret = new LazyOptional[3];

        ret[0] = LazyOptional.of(() -> new EntityHandsInvWrapper(entity));
        ret[1] = LazyOptional.of(() -> new EntityArmorInvWrapper(entity));
        ret[2] = LazyOptional.of(() -> new CombinedInvWrapper(ret[0].orElse(null), ret[1].orElse(null)));
        return ret;
    }
}
