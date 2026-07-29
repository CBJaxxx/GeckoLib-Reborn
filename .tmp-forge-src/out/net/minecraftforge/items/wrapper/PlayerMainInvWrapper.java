/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items.wrapper;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Exposes the player inventory WITHOUT the armor inventory as IItemHandler.
 * Also takes core of inserting/extracting having the same logic as picking up items.
 */
public class PlayerMainInvWrapper extends RangedWrapper {
    private final Inventory inventoryPlayer;

    public PlayerMainInvWrapper(Inventory inv) {
        super(new InvWrapper(inv), 0, inv.m_384294_().size());
        inventoryPlayer = inv;
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack rest = super.insertItem(slot, stack, simulate);
        if (rest.m_41613_()!= stack.m_41613_()) {
            // the stack in the slot changed, animate it
            ItemStack inSlot = getStackInSlot(slot);
            if(!inSlot.m_41619_()) {
                var world = getInventoryPlayer().f_35978_.m_9236_();
                if (world.f_46443_) {
                    inSlot.m_41754_(5);
                } else if (getInventoryPlayer().f_35978_ instanceof ServerPlayer) {
                    getInventoryPlayer().f_35978_.f_36096_.m_38946_();
                }
            }
        }
        return rest;
    }

    public Inventory getInventoryPlayer() {
        return inventoryPlayer;
    }
}
