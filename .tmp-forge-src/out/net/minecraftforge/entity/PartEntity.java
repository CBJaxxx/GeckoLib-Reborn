/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.entity;

import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public abstract class PartEntity<T extends Entity> extends Entity {
    private final T parent;

    public PartEntity(T parent) {
        super(parent.m_6095_(), parent.m_9236_());
        this.parent = parent;
    }

    public T getParent() {
        return parent;
    }

    @Override
    public Packet<ClientGamePacketListener> m_5654_(ServerEntity p_344981_) {
        throw new UnsupportedOperationException();
    }
}
