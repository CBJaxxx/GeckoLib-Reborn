/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;

/**
 * Transformer for {@link BakedQuad baked quads}.
 *
 * @see QuadTransformers
 */
@FunctionalInterface
public interface IQuadTransformer {
    int STRIDE = DefaultVertexFormat.f_85811_.getVertexSize() / 4;
    int POSITION = findOffset(VertexFormatElement.POSITION);
    int COLOR = findOffset(VertexFormatElement.COLOR);
    int UV0 = findOffset(VertexFormatElement.UV0);
    int UV1 = findOffset(VertexFormatElement.UV1);
    int UV2 = findOffset(VertexFormatElement.UV2);
    int NORMAL = findOffset(VertexFormatElement.NORMAL);

    BakedQuad process(BakedQuad quad);

    default List<BakedQuad> process(List<BakedQuad> inputs) {
        return inputs.stream().map(this::process).toList();
    }

    default IQuadTransformer andThen(IQuadTransformer other) {
        return quad -> other.process(process(quad));
    }

    private static int findOffset(VertexFormatElement element) {
        // Divide by 4 because we want the int offset
        var index = DefaultVertexFormat.f_85811_.getOffset(element);
        return index < 0 ? -1 : index / 4;
    }
}
