/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.registries.holdersets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.ForgeMod;

/**
 * <p>Holderset that represents all elements of a registry not present in another holderset.
 * forge:exclusion is preferable when the number of allowed elements is small relative to the size of the registry.
 * Json format:</p>
 * <pre>
 * {
 *   "type": "forge:not",
 *   "value": "not_this_holderset" // string, list, or object
 * }
 * </pre>
 */
// this doesn't extend CompositeHolderSet because it doesn't need to cache a set
public class NotHolderSet<T> implements ICustomHolderSet<T> {
    public static <T> MapCodec<? extends ICustomHolderSet<T>> codec(ResourceKey<? extends Registry<T>> registryKey, Codec<Holder<T>> holderCodec, boolean forceList) {
        return RecordCodecBuilder.<NotHolderSet<T>>mapCodec(builder -> builder.group(
                RegistryOps.retrieveRegistryLookup(registryKey).forGetter(NotHolderSet::registryLookup),
                HolderSetCodec.m_206685_(registryKey, holderCodec, forceList).fieldOf("value").forGetter(NotHolderSet::value)
            ).apply(builder, NotHolderSet::new));
    }

    private final List<Runnable> owners = new ArrayList<>();
    private final HolderLookup.RegistryLookup<T> registryLookup;
    private final HolderSet<T> value;
    @Nullable
    private List<Holder<T>> list = null;

    public NotHolderSet(HolderLookup.RegistryLookup<T> registryLookup, HolderSet<T> value) {
        this.registryLookup = registryLookup;
        this.value = value;
        this.value.addInvalidationListener(this::invalidate);
    }

    public HolderLookup.RegistryLookup<T> registryLookup() {
        return this.registryLookup;
    }

    public HolderSet<T> value() {
        return this.value;
    }

    @Override
    public HolderSetType type() {
        return ForgeMod.NOT_HOLDER_SET.get();
    }

    @Override
    public void addInvalidationListener(Runnable runnable) {
        this.owners.add(runnable);
    }

    @Override
    public Iterator<Holder<T>> iterator() {
        return this.getList().iterator();
    }

    @Override
    public Stream<Holder<T>> m_203614_() {
        return this.getList().stream();
    }

    @Override
    public int m_203632_() {
        return this.getList().size();
    }

    @Override
    public Either<TagKey<T>, List<Holder<T>>> m_203440_() {
        return Either.right(this.getList());
    }

    @Override
    public Optional<Holder<T>> m_213653_(RandomSource random) {
        List<Holder<T>> list = this.getList();
        int size = list.size();
        return size > 0
            ? Optional.of(list.get(random.m_188503_(size)))
            : Optional.empty();
    }

    @Override
    public Holder<T> m_203662_(int i) {
        return this.getList().get(i);
    }

    @Override
    public boolean m_203333_(Holder<T> holder) {
        return !this.value.m_203333_(holder);
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
        return "NotSet(" + this.value + ")";
    }

    private List<Holder<T>> getList() {
        List<Holder<T>> thisList = this.list;
        if (thisList == null) {
            List<Holder<T>> list = this.registryLookup.m_205916_()
                .filter(holder -> !this.value.m_203333_(holder))
                .map(holder -> (Holder<T>)holder)
                .toList();
            this.list = list;
            return list;
        } else {
            return thisList;
        }
    }

    private void invalidate() {
        this.list = null;
        for (Runnable runnable : this.owners) {
            runnable.run();
        }
    }

    @Override
    public boolean m_353784_() {
        return this.value.m_353784_();
    }
}