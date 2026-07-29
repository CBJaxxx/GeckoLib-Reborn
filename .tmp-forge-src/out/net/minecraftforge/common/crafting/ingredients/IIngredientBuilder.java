/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.crafting.ingredients;

import java.util.Arrays;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface IIngredientBuilder {
    default PartialNBTIngredient.Builder partialNbt() {
        return PartialNBTIngredient.builder();
    }

    default Ingredient strictNbt(ItemStack value) {
        return StrictNBTIngredient.of(value);
    }

    default Ingredient compound(Ingredient... values) {
        return CompoundIngredient.of(values);
    }

    @SuppressWarnings("unchecked")
    default Ingredient intersection(HolderGetter<Item> lookup, @SuppressWarnings("rawtypes") TagKey... values) {
        return intersection(Arrays.stream(values).map(k -> holder(lookup, (TagKey<Item>)k)).map(Ingredient::m_204132_).toArray(Ingredient[]::new));
    }

    default Ingredient intersection(Ingredient... values) {
        return IntersectionIngredient.of(values);
    }

    default Ingredient difference(HolderGetter<Item> lookup, TagKey<Item> base, TagKey<Item> subtracted) {
        return difference(Ingredient.m_204132_(holder(lookup, base)), Ingredient.m_204132_(holder(lookup, subtracted)));
    }

    default Ingredient difference(Ingredient base, Ingredient subtracted) {
        return DifferenceIngredient.of(base, subtracted);
    }

    private static HolderSet<Item> holder(HolderGetter<Item> lookup, TagKey<Item> key) {
        return lookup.m_254956_(key);
    }
}
