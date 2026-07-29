/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model.geometry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.mojang.math.Transformation;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelDiscovery.ModelWrapper;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.ForgeBlockModelData;

@ApiStatus.Internal
public record ModelContext(
    ResolvedModel self,
    ResolvedModel parent,
    @Nullable ForgeBlockModelData data,
    boolean gui3d
) implements IGeometryBakingContext {
    public ModelContext(ResolvedModel self) {
        this(self, self.m_385886_(), makeData(self, self.m_385886_()), true);
    }

    private static ForgeBlockModelData makeData(ResolvedModel self, ResolvedModel parent) {
        var data = self.m_384361_() instanceof BlockModel block ? block.forgeData() : null;

        var p = parent;
        while (p != null && (data == null || !data.full())) {
            if (p.m_384361_() instanceof BlockModel block) {
                if (data == null)
                    data = block.forgeData();
                else
                    data = data.merge(block.forgeData());
            }
            p = (ModelWrapper)p.m_385886_();
        }

        return data;
    }

    @Override
    public boolean isGui3d() {
        return gui3d;
    }

    public ModelContext withGui3d(boolean value) {
        return new ModelContext(self, parent, data, value);
    }

    @Override
    public boolean useBlockLight() {
        var value = self.m_384361_().m_111479_();
        return value != null && value.m_374382_();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return Boolean.TRUE.equals(self.m_384361_().m_385192_());
    }

    @Override
    public ItemTransforms getTransforms() {
        return self.m_384361_().m_111491_();
    }

    @Override
    public Transformation getRootTransform() {
        return data == null ? Transformation.m_121093_() : data.transform().orElse(Transformation.m_121093_());
    }

    @Override
    public @Nullable ResourceLocation getRenderTypeHint() {
        return data == null ? null : data.renderType().orElse(null);
    }

    @Override
    public boolean isComponentVisible(String component, boolean fallback) {
        if (data == null || data.visibility().isEmpty())
            return fallback;
        return data.visibility().get().getOrDefault(component, fallback);
    }
}
