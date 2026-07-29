/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.network.packets;

import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DataPackRegistriesHooks;
import net.minecraftforge.registries.RegistryManager;

public record RegistryList(
    int token,
    List<ResourceLocation> normal,
    List<ResourceKey<? extends Registry<?>>> datapacks) {

    public static final StreamCodec<FriendlyByteBuf, RegistryList> STREAM_CODEC = StreamCodec.m_324771_(RegistryList::encode, RegistryList::decode);

    public RegistryList(int token) {
        this(token, RegistryManager.getRegistryNamesForSyncToClient(), List.copyOf(DataPackRegistriesHooks.getSyncedCustomRegistries()));
    }

    public static RegistryList decode(FriendlyByteBuf buf) {
        var token = buf.m_130242_();
        var normal = buf.m_236845_(FriendlyByteBuf::m_130281_);
        List<ResourceKey<? extends Registry<?>>> datapacks = buf.m_236845_(b -> ResourceKey.m_135788_(buf.m_130281_()));
        return new RegistryList(token, normal, datapacks);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.m_130130_(token());
        buf.m_236828_(normal(), FriendlyByteBuf::m_130085_);
        buf.m_236828_(datapacks(), FriendlyByteBuf::m_236858_);
    }
}