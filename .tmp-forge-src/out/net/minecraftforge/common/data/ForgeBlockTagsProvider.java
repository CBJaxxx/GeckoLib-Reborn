/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.VanillaBlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// We typically don't do static imports as S2S can't remap them {as they are not qualified}, however this conflicts with vanilla and our tag class names, and our tags don't get obfed so its one line of warning.
import static net.minecraftforge.common.Tags.Blocks.*;

@ApiStatus.Internal
public final class ForgeBlockTagsProvider extends VanillaBlockTagsProvider {
    public ForgeBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "forge", existingFileHelper);
    }

    @Override
    public void m_6577_(HolderLookup.Provider p_256380_) {
        (new ForgeBlockItemTagsProvider() {
            @Override
            protected TagAppender<Block, Block> m_401961_(TagKey<Block> p_406922_, TagKey<Item> p_408417_) {
                return ForgeBlockTagsProvider.this.m_255286_(p_406922_);
            }
        }).m_401405_();
        m_255286_(CHORUS_ADDITIONALLY_GROWS_ON)
            .m_401951_(END_STONES);
        addColored(DYED, "{color}_banner");
        addColored(DYED, "{color}_bed");
        addColored(DYED, "{color}_candle");
        addColored(DYED, "{color}_carpet");
        addColored(DYED, "{color}_concrete");
        addColored(DYED, "{color}_concrete_powder");
        addColored(DYED, "{color}_glazed_terracotta");
        addColored(DYED, "{color}_shulker_box");
        addColored(DYED, "{color}_stained_glass");
        addColored(DYED, "{color}_stained_glass_pane");
        addColored(DYED, "{color}_terracotta");
        addColored(DYED, "{color}_wall_banner");
        addColored(DYED, "{color}_wool");
        addColoredTags(m_255286_(DYED)::m_401951_, DYED);
        m_255286_(ENDERMAN_PLACE_ON_BLACKLIST); // forge:enderman_place_on_blacklist
        m_255286_(SKULLS).m_401973_(Blocks.f_50310_, Blocks.f_50311_, Blocks.f_50312_, Blocks.f_50313_, Blocks.f_50316_, Blocks.f_50317_, Blocks.f_50314_, Blocks.f_50315_, Blocks.f_50318_, Blocks.f_50319_, Blocks.f_260630_, Blocks.f_260585_, Blocks.f_50320_, Blocks.f_50321_);
        m_255286_(HIDDEN_FROM_RECIPE_VIEWERS);
        m_255286_(RELOCATION_NOT_SUPPORTED);
        m_255286_(VILLAGER_JOB_SITES).m_401973_(
                Blocks.f_50618_, Blocks.f_50620_, Blocks.f_50255_, Blocks.f_50621_,
                Blocks.f_50256_, Blocks.f_152476_, Blocks.f_152477_, Blocks.f_152478_,
                Blocks.f_50715_, Blocks.f_50622_, Blocks.f_50623_, Blocks.f_50624_,
                Blocks.f_50617_, Blocks.f_50625_, Blocks.f_50619_, Blocks.f_50679_);
    }

    private void addColored(TagKey<Block> group, String pattern) {
        String prefix = group.f_203868_().m_135815_().toUpperCase(Locale.ENGLISH) + '_';
        for (var color : DyeColor.values()) {
            var key = ResourceLocation.m_339182_("minecraft", pattern.replace("{color}", color.m_41065_()));
            TagKey<Block> tag = getForgeTag(prefix + color.m_41065_());
            var block = ForgeRegistries.BLOCKS.getValue(key);
            if (block == null || block == Blocks.f_50016_)
                throw new IllegalStateException("Unknown vanilla block: " + key);
            m_255286_(tag).m_402050_(block);
        }
    }

    private static void addColoredTags(Consumer<TagKey<Block>> consumer, TagKey<Block> group) {
        String prefix = group.f_203868_().m_135815_().toUpperCase(Locale.ENGLISH) + '_';
        for (var color : DyeColor.values()) {
            TagKey<Block> tag = getForgeTag(prefix + color.m_41065_());
            consumer.accept(tag);
        }
    }

    @SuppressWarnings("unchecked")
    private static TagKey<Block> getForgeTag(String name) {
        try {
            name = name.toUpperCase(Locale.ENGLISH);
            return (TagKey<Block>) Tags.Blocks.class.getDeclaredField(name).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(Tags.Blocks.class.getName() + " is missing tag name: " + name);
        }
    }

    private static TagKey<Block> forgeTagKey(String path) {
        return BlockTags.create(ResourceLocation.m_339182_("forge", path));
    }

    @Override
    public String m_6055_() {
        return "Forge Block Tags";
    }
}
