/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.network.packets;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;

public record ChannelVersions(Map<ResourceLocation, @NotNull Integer> channels) {
    public static StreamCodec<FriendlyByteBuf, ChannelVersions> STREAM_CODEC = StreamCodec.m_324771_(ChannelVersions::encode, ChannelVersions::decode);

    public ChannelVersions() {
        this(NetworkRegistry.buildChannelVersions());
    }

    public static ChannelVersions decode(FriendlyByteBuf buf) {
        return new ChannelVersions(buf.m_236841_(Object2IntOpenHashMap::new, FriendlyByteBuf::m_130281_, FriendlyByteBuf::m_130242_));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.m_236831_(channels, FriendlyByteBuf::m_130085_, FriendlyByteBuf::m_130130_);
    }
}