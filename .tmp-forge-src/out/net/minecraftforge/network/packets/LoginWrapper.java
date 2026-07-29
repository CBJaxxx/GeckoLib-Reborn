/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;

public class LoginWrapper {
    public static final StreamCodec<FriendlyByteBuf, LoginWrapper> STREAM_CODEC = StreamCodec.m_324771_(LoginWrapper::encode, LoginWrapper::new);
    private final ResourceLocation name;
    private FriendlyByteBuf data;
    private final Channel<Object> channel;
    private final Object packet;

    public <MSG> LoginWrapper(Channel<MSG> channel, MSG packet) {
        this(channel.getName(), null, channel, packet);
    }

    private LoginWrapper(FriendlyByteBuf buf) {
        this(buf.m_130281_(), buf.wrap(buf.readBytes(buf.m_130242_())), null, null);
    }

    @SuppressWarnings("unchecked")
    private LoginWrapper(ResourceLocation name, FriendlyByteBuf data, Channel<?> channel, Object packet) {
        this.name = name;
        this.data = data;
        this.channel = (Channel<Object>)channel;
        this.packet = packet;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.m_130085_(name);

        if (data == null) {
            data = buf.wrap(Unpooled.buffer());
            ((Channel<Object>)channel).encode(data, packet);
        }

        buf.m_130130_(data.readableBytes());
        buf.writeBytes(data.slice());
    }

    public ResourceLocation name() {
        return this.name;
    }

    public FriendlyByteBuf data() {
        return this.data;
    }
}
