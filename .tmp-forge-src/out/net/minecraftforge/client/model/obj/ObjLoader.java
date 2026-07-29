/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model.obj;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.UnbakedGeometry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.StandaloneGeometryBakingContext;

import java.util.Map;

/**
 * A loader for {@link ObjModel OBJ models}.
 * <p>
 * Allows the user to enable automatic face culling, toggle quad shading, flip UVs, render emissively and specify a
 * {@link ObjMaterialLibrary material library} override.
 */
public class ObjLoader implements IGeometryLoader, ResourceManagerReloadListener {
    public static ObjLoader INSTANCE = new ObjLoader();

    private final Map<ObjModel.ModelSettings, ObjModel> modelCache = Maps.newConcurrentMap();
    private final Map<ResourceLocation, ObjMaterialLibrary> materialCache = Maps.newConcurrentMap();

    private ResourceManager manager = Minecraft.m_91087_().m_91098_();

    @Override
    public void m_6213_(ResourceManager resourceManager) {
        modelCache.clear();
        materialCache.clear();
        manager = resourceManager;
    }

    @Override
    public UnbakedGeometry read(JsonObject json, JsonDeserializationContext deserializationContext) {
        var location = ResourceLocation.m_338530_(GsonHelper.m_13906_(json, "model"));

        boolean automaticCulling = GsonHelper.m_13855_(json, "automatic_culling", true);
        boolean shadeQuads = GsonHelper.m_13855_(json, "shade_quads", true);
        boolean flipV = GsonHelper.m_13855_(json, "flip_v", false);
        boolean emissiveAmbient = GsonHelper.m_13855_(json, "emissive_ambient", true);
        String mtlOverride = GsonHelper.m_13851_(json, "mtl_override", null);

        var settings = new ObjModel.ModelSettings(location, automaticCulling, shadeQuads, flipV, emissiveAmbient, mtlOverride);
        var model = loadModel(settings);

        return new ObjGeometry(model);
    }

    public ObjModel loadModel(ObjModel.ModelSettings settings) {
        return modelCache.computeIfAbsent(settings, (data) -> {
            Resource resource = manager.m_213713_(settings.modelLocation()).orElseThrow();
            try (ObjTokenizer tokenizer = new ObjTokenizer(resource.m_215507_())) {
                return ObjModel.parse(tokenizer, settings);
            } catch (Exception e) {
                throw new RuntimeException("Could not read OBJ model", e);
            }
        });
    }

    public ObjMaterialLibrary loadMaterialLibrary(ResourceLocation materialLocation) {
        return materialCache.computeIfAbsent(materialLocation, (location) -> {
            Resource resource = manager.m_213713_(location).orElseThrow();
            try (ObjTokenizer rdr = new ObjTokenizer(resource.m_215507_())) {
                return new ObjMaterialLibrary(rdr);
            } catch (Exception e) {
                throw new RuntimeException("Could not read OBJ material library", e);
            }
        });
    }

    private static class ObjGeometry implements UnbakedGeometry {
        private final ObjModel model;

        public ObjGeometry(ObjModel model) {
            this.model = model;
        }

        @Override
        public QuadCollection m_384716_(TextureSlots slots, ModelBaker baker, ModelState state, ModelDebugName name) {
            return bake(slots, baker, state, name, StandaloneGeometryBakingContext.INSTANCE);
        }

        @Override
        public QuadCollection bake(TextureSlots slots, ModelBaker baker, ModelState state, ModelDebugName name, IGeometryBakingContext context) {
            var builder = new QuadCollection.Builder();

            for (var part : model.getParts().values()) {
                if (context.isComponentVisible(part.name(), true))
                    part.bake(slots, baker, state, name, context, builder);
            }

            return builder.m_388313_();
        }
    }
}
