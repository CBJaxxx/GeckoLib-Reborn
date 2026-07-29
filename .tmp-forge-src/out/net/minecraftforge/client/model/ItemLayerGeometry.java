/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.SimpleUnbakedGeometry;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.UnbakedGeometry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

/**
 * Forge version of vanilla's {@link ItemModelGenerator}, i.e. builtin/generated models with some tweaks:
 * - Represented as {@link UnbakedGeometry} so it can be used in multiple root models
 * - Not limited to an arbitrary number of layers (5)
 //* - Support for per-layer render types - Currently Doesn't work. TODO: [Forge][Model] Make a Model loader for this
 */
@SuppressWarnings("unused")
public class ItemLayerGeometry implements UnbakedGeometry {
    private static final ItemLayerGeometry INSTANCE = new ItemLayerGeometry(Int2ObjectMaps.emptyMap(), Int2ObjectMaps.emptyMap());

    private final Int2ObjectMap<ResourceLocation> renderTypeNames;
    private final Int2ObjectMap<ResourceLocation> renderTypeFastNames;

    private ItemLayerGeometry(Int2ObjectMap<ResourceLocation> renderTypeNames, Int2ObjectMap<ResourceLocation> renderTypeFastNames) {
        this.renderTypeNames = renderTypeNames;
        this.renderTypeFastNames = renderTypeFastNames;
    }

    @Override
    public QuadCollection m_384716_(TextureSlots textures, ModelBaker baker, ModelState state, ModelDebugName name) {
        List<BlockElement> elements = new ArrayList<>();

        int i = 0;
        while (true) {
            var layer = "layer" + i++;
            var material = textures.m_372672_(layer);
            if (material == null)
                break;
            var contents = baker.m_372755_().m_372108_(material, name).m_245424_();
            elements.addAll(ItemModelGenerator.m_111638_(i, layer, contents));
        }

        return SimpleUnbakedGeometry.m_387897_(elements, textures, baker.m_372755_(), state, name);
        /*
        Material particleMaterial = spriteGetter.getMaterial("particle");
        var particle = baker.sprites().get(particleMaterial  == null ? textures.get(0) : particleMaterial, name);
        var rootTransform = context.getRootTransform();
        if (!rootTransform.isIdentity())
            modelState = UnbakedGeometryHelper.composeRootTransformIntoModelState(modelState, rootTransform);

        var normalRenderTypes = new RenderTypeGroup(RenderType.translucent(), ForgeRenderTypes.ITEM_UNSORTED_TRANSLUCENT.get());
        var builder = CompositeModel.Baked.builder(context, particle, context.getTransforms());
        for (int i = 0; i < textures.size(); i++) {
            var sprite = baker.sprites().get(textures.get(i), name);
            var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(i, sprite.contents());
            var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, modelState);
            var renderTypeName = renderTypeNames.get(i);
            var renderTypes = renderTypeName != null ? context.getRenderType(renderTypeName) : null;
            var renderTypeFastName = renderTypeFastNames.get(i);
            var renderTypesFast = renderTypeFastName != null ? context.getRenderType(renderTypeFastName) : null;
            builder.addQuads(renderTypes != null ? renderTypes : normalRenderTypes, renderTypesFast != null ? renderTypesFast : RenderTypeGroup.EMPTY, quads);
        }

        return builder.build();
        */
    }

    public static final class Loader implements IGeometryLoader {
        public static final Loader INSTANCE = new Loader();

        @Override
        public UnbakedGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
            return ItemLayerGeometry.INSTANCE;
            /*
            var renderTypeNames = readRenderTypeNames(jsonObject, "render_types");
            var renderTypeFastNames = readRenderTypeNames(jsonObject, "render_types_fast");
            return new ItemLayerModel(renderTypeNames, renderTypeFastNames);
            */
        }

        private static Int2ObjectMap<ResourceLocation> readRenderTypeNames(JsonObject jsonObject, String key) {
            var renderTypeNames = new Int2ObjectOpenHashMap<ResourceLocation>();
            if (jsonObject.has(key)) {
                var renderTypes = jsonObject.getAsJsonObject(key);
                for (var entry : renderTypes.entrySet()) {
                    var renderType = ResourceLocation.m_338530_(entry.getKey());
                    for (var layer : entry.getValue().getAsJsonArray())
                        if (renderTypeNames.put(layer.getAsInt(), renderType) != null)
                            throw new JsonParseException("Registered duplicate " + key + " for layer " + layer);
                }
            }

            return renderTypeNames;
        }
    }
}
