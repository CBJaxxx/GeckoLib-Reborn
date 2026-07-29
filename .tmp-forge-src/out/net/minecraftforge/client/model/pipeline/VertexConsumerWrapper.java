/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model.pipeline;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;

/**
 * Wrapper for {@link VertexConsumer} which delegates all operations to its parent.
 * <p>
 * Useful for defining custom pipeline elements that only process certain data.
 */
public abstract class VertexConsumerWrapper implements VertexConsumer
{
    protected final VertexConsumer parent;

    public VertexConsumerWrapper(VertexConsumer parent)
    {
        this.parent = parent;
    }

    @Override
    public VertexConsumer m_167146_(float x, float y, float z)
    {
        parent.m_167146_(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer m_167129_(int r, int g, int b, int a)
    {
        parent.m_167129_(r, g, b, a);
        return this;
    }

    @Override
    public VertexConsumer m_167083_(float u, float v)
    {
        parent.m_167083_(u, v);
        return this;
    }

    @Override
    public VertexConsumer m_338369_(int u, int v)
    {
        parent.m_338369_(u, v);
        return this;
    }

    @Override
    public VertexConsumer m_338813_(int u, int v)
    {
        parent.m_338813_(u, v);
        return this;
    }

    @Override
    public VertexConsumer m_338525_(float x, float y, float z)
    {
        parent.m_338525_(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer misc(VertexFormatElement element, int... values)
    {
        parent.misc(element, values);
        return this;
    }
}
