/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.crafting.ingredients;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

public interface IIngredientSerializer<T extends Ingredient> {
    MapCodec<? extends T> codec();

    void write(RegistryFriendlyByteBuf buffer, T value);
    T read(RegistryFriendlyByteBuf buffer);
}
