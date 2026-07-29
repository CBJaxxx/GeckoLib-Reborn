/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.registries;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.util.RandomSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class NamespacedWrapper<T> extends MappedRegistry<T> implements ILockableRegistry {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker MARKER = ForgeRegistry.REGISTRIES;
    private final ForgeRegistry<T> delegate;
    @Nullable
    private final Function<T, Holder.Reference<T>> intrusiveHolderCallback;
    private final Multimap<TagKey<T>, Supplier<T>> optionalTags = Multimaps.newSetMultimap(new IdentityHashMap<>(), HashSet::new);

    boolean locked = false;
    Lifecycle registryLifecycle = Lifecycle.stable();
    private boolean frozen = false; // Frozen is vanilla's variant of locked, but it can be unfrozen
    private List<Holder.Reference<T>> holdersSorted;
    private final ObjectList<Holder.Reference<T>> holdersById = new ObjectArrayList<>(256);
    private final Map<ResourceLocation, Holder.Reference<T>> holdersByName = new HashMap<>();
    private final Map<T, Holder.Reference<T>> holders = new IdentityHashMap<>();
    private final RegistryManager stage;
    private volatile Map<TagKey<T>, HolderSet.Named<T>> tags = new IdentityHashMap<>();
    private final Map<ResourceKey<T>, RegistrationInfo> registrationInfos = new IdentityHashMap<>();
    private MappedRegistry.TagSet<T> frozenTags = MappedRegistry.TagSet.m_352814_();

    NamespacedWrapper(ForgeRegistry<T> fowner, Function<T, Holder.Reference<T>> intrusiveHolderCallback, RegistryManager stage) {
        super(fowner.getRegistryKey(), Lifecycle.stable(), intrusiveHolderCallback != null);
        this.delegate = fowner;
        this.intrusiveHolderCallback = intrusiveHolderCallback;
        this.stage = stage;
    }

    @Override
    public Holder.Reference<T> m_255290_(ResourceKey<T> key, T value, RegistrationInfo info) {
        if (locked)
            throw new IllegalStateException("Can not register to a locked registry. Modder should use Forge Register methods.");

        Objects.requireNonNull(value);
        markKnown();

        this.registrationInfos.put(key, info);
        this.registryLifecycle = this.registryLifecycle.add(info.f_313951_());

        this.delegate.add(-1, key.m_135782_(), value);

        return getHolder(key, value);
    }

    // Reading Functions
    @Override
    @Nullable
    public T m_122327_(@Nullable ResourceLocation name) {
        return this.delegate.getRaw(name); //get without default
    }

    @Override
    public Optional<T> m_6612_(@Nullable ResourceLocation name) {
        return Optional.ofNullable(this.delegate.getRaw(name)); //get without default
    }

    @Override
    @Nullable
    public T m_122713_(@Nullable ResourceKey<T> name) {
        return name == null ? null : this.delegate.getRaw(name.m_135782_()); //get without default
    }

    @Override
    @Nullable
    public ResourceLocation m_7981_(T value) {
        return this.delegate.getKey(value);
    }

    @Override
    public Optional<ResourceKey<T>> m_7854_(T value) {
        return this.delegate.getResourceKey(value);
    }

    @Override
    public boolean m_7804_(ResourceLocation key) {
        return this.delegate.containsKey(key);
    }

    @Override
    public boolean m_142003_(ResourceKey<T> key) {
        return this.delegate.getRegistryName().equals(key.m_211136_()) && m_7804_(key.m_135782_());
    }

    @Override
    public int m_7447_(@Nullable T value) {
        return this.delegate.getID(value);
    }

    @Override
    @Nullable
    public T m_7942_(int id) {
        return this.delegate.getValue(id);
    }

    @Override
    public Lifecycle m_255098_() {
        return this.registryLifecycle;
    }

    @Override
    public Iterator<T> iterator() {
        return this.delegate.iterator();
    }

    @Override
    public Set<ResourceLocation> m_6566_() {
        return this.delegate.getKeys();
    }

    @Override
    public Set<ResourceKey<T>> m_214010_() {
        return this.delegate.getResourceKeys();
    }

    @Override
    public Set<Map.Entry<ResourceKey<T>, T>> m_6579_() {
        return this.delegate.getEntries();
    }

    @Override
    public boolean m_142427_() {
        return this.delegate.isEmpty();
    }

    @Override
    public int m_13562_() {
        return this.delegate.size();
    }

    /**
     * @deprecated Forge: For internal use only. Use the Register events when registering values.
     */
    @Deprecated
    @Override
    public void lock() {
        this.locked = true;
    }

    @Override
    public Optional<Holder.Reference<T>> m_7745_(int id) {
        return id >= 0 && id < this.holdersById.size() ? Optional.ofNullable(this.holdersById.get(id)) : Optional.empty();
    }

    @Override
    public Optional<Holder.Reference<T>> m_205904_(ResourceKey<T> key) {
        return Optional.ofNullable(this.holdersByName.get(key.m_135782_()));
    }

    @Override
    public Optional<Holder.Reference<T>> m_6246_(ResourceLocation p_333710_) {
        return Optional.ofNullable(this.holdersByName.get(p_333710_));
    }

    @Override
    public @NotNull Holder<T> m_263177_(@NotNull T value) {
        final Holder<T> holder = this.holders.get(value);
        return holder == null ? Holder.m_205709_(value) : holder;
    }

    @Override
    public Optional<RegistrationInfo> m_6228_(ResourceKey<T> p_331530_) {
        return Optional.ofNullable(this.registrationInfos.get(p_331530_));
    }

    public Optional<Holder.Reference<T>> getHolder(ResourceLocation location) {
        return Optional.ofNullable(this.holdersByName.get(location));
    }

    Optional<Holder<T>> getHolder(T value) {
        return Optional.ofNullable(this.holders.get(value));
    }

    @Override
    public HolderGetter<T> m_203505_() {
        this.validateWrite();
        return new HolderGetter<T>() {
            public Optional<Holder.Reference<T>> m_205904_(ResourceKey<T> key) {
                return Optional.of(this.m_255043_(key));
            }

            public Holder.Reference<T> m_255043_(ResourceKey<T> key) {
                return NamespacedWrapper.this.m_245420_(key);
            }

            public Optional<HolderSet.Named<T>> m_255050_(TagKey<T> key) {
                return Optional.of(this.m_254956_(key));
            }

            public HolderSet.Named<T> m_254956_(TagKey<T> key) {
                return NamespacedWrapper.this.m_354248_(key);
            }
        };
    }

    void validateWrite() {
        if (this.frozen)
            throw new IllegalStateException("Registry " + this.m_123023_().m_135782_() + " is already frozen");
    }

    void validateWrite(ResourceKey<T> key) {
        if (this.frozen)
            throw new IllegalStateException("Registry " + this.m_123023_().m_135782_() + " is already frozen (trying to add key " + key + ")");
    }

    protected Holder.Reference<T> m_245420_(ResourceKey<T> key) {
        return this.holdersByName.computeIfAbsent(key.m_135782_(), k -> {
            if (this.isIntrusive()) {
                throw new IllegalStateException("This registry can't create new holders without value");
            } else {
                this.validateWrite(key);
                return Holder.Reference.m_254896_(this, key);
            }
        });
    }

    @Override
    public Optional<Holder.Reference<T>> m_213642_(RandomSource rand) {
        return Util.m_214676_(this.getSortedHolders(), rand);
    }

    @Override
    public Stream<Holder.Reference<T>> m_205916_() {
        return this.getSortedHolders().stream();
    }

    @Override
    public Stream<HolderSet.Named<T>> m_203612_() {
        return this.frozenTags.m_353074_();
    }

    @Override
    public HolderSet.Named<T> m_354248_(TagKey<T> name) {
        return this.tags.computeIfAbsent(name, this::createTag);
    }

    void addOptionalTag(TagKey<T> name, @NotNull Set<? extends Supplier<T>> defaults) {
        this.optionalTags.putAll(name, defaults);
    }

    /*
    @Override
    public Stream<TagKey<T>> getTagNames() {
        return this.tags.keySet().stream();
    }
    */

    @Override
    public Registry<T> m_203521_() {
        if (frozen) // vanilla calls freeze on frozen registries all the time. which makes things annoying
            return this;

        this.frozen = true;
        List<ResourceLocation> unregistered = this.holdersByName.entrySet().stream()
                .filter(e -> !e.getValue().m_203633_())
                .map(Map.Entry::getKey).sorted().toList();

        if (!unregistered.isEmpty())
            throw new IllegalStateException("Unbound values in registry " + this.m_123023_() + ": " + unregistered.stream().map(ResourceLocation::toString).collect(Collectors.joining(", \n\t")));

        if (this.f_244282_ != null && this.f_244282_.values().stream().anyMatch(r -> !r.m_203633_() && r.getType() == Holder.Reference.Type.INTRUSIVE)) {
            throw new IllegalStateException("Some intrusive holders were not registered: " + this.f_244282_.values() + " Hint: Did you register all your registry objects? Registry stage: " + stage.getName());
        }

        if (this.frozenTags.m_355333_())
            throw new IllegalStateException("Tags already present before freezing");

        var unbound = this.tags.entrySet().stream()
            .filter(t -> !t.getValue().m_353784_())
            .map(t -> t.getKey().f_203868_())
            .sorted()
            .toList();

        if (!unbound.isEmpty()) {
            LOGGER.debug(MARKER, "Unbound tags in registry " + this.m_123023_() + ": " + unbound);
            this.bindAllUnboundTagsToEmpty();
        }

        this.frozenTags = MappedRegistry.TagSet.m_353726_(this.tags);
        this.delegate.onBindTags(this.tags);
        this.refreshTagsInHoldersForge();
        return this;
    }

    private void refreshTagsInHoldersForge() {
        Map<Holder.Reference<T>, List<TagKey<T>>> map = new IdentityHashMap<>();
        for (var value : this.holdersByName.values()) {
            map.put(value, new ArrayList<>());
        }

        this.frozenTags.m_352679_((key, value) -> {
            for (var holder : value) {
                var reference = this.validateAndUnwrapTagElement(key, holder);
                map.get(reference).add(key);
            }
        });

        for (var entry : map.entrySet()) {
            entry.getKey().m_205769_(entry.getValue());
        }
    }

    private Holder.Reference<T> validateAndUnwrapTagElement(TagKey<T> key, Holder<T> value) {
        if (!value.m_203401_(this))
            throw new IllegalStateException("Can't create named set " + key + " containing value " + value + " from outside registry " + this);

        if (value instanceof Holder.Reference<T> reference)
            return reference;

        throw new IllegalStateException("Found direct holder " + value + " value in tag " + key);
    }

    @Override
    public Holder.Reference<T> m_203693_(T value) {
        if (!this.isIntrusive())
            throw new IllegalStateException("This registry can't create intrusive holders");

        this.validateWrite();

        return super.m_203693_(value);
    }

    @Override
    public Optional<HolderSet.Named<T>> m_255050_(TagKey<T> name) {
        return this.frozenTags.m_354678_(name);
    }

    @Override
    public void m_355219_(TagKey<T> key, List<Holder<T>> newTags) {
        this.validateWrite();
        this.m_354248_(key).m_205835_(newTags);
    }

    @Override
    public void m_357154_() {
        this.validateWrite();
        for (var tag : this.tags.values())
            tag.m_205835_(List.of());
    }

    private void bindAllUnboundTagsToEmpty() {
        for (var tag : this.tags.values()) {
            if (!tag.m_353784_())
                tag.m_205835_(List.of());
        }
    }

    @Override
    public Registry.PendingTags<T> m_351680_(TagLoader.LoadResult<T> data) {
        if (!this.frozen)
            throw new IllegalStateException("Invalid method used for tag loading");

        var _old = ImmutableMap.<TagKey<T>, HolderSet.Named<T>>builder();
        var _new = ImmutableMap.<TagKey<T>, List<Holder<T>>>builder();

        for (var entry : data.f_349435_().entrySet()) {
            var key = entry.getKey();
            var existing = this.tags.get(key);
            if (existing == null)
                existing = this.createTag(key);
            _old.put(key, existing);
            _new.put(key, List.copyOf(entry.getValue()));
        }

        for (var entry : this.optionalTags.entries()) {
            var key = entry.getKey();
            if (data.f_349435_().containsKey(key))
                continue;

            var value = entry.getValue().get();
            var holder = this.getHolder(value).orElse(null);
            if (holder == null)
                continue;

            var existing = this.tags.get(key);
            if (existing == null)
                existing = this.createTag(key);
            _old.put(key, existing);
            _new.put(key, List.of(holder));
        }

        var oldBindings = _old.build();
        var newBindings = _new.build();

        var oldSnapshot = new HolderLookup.RegistryLookup.Delegate<T>() {
            @Override
            public HolderLookup.RegistryLookup<T> m_254893_() {
                return NamespacedWrapper.this;
            }

            @Override
            public Optional<HolderSet.Named<T>> m_255050_(TagKey<T> p_259486_) {
                return Optional.ofNullable(oldBindings.get(p_259486_));
            }

            @Override
            public Stream<HolderSet.Named<T>> m_214063_() {
                return oldBindings.values().stream();
            }
        };

        return new Registry.PendingTags<T>() {
            @Override
            public ResourceKey<? extends Registry<? extends T>> m_352508_() {
                return NamespacedWrapper.this.m_123023_();
            }

            @Override
            public int m_352479_() {
                return newBindings.size();
            }

            @Override
            public HolderLookup.RegistryLookup<T> m_351665_() {
                return oldSnapshot;
            }

            @Override
            public List<Holder<T>> getPending(TagKey<T> key) {
                return newBindings.getOrDefault(key, List.of());
            }

            @Override
            public void m_354631_() {
                for (var entry : oldBindings.entrySet()) {
                    var newList = newBindings.getOrDefault(entry.getKey(), List.of());
                    entry.getValue().m_205835_(newList);
                }

                NamespacedWrapper.this.frozenTags = MappedRegistry.TagSet.m_353726_(oldBindings);
                NamespacedWrapper.this.delegate.onBindTags(NamespacedWrapper.this.tags);
                NamespacedWrapper.this.refreshTagsInHoldersForge();
            }
        };
    }

    @Override
    public void unfreeze() {
        this.frozen = false;
        this.frozenTags = MappedRegistry.TagSet.m_352814_();
    }

    boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public boolean isIntrusive() {
        return this.intrusiveHolderCallback != null && this.stage == RegistryManager.ACTIVE;
    }

    @Nullable
    Holder.Reference<T> onAdded(RegistryManager stage, int id, ResourceKey<T> key, T newValue, T oldValue)  {
        //Holder.Reference<T> oldHolder = oldValue == null ? null : getHolder(key, oldValue);
        // Do we need to do anything with the old holder? We cant update its pointer unless its non-intrusive...
        // And if thats the case, then we *should* get its reference in newHolder

        Holder.Reference<T> newHolder = getHolder(key, newValue);

        this.holdersById.size(Math.max(this.holdersById.size(), id + 1));
        this.holdersById.set(id, newHolder);
        this.holdersByName.put(key.m_135782_(), newHolder);
        this.holders.put(newValue, newHolder);
        if (this.f_244282_ != null) {
            this.f_244282_.remove(newValue);
            newHolder.m_246870_(key);
        }
        newHolder.m_247654_(newValue);
        this.holdersSorted = null;

        return newHolder;
    }

    private HolderSet.Named<T> createTag(TagKey<T> name) {
        return new HolderSet.Named<>(this, name);
    }

    private Holder.Reference<T> getHolder(ResourceKey<T> key, T value) {
        if (this.isIntrusive())
            return this.intrusiveHolderCallback.apply(value);

        return this.holdersByName.computeIfAbsent(key.m_135782_(), k -> Holder.Reference.m_254896_(this, key));
    }

    private List<Holder.Reference<T>> getSortedHolders() {
        if (this.holdersSorted == null)
            this.holdersSorted = this.holdersById.stream().filter(Objects::nonNull).toList();

        return this.holdersSorted;
    }
}
