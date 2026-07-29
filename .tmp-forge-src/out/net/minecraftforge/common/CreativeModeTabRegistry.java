/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.Comparator;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.fml.loading.toposort.TopologicalSort;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CreativeModeTabRegistry {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation CREATIVE_MODE_TAB_ORDERING_JSON = ResourceLocation.m_339182_("forge", "creative_mode_tab_ordering.json");
    private static final List<CreativeModeTab> SORTED_TABS = new ArrayList<>();
    private static final List<CreativeModeTab> SORTED_TABS_VIEW = Collections.unmodifiableList(SORTED_TABS);
    private static final List<CreativeModeTab> DEFAULT_TABS = new ArrayList<>();

    /**
     * {@return an unmodifiable view of the sorted list of creative mode tabs in ascending order}
     */
    public static List<CreativeModeTab> getSortedCreativeModeTabs() {
        return SORTED_TABS_VIEW;
    }

    public static List<CreativeModeTab> getDefaultTabs() {
        return Collections.unmodifiableList(DEFAULT_TABS);
    }

    /**
     * {@return the {@linkplain CreativeModeTab} with the given name, or null}
     *
     * @param name the name to look up
     */
    @Nullable
    public static CreativeModeTab getTab(ResourceLocation name) {
        return BuiltInRegistries.f_279662_.m_122327_(name);
    }

    /**
     * {@return the name of the given {@linkplain CreativeModeTab}, or null}
     *
     * @param tab the tab to look up
     */
    @Nullable
    public static ResourceLocation getName(CreativeModeTab tab) {
        return BuiltInRegistries.f_279662_.m_7981_(tab);
    }

    private static final Multimap<ResourceLocation, ResourceLocation> edges = HashMultimap.create();

    static PreparableReloadListener getReloadListener() {
        return new SimplePreparableReloadListener<JsonObject>() {
            final Gson gson = new GsonBuilder().create();

            @NotNull
            @Override
            protected JsonObject m_5944_(@NotNull ResourceManager resourceManager, ProfilerFiller profiler) {
                Optional<Resource> res = resourceManager.m_213713_(CREATIVE_MODE_TAB_ORDERING_JSON);
                if (res.isEmpty())
                    return new JsonObject();

                try (Reader reader = res.get().m_215508_()) {
                    return this.gson.fromJson(reader, JsonObject.class);
                } catch (IOException e) {
                    LOGGER.error("Could not read CreativeModeTab sorting file " + CREATIVE_MODE_TAB_ORDERING_JSON, e);
                    return new JsonObject();
                }
            }

            @Override
            protected void m_5787_(@NotNull JsonObject data, @NotNull ResourceManager resourceManager, ProfilerFiller p) {
                try {
                    if (!data.isEmpty()) {
                        JsonArray order = GsonHelper.m_13933_(data, "order");
                        List<CreativeModeTab> customOrder = new ArrayList<>();
                        for (JsonElement entry : order) {
                            ResourceLocation id = ResourceLocation.m_338530_(entry.getAsString());
                            CreativeModeTab CreativeModeTab = getTab(id);
                            if (CreativeModeTab == null)
                                throw new IllegalStateException("CreativeModeTab not found with name " + id);
                            customOrder.add(CreativeModeTab);
                        }

                        List<CreativeModeTab> missingCreativeModeTabs = BuiltInRegistries.f_279662_.m_123024_().filter(CreativeModeTab -> !customOrder.contains(CreativeModeTab)).toList();
                        if (!missingCreativeModeTabs.isEmpty())
                            throw new IllegalStateException("CreativeModeTabs missing from the ordered list: " + missingCreativeModeTabs.stream()
                                    .map(CreativeModeTab -> Objects.toString(CreativeModeTabRegistry.getName(CreativeModeTab))).collect(Collectors.joining(", ")));

                        setCreativeModeTabOrder(customOrder);
                        return;
                    }
                } catch (Exception e) {
                    LOGGER.error("Error parsing CreativeModeTab sorting file " + CREATIVE_MODE_TAB_ORDERING_JSON, e);
                }

                recalculateItemCreativeModeTabs();
            }
        };
    }

    private static void recalculateItemCreativeModeTabs() {
        final MutableGraph<CreativeModeTab> graph = GraphBuilder.directed().nodeOrder(ElementOrder.<CreativeModeTab>insertion()).build();

        for (CreativeModeTab tab : BuiltInRegistries.f_279662_) {
            if (!DEFAULT_TABS.contains(tab))
                graph.addNode(tab);
        }

        edges.forEach((key, value) -> {
            final CreativeModeTab keyC = getTab(key);
            final CreativeModeTab valueC = getTab(value);
            if (keyC != null && valueC != null)
                graph.putEdge(keyC, valueC);
        });

        //Note: We can ignore the null warning as we validate getting tabs by key before adding to the graph
        // so the graph should only contain tabs that we know have names in the registry
        List<CreativeModeTab> tierList = TopologicalSort.topologicalSort(graph, Comparator.comparing(tab -> getName(tab)));

        setCreativeModeTabOrder(tierList);
    }

    private static void setCreativeModeTabOrder(List<CreativeModeTab> tierList) {
        runInServerThreadIfPossible(hasServer -> {
            SORTED_TABS.clear();
            SORTED_TABS.addAll(tierList);
        });
    }

    private static void runInServerThreadIfPossible(BooleanConsumer runnable) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null)
            server.execute(() -> runnable.accept(true));
        else
            runnable.accept(false);
    }

    @ApiStatus.Internal
    public static void sortTabs() {
        edges.clear();

        DEFAULT_TABS.add(BuiltInRegistries.f_279662_.m_122713_(CreativeModeTabs.f_256917_));
        DEFAULT_TABS.add(BuiltInRegistries.f_279662_.m_122713_(CreativeModeTabs.f_256750_));
        DEFAULT_TABS.add(BuiltInRegistries.f_279662_.m_122713_(CreativeModeTabs.f_256837_));
        DEFAULT_TABS.add(BuiltInRegistries.f_279662_.m_122713_(CreativeModeTabs.f_257039_));

        final List<? extends Holder<CreativeModeTab>> indexed = BuiltInRegistries.f_279662_
            .m_205916_()
            .filter(c -> !DEFAULT_TABS.contains(c.get()))
            .toList();
        int vanillaTabs = 10;

        for (int i = 0; i < vanillaTabs; i++) { // Vanilla ordering
            final Holder<CreativeModeTab> value = indexed.get(i);
            final CreativeModeTab tab = value.get();
            final ResourceLocation name = value.m_203543_().orElseThrow().m_135782_();

            if (!tab.tabsBefore.isEmpty() || !tab.tabsAfter.isEmpty())
                addTabOrder(tab, name);
            else {
                // If there is no order specified ensure vanilla ordering by specifying the previous and next indexed tab as edges
                if (i != 0)
                    edges.put(indexed.get(i - 1).m_203543_().orElseThrow().m_135782_(), name);
                if (i + 1 < indexed.size())
                    edges.put(name, indexed.get(i + 1).m_203543_().orElseThrow().m_135782_());
            }
        }

        ResourceLocation lastVanilla = indexed.get(vanillaTabs - 1).m_203543_().orElseThrow().m_135782_();
        for (int i = vanillaTabs; i < indexed.size(); i++) {
            final Holder<CreativeModeTab> value = indexed.get(i);
            final CreativeModeTab tab = value.get();
            final ResourceLocation name = value.m_203543_().orElseThrow().m_135782_();

            if (!tab.tabsBefore.isEmpty() || !tab.tabsAfter.isEmpty())
                addTabOrder(tab, name);
            else // if there is no order specified ensure the tab comes after the last vanilla tab
                edges.put(lastVanilla, name);
        }

        recalculateItemCreativeModeTabs();
    }

    private static void addTabOrder(CreativeModeTab tab, ResourceLocation name) {
        for (var after : tab.tabsAfter) {
            edges.put(name, after);
        }

        for (var before : tab.tabsBefore) {
            edges.put(before, name);
        }
    }
}
