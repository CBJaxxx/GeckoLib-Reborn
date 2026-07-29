/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.network.packets;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkContext.NetworkMismatchData;
import net.minecraftforge.network.NetworkContext.NetworkMismatchData.Version;

/**
 * Notifies the client of a channel mismatch on the server, so a {@link net.minecraftforge.client.gui.ModMismatchDisconnectedScreen} is used to notify the user of the disconnection.
 * This packet also sends the data of a channel mismatch (currently, the ids and versions of the mismatched channels) to the client for it to display the correct information in said screen.
 */
public record MismatchData(
    Map<ResourceLocation, Version> mismatched,
    Set<ResourceLocation> missing
) {
    public static final StreamCodec<FriendlyByteBuf, MismatchData> STREAM_CODEC = StreamCodec.m_324771_(MismatchData::encode, MismatchData::decode);
    private static final int MAX_LENGTH = 0x100;

    public MismatchData(NetworkMismatchData data) {
        this(data.mismatched(), data.missing());
    }

    public static MismatchData decode(FriendlyByteBuf buf) {
        var mismatched = buf.m_236847_(
            i -> ResourceLocation.m_338530_(i.m_130136_(MAX_LENGTH)),
            i -> new Version(
                i.m_130136_(MAX_LENGTH),
                i.m_130136_(MAX_LENGTH)
            )
        );
        var missing = buf.m_236838_(HashSet::new,
            i -> ResourceLocation.m_338530_(i.m_130136_(MAX_LENGTH))
        );
        return new MismatchData(mismatched, missing);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.m_236831_(mismatched,
            (o, k) -> o.m_130072_(k.toString(), MAX_LENGTH),
            (o, v) -> {
                o.m_130072_(v.received(), MAX_LENGTH);
                o.m_130072_(v.had(), MAX_LENGTH);
            }
        );
        buf.m_236828_(missing,
            (o, k) -> o.m_130072_(k.toString(), MAX_LENGTH)
        );
    }
}