/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;

import static net.minecraftforge.common.Tags.Biomes.*;

@ApiStatus.Internal
public final class ForgeBiomeTagsProvider extends BiomeTagsProvider {
    public ForgeBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "forge", existingFileHelper);
    }

    @Override
    protected void m_6577_(HolderLookup.Provider lookupProvider) {
        m_404511_(NO_DEFAULT_MONSTERS).m_402050_(Biomes.f_48215_).m_402050_(Biomes.f_220594_);
        m_404511_(HIDDEN_FROM_LOCATOR_SELECTION); // Create tag file for visibility

        m_404511_(IS_VOID).m_402050_(Biomes.f_48173_);

        m_404511_(IS_END).m_401951_(BiomeTags.f_215818_);
        m_404511_(IS_NETHER).m_401951_(BiomeTags.f_207612_);
        m_404511_(IS_OVERWORLD).m_401951_(BiomeTags.f_215817_);

        m_404511_(IS_HOT_OVERWORLD)
                .m_402050_(Biomes.f_48207_)
                .m_402050_(Biomes.f_220595_)
                .m_402050_(Biomes.f_48222_)
                .m_402050_(Biomes.f_48197_)
                .m_402050_(Biomes.f_186769_)
                .m_402050_(Biomes.f_48203_)
                .m_402050_(Biomes.f_48159_)
                .m_402050_(Biomes.f_186753_)
                .m_402050_(Biomes.f_48194_)
                .m_402050_(Biomes.f_48157_)
                .m_402050_(Biomes.f_48158_)
                .m_402050_(Biomes.f_186768_)
                .m_402050_(Biomes.f_186759_)
                .m_402050_(Biomes.f_48166_)
                .m_401692_(forgeTagKey("is_hot/overworld"));
        m_404511_(IS_HOT_NETHER)
                .m_402050_(Biomes.f_48209_)
                .m_402050_(Biomes.f_48200_)
                .m_402050_(Biomes.f_48201_)
                .m_402050_(Biomes.f_48199_)
                .m_402050_(Biomes.f_48175_)
                .m_401692_(forgeTagKey("is_hot/nether"));
        m_404511_(IS_HOT_END); // forge:is_hot/end
        m_404511_(IS_HOT).m_401951_(IS_HOT_OVERWORLD).m_401951_(IS_HOT_NETHER).m_401692_(IS_HOT_END);

        m_404511_(IS_COLD_OVERWORLD)
                .m_402050_(Biomes.f_48206_)
                .m_402050_(Biomes.f_186763_)
                .m_402050_(Biomes.f_186764_)
                .m_402050_(Biomes.f_186765_)
                .m_402050_(Biomes.f_186766_)
                .m_402050_(Biomes.f_186767_)
                .m_402050_(Biomes.f_186761_)
                .m_402050_(Biomes.f_48182_)
                .m_402050_(Biomes.f_186755_)
                .m_402050_(Biomes.f_186756_)
                .m_402050_(Biomes.f_186758_)
                .m_402050_(Biomes.f_186757_)
                .m_402050_(Biomes.f_186760_)
                .m_402050_(Biomes.f_48148_)
                .m_402050_(Biomes.f_48152_)
                .m_402050_(Biomes.f_48212_)
                .m_402050_(Biomes.f_48168_)
                .m_402050_(Biomes.f_48211_)
                .m_402050_(Biomes.f_48171_)
                .m_402050_(Biomes.f_48172_)
                .m_401692_(forgeTagKey("is_cold/overworld"));
        m_404511_(IS_COLD_NETHER); // forge:is_cold/nether
        m_404511_(IS_COLD_END)
                .m_402050_(Biomes.f_48210_)
                .m_402050_(Biomes.f_48162_)
                .m_402050_(Biomes.f_48163_)
                .m_402050_(Biomes.f_48164_)
                .m_402050_(Biomes.f_48165_)
                .m_401692_(forgeTagKey("is_cold/end"));
        m_404511_(IS_COLD).m_401951_(IS_COLD_OVERWORLD).m_401692_(IS_COLD_NETHER).m_401951_(IS_COLD_END);

        m_404511_(IS_DEAD)
                .m_401692_(forgeTagKey("is_dead"));

        m_404511_(IS_SPARSE_VEGETATION_OVERWORLD)
                .m_402050_(Biomes.f_186753_)
                .m_402050_(Biomes.f_48157_)
                .m_402050_(Biomes.f_48158_)
                .m_402050_(Biomes.f_186769_)
                .m_402050_(Biomes.f_186768_)
                .m_402050_(Biomes.f_186767_)
                .m_402050_(Biomes.f_186765_)
                .m_402050_(Biomes.f_186766_)
                .m_402050_(Biomes.f_186756_)
                .m_402050_(Biomes.f_186758_)
                .m_402050_(Biomes.f_186757_);
                //.addOptionalTag(forgeTagKey("is_sparse/overworld")); // can't because forge:is_sparse/overworld contains dripstone caves and eroded badlands, while c:is_sparse_vegetation/overworld does not
        m_404511_(IS_SPARSE_NETHER); // forge:is_sparse/nether
        m_404511_(IS_SPARSE_END); // forge:is_sparse/end
        m_404511_(IS_SPARSE_VEGETATION).m_401951_(IS_SPARSE_VEGETATION_OVERWORLD).m_401692_(IS_SPARSE_NETHER).m_401692_(IS_SPARSE_END);

        m_404511_(IS_DENSE_VEGETATION_OVERWORLD)
                .m_402050_(Biomes.f_48151_)
                .m_402050_(Biomes.f_186762_)
                .m_402050_(Biomes.f_186764_)
                .m_402050_(Biomes.f_48222_)
                .m_402050_(Biomes.f_48197_)
                .m_402050_(Biomes.f_220595_)
                .m_401692_(forgeTagKey("is_dense/overworld"));
        m_404511_(IS_DENSE_NETHER);
        m_404511_(IS_DENSE_END);
        m_404511_(IS_DENSE_VEGETATION).m_401951_(IS_DENSE_VEGETATION_OVERWORLD).m_401692_(IS_DENSE_NETHER).m_401692_(IS_DENSE_END);

        m_404511_(IS_WET_OVERWORLD)
                .m_402050_(Biomes.f_48207_)
                .m_402050_(Biomes.f_220595_)
                .m_402050_(Biomes.f_48222_)
                .m_402050_(Biomes.f_48197_)
                .m_402050_(Biomes.f_186769_)
                .m_402050_(Biomes.f_48217_)
                .m_402050_(Biomes.f_151785_)
                .m_402050_(Biomes.f_151784_)
                .m_401692_(forgeTagKey("is_wet/overworld"));
        m_404511_(IS_WET_NETHER);
        m_404511_(IS_WET_END);
        m_404511_(IS_WET).m_401951_(IS_WET_OVERWORLD).m_401692_(IS_WET_NETHER).m_401692_(IS_WET_END);

        m_404511_(IS_DRY_OVERWORLD)
                .m_402050_(Biomes.f_48203_)
                .m_402050_(Biomes.f_48159_)
                .m_402050_(Biomes.f_186753_)
                .m_402050_(Biomes.f_48194_)
                .m_402050_(Biomes.f_48157_)
                .m_402050_(Biomes.f_48158_)
                .m_402050_(Biomes.f_186768_)
                .m_401692_(forgeTagKey("is_dry/overworld"));
        m_404511_(IS_DRY_NETHER)
                .m_402050_(Biomes.f_48209_)
                .m_402050_(Biomes.f_48200_)
                .m_402050_(Biomes.f_48201_)
                .m_402050_(Biomes.f_48199_)
                .m_402050_(Biomes.f_48175_)
                .m_401692_(forgeTagKey("is_dry/nether"));
        m_404511_(IS_DRY_END)
                .m_402050_(Biomes.f_48210_)
                .m_402050_(Biomes.f_48162_)
                .m_402050_(Biomes.f_48163_)
                .m_402050_(Biomes.f_48164_)
                .m_402050_(Biomes.f_48165_)
                .m_401692_(forgeTagKey("is_dry/end"));
        m_404511_(IS_DRY)
                .m_401951_(IS_DRY_OVERWORLD).m_401951_(IS_DRY_NETHER).m_401951_(IS_DRY_END)
                .m_401692_(forgeTagKey("is_dry"));

        m_404511_(IS_CONIFEROUS_TREE)
                .m_401951_(IS_TAIGA)
                .m_402050_(Biomes.f_186755_)
                .m_401692_(forgeTagKey("is_coniferous"));
        m_404511_(IS_SAVANNA_TREE).m_401951_(IS_SAVANNA);
        m_404511_(IS_JUNGLE_TREE).m_401951_(IS_JUNGLE);
        m_404511_(IS_DECIDUOUS_TREE).m_402050_(Biomes.f_48205_).m_402050_(Biomes.f_48179_).m_402050_(Biomes.f_48149_).m_402050_(Biomes.f_48151_).m_402050_(Biomes.f_186762_).m_402050_(Biomes.f_186767_);

        m_404511_(IS_MOUNTAIN_SLOPE).m_402050_(Biomes.f_186756_).m_402050_(Biomes.f_186754_).m_402050_(Biomes.f_186755_).m_402050_(Biomes.f_271432_);
        m_404511_(IS_MOUNTAIN_PEAK)
                .m_402050_(Biomes.f_186758_).m_402050_(Biomes.f_186757_).m_402050_(Biomes.f_186759_)
                .m_401692_(forgeTagKey("is_peak"));
        m_404511_(IS_MOUNTAIN)
                .m_401951_(BiomeTags.f_207606_).m_401951_(IS_MOUNTAIN_PEAK).m_401951_(IS_MOUNTAIN_SLOPE);
                //.addOptionalTag(forgeTagKey("is_mountain")); // can't because forge:is_mountain contains savanna plateau, while c:is_mountain does not

        m_404511_(IS_FOREST).m_401951_(BiomeTags.f_207611_);
        m_404511_(IS_BIRCH_FOREST).m_402050_(Biomes.f_48149_).m_402050_(Biomes.f_186762_);
        m_404511_(IS_FLOWER_FOREST).m_402050_(Biomes.f_48179_);
        m_404511_(IS_FLORAL).m_401951_(IS_FLOWER_FOREST).m_402050_(Biomes.f_48176_).m_402050_(Biomes.f_271432_).m_402050_(Biomes.f_186754_);
        m_404511_(IS_BEACH).m_401951_(BiomeTags.f_207604_);
        m_404511_(IS_STONY_SHORES).m_402050_(Biomes.f_186760_);
        m_404511_(IS_DESERT).m_402050_(Biomes.f_48203_);
        m_404511_(IS_BADLANDS).m_401951_(BiomeTags.f_207607_);
        m_404511_(IS_PLAINS).m_402050_(Biomes.f_48202_).m_402050_(Biomes.f_48176_);
        m_404511_(IS_SNOWY_PLAINS).m_402050_(Biomes.f_186761_);
        m_404511_(IS_TAIGA).m_401951_(BiomeTags.f_207609_);
        m_404511_(IS_HILL).m_401951_(BiomeTags.f_207608_);
        m_404511_(IS_WINDSWEPT).m_402050_(Biomes.f_186765_).m_402050_(Biomes.f_186766_).m_402050_(Biomes.f_186767_).m_402050_(Biomes.f_186768_);
        m_404511_(IS_SAVANNA).m_401951_(BiomeTags.f_215816_);
        m_404511_(IS_JUNGLE).m_401951_(BiomeTags.f_207610_);
        m_404511_(IS_SNOWY)
            .m_402050_(Biomes.f_48148_)
            .m_402050_(Biomes.f_186761_)
            .m_402050_(Biomes.f_48182_)
            .m_402050_(Biomes.f_48152_)
            .m_402050_(Biomes.f_186755_)
            .m_402050_(Biomes.f_186756_)
            .m_402050_(Biomes.f_186758_)
            .m_402050_(Biomes.f_186757_);
                //.addOptionalTag(forgeTagKey("is_snowy")); // can't add forge:is_snowy because it contains frozen ocean and frozen river, while c:is_snowy does not
        m_404511_(IS_ICY).m_402050_(Biomes.f_48182_).m_402050_(Biomes.f_186757_);
        m_404511_(IS_SWAMP).m_402050_(Biomes.f_48207_).m_402050_(Biomes.f_220595_);
        m_404511_(IS_OLD_GROWTH).m_402050_(Biomes.f_186762_).m_402050_(Biomes.f_186763_).m_402050_(Biomes.f_186764_);
        m_404511_(IS_LUSH).m_402050_(Biomes.f_151785_);
        m_404511_(IS_MAGICAL);
        m_404511_(IS_MODIFIED);
        m_404511_(IS_SANDY).m_402050_(Biomes.f_48203_).m_402050_(Biomes.f_48159_).m_402050_(Biomes.f_186753_).m_402050_(Biomes.f_48194_).m_402050_(Biomes.f_48217_);
        m_404511_(IS_MUSHROOM).m_402050_(Biomes.f_48215_);
        m_404511_(IS_PLATEAU).m_402050_(Biomes.f_186753_).m_402050_(Biomes.f_48158_).m_402050_(Biomes.f_271432_).m_402050_(Biomes.f_186754_);
        m_404511_(IS_SPOOKY).m_402050_(Biomes.f_48151_).m_402050_(Biomes.f_220594_);
        m_404511_(IS_WASTELAND);
        m_404511_(IS_RARE).m_402050_(Biomes.f_48176_).m_402050_(Biomes.f_48179_).m_402050_(Biomes.f_186762_).m_402050_(Biomes.f_186764_).m_402050_(Biomes.f_48197_).m_402050_(Biomes.f_186769_).m_402050_(Biomes.f_48194_).m_402050_(Biomes.f_48158_).m_402050_(Biomes.f_186768_).m_402050_(Biomes.f_48182_).m_402050_(Biomes.f_186766_).m_402050_(Biomes.f_48215_).m_402050_(Biomes.f_220594_);

        m_404511_(IS_RIVER).m_401951_(BiomeTags.f_207605_);
        m_404511_(IS_SHALLOW_OCEAN).m_402050_(Biomes.f_48174_).m_402050_(Biomes.f_48167_).m_402050_(Biomes.f_48166_).m_402050_(Biomes.f_48168_).m_402050_(Biomes.f_48211_);
        m_404511_(IS_DEEP_OCEAN).m_401951_(BiomeTags.f_207602_);
        m_404511_(IS_OCEAN).m_401951_(BiomeTags.f_207603_).m_401951_(IS_SHALLOW_OCEAN).m_401951_(IS_DEEP_OCEAN);
        m_404511_(IS_AQUATIC_ICY).m_402050_(Biomes.f_48212_).m_402050_(Biomes.f_48172_).m_402050_(Biomes.f_48211_);
        m_404511_(IS_AQUATIC).m_401951_(IS_OCEAN).m_401951_(IS_RIVER);

        m_404511_(IS_CAVE)
                .m_402050_(Biomes.f_151785_).m_402050_(Biomes.f_151784_).m_402050_(Biomes.f_220594_)
                .m_401692_(forgeTagKey("is_cave"));
        m_404511_(IS_UNDERGROUND).m_401951_(IS_CAVE);

        m_404511_(IS_NETHER_FOREST).m_402050_(Biomes.f_48200_).m_402050_(Biomes.f_48201_);
        m_404511_(IS_OUTER_END_ISLAND).m_402050_(Biomes.f_48164_).m_402050_(Biomes.f_48163_).m_402050_(Biomes.f_48165_);

        // Backwards compat definitions for pre-1.21 legacy `forge:` tags.
        // TODO: Remove backwards compat tag entries in 1.22
        tag(Biomes.f_48202_, forgeTagKey("is_plains"));
        tag(Biomes.f_48203_, forgeTagKey("is_hot/overworld"), forgeTagKey("is_dry/overworld"), forgeTagKey("is_sandy"), forgeTagKey("is_desert"));
        tag(Biomes.f_48206_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_coniferous"));
        tag(Biomes.f_48207_, forgeTagKey("is_wet/overworld"), forgeTagKey("is_swamp"));
        tag(Biomes.f_48209_, forgeTagKey("is_hot/nether"), forgeTagKey("is_dry/nether"));
        tag(Biomes.f_48210_, forgeTagKey("is_cold/end"), forgeTagKey("is_dry/end"));
        tag(Biomes.f_48211_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_snowy"));
        tag(Biomes.f_48212_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_snowy"));
        tag(Biomes.f_186761_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_snowy"), forgeTagKey("is_wasteland"), forgeTagKey("is_plains"));
        tag(Biomes.f_48215_, forgeTagKey("is_mushroom"), forgeTagKey("is_rare"));
        tag(Biomes.f_48222_, forgeTagKey("is_hot/overworld"), forgeTagKey("is_wet/overworld"), forgeTagKey("is_dense/overworld"));
        tag(Biomes.f_186769_, forgeTagKey("is_hot/overworld"), forgeTagKey("is_wet/overworld"), forgeTagKey("is_rare"));
        tag(Biomes.f_48217_, forgeTagKey("is_wet/overworld"), forgeTagKey("is_sandy"));
        tag(Biomes.f_48148_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_snowy"));
        tag(Biomes.f_48151_, forgeTagKey("is_spooky"), forgeTagKey("is_dense/overworld"));
        tag(Biomes.f_48152_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_coniferous"), forgeTagKey("is_snowy"));
        tag(Biomes.f_186763_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_coniferous"));
        tag(Biomes.f_186767_, forgeTagKey("is_sparse/overworld"));
        tag(Biomes.f_48157_, forgeTagKey("is_hot/overworld"), forgeTagKey("is_sparse/overworld"));
        tag(Biomes.f_48158_, forgeTagKey("is_hot/overworld"), forgeTagKey("is_sparse/overworld"), forgeTagKey("is_rare"), forgeTagKey("is_slope"), Tags.Biomes.IS_PLATEAU);
        tag(Biomes.f_48159_, forgeTagKey("is_sandy"), forgeTagKey("is_dry/overworld"));
        tag(Biomes.f_186753_, forgeTagKey("is_sandy"), forgeTagKey("is_dry/overworld"), forgeTagKey("is_sparse/overworld"), forgeTagKey("is_slope"), Tags.Biomes.IS_PLATEAU);
        tag(Biomes.f_186754_, forgeTagKey("is_plains"), Tags.Biomes.IS_PLATEAU, forgeTagKey("is_slope"));
        tag(Biomes.f_186755_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_coniferous"), forgeTagKey("is_snowy"), forgeTagKey("is_slope"));
        tag(Biomes.f_186756_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_sparse/overworld"), forgeTagKey("is_snowy"), forgeTagKey("is_slope"));
        tag(Biomes.f_186758_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_sparse/overworld"), forgeTagKey("is_snowy"), forgeTagKey("is_peak"));
        tag(Biomes.f_186757_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_sparse/overworld"), forgeTagKey("is_snowy"), forgeTagKey("is_peak"));
        tag(Biomes.f_186759_, forgeTagKey("is_hot/overworld"), forgeTagKey("is_peak"));
        tag(Biomes.f_48162_, forgeTagKey("is_cold/end"), forgeTagKey("is_dry/end"));
        tag(Biomes.f_48163_, forgeTagKey("is_cold/end"), forgeTagKey("is_dry/end"));
        tag(Biomes.f_48164_, forgeTagKey("is_cold/end"), forgeTagKey("is_dry/end"));
        tag(Biomes.f_48165_, forgeTagKey("is_cold/end"), forgeTagKey("is_dry/end"));
        tag(Biomes.f_48166_, forgeTagKey("is_hot/overworld"));
        tag(Biomes.f_48168_, forgeTagKey("is_cold/overworld"));
        tag(Biomes.f_48171_, forgeTagKey("is_cold/overworld"));
        tag(Biomes.f_48172_, forgeTagKey("is_cold/overworld"));
        tag(Biomes.f_48173_, forgeTagKey("is_void"));
        tag(Biomes.f_48176_, forgeTagKey("is_plains"), forgeTagKey("is_rare"));
        tag(Biomes.f_186766_, forgeTagKey("is_sparse/overworld"), forgeTagKey("is_rare"));
        tag(Biomes.f_48179_, forgeTagKey("is_rare"));
        tag(Biomes.f_48182_, forgeTagKey("is_cold/overworld"), forgeTagKey("is_snowy"), forgeTagKey("is_rare"));
        tag(Biomes.f_186762_, forgeTagKey("is_dense/overworld"), forgeTagKey("is_rare"));
        tag(Biomes.f_186764_, forgeTagKey("is_dense/overworld"), forgeTagKey("is_rare"));
        tag(Biomes.f_186768_, forgeTagKey("is_hot/overworld"), forgeTagKey("is_dry/overworld"), forgeTagKey("is_sparse/overworld"), forgeTagKey("is_rare"));
        tag(Biomes.f_48194_, forgeTagKey("is_hot/overworld"), forgeTagKey("is_dry/overworld"), forgeTagKey("is_sparse/overworld"), forgeTagKey("is_rare"));
        tag(Biomes.f_48197_, forgeTagKey("is_hot/overworld"), forgeTagKey("is_wet/overworld"), forgeTagKey("is_rare"));
        tag(Biomes.f_151785_, forgeTagKey("is_cave"), forgeTagKey("is_lush"), forgeTagKey("is_wet/overworld"));
        tag(Biomes.f_151784_, forgeTagKey("is_cave"), forgeTagKey("is_sparse/overworld"));
        tag(Biomes.f_48199_, forgeTagKey("is_hot/nether"), forgeTagKey("is_dry/nether"));
        tag(Biomes.f_48200_, forgeTagKey("is_hot/nether"), forgeTagKey("is_dry/nether"));
        tag(Biomes.f_48201_, forgeTagKey("is_hot/nether"), forgeTagKey("is_dry/nether"));
        tag(Biomes.f_48175_, forgeTagKey("is_hot/nether"), forgeTagKey("is_dry/nether"));
        tag(Biomes.f_220595_, forgeTagKey("is_wet/overworld"), forgeTagKey("is_hot/overworld"), forgeTagKey("is_swamp"));
        tag(Biomes.f_220594_, forgeTagKey("is_cave"), forgeTagKey("is_rare"), forgeTagKey("is_spooky"));

        m_404511_(forgeTagKey("is_hot")).m_401951_(forgeTagKey("is_hot/overworld")).m_401951_(forgeTagKey("is_hot/nether")).m_401692_(Tags.Biomes.IS_HOT_END);
        m_404511_(forgeTagKey("is_cold")).m_401951_(forgeTagKey("is_cold/overworld")).m_401692_(Tags.Biomes.IS_COLD_NETHER).m_401951_(forgeTagKey("is_cold/end"));
        m_404511_(forgeTagKey("is_sparse")).m_401951_(forgeTagKey("is_sparse/overworld")).m_401692_(Tags.Biomes.IS_SPARSE_NETHER).m_401692_(Tags.Biomes.IS_SPARSE_END);
        m_404511_(forgeTagKey("is_dense")).m_401951_(forgeTagKey("is_dense/overworld")).m_401692_(Tags.Biomes.IS_DENSE_NETHER).m_401692_(Tags.Biomes.IS_DENSE_END);
        m_404511_(forgeTagKey("is_wet")).m_401951_(forgeTagKey("is_wet/overworld")).m_401692_(Tags.Biomes.IS_WET_NETHER).m_401692_(Tags.Biomes.IS_WET_END);
        m_404511_(forgeTagKey("is_dry")).m_401951_(forgeTagKey("is_dry/overworld")).m_401951_(forgeTagKey("is_dry/nether")).m_401951_(forgeTagKey("is_dry/end"));
        m_404511_(forgeTagKey("is_dead"));

        m_404511_(forgeTagKey("is_water")).m_401951_(BiomeTags.f_207603_).m_401951_(BiomeTags.f_207605_);
        m_404511_(forgeTagKey("is_mountain")).m_401951_(forgeTagKey("is_peak")).m_401951_(forgeTagKey("is_slope"));
        m_404511_(forgeTagKey("is_underground")).m_401951_(forgeTagKey("is_cave"));
    }

    @SafeVarargs
    private void tag(ResourceKey<Biome> biome, TagKey<Biome>... tags) {
        for (TagKey<Biome> key : tags) {
            m_404511_(key).m_402050_(biome);
        }
    }

    private static TagKey<Biome> forgeTagKey(String path) {
        return BiomeTags.create(ResourceLocation.m_339182_("forge", path));
    }

    @Override
    public String m_6055_() {
        return "Forge Biome Tags";
    }

}
