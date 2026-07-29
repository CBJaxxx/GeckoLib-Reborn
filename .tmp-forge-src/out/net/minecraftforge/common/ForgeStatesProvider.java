/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common;

import java.util.List;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.IModLoadingState;
import net.minecraftforge.fml.IModStateProvider;
import net.minecraftforge.fml.ModLoadingPhase;
import net.minecraftforge.fml.ModLoadingState;
import net.minecraftforge.fml.core.ModStateProvider;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.ObjectHolderRegistry;
import net.minecraftforge.registries.RegistryManager;

public class ForgeStatesProvider implements IModStateProvider {
    public static final ModLoadingState CREATE_REGISTRIES = gather("CREATE_REGISTRIES", ModStateProvider.CONSTRUCT, RegistryManager::postNewRegistryEvent);
    public static final ModLoadingState OBJECT_HOLDERS = gather("OBJECT_HOLDERS", CREATE_REGISTRIES, ObjectHolderRegistry::findObjectHolders);
    public static final ModLoadingState INJECT_CAPABILITIES = gather("INJECT_CAPABILITIES", OBJECT_HOLDERS).withInline(CapabilityManager::injectCapabilities);
    public static final ModLoadingState UNFREEZE_DATA = gather("UNFREEZE_DATA", INJECT_CAPABILITIES, GameData::unfreezeData);
    public static final ModLoadingState LOAD_REGISTRIES = gather("LOAD_REGISTRIES", UNFREEZE_DATA, GameData::postRegisterEvents);
    public static final ModLoadingState FREEZE_DATA = complete("FREEZE_DATA", ModStateProvider.COMPLETE, GameData::freezeData);
    public static final ModLoadingState NETWORK_LOCK = complete("NETWORK_LOCK", FREEZE_DATA, NetworkRegistry::lock);

    private static ModLoadingState.Builder gather(String name, ModLoadingState after) {
        return ModLoadingState.of(name, ModLoadingPhase.GATHER).after(after);
    }

    private static ModLoadingState gather(String name, ModLoadingState after, Runnable inline) {
        return gather(name, after).withInline(ml -> inline.run());
    }

    private static ModLoadingState.Builder complete(String name, ModLoadingState after) {
        return ModLoadingState.of(name, ModLoadingPhase.COMPLETE).after(after);
    }

    private static ModLoadingState complete(String name, ModLoadingState after, Runnable inline) {
        return complete(name, after).withInline(ml -> inline.run());
    }

    @Override
    public List<IModLoadingState> getAllStates() {
        return List.of(CREATE_REGISTRIES, OBJECT_HOLDERS, INJECT_CAPABILITIES, UNFREEZE_DATA, LOAD_REGISTRIES, FREEZE_DATA, NETWORK_LOCK);
    }
}
