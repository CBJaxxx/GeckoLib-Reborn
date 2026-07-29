/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.crafting.ingredients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Ingredient that matches if any of the child ingredients match */
public class CompoundIngredient extends AbstractIngredient {
    public static Ingredient of(Ingredient... children) {
        if (children.length == 0)
            throw new IllegalArgumentException("Cannot create a compound ingredient with no children, use Ingredient.of() to create an empty ingredient");

        if (children.length == 1)
            return children[0];

        return new CompoundIngredient(Arrays.asList(children));
    }

    private final List<Ingredient> children;
    private List<Holder<Item>> items;
    private final boolean isSimple;

    private CompoundIngredient(List<Ingredient> children) {
        this.children = Collections.unmodifiableList(children);
        this.isSimple = children.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    @NotNull
    public Stream<Holder<Item>> m_357234_() {
        if (this.items == null) {
            var tmp = new ArrayList<Holder<Item>>();
            for (var child : children)
                child.m_357234_().forEach(tmp::add);
            this.items = Collections.unmodifiableList(tmp);
        }
        return this.items.stream();
    }

    @Override
    public boolean test(@Nullable ItemStack target) {
        if (target == null)
            return false;

        for (var child : children) {
            if (child.test(target))
                return true;
        }

        return false;
    }

    @Override
    public boolean isSimple() {
        return isSimple;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> serializer() {
        return SERIALIZER;
    }

    public static final MapCodec<CompoundIngredient> CODEC = RecordCodecBuilder.mapCodec(builder ->
        builder.group(
            Ingredient.f_291570_.listOf().fieldOf("children").forGetter(i -> i.children)
        ).apply(builder, CompoundIngredient::new)
    );

    public static final IIngredientSerializer<CompoundIngredient> SERIALIZER = new IIngredientSerializer<>() {
        @Override
        public MapCodec<CompoundIngredient> codec() {
            return CODEC;
        }

        @Override
        public void write(RegistryFriendlyByteBuf buffer, CompoundIngredient value) {
            buffer.m_236828_(value.children, (buf, child) -> Ingredient.f_317040_.m_318638_(buffer, child));
        }

        @Override
        public CompoundIngredient read(RegistryFriendlyByteBuf buffer) {
            var children = buffer.m_236838_(ArrayList::new, buf -> Ingredient.f_317040_.m_318688_(buffer));
            return new CompoundIngredient(children);
        }
    };
}
