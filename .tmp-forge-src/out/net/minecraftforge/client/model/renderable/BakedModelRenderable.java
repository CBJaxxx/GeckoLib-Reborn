/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model.renderable;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * {@linkplain IRenderable Renderable} wrapper for {@linkplain BakedModel baked models}.
 * <p>
 * The context can provide the {@link BlockState}, faces to be rendered, a {@link RandomSource} and seed,
 * a {@link ModelData} instance, and a {@link Vector4f tint}.
 *
 * @see Context
 */
public class BakedModelRenderable implements IRenderable<BakedModelRenderable.Context> {
    /**
     * Constructs a {@link BakedModelRenderable} from the given model location.
     * The model is expected to have been baked ahead of time.
     *
     * @see net.minecraftforge.client.event.ModelEvent.RegisterModelStateDefinitions
     */
    public static BakedModelRenderable of(BlockState state) {
        return of(Minecraft.m_91087_().m_91304_().m_119430_().m_110893_(state));
    }

    /**
     * Constructs a {@link BakedModelRenderable} from the given baked model.
     */
    public static BakedModelRenderable of(BlockStateModel model) {
        return new BakedModelRenderable(model);
    }

    private final BlockStateModel model;

    private BakedModelRenderable(BlockStateModel model) {
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, ITextureRenderTypeLookup textureRenderTypeLookup, int lightmap, int overlay, float partialTick, Context context) {
        @SuppressWarnings("deprecation")
        var buffer = bufferSource.m_6299_(textureRenderTypeLookup.get(TextureAtlas.f_118259_));
        var tint = context.tint();
        var randomSource = context.randomSource();
        randomSource.m_188584_(context.seed());
        // Given the lack of context, the requested render type has to be null to ensure the model renders all of its geometry
        var parts = new ArrayList<BlockModelPart>();
        model.collectParts(randomSource, parts, context.data(), null);
        for (var part : parts) {
            for (var direction : Direction.valuesView()) {
                for (var quad : part.m_385823_(direction)) {
                    buffer.putBulkData(poseStack.m_85850_(), quad, tint.x(), tint.y(), tint.z(), tint.w(), lightmap, overlay, true);
                }
            }
        }
    }

    public IRenderable<Unit> withContext(ModelData modelData) {
        return withContext(new Context(modelData));
    }

    public IRenderable<ModelData> withModelDataContext() {
        return (poseStack, bufferSource, textureRenderTypeLookup, lightmap, overlay, partialTick, context) ->
                render(poseStack, bufferSource, textureRenderTypeLookup, lightmap, overlay, partialTick, new Context(context));
    }

    public record Context(@Nullable BlockState state, Direction[] faces, RandomSource randomSource, long seed, ModelData data, Vector4f tint) {
        private static final Direction[] ALL_FACES_AND_NULL = Arrays.copyOf(Direction.values(), Direction.values().length + 1);
        private static final Vector4f WHITE = new Vector4f(1, 1, 1, 1);

        public Context(ModelData data) {
            this(null, ALL_FACES_AND_NULL, RandomSource.m_216327_(), 42, data, WHITE);
        }
    }
}
