/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.loot;

import java.io.IOException;
import java.util.*;

import com.mojang.serialization.JsonOps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;

public class LootModifierManager extends SimpleJsonResourceReloadListener<JsonElement> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final String FOLDER = "loot_modifiers";

    private final HolderLookup.Provider registries;
    private Map<ResourceLocation, IGlobalLootModifier> modifiers = ImmutableMap.of();

    public LootModifierManager(HolderLookup.Provider registries) {
        super(registries.m_318927_(JsonOps.INSTANCE), ExtraCodecs.f_252400_, FileToIdConverter.m_246568_(FOLDER));
        this.registries = registries;
    }

    @Override
    protected Map<ResourceLocation, JsonElement> m_5944_(ResourceManager resources, ProfilerFiller profilerFiller) {
        var path = ResourceLocation.m_339182_("forge", FOLDER + "/global_loot_modifiers.json");

        List<ResourceLocation> toLoad = new ArrayList<>();
        //read in all data files from forge:loot_modifiers/global_loot_modifiers in order to do layering
        for (var resource : resources.m_213829_(path)) {
            try (var reader = resource.m_215508_()) {
                var json = GsonHelper.m_13776_(GSON, reader, JsonObject.class);

                if (GsonHelper.m_13855_(json, "replace", false))
                    toLoad.clear();

                for(var entry : GsonHelper.m_13933_(json, "entries")) {
                    ResourceLocation loc = ResourceLocation.m_338530_(entry.getAsString());
                    toLoad.remove(loc); //remove and re-add if needed, to update the ordering.
                    toLoad.add(loc);
                }
            } catch (RuntimeException | IOException ioexception) {
                LOGGER.error("Couldn't read global loot modifier list {} in data pack {}", path, resource.m_215506_(), ioexception);
            }
        }

        //use layered config to fetch modifier data files (modifiers missing from config are disabled)
        var ret = super.m_5944_(resources, profilerFiller);
        ret.keySet().removeIf(k -> !toLoad.contains(k));
        return ret;
    }

    @Override
    protected void m_5787_(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        Builder<ResourceLocation, IGlobalLootModifier> builder = ImmutableMap.builder();
        var ops = registries.m_318927_(JsonOps.INSTANCE);
        resources.forEach((location, json) -> {
            IGlobalLootModifier.DIRECT_CODEC.parse(ops, json)
                // log error if parse fails
                .ifError(error -> LOGGER.warn("Could not decode GlobalLootModifier with json id {} - error: {}", location, error.message()))
                // add loot modifier if parse succeeds
                .ifSuccess(modifier -> builder.put(location, modifier));
        });
        this.modifiers = builder.build();
    }

    /**
     * An immutable collection of the registered loot modifiers in layered order.
     */
    public Collection<IGlobalLootModifier> getAllLootMods() {
        return modifiers.values();
    }
}