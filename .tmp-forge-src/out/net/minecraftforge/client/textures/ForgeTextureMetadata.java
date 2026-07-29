/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.textures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.resources.Resource;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * The "forge" section of texture metadata files (.mcmeta). Currently used only to specify custom
 * TextureAtlasSprite loaders.
 *
 * @see ITextureAtlasSpriteLoader
 */
public record ForgeTextureMetadata(@Nullable ITextureAtlasSpriteLoader loader) {
    public static final ForgeTextureMetadata EMPTY = new ForgeTextureMetadata(null);

    private static final Codec<ITextureAtlasSpriteLoader> LOADER_CODEC = Codec.<ITextureAtlasSpriteLoader>stringResolver(
        loader -> {
            var ret = TextureAtlasSpriteLoaderManager.getKey(loader);
            return ret == null ? null : ret.toString();
        },
        name -> TextureAtlasSpriteLoaderManager.get(ResourceLocation.m_338530_(name))
    );

    private static final Codec<ForgeTextureMetadata> CODEC = RecordCodecBuilder.create(i ->
        i.group(
            LOADER_CODEC.fieldOf("loader").forGetter(ForgeTextureMetadata::loader)
        ).apply(i, ForgeTextureMetadata::new)
    );


    public static final MetadataSectionType<ForgeTextureMetadata> TYPE = new MetadataSectionType<>("forge", CODEC);

    public static ForgeTextureMetadata forResource(Resource resource) throws IOException {
        return resource.m_215509_().m_214059_(TYPE).orElse(EMPTY);
    }
}
