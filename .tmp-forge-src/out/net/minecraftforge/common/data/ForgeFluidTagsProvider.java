/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;
import static net.minecraftforge.common.Tags.Fluids.*;

@ApiStatus.Internal
public final class ForgeFluidTagsProvider extends FluidTagsProvider {
    public ForgeFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "forge", existingFileHelper);
    }

    @Override
    public void m_6577_(HolderLookup.Provider lookupProvider) {
        m_255286_(WATER).m_402050_(net.minecraft.world.level.material.Fluids.f_76193_).m_402050_(net.minecraft.world.level.material.Fluids.f_76192_);
        m_255286_(LAVA).m_402050_(net.minecraft.world.level.material.Fluids.f_76195_).m_402050_(net.minecraft.world.level.material.Fluids.f_76194_);
        m_255286_(MILK)
            .addOptional(ForgeMod.MILK.getKey().m_135782_())
            .addOptional(ForgeMod.FLOWING_MILK.getKey().m_135782_());
        m_255286_(GASEOUS);
        m_255286_(HONEY);
        m_255286_(POTION);
        m_255286_(SUSPICIOUS_STEW);
        m_255286_(MUSHROOM_STEW);
        m_255286_(RABBIT_STEW);
        m_255286_(BEETROOT_SOUP);
        m_255286_(HIDDEN_FROM_RECIPE_VIEWERS);

        // Backwards compat definitions for pre-1.21 legacy `forge:` tags.
        // TODO: Remove backwards compat tag entries in 1.22
        m_255286_(forgeTagKey("milk"))
            .addOptional(ForgeMod.MILK.getKey().m_135782_())
            .addOptional(ForgeMod.FLOWING_MILK.getKey().m_135782_());
    }

    private static TagKey<Fluid> forgeTagKey(String path) {
        return FluidTags.create(ResourceLocation.m_339182_("forge", path));
    }

    @Override
    public String m_6055_() {
        return "Forge Fluid Tags";
    }
}
