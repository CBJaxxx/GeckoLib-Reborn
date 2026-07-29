/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TagConventionMappings {
    private TagConventionMappings() {}

    /**
     * A map of known legacy tags to their common convention equivalents.
     */
    public static final Map<TagKey<?>, String> MAPPINGS = Map.<TagKey<?>, String>ofEntries(
            //region Blocks
            legacyToCommon(Registries.f_256747_, forgeRl("barrels"), Tags.Blocks.BARRELS), // forge:barrels -> c:barrels
            legacyToCommon(Registries.f_256747_, forgeRl("barrels/wooden"), Tags.Blocks.BARRELS_WOODEN),
            legacyToCommon(Registries.f_256747_, forgeRl("bookshelves"), Tags.Blocks.BOOKSHELVES),

            legacyToCommon(Registries.f_256747_, forgeRl("chests"), Tags.Blocks.CHESTS),
            legacyToCommon(Registries.f_256747_, forgeRl("chests/wooden"), Tags.Blocks.CHESTS_WOODEN),

            legacyToCommon(Registries.f_256747_, forgeRl("cobblestone"), Tags.Blocks.COBBLESTONES),

            legacyToCommon(Registries.f_256747_, forgeRl("glass"), Tags.Blocks.GLASS_BLOCKS),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/black"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_BLACK)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/blue"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_BLUE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/brown"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_BROWN)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/colorless"), Tags.Blocks.GLASS_BLOCKS_COLORLESS),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/cyan"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_CYAN)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/gray"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_GRAY)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/green"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_GREEN)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/light_blue"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_LIGHT_BLUE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/light_gray"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_LIGHT_GRAY)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/lime"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_LIME)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/magenta"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_MAGENTA)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/orange"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_ORANGE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/pink"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_PINK)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/purple"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_PURPLE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/red"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_RED)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/silica"), Tags.Blocks.GLASS_BLOCKS_CHEAP),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/tinted"), Tags.Blocks.GLASS_BLOCKS_TINTED),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/white"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_WHITE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass/yellow"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Items.DYED_YELLOW)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes"), Tags.Blocks.GLASS_PANES),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/colorless"), Tags.Blocks.GLASS_PANES_COLORLESS),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/black"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_BLACK)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/blue"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_BLUE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/brown"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_BROWN)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/cyan"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_CYAN)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/gray"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_GRAY)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/green"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_GREEN)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/light_blue"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_LIGHT_BLUE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/light_gray"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_LIGHT_GRAY)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/lime"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_LIME)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/magenta"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_MAGENTA)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/orange"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_ORANGE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/pink"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_PINK)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/purple"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_PURPLE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/red"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_RED)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/white"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_WHITE)),
            legacyToCommon(Registries.f_256747_, forgeRl("glass_panes/yellow"), List.of(Tags.Blocks.GLASS_PANES, Tags.Items.DYED_YELLOW)),

            legacyToCommon(Registries.f_256747_, forgeRl("obsidian"), Tags.Blocks.OBSIDIANS),
            legacyToCommon(Registries.f_256747_, forgeRl("ores"), Tags.Blocks.ORES),
            legacyToCommon(Registries.f_256747_, forgeRl("ores/netherite_scrap"), Tags.Blocks.ORES_NETHERITE_SCRAP),
            legacyToCommon(Registries.f_256747_, forgeRl("ores/quartz"), Tags.Blocks.ORES_QUARTZ),

            legacyToCommon(Registries.f_256747_, forgeRl("sandstone"), Tags.Blocks.SANDSTONE_BLOCKS),
            legacyToCommon(Registries.f_256747_, forgeRl("stained_glass"), List.of(Tags.Blocks.GLASS_BLOCKS, Tags.Blocks.DYED)),
            legacyToCommon(Registries.f_256747_, forgeRl("stained_glass_panes"), List.of(Tags.Blocks.GLASS_PANES, Tags.Blocks.DYED)),
            legacyToCommon(Registries.f_256747_, forgeRl("stone"), Tags.Blocks.STONES),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks"), Tags.Blocks.STORAGE_BLOCKS),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/coal"), Tags.Blocks.STORAGE_BLOCKS_COAL),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/copper"), Tags.Blocks.STORAGE_BLOCKS_COPPER),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/diamond"), Tags.Blocks.STORAGE_BLOCKS_DIAMOND),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/emerald"), Tags.Blocks.STORAGE_BLOCKS_EMERALD),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/gold"), Tags.Blocks.STORAGE_BLOCKS_GOLD),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/iron"), Tags.Blocks.STORAGE_BLOCKS_IRON),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/lapis"), Tags.Blocks.STORAGE_BLOCKS_LAPIS),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/netherite"), Tags.Blocks.STORAGE_BLOCKS_NETHERITE),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/quartz"), Tags.Blocks.STORAGE_BLOCKS_QUARTZ),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/raw_copper"), Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/raw_gold"), Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/raw_iron"), Tags.Blocks.STORAGE_BLOCKS_RAW_IRON),
            legacyToCommon(Registries.f_256747_, forgeRl("storage_blocks/redstone"), Tags.Blocks.STORAGE_BLOCKS_REDSTONE),
            //endregion

            //region Entity Types
            legacyToCommon(Registries.f_256939_, forgeRl("bosses"), Tags.EntityTypes.BOSSES),
            //endregion

            //region Items
            legacyToCommon(Registries.f_256913_, forgeRl("barrels"), Tags.Items.BARRELS),
            legacyToCommon(Registries.f_256913_, forgeRl("barrels/wooden"), Tags.Items.BARRELS_WOODEN),
            legacyToCommon(Registries.f_256913_, forgeRl("bookshelves"), Tags.Items.BOOKSHELVES),

            legacyToCommon(Registries.f_256913_, forgeRl("chests"), Tags.Items.CHESTS),
            legacyToCommon(Registries.f_256913_, forgeRl("chests/wooden"), Tags.Items.CHESTS_WOODEN),
            legacyToCommon(Registries.f_256913_, forgeRl("cobblestone"), Tags.Items.COBBLESTONES),
            legacyToCommon(Registries.f_256913_, forgeRl("crops"), Tags.Items.CROPS),
            legacyToCommon(Registries.f_256913_, forgeRl("crops/beetroot"), Tags.Items.CROPS_BEETROOT),
            legacyToCommon(Registries.f_256913_, forgeRl("crops/carrot"), Tags.Items.CROPS_CARROT),
            legacyToCommon(Registries.f_256913_, forgeRl("crops/potato"), Tags.Items.CROPS_POTATO),
            legacyToCommon(Registries.f_256913_, forgeRl("crops/wheat"), Tags.Items.CROPS_WHEAT),

            legacyToCommon(Registries.f_256913_, forgeRl("dusts"), Tags.Items.DUSTS),
            legacyToCommon(Registries.f_256913_, forgeRl("dusts/redstone"), Tags.Items.DUSTS_REDSTONE),
            legacyToCommon(Registries.f_256913_, forgeRl("dusts/glowstone"), Tags.Items.DUSTS_GLOWSTONE),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes"), Tags.Items.DYES),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/black"), Tags.Items.DYES_BLACK),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/red"), Tags.Items.DYES_RED),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/green"), Tags.Items.DYES_GREEN),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/brown"), Tags.Items.DYES_BROWN),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/blue"), Tags.Items.DYES_BLUE),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/purple"), Tags.Items.DYES_PURPLE),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/cyan"), Tags.Items.DYES_CYAN),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/light_gray"), Tags.Items.DYES_LIGHT_GRAY),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/gray"), Tags.Items.DYES_GRAY),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/pink"), Tags.Items.DYES_PINK),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/lime"), Tags.Items.DYES_LIME),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/yellow"), Tags.Items.DYES_YELLOW),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/light_blue"), Tags.Items.DYES_LIGHT_BLUE),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/magenta"), Tags.Items.DYES_MAGENTA),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/orange"), Tags.Items.DYES_ORANGE),
            legacyToCommon(Registries.f_256913_, forgeRl("dyes/white"), Tags.Items.DYES_WHITE),

            legacyToCommon(Registries.f_256913_, forgeRl("ender_pearls"), Tags.Items.ENDER_PEARLS),

            legacyToCommon(Registries.f_256913_, forgeRl("foods/pie"), Tags.Items.FOODS_PIE),

            legacyToCommon(Registries.f_256913_, forgeRl("gems"), Tags.Items.GEMS),
            legacyToCommon(Registries.f_256913_, forgeRl("gems/diamond"), Tags.Items.GEMS_DIAMOND),
            legacyToCommon(Registries.f_256913_, forgeRl("gems/emerald"), Tags.Items.GEMS_EMERALD),
            legacyToCommon(Registries.f_256913_, forgeRl("gems/amethyst"), Tags.Items.GEMS_AMETHYST),
            legacyToCommon(Registries.f_256913_, forgeRl("gems/lapis"), Tags.Items.GEMS_LAPIS),
            legacyToCommon(Registries.f_256913_, forgeRl("gems/quartz"), Tags.Items.GEMS_QUARTZ),
            legacyToCommon(Registries.f_256913_, forgeRl("glass"), Tags.Items.GLASS_BLOCKS),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/black"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_BLACK)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/blue"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_BLUE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/brown"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_BROWN)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/colorless"), Tags.Items.GLASS_BLOCKS_COLORLESS),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/cyan"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_CYAN)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/gray"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_GRAY)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/green"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_GREEN)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/light_blue"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_LIGHT_BLUE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/light_gray"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_LIGHT_GRAY)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/lime"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_LIME)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/magenta"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_MAGENTA)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/orange"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_ORANGE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/pink"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_PINK)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/purple"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_PURPLE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/red"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_RED)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/silica"), Tags.Items.GLASS_BLOCKS_CHEAP),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/tinted"), Tags.Items.GLASS_BLOCKS_TINTED),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/white"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_WHITE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass/yellow"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Items.DYED_YELLOW)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes"), Tags.Items.GLASS_PANES),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/black"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_BLACK)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/blue"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_BLUE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/brown"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_BROWN)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/colorless"), Tags.Items.GLASS_PANES_COLORLESS),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/cyan"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_CYAN)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/gray"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_GRAY)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/green"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_GREEN)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/light_blue"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_LIGHT_BLUE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/light_gray"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_LIGHT_GRAY)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/lime"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_LIME)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/magenta"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_MAGENTA)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/orange"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_ORANGE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/pink"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_PINK)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/purple"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_PURPLE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/red"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_RED)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/white"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_WHITE)),
            legacyToCommon(Registries.f_256913_, forgeRl("glass_panes/yellow"), List.of(Tags.Items.GLASS_PANES, Tags.Items.DYED_YELLOW)),

            legacyToCommon(Registries.f_256913_, forgeRl("obsidian"), Tags.Items.OBSIDIANS),
            legacyToCommon(Registries.f_256913_, forgeRl("ingots"), Tags.Items.INGOTS),
            legacyToCommon(Registries.f_256913_, forgeRl("ingots/brick"), Tags.Items.BRICKS_NORMAL),
            legacyToCommon(Registries.f_256913_, forgeRl("ingots/copper"), Tags.Items.INGOTS_COPPER),
            legacyToCommon(Registries.f_256913_, forgeRl("ingots/gold"), Tags.Items.INGOTS_GOLD),
            legacyToCommon(Registries.f_256913_, forgeRl("ingots/iron"), Tags.Items.INGOTS_IRON),
            legacyToCommon(Registries.f_256913_, forgeRl("ingots/netherite"), Tags.Items.INGOTS_NETHERITE),
            legacyToCommon(Registries.f_256913_, forgeRl("ingots/nether_brick"), Tags.Items.BRICKS_NETHER),

            legacyToCommon(Registries.f_256913_, forgeRl("leather"), Tags.Items.LEATHERS),

            legacyToCommon(Registries.f_256913_, forgeRl("nuggets"), Tags.Items.NUGGETS),
            legacyToCommon(Registries.f_256913_, forgeRl("nuggets/gold"), Tags.Items.NUGGETS_GOLD),
            legacyToCommon(Registries.f_256913_, forgeRl("nuggets/iron"), Tags.Items.NUGGETS_IRON),

            legacyToCommon(Registries.f_256913_, forgeRl("ores"), Tags.Items.ORES),
            legacyToCommon(Registries.f_256913_, forgeRl("ores/netherite_scrap"), Tags.Items.ORES_NETHERITE_SCRAP),
            legacyToCommon(Registries.f_256913_, forgeRl("ores/quartz"), Tags.Items.ORES_QUARTZ),

            legacyToCommon(Registries.f_256913_, forgeRl("raw_materials"), Tags.Items.RAW_MATERIALS),
            legacyToCommon(Registries.f_256913_, forgeRl("raw_materials/copper"), Tags.Items.RAW_MATERIALS_COPPER),
            legacyToCommon(Registries.f_256913_, forgeRl("raw_materials/gold"), Tags.Items.RAW_MATERIALS_GOLD),
            legacyToCommon(Registries.f_256913_, forgeRl("raw_materials/iron"), Tags.Items.RAW_MATERIALS_IRON),
            legacyToCommon(Registries.f_256913_, forgeRl("rods"), Tags.Items.RODS),
            legacyToCommon(Registries.f_256913_, forgeRl("rods/blaze"), Tags.Items.RODS_BLAZE),
            legacyToCommon(Registries.f_256913_, forgeRl("rods/wooden"), Tags.Items.RODS_WOODEN),

            legacyToCommon(Registries.f_256913_, forgeRl("sandstone"), Tags.Items.SANDSTONE_BLOCKS),

            legacyToCommon(Registries.f_256913_, forgeRl("slimeballs"), Tags.Items.SLIME_BALLS),
            legacyToCommon(Registries.f_256913_, forgeRl("stained_glass"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Blocks.DYED)),
            legacyToCommon(Registries.f_256913_, forgeRl("stained_glass_panes"), List.of(Tags.Items.GLASS_BLOCKS, Tags.Blocks.DYED)),
            legacyToCommon(Registries.f_256913_, forgeRl("stone"), Tags.Items.STONES),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks"), Tags.Items.STORAGE_BLOCKS),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/coal"), Tags.Items.STORAGE_BLOCKS_COAL),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/copper"), Tags.Items.STORAGE_BLOCKS_COPPER),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/diamond"), Tags.Items.STORAGE_BLOCKS_DIAMOND),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/emerald"), Tags.Items.STORAGE_BLOCKS_EMERALD),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/gold"), Tags.Items.STORAGE_BLOCKS_GOLD),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/iron"), Tags.Items.STORAGE_BLOCKS_IRON),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/lapis"), Tags.Items.STORAGE_BLOCKS_LAPIS),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/netherite"), Tags.Items.STORAGE_BLOCKS_NETHERITE),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/quartz"), Tags.Items.STORAGE_BLOCKS_QUARTZ),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/raw_copper"), Tags.Items.STORAGE_BLOCKS_RAW_COPPER),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/raw_gold"), Tags.Items.STORAGE_BLOCKS_RAW_GOLD),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/raw_iron"), Tags.Items.STORAGE_BLOCKS_RAW_IRON),
            legacyToCommon(Registries.f_256913_, forgeRl("storage_blocks/redstone"), Tags.Items.STORAGE_BLOCKS_REDSTONE),
            legacyToCommon(Registries.f_256913_, forgeRl("string"), Tags.Items.STRINGS),

            legacyToCommon(Registries.f_256913_, forgeRl("tools"), Tags.Items.TOOLS),
            legacyToCommon(Registries.f_256913_, forgeRl("tools/shields"), Tags.Items.TOOLS_SHIELD),
            legacyToCommon(Registries.f_256913_, forgeRl("tools/bows"), Tags.Items.TOOLS_BOW),
            legacyToCommon(Registries.f_256913_, forgeRl("tools/crossbows"), Tags.Items.TOOLS_CROSSBOW),
            legacyToCommon(Registries.f_256913_, forgeRl("tools/fishing_rods"), Tags.Items.TOOLS_FISHING_ROD),
            legacyToCommon(Registries.f_256913_, forgeRl("tools/tridents"), Tags.Items.TOOLS_SPEAR),
            legacyToCommon(Registries.f_256913_, forgeRl("tools/shears"), Tags.Items.TOOLS_SHEAR),

            legacyToCommon(Registries.f_256913_, forgeRl("armors"), Tags.Items.ARMORS),
            //endregion

            //region Fluids
            legacyToCommon(Registries.f_256808_, forgeRl("milk"), Tags.Fluids.MILK),
            //endregion

            //region Biomes
            legacyToCommon(Registries.f_256952_, forgeRl("is_void"), Tags.Biomes.IS_VOID),

            legacyToCommon(Registries.f_256952_, forgeRl("is_hot"), Tags.Biomes.IS_HOT),
            legacyToCommon(Registries.f_256952_, forgeRl("is_hot/overworld"), Tags.Biomes.IS_HOT_OVERWORLD),
            legacyToCommon(Registries.f_256952_, forgeRl("is_hot/nether"), Tags.Biomes.IS_HOT_NETHER),

            legacyToCommon(Registries.f_256952_, forgeRl("is_cold"), Tags.Biomes.IS_COLD),
            legacyToCommon(Registries.f_256952_, forgeRl("is_cold/overworld"), Tags.Biomes.IS_COLD_OVERWORLD),
            legacyToCommon(Registries.f_256952_, forgeRl("is_cold/end"), Tags.Biomes.IS_COLD_END),

            legacyToCommon(Registries.f_256952_, forgeRl("is_sparse"), Tags.Biomes.IS_SPARSE_VEGETATION),
            legacyToCommon(Registries.f_256952_, forgeRl("is_sparse/overworld"), Tags.Biomes.IS_SPARSE_VEGETATION_OVERWORLD),
            legacyToCommon(Registries.f_256952_, forgeRl("is_dense"), Tags.Biomes.IS_DENSE_VEGETATION),
            legacyToCommon(Registries.f_256952_, forgeRl("is_dense/overworld"), Tags.Biomes.IS_DENSE_VEGETATION_OVERWORLD),

            legacyToCommon(Registries.f_256952_, forgeRl("is_wet"), Tags.Biomes.IS_WET),
            legacyToCommon(Registries.f_256952_, forgeRl("is_wet/overworld"), Tags.Biomes.IS_WET_OVERWORLD),
            legacyToCommon(Registries.f_256952_, forgeRl("is_dry"), Tags.Biomes.IS_DRY),
            legacyToCommon(Registries.f_256952_, forgeRl("is_dry/overworld"), Tags.Biomes.IS_DRY_OVERWORLD),
            legacyToCommon(Registries.f_256952_, forgeRl("is_dry/nether"), Tags.Biomes.IS_DRY_NETHER),
            legacyToCommon(Registries.f_256952_, forgeRl("is_dry/end"), Tags.Biomes.IS_DRY_END),

            legacyToCommon(Registries.f_256952_, forgeRl("is_coniferous"), Tags.Biomes.IS_CONIFEROUS_TREE),

            legacyToCommon(Registries.f_256952_, forgeRl("is_mountain"), Tags.Biomes.IS_MOUNTAIN),
            legacyToCommon(Registries.f_256952_, forgeRl("is_peak"), Tags.Biomes.IS_MOUNTAIN_PEAK),
            legacyToCommon(Registries.f_256952_, forgeRl("is_slope"), Tags.Biomes.IS_MOUNTAIN_SLOPE),

            legacyToCommon(Registries.f_256952_, forgeRl("is_plains"), Tags.Biomes.IS_PLAINS),
            legacyToCommon(Registries.f_256952_, forgeRl("is_swamp"), Tags.Biomes.IS_SWAMP),
            legacyToCommon(Registries.f_256952_, forgeRl("is_desert"), Tags.Biomes.IS_DESERT),
            legacyToCommon(Registries.f_256952_, forgeRl("is_mushroom"), Tags.Biomes.IS_MUSHROOM),

            legacyToCommon(Registries.f_256952_, forgeRl("is_underground"), Tags.Biomes.IS_UNDERGROUND),
            legacyToCommon(Registries.f_256952_, forgeRl("is_cave"), Tags.Biomes.IS_CAVE),

            legacyToCommon(Registries.f_256952_, forgeRl("is_wasteland"), Tags.Biomes.IS_WASTELAND),
            legacyToCommon(Registries.f_256952_, forgeRl("is_dead"), Tags.Biomes.IS_DEAD),
            legacyToCommon(Registries.f_256952_, forgeRl("is_water"), Tags.Biomes.IS_AQUATIC),
            //endregion

            //region Unofficial
            // These tags have technically never been included with Forge in the past, but now that common convention
            // equivalents exist that Forge does include, these mappings have been added for convenience, just in case
            // some mods have been using them as a de-facto standard.

            // Workstations
            legacyToCommon(Registries.f_256747_, forgeRl("crafting_table"), Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES),
            legacyToCommon(Registries.f_256747_, forgeRl("crafting_tables"), Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES),
            legacyToCommon(Registries.f_256747_, forgeRl("furnace"), Tags.Blocks.PLAYER_WORKSTATIONS_FURNACES),
            legacyToCommon(Registries.f_256747_, forgeRl("furnaces"), Tags.Blocks.PLAYER_WORKSTATIONS_FURNACES),
            legacyToCommon(Registries.f_256747_, forgeRl("workbench"), Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES),
            legacyToCommon(Registries.f_256747_, forgeRl("workbenches"), Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES),

            legacyToCommon(Registries.f_256913_, forgeRl("crafting_table"), Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES),
            legacyToCommon(Registries.f_256913_, forgeRl("crafting_tables"), Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES),
            legacyToCommon(Registries.f_256913_, forgeRl("furnace"), Tags.Items.PLAYER_WORKSTATIONS_FURNACES),
            legacyToCommon(Registries.f_256913_, forgeRl("furnaces"), Tags.Items.PLAYER_WORKSTATIONS_FURNACES),
            legacyToCommon(Registries.f_256913_, forgeRl("workbench"), Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES),
            legacyToCommon(Registries.f_256913_, forgeRl("workbenches"), Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES),

            // Foods
            legacyToCommon(Registries.f_256913_, forgeRl("food"), Tags.Items.FOODS),
            legacyToCommon(Registries.f_256913_, forgeRl("foods"), Tags.Items.FOODS),
            legacyToCommon(Registries.f_256913_, forgeRl("fruit"), Tags.Items.FOODS_FRUIT),
            legacyToCommon(Registries.f_256913_, forgeRl("fruits"), Tags.Items.FOODS_FRUIT),
            legacyToCommon(Registries.f_256913_, forgeRl("vegetable"), Tags.Items.FOODS_VEGETABLE),
            legacyToCommon(Registries.f_256913_, forgeRl("vegetables"), Tags.Items.FOODS_VEGETABLE),
            legacyToCommon(Registries.f_256913_, forgeRl("berry"), Tags.Items.FOODS_BERRY),
            legacyToCommon(Registries.f_256913_, forgeRl("berries"), Tags.Items.FOODS_BERRY),
            legacyToCommon(Registries.f_256913_, forgeRl("bread"), Tags.Items.FOODS_BREAD),
            legacyToCommon(Registries.f_256913_, forgeRl("breads"), Tags.Items.FOODS_BREAD),
            legacyToCommon(Registries.f_256913_, forgeRl("cookie"), Tags.Items.FOODS_COOKIE),
            legacyToCommon(Registries.f_256913_, forgeRl("cookies"), Tags.Items.FOODS_COOKIE),
            legacyToCommon(Registries.f_256913_, forgeRl("raw_meat"), Tags.Items.FOODS_RAW_MEAT),
            legacyToCommon(Registries.f_256913_, forgeRl("raw_meats"), Tags.Items.FOODS_RAW_MEAT),
            legacyToCommon(Registries.f_256913_, forgeRl("raw_fish"), Tags.Items.FOODS_RAW_FISH),
            legacyToCommon(Registries.f_256913_, forgeRl("raw_fishes"), Tags.Items.FOODS_RAW_FISH),
            legacyToCommon(Registries.f_256913_, forgeRl("cooked_meat"), Tags.Items.FOODS_COOKED_MEAT),
            legacyToCommon(Registries.f_256913_, forgeRl("cooked_meats"), Tags.Items.FOODS_COOKED_MEAT),
            legacyToCommon(Registries.f_256913_, forgeRl("cooked_fish"), Tags.Items.FOODS_COOKED_FISH),
            legacyToCommon(Registries.f_256913_, forgeRl("cooked_fishes"), Tags.Items.FOODS_COOKED_FISH),
            legacyToCommon(Registries.f_256913_, forgeRl("soup"), Tags.Items.FOODS_SOUP),
            legacyToCommon(Registries.f_256913_, forgeRl("soups"), Tags.Items.FOODS_SOUP),
            legacyToCommon(Registries.f_256913_, forgeRl("stew"), Tags.Items.FOODS_SOUP),
            legacyToCommon(Registries.f_256913_, forgeRl("stews"), Tags.Items.FOODS_SOUP),
            legacyToCommon(Registries.f_256913_, forgeRl("candy"), Tags.Items.FOODS_CANDY),
            legacyToCommon(Registries.f_256913_, forgeRl("candies"), Tags.Items.FOODS_CANDY),

            // Fluids
            legacyToCommon(Registries.f_256808_, forgeRl("water"), Tags.Fluids.WATER),
            legacyToCommon(Registries.f_256808_, forgeRl("lava"), Tags.Fluids.LAVA),
            legacyToCommon(Registries.f_256808_, forgeRl("honey"), Tags.Fluids.HONEY),

            // Biomes
            legacyToCommon(Registries.f_256952_, forgeRl("is_overworld"), Tags.Biomes.IS_OVERWORLD),
            legacyToCommon(Registries.f_256952_, forgeRl("is_nether"), Tags.Biomes.IS_NETHER),
            legacyToCommon(Registries.f_256952_, forgeRl("is_nether_forest"), Tags.Biomes.IS_NETHER_FOREST),
            legacyToCommon(Registries.f_256952_, forgeRl("is_end"), Tags.Biomes.IS_END),

            legacyToCommon(Registries.f_256952_, forgeRl("is_forest"), Tags.Biomes.IS_FOREST),
            legacyToCommon(Registries.f_256952_, forgeRl("is_jungle"), Tags.Biomes.IS_JUNGLE),
            legacyToCommon(Registries.f_256952_, forgeRl("is_savanna"), Tags.Biomes.IS_SAVANNA),
            legacyToCommon(Registries.f_256952_, forgeRl("is_floral"), Tags.Biomes.IS_FLORAL),
            legacyToCommon(Registries.f_256952_, forgeRl("is_snowy"), Tags.Biomes.IS_SNOWY),
            legacyToCommon(Registries.f_256952_, forgeRl("is_icy"), Tags.Biomes.IS_ICY),
            legacyToCommon(Registries.f_256952_, forgeRl("is_ocean"), Tags.Biomes.IS_OCEAN)
            //endregion
    );

    /**
     * Creates a mapping from a legacy tag to a common convention equivalent.
     * <p>
     *     Example: {@code legacyToCommon(Registries.BLOCK, forgeRl("cobblestone"), Tags.Blocks.COBBLESTONES)} for {@code forge:cobblestone} -> {@code c:cobblestones}
     * </p>
     * @param registryKey Example: {@link net.minecraft.core.registries.Registries.BLOCK}
     * @param legacyTagResourceLocation Example: {@code forgeRl("cobblestone")}
     * @param replacementTag Example: {@link Tags.Blocks#COBBLESTONES}
     * @return A map entry with the legacy tag's TagKey and the suggested replacement tag ID.
     */
    private static <T, R> Map.Entry<TagKey<T>, String> legacyToCommon(ResourceKey<Registry<T>> registryKey,
                                                                      ResourceLocation legacyTagResourceLocation,
                                                                      TagKey<R> replacementTag) {
        return Map.entry(TagKey.m_203882_(registryKey, legacyTagResourceLocation), replacementTag.toString());
    }

    /**
     * Same as {@link #legacyToCommon(ResourceKey, ResourceLocation, TagKey)} but for multiple replacement tags.
     * This is useful when a single legacy tag is intended to be replaced by combining multiple common convention tags.
     */
    private static <T> Map.Entry<TagKey<T>, String> legacyToCommon(ResourceKey<Registry<T>> registryKey,
                                                                   ResourceLocation legacyTagRessourceLocation,
                                                                   List<TagKey<?>> replacementTags) {
        return Map.entry(
                TagKey.m_203882_(registryKey, legacyTagRessourceLocation),
                replacementTags.stream().map(TagKey::toString).collect(Collectors.joining(" and "))
        );
    }

    private static ResourceLocation forgeRl(String path) {
        return ResourceLocation.m_339182_("forge", path);
    }
}
