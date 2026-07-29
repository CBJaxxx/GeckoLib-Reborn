/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry.Snapshot;

public record RegistryData(int token, ResourceLocation name, Snapshot data) {
    public static final StreamCodec<FriendlyByteBuf, RegistryData> STREAM_CODEC = StreamCodec.m_324771_(RegistryData::encode, RegistryData::decode);

    public void encode(FriendlyByteBuf buf) {
        buf.m_130130_(token);
        buf.m_130085_(name);
        buf.writeBytes(data.getPacketData());
    }

    public static RegistryData decode(FriendlyByteBuf buf) {
        return new RegistryData(buf.m_130242_(), buf.m_130281_(), Snapshot.read(buf));
    }
}