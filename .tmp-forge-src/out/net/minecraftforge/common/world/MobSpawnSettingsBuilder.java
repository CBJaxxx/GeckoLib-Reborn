/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.world;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.Util;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class MobSpawnSettingsBuilder extends MobSpawnSettings.Builder {
    private final Set<MobCategory> typesView = Collections.unmodifiableSet(this.f_48362_.keySet());
    private final Set<EntityType<?>> costView = Collections.unmodifiableSet(this.f_48363_.keySet());

    public MobSpawnSettingsBuilder(MobSpawnSettings orig) {
        orig.getSpawnerTypes().forEach(k ->
            f_48362_.put(k, WeightedList.<MobSpawnSettings.SpawnerData>m_384576_().addAll(orig.m_151798_(k).m_387721_()))
        );
        orig.getEntityTypes().forEach(k -> f_48363_.put(k, orig.m_48345_(k)));
        f_48364_ = orig.m_48344_();
    }

    public Set<MobCategory> getSpawnerTypes() {
        return this.typesView;
    }

    public WeightedList.Builder<MobSpawnSettings.SpawnerData> getSpawner(MobCategory type) {
        return this.f_48362_.get(type);
    }

    public Set<EntityType<?>> getEntityTypes() {
        return this.costView;
    }

    @Nullable
    public MobSpawnSettings.MobSpawnCost getCost(EntityType<?> type) {
        return this.f_48363_.get(type);
    }

    public float getProbability() {
        return this.f_48364_;
    }

    public MobSpawnSettingsBuilder disablePlayerSpawn() {
        return this;
    }
}