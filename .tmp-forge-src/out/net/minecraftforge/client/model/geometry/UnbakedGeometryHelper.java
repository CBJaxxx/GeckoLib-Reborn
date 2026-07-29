/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model.geometry;

import com.mojang.math.Quadrant;
import com.mojang.math.Transformation;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.SimpleModelState;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.BitSet;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper for dealing with unbaked models and geometries.
 */
@ApiStatus.Internal
public class UnbakedGeometryHelper {
    /**
     * Explanation:
     * This takes anything that looks like a valid resourcepack texture location, and tries to extract a resourcelocation out of it.
     *  1. it will ignore anything up to and including an /assets/ folder,
     *  2. it will take the next path component as a namespace,
     *  3. it will match but skip the /textures/ part of the path,
     *  4. it will take the rest of the path up to but excluding the .png extension as the resource path
     * It's a best-effort situation, to allow model files exported by modelling software to be used without post-processing.
     * Example:
     *   C:\Something\Or Other\src\main\resources\assets\mymodid\textures\item\my_thing.png
     *   ........................................--------_______----------_____________----
     *                                                 <namespace>        <path>
     * Result after replacing '\' to '/': mymodid:item/my_thing
     */
    private static final Pattern FILESYSTEM_PATH_TO_RESLOC =
            Pattern.compile("(?:.*[\\\\/]assets[\\\\/](?<namespace>[a-z_-]+)[\\\\/]textures[\\\\/])?(?<path>[a-z_\\\\/-]+)\\.png");

    private static final Material MISSING_MATERIAL = getMaterial(MissingTextureAtlasSprite.m_118071_().toString());

    /**
     * Resolves a material that may have been defined with a filesystem path instead of a proper {@link ResourceLocation}.
     * <p>
     * The target atlas will always be {@link TextureAtlas#LOCATION_BLOCKS}.
     */
    public static Material resolveDirtyMaterial(@Nullable String tex, TextureSlots textures) {
        if (tex == null)
            return MISSING_MATERIAL;

        if (tex.startsWith("#"))
            return textures.m_372672_(tex);

        // Attempt to convert a common (windows/linux/mac) filesystem path to a ResourceLocation.
        // This makes no promises, if it doesn't work, too bad, fix your mtl file.
        Matcher match = FILESYSTEM_PATH_TO_RESLOC.matcher(tex);
        if (match.matches()) {
            String namespace = match.group("namespace");
            String path = match.group("path").replace("\\", "/");
            tex = namespace != null ? namespace + ":" + path : path;
        }

        return getMaterial(tex);
    }

    /**
     * Creates a list of {@linkplain BlockElement block elements} in the shape of the specified sprite contents.
     * These can later be baked using the same, or another texture.
     * <p>
     * The {@link Direction#NORTH} and {@link Direction#SOUTH} faces take up the whole surface.
     */
    public static List<BlockElement> createUnbakedItemElements(int layerIndex, SpriteContents spriteContents) {
        return ItemModelGenerator.m_111638_(layerIndex, "layer" + layerIndex, spriteContents);
    }

    /**
     * Creates a list of {@linkplain BlockElement block elements} in the shape of the specified sprite contents.
     * These can later be baked using the same, or another texture.
     * <p>
     * The {@link Direction#NORTH} and {@link Direction#SOUTH} faces take up only the pixels the texture uses.
     */
    public static List<BlockElement> createUnbakedItemMaskElements(int layerIndex, SpriteContents spriteContents) {
        var elements = createUnbakedItemElements(layerIndex, spriteContents);
        elements.remove(0); // Remove north and south faces

        int width = spriteContents.m_246492_(), height = spriteContents.m_245330_();
        var bits = new BitSet(width * height);

        // For every frame in the texture, mark all the opaque pixels (this is what vanilla does too)
        spriteContents.m_245638_().forEach(frame -> {
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                    if (!spriteContents.m_245970_(frame, x, y))
                        bits.set(x + y * width);
        });

        // Scan in search of opaque pixels
        for (int y = 0; y < height; y++) {
            int xStart = -1;
            for (int x = 0; x < width; x++) {
                var opaque = bits.get(x + y * width);
                if (opaque == (xStart == -1)) { // (opaque && -1) || (!opaque && !-1)
                    if (xStart == -1) {
                        // We have found the start of a new segment, continue
                        xStart = x;
                        continue;
                    }

                    // The segment is over, expand down as far as possible
                    int yEnd = y + 1;
                    expand:
                    for (; yEnd < height; yEnd++)
                        for (int x2 = xStart; x2 <= x; x2++)
                            if (!bits.get(x2 + yEnd * width))
                                break expand;

                    // Mark all pixels in the area as visited
                    for (int i = xStart; i < x; i++)
                        for (int j = y; j < yEnd; j++)
                            bits.clear(i + j * width);

                    // Create element
                    elements.add(new BlockElement(
                        new Vector3f(16 * xStart / (float) width, 16 - 16 * yEnd / (float) height, 7.5F),
                        new Vector3f(16 * x / (float) width, 16 - 16 * y / (float) height, 8.5F),
                        Util.m_356390_(Direction.class, dir -> new BlockElementFace(dir, layerIndex, "layer" + layerIndex, new BlockElementFace.UVs(0F, 0F, 16F, 16F), Quadrant.R0))
                    ));

                    // Reset xStart
                    xStart = -1;
                }
            }
        }
        return elements;
    }

    /**
     * Bakes a list of {@linkplain BlockElement block elements} and returns a {@link QuadCollection}.
     */
    public static QuadCollection bakeElements(List<BlockElement> elements, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
        return bakeElements(elements, spriteGetter, modelState, QuadTransformers.empty());
    }

    /**
     * Bakes a list of {@linkplain BlockElement block elements} and returns a {@link QuadCollection}.
     */
    public static QuadCollection bakeElements(List<BlockElement> elements, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, IQuadTransformer transformer) {
        var builder = new QuadCollection.Builder();
        bakeElements(elements, spriteGetter, modelState, builder);
        return builder.m_388313_();
    }

    /**
     * Adds a list of {@linkplain BlockElement block elements} int a {@link QuadCollection.Builder}.
     */
    public static void bakeElements(List<BlockElement> elements, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, QuadCollection.Builder builder) {
        bakeElements(elements, spriteGetter, modelState, QuadTransformers.empty(), builder);
    }

    /**
     * Adds a list of {@linkplain BlockElement block elements} int a {@link QuadCollection.Builder}.
     */
    public static void bakeElements(List<BlockElement> elements, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, IQuadTransformer transformer, QuadCollection.Builder builder) {
        for (var element : elements) {
            element.f_111310_().forEach((side, face) -> {
                var sprite = spriteGetter.apply(getMaterial(face.f_111356_()));
                var quad = bakeElementFace(element, face, sprite, side, modelState, element.f_346257_());
                if (face.f_111354_() == null)
                    builder.m_388522_(quad);
                else
                    builder.m_387449_(Direction.m_252919_(modelState.m_6189_().m_252783_(), face.f_111354_()), quad);
            });
        }
    }

    @SuppressWarnings("deprecation")
    private static Material getMaterial(String texture) {
        return new Material(TextureAtlas.f_118259_, ResourceLocation.m_338530_(texture));
    }

    /**
     * Turns a single {@link BlockElementFace} into a {@link BakedQuad}.
     */
    public static BakedQuad bakeElementFace(BlockElement element, BlockElementFace face, TextureAtlasSprite sprite, Direction direction, ModelState state, int lightEmission) {
        return FaceBakery.m_111600_(element.f_111308_(), element.f_111309_(), face, sprite, direction, state, element.f_111311_(), element.f_111312_(), lightEmission);
    }

    /**
     * Create an {@link IQuadTransformer} to apply a {@link Transformation} that undoes the {@link ModelState}
     * transform (blockstate transform), applies the given root transform and then re-applies the
     * blockstate transform.
     *
     * @return an {@code IQuadTransformer} that applies the root transform to a baked quad that already has the
     * transformation of the given {@code ModelState} applied to it
     */
    public static IQuadTransformer applyRootTransform(ModelState modelState, Transformation rootTransform) {
        // Move the origin of the ModelState transform and its inverse from the negative corner to the block center
        // to replicate the way the ModelState transform is applied in the FaceBakery by moving the vertices such that
        // the negative corner acts as the block center
        Transformation transform = modelState.m_6189_().applyOrigin(new Vector3f(.5F, .5F, .5F));
        return QuadTransformers.applying(transform.m_121096_(rootTransform).m_121096_(transform.m_121103_()));
    }

    /**
     * {@return a {@link ModelState} that combines the existing model state and the {@linkplain Transformation root transform}}
     */
    public static ModelState composeRootTransformIntoModelState(ModelState modelState, Transformation rootTransform) {
        // Move the origin of the root transform as if the negative corner were the block center to match the way the
        // ModelState transform is applied in the FaceBakery by moving the vertices to be centered on that corner
        rootTransform = rootTransform.applyOrigin(new Vector3f(-.5F, -.5F, -.5F));
        return new SimpleModelState(modelState.m_6189_().m_121096_(rootTransform));
    }
}
