/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model.geometry;

import java.util.Set;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.UnbakedModel;

/**
 * General interface for any model that can be baked, superset of vanilla {@link UnbakedModel}.
 * <p>
 * Instances of this class are usually created via {@link IGeometryLoader}.
 *
 * @see IGeometryLoader
 * @see IGeometryBakingContext
 */
public interface IUnbakedGeometry<T extends IUnbakedGeometry<T>> {
    BlockModelPart bake(IGeometryBakingContext context, ModelBaker baker, TextureSlots spriteGetter, ModelState modelState);

    /**
     * Resolve parents of nested {@link BlockModel}s which are later used in
     * {@link IUnbakedGeometry#bake(IGeometryBakingContext, ModelBaker, TextureSlots, ModelState)}
     * via {@link BlockModel#resolveParents(Function)}
     */
    default void resolveDependencies(ResolvableModel.Resolver resolver, IGeometryBakingContext context) { }

    /**
     * {@return a set of all the components whose visibility may be configured via {@link IGeometryBakingContext}}
     */
    default Set<String> getConfigurableComponentNames() {
        return Set.of();
    }
}
