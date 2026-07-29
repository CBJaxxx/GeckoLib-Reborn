/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import org.joml.Vector3f;

/**
 * Extension interface for {@link com.mojang.blaze3d.vertex.PoseStack}.
 */
public interface IForgePoseStack {
    private PoseStack self() {
        return (PoseStack)this;
    }

    /**
     * Pushes and applies the {@code transformation} to this pose stack. <br>
     * The effects of this method can be reversed by a corresponding {@link PoseStack#popPose()} call.
     *
     * @param transformation the transformation to push
     */
    default void pushTransformation(Transformation transformation) {
        final PoseStack self = self();
        self.m_85836_();

        Vector3f trans = transformation.m_252829_();
        self.m_252880_(trans.x(), trans.y(), trans.z());

        self.m_318714_(transformation.m_253244_());

        Vector3f scale = transformation.m_252900_();
        self.m_85841_(scale.x(), scale.y(), scale.z());

        self.m_318714_(transformation.m_252848_());
    }
}
