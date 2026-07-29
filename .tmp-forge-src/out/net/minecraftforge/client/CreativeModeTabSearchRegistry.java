/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.CreativeModeTabRegistry;
import org.jetbrains.annotations.Nullable;

public class CreativeModeTabSearchRegistry {
    private static final Map<CreativeModeTab, SessionSearchTrees.Key> NAME_SEARCH_KEYS = new IdentityHashMap<>();
    private static final Map<CreativeModeTab, SessionSearchTrees.Key> TAG_SEARCH_KEYS = new IdentityHashMap<>();

    public static Map<CreativeModeTab, SessionSearchTrees.Key> getNameSearchKeys() {
        Map<CreativeModeTab, SessionSearchTrees.Key> nameSearchKeys = new IdentityHashMap<>();

        nameSearchKeys.put(CreativeModeTabs.m_258007_(), getNameSearchKey(CreativeModeTabs.m_258007_()));

        for (var tab : CreativeModeTabRegistry.getSortedCreativeModeTabs()) {
            var nameSearchKey = getNameSearchKey(tab);
            if (nameSearchKey != null)
                nameSearchKeys.put(tab, nameSearchKey);
        }

        return nameSearchKeys;
    }

    public static Map<CreativeModeTab, SessionSearchTrees.Key> getTagSearchKeys() {
        Map<CreativeModeTab, SessionSearchTrees.Key> tagSearchKeys = new IdentityHashMap<>();

        tagSearchKeys.put(CreativeModeTabs.m_258007_(), getTagSearchKey(CreativeModeTabs.m_258007_()));

        for (var tab : CreativeModeTabRegistry.getSortedCreativeModeTabs()) {
            var tagSearchKey = getTagSearchKey(tab);
            if (tagSearchKey != null)
                tagSearchKeys.put(tab, tagSearchKey);
        }

        return tagSearchKeys;
    }

    @Nullable
    public static SessionSearchTrees.Key getNameSearchKey(CreativeModeTab tab) {
        if (tab == CreativeModeTabs.m_258007_())
            return SessionSearchTrees.f_337599_;

        if (!tab.hasSearchBar())
            return null;

        return NAME_SEARCH_KEYS.computeIfAbsent(tab, k -> new SessionSearchTrees.Key());
    }

    @Nullable
    public static SessionSearchTrees.Key getTagSearchKey(CreativeModeTab tab) {
        if (tab == CreativeModeTabs.m_258007_())
            return SessionSearchTrees.f_336706_;

        if (!tab.hasSearchBar())
            return null;

        return TAG_SEARCH_KEYS.computeIfAbsent(tab, k -> new SessionSearchTrees.Key());
    }
}
