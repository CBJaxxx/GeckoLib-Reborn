/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.model;

import java.util.Map;
import java.util.Optional;

import com.mojang.math.Transformation;

import net.minecraft.resources.ResourceLocation;

public record ForgeBlockModelData(
    Optional<Transformation> transform,
    Optional<ResourceLocation> renderType,
    Optional<ResourceLocation> renderTypeFast,
    Optional<Map<String, Boolean>> visibility
) {
    public ForgeBlockModelData() {
        this(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    public ForgeBlockModelData merge(ForgeBlockModelData other) {
        if (other == null)
            return this;

        var vis = this.visibility();
        if (this.visibility().isPresent() && other.visibility().isPresent()) {
            var map = Map.copyOf(this.visibility().get());
            map.putAll(other.visibility().get());
            vis = Optional.of(map);
        }

        return new ForgeBlockModelData(
            this.transform().isPresent()      ? this.transform()      : other.transform(),
            this.renderType().isPresent()     ? this.renderType()     : other.renderType(),
            this.renderTypeFast().isPresent() ? this.renderTypeFast() : other.renderTypeFast(),
            vis.isPresent()                   ? vis                   : other.visibility()
        );
    }

    public boolean full() {
        return transform.isPresent() && renderType.isPresent() && renderTypeFast.isPresent() && visibility.isPresent();
    }
}
