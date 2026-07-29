/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.crafting.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.Holder;
import net.minecraft.resources.DelegatingOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

import org.jetbrains.annotations.ApiStatus;

public interface ICondition {
    Codec<ICondition> CODEC = Codec.lazyInitialized(() -> ForgeRegistries.CONDITION_SERIALIZERS.get().getCodec().dispatch(ICondition::codec, Function.identity()));
    String DEFAULT_FIELD = "forge:condition";
    MapCodec<Optional<ICondition>> OPTIONAL_FEILD_CODEC = CODEC.optionalFieldOf(DEFAULT_FIELD);
    Codec<ICondition> SAFE_CODEC = CODEC.orElse(FalseCondition.INSTANCE);

    boolean test(IContext context, DynamicOps<?> ops);

    MapCodec<? extends ICondition> codec();

    interface IContext {
        /* Key used to attach this context option to a DynamicOps instance */
        @ApiStatus.Internal
        public static ResourceLocation KEY = ResourceLocation.m_339182_("forge", "condition_context");

        default <T, O extends DelegatingOps<T>> O wrap(O ops) {
            return ops.withContext(KEY, this);
        }

        IContext EMPTY = new IContext() {
            @Override
            public <T> Collection<Holder<T>> getTag(TagKey<T> key) {
                return Collections.emptyList();
            }
        };

        IContext TAGS_INVALID = new IContext() {
            @Override
            public <T> Collection<Holder<T>> getTag(TagKey<T> key) {
                throw new UnsupportedOperationException("Usage of tag-based conditions is not permitted in this context!");
            }
        };

        /**
         * Return the requested tag if available, or an empty tag otherwise.
         */
        <T> Collection<Holder<T>> getTag(TagKey<T> key);
    }
}
