/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.world;

import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

/**
 * Extension of the vanilla builder but also provides read access and a copy-from-existing-data helper.
 * Also, the base builder crashes if certain values aren't specified on build, so this enforces the setting of those.
 */
public class BiomeSpecialEffectsBuilder extends BiomeSpecialEffects.Builder {
    public static BiomeSpecialEffectsBuilder copyOf(BiomeSpecialEffects baseEffects) {
        BiomeSpecialEffectsBuilder builder = BiomeSpecialEffectsBuilder.create(baseEffects.m_47967_(), baseEffects.m_47972_(), baseEffects.m_47975_(), baseEffects.m_47978_());
        builder.f_48011_ = baseEffects.m_47987_();
        baseEffects.m_47981_().ifPresent(builder::m_48043_);
        baseEffects.m_47984_().ifPresent(builder::m_48045_);
        baseEffects.m_47990_().ifPresent(builder::m_48029_);
        baseEffects.m_47993_().ifPresent(builder::m_48023_);
        baseEffects.m_47996_().ifPresent(builder::m_48027_);
        baseEffects.m_47999_().ifPresent(builder::m_48025_);
        baseEffects.m_48002_().ifPresent(builder::m_373224_);
        return builder;
    }

    public static BiomeSpecialEffectsBuilder create(int fogColor, int waterColor, int waterFogColor, int skyColor) {
        return new BiomeSpecialEffectsBuilder(fogColor, waterColor, waterFogColor, skyColor);
    }

    protected BiomeSpecialEffectsBuilder(int fogColor, int waterColor, int waterFogColor, int skyColor) {
        super();
        this.m_48019_(fogColor);
        this.m_48034_(waterColor);
        this.m_48037_(waterFogColor);
        this.m_48040_(skyColor);
    }

    public int getFogColor() {
        return this.f_48005_.getAsInt();
    }

    public int waterColor() {
        return this.f_48006_.getAsInt();
    }

    public int getWaterFogColor() {
        return this.f_48007_.getAsInt();
    }

    public int getSkyColor() {
        return this.f_48008_.getAsInt();
    }

    public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
        return this.f_48011_;
    }

    public Optional<Integer> getFoliageColorOverride() {
        return this.f_48009_;
    }

    public Optional<Integer> getGrassColorOverride() {
        return this.f_48010_;
    }

    public Optional<AmbientParticleSettings> getAmbientParticle() {
        return this.f_48012_;
    }

    public Optional<Holder<SoundEvent>> getAmbientLoopSound() {
        return this.f_48013_;
    }

    public Optional<AmbientMoodSettings> getAmbientMoodSound() {
        return this.f_48014_;
    }

    public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
        return this.f_48015_;
    }

    public Optional<WeightedList<Music>> getBackgroundMusic() {
        return this.f_48016_;
    }
}