/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.UnbakedGeometry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.StandaloneGeometryBakingContext;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * A dynamic fluid container model, capable of re-texturing itself at runtime to match the contained fluid.
 * <p>
 * Composed of a base layer, a fluid layer (applied with a mask) and a cover layer (optionally applied with a mask).
 * The entire model may optionally be flipped if the fluid is gaseous, and the fluid layer may glow if light-emitting.
 * <p>
 * Fluid tinting requires registering a separate {@link ItemColor}. An implementation is provided in {@link Colors}.
 *
 * @see Colors
 */
public class DynamicFluidContainerModel implements UnbakedGeometry {
    // Depth offsets to prevent Z-fighting
    private static final Transformation FLUID_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.002f), new Quaternionf());
    private static final Transformation COVER_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.004f), new Quaternionf());

    private final Fluid fluid;
    private final boolean flipGas;
    private final boolean coverIsMask;
    private final boolean applyFluidLuminosity;

    private DynamicFluidContainerModel(Fluid fluid, boolean flipGas, boolean coverIsMask, boolean applyFluidLuminosity) {
        this.fluid = fluid;
        this.flipGas = flipGas;
        this.coverIsMask = coverIsMask;
        this.applyFluidLuminosity = applyFluidLuminosity;
    }

    public static RenderTypeGroup getLayerRenderTypes(boolean unlit) {
        return new RenderTypeGroup(ChunkSectionLayer.TRANSLUCENT, unlit ? ForgeRenderTypes.ITEM_UNSORTED_UNLIT_TRANSLUCENT.get() : ForgeRenderTypes.ITEM_UNSORTED_TRANSLUCENT.get());
    }

    @SuppressWarnings("deprecation")
    private static Material getMaterial(ResourceLocation texture) {
        return new Material(TextureAtlas.f_118259_, texture);
    }

    /**
     * Returns a new ModelDynBucket representing the given fluid, but with the same
     * other properties (flipGas, tint, coverIsMask).
     */
    public DynamicFluidContainerModel withFluid(Fluid newFluid) {
        return new DynamicFluidContainerModel(newFluid, flipGas, coverIsMask, applyFluidLuminosity);
    }

    @Override
    public QuadCollection m_384716_(TextureSlots slots, ModelBaker baker, ModelState state, ModelDebugName name) {
        return bake(slots, baker, state, name, StandaloneGeometryBakingContext.INSTANCE);
    }

    @Override
    public QuadCollection bake(TextureSlots textures, ModelBaker baker, ModelState state, ModelDebugName name, IGeometryBakingContext context) {
        Material fluidMaskLocation = textures.m_372672_("fluid");
        Material stillMaterial = null;

        if (fluid != Fluids.f_76191_) {
            var stillTexture = IClientFluidTypeExtensions.of(fluid).getStillTexture();
            stillMaterial = getMaterial(stillTexture);
        }

        var sprites = baker.m_372755_();
        var baseSprite = sprites.m_387824_(textures, "base", name);
        var fluidSprite = stillMaterial == null ? null : sprites.m_372108_(stillMaterial, name);
        var coverSprite = sprites.m_387824_(textures, "cover", name);
        /*
         var particleSprite = sprites.resolveSlot(textures, "particle", name);

        if (particleSprite == null) particleSprite = fluidSprite;
        if (particleSprite == null) particleSprite = baseSprite;
        if (particleSprite == null && !coverIsMask) particleSprite = coverSprite;
         */

        var transformation = state.m_6189_();

        // TODO: [Forge][Rendering] See if we can get rid of SimpleModelState and wrap transforms completely
        // If the fluid is lighter than air, rotate 180deg to turn it upside down
        if (flipGas && fluid != Fluids.f_76191_ && fluid.getFluidType().isLighterThanAir())
            transformation = transformation.m_121096_(new Transformation(null, new Quaternionf(0, 0, 1, 0), null, null));

        var buf = new QuadCollection.Builder();

        if (baseSprite != null) {
            // Base texture
            var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, baseSprite.m_245424_());
            UnbakedGeometryHelper.bakeElements(unbaked, $ -> baseSprite, state, buf);
        }

        if (fluidMaskLocation != null && fluidSprite != null) {
            TextureAtlasSprite templateSprite = sprites.m_372108_(fluidMaskLocation, name);
            if (templateSprite != null) {
                // Fluid layer
                var transformedState = new SimpleModelState(transformation.m_121096_(FLUID_TRANSFORM));
                var unbaked = UnbakedGeometryHelper.createUnbakedItemMaskElements(1, templateSprite.m_245424_()); // Use template as mask

                var emissive = applyFluidLuminosity && fluid.getFluidType().getLightLevel() > 0;
                var transformer = emissive ? QuadTransformers.settingMaxEmissivity() : QuadTransformers.empty();

                UnbakedGeometryHelper.bakeElements(unbaked, $ -> fluidSprite, transformedState, transformer, buf); // Bake with fluid texture
            }
        }

        if (coverSprite != null) {
            var sprite = coverIsMask ? baseSprite : coverSprite;
            if (sprite != null) {
                // Cover/overlay
                var transformedState = new SimpleModelState(transformation.m_121096_(COVER_TRANSFORM));
                var unbaked = UnbakedGeometryHelper.createUnbakedItemMaskElements(2, coverSprite.m_245424_()); // Use cover as mask
                UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, transformedState, buf); // Bake with selected texture
            }
        }


        return buf.m_388313_();
    }

    public static final class Loader implements IGeometryLoader {
        public static final Loader INSTANCE = new Loader();

        private Loader() { }

        @Override
        public UnbakedGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
            var fluidName = ResourceLocation.m_338530_(GsonHelper.m_13906_(jsonObject, "fluid"));

            Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);

            boolean flip = GsonHelper.m_13855_(jsonObject, "flip_gas", false);
            boolean coverIsMask = GsonHelper.m_13855_(jsonObject, "cover_is_mask", true);
            boolean applyFluidLuminosity = GsonHelper.m_13855_(jsonObject, "apply_fluid_luminosity", true);

            // create new model with correct liquid
            return new DynamicFluidContainerModel(fluid, flip, coverIsMask, applyFluidLuminosity);
        }
    }
}
