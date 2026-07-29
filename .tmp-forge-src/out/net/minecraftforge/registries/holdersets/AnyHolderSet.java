/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.registries.holdersets;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.ForgeMod;

/**
 * <p>Holderset that represents all elements of a registry. Json format:</p>
 * <pre>
 * {
 *   "type": "forge:any"
 * }
 * </pre>
 */
public record AnyHolderSet<T>(HolderLookup.RegistryLookup<T> registryLookup) implements ICustomHolderSet<T> {
    public static <T> MapCodec<? extends ICustomHolderSet<T>> codec(ResourceKey<? extends Registry<T>> registryKey, Codec<Holder<T>> holderCodec, boolean forceList) {
        return RegistryOps.retrieveRegistryLookup(registryKey)
            .xmap(AnyHolderSet::new, AnyHolderSet::registryLookup);
    }

    @Override
    public HolderSetType type() {
        return ForgeMod.ANY_HOLDER_SET.get();
    }

    @Override
    public Iterator<Holder<T>> iterator() {
        return this.m_203614_().iterator();
    }

    @Override
    public Stream<Holder<T>> m_203614_() {
        return this.registryLookup.m_205916_().map(Function.identity());
    }

    @Override
    public int m_203632_() {
        return (int)this.m_203614_().count();
    }

    @Override
    public Either<TagKey<T>, List<Holder<T>>> m_203440_() {
        return Either.right(this.m_203614_().toList());
    }

    @Override
    public Optional<Holder<T>> m_213653_(RandomSource random) {
        return Util.m_214676_(this.m_203614_().toList(), random);
    }

    @Override
    public Holder<T> m_203662_(int i) {
        List<Holder<T>> holders = this.m_203614_().toList();
        Holder<T> holder = i >= holders.size() ? null : holders.get(i);
        if (holder == null)
            throw new NoSuchElementException("No element " + i + " in registry " + this.registryLookup.m_123023_());

        return holder;
    }

    @Override
    public boolean m_203333_(Holder<T> holder) {
        return holder.m_203543_().map(key -> this.registryLookup.m_255209_().anyMatch(key::equals)).orElse(false);
    }

    @Override
    public boolean m_207277_(HolderOwner<T> holderOwner) {
        return this.registryLookup.m_255301_(holderOwner);
    }

    @Override
    public Optional<TagKey<T>> m_245234_() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "AnySet(" + this.registryLookup.m_123023_() + ")";
    }

    @Override
    public boolean m_353784_() {
        return true;
    }
}