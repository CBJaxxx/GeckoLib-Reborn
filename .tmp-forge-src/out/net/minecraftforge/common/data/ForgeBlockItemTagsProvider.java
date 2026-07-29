/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.data;

import java.util.Locale;
import java.util.function.Consumer;

import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ForgeBlockItemTagsProvider extends BlockItemTagsProvider {
    @Override
    @SuppressWarnings({ "unchecked", "removal" })
    protected void m_401405_() {
        m_401961_(Tags.Blocks.BARRELS, Tags.Items.BARRELS)
            .m_401951_(Tags.Blocks.BARRELS_WOODEN)
            .m_401692_(Legacy.Blocks.BARRELS);
        tag(Tags.Blocks.BARRELS_WOODEN, Tags.Items.BARRELS_WOODEN, Legacy.Blocks.BARRELS_WOODEN, Legacy.Items.BARRELS_WOODEN)
            .m_402050_(Blocks.f_50618_)
            .m_401692_(Legacy.Blocks.BARRELS_WOODEN);
        tag(Tags.Blocks.BOOKSHELVES, Tags.Items.BOOKSHELVES, Legacy.Blocks.BOOKSHELVES, Legacy.Items.BOOKSHELVES)
            .m_402050_(Blocks.f_50078_)
            .m_401692_(Legacy.Blocks.BOOKSHELVES);
        m_401961_(Tags.Blocks.BUDDING_BLOCKS, Tags.Items.BUDDING_BLOCKS)
            .m_402050_(Blocks.f_152491_);
        m_401961_(Tags.Blocks.BUDS, Tags.Items.BUDS)
            .m_402050_(Blocks.f_152495_)
            .m_402050_(Blocks.f_152494_)
            .m_402050_(Blocks.f_152493_);
        m_401961_(Tags.Blocks.CHAINS, Tags.Items.CHAINS)
            .m_402050_(Blocks.f_50184_);
        m_401961_(Tags.Blocks.CHESTS, Tags.Items.CHESTS)
            .addTags(
                Tags.Blocks.CHESTS_ENDER,
                Tags.Blocks.CHESTS_TRAPPED,
                Tags.Blocks.CHESTS_WOODEN
            )
            .m_401692_(Legacy.Blocks.CHESTS);
        //copy(forgeBlockTagKey("chests/ender"), forgeItemTagKey("chests/ender"));
        m_401961_(Tags.Blocks.CHESTS_ENDER, Tags.Items.CHESTS_ENDER)
            .m_402050_(Blocks.f_50265_); // forge:chests/ender
        //copy(forgeBlockTagKey("chests/trapped"), forgeItemTagKey("chests/trapped")));
        m_401961_(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED)
            .m_402050_(Blocks.f_50325_); // forge:chests/trapped
        tag(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN, Legacy.Blocks.CHESTS_WOODEN, Legacy.Items.CHESTS_WOODEN)
            .m_401973_(
                Blocks.f_50087_,
                Blocks.f_50325_
            )
            .m_401692_(Legacy.Blocks.CHESTS_WOODEN);
        m_401961_(Tags.Blocks.CLUSTERS, Tags.Items.CLUSTERS)
            .m_402050_(Blocks.f_152492_);
        tag(Tags.Blocks.COBBLESTONES, Tags.Items.COBBLESTONES, Legacy.Blocks.COBBLESTONE, Legacy.Items.COBBLESTONE)
            .addTags(
                Tags.Blocks.COBBLESTONE_NORMAL,
                Tags.Blocks.COBBLESTONE_INFESTED,
                Tags.Blocks.COBBLESTONE_MOSSY,
                Tags.Blocks.COBBLESTONE_DEEPSLATE
            )
            .m_401692_(Legacy.Blocks.COBBLESTONE);
        //copy(forgeBlockTagKey("cobblestone/normal"), forgeItemTagKey("cobblestone/normal"));
        m_401961_(Tags.Blocks.COBBLESTONE_NORMAL, Tags.Items.COBBLESTONE_NORMAL)
            .m_402050_(Blocks.f_50652_); // forge:cobblestone/normal
        //copy(forgeBlockTagKey("cobblestone/infested"), forgeItemTagKey("cobblestone/infested"));
        m_401961_(Tags.Blocks.COBBLESTONE_INFESTED, Tags.Items.COBBLESTONE_INFESTED)
            .m_402050_(Blocks.f_50227_); // forge:cobblestone/infested
        //copy(forgeBlockTagKey("cobblestone/mossy"), forgeItemTagKey("cobblestone/mossy"));
        m_401961_(Tags.Blocks.COBBLESTONE_MOSSY, Tags.Items.COBBLESTONE_MOSSY)
            .m_402050_(Blocks.f_50079_); // forge:cobblestone/mossy
        //copy(forgeBlockTagKey("cobblestone/deepslate"), forgeItemTagKey("cobblestone/deepslate"));
        m_401961_(Tags.Blocks.COBBLESTONE_DEEPSLATE, Tags.Items.COBBLESTONE_DEEPSLATE)
            .m_402050_(Blocks.f_152551_); // forge:cobblestone/deepslate
        m_401961_(Tags.Blocks.CONCRETES, Tags.Items.CONCRETES)
            .m_401973_(
                Blocks.f_50542_,
                Blocks.f_50543_,
                Blocks.f_50544_,
                Blocks.f_50545_,
                Blocks.f_50494_,
                Blocks.f_50495_,
                Blocks.f_50496_,
                Blocks.f_50497_,
                Blocks.f_50498_,
                Blocks.f_50499_,
                Blocks.f_50500_,
                Blocks.f_50501_,
                Blocks.f_50502_,
                Blocks.f_50503_,
                Blocks.f_50504_,
                Blocks.f_50505_
            );
        //copy(forgeBlockTagKey("end_stones"), forgeItemTagKey("end_stones"));
        m_401961_(Tags.Blocks.END_STONES, Tags.Items.END_STONES)
            .m_402050_(Blocks.f_50259_); // forge:end_stones
        //copy(forgeBlockTagKey("fence_gates"), forgeItemTagKey("fence_gates"));
        m_401961_(Tags.Blocks.FENCE_GATES, Tags.Items.FENCE_GATES)
            .addTags(Tags.Blocks.FENCE_GATES_WOODEN); // forge:fence_gates
        //copy(forgeBlockTagKey("fence_gates/wooden"), forgeItemTagKey("fence_gates/wooden"));
        m_401961_(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN)
            .m_401973_(
                Blocks.f_50192_,
                Blocks.f_50474_,
                Blocks.f_50475_,
                Blocks.f_50476_,
                Blocks.f_50477_,
                Blocks.f_50478_,
                Blocks.f_50665_,
                Blocks.f_50666_,
                Blocks.f_220850_,
                Blocks.f_244313_,
                Blocks.f_271274_
            );
        //copy(forgeBlockTagKey("fences"), forgeItemTagKey("fences"));
        m_401961_(Tags.Blocks.FENCES, Tags.Items.FENCES)
            .addTags(
                Tags.Blocks.FENCES_NETHER_BRICK,
                Tags.Blocks.FENCES_WOODEN
            ); // forge:fences
        //copy(forgeBlockTagKey("fences/nether_brick"), forgeItemTagKey("fences/nether_brick"));
        m_401961_(Tags.Blocks.FENCES_NETHER_BRICK, Tags.Items.FENCES_NETHER_BRICK)
            .m_402050_(Blocks.f_50198_); // forge:fences/nether_brick
        //copy(forgeBlockTagKey("fences/wooden"), forgeItemTagKey("fences/wooden"));
        m_401961_(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN)
            .m_401951_(BlockTags.f_13098_); // forge:fences/wooden
        m_401961_(Tags.Blocks.FLOWERS_SMALL, Tags.Items.FLOWERS_SMALL)
            .m_401973_(
                Blocks.f_50111_,
                Blocks.f_50112_,
                Blocks.f_50113_,
                Blocks.f_50114_,
                Blocks.f_50115_,
                Blocks.f_50116_,
                Blocks.f_50117_,
                Blocks.f_50118_,
                Blocks.f_50119_,
                Blocks.f_50120_,
                Blocks.f_50121_,
                Blocks.f_50071_,
                Blocks.f_50070_,
                Blocks.f_271329_,
                Blocks.f_370909_,
                Blocks.f_371390_
            )
            .m_401692_(BlockTags.f_13037_);
        m_401961_(Tags.Blocks.FLOWERS_TALL, Tags.Items.FLOWERS_TALL)
            .m_401973_(
                Blocks.f_50355_,
                Blocks.f_50356_,
                Blocks.f_50358_,
                Blocks.f_50357_,
                Blocks.f_276668_
            )
            .m_401692_(tagKey("tall_flowers")); //This is old vanilla tag, should it be removed?
        m_401961_(Tags.Blocks.FLOWERS, Tags.Items.FLOWERS)
            .m_401973_(
                Blocks.f_152471_,
                Blocks.f_152542_,
                Blocks.f_220831_,
                Blocks.f_271445_,
                Blocks.f_50491_,
                Blocks.f_152540_
            )
            .addTags(
                Tags.Blocks.FLOWERS_SMALL,
                Tags.Blocks.FLOWERS_TALL
            )
            .m_401692_(BlockTags.f_13041_);
        m_401961_(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS)
            .addTags(
                Tags.Blocks.GLASS_BLOCKS_COLORLESS,
                Tags.Blocks.GLASS_BLOCKS_CHEAP,
                Tags.Blocks.GLASS_BLOCKS_TINTED
            )
            .m_401692_(Tags.Blocks.GLASS);
        m_401961_(Tags.Blocks.GLASS_BLOCKS_COLORLESS, Tags.Items.GLASS_BLOCKS_COLORLESS)
            .m_402050_(Blocks.f_50058_)
            .m_401692_(Tags.Blocks.GLASS_COLORLESS);
        m_401961_(Tags.Blocks.GLASS_BLOCKS_TINTED, Tags.Items.GLASS_BLOCKS_TINTED)
            .m_402050_(Blocks.f_152498_)
            .m_401692_(Legacy.Blocks.GLASS_TINTED);

        tag(Tags.Blocks.GLASS_BLOCKS_CHEAP, Tags.Items.GLASS_BLOCKS_CHEAP, Legacy.Blocks.GLASS_SILICA, Legacy.Items.GLASS_SILICA)
            .m_401973_(
                Blocks.f_50058_,
                Blocks.f_50147_,
                Blocks.f_50148_,
                Blocks.f_50202_,
                Blocks.f_50203_,
                Blocks.f_50204_,
                Blocks.f_50205_,
                Blocks.f_50206_,
                Blocks.f_50207_,
                Blocks.f_50208_,
                Blocks.f_50209_,
                Blocks.f_50210_,
                Blocks.f_50211_,
                Blocks.f_50212_,
                Blocks.f_50213_,
                Blocks.f_50214_,
                Blocks.f_50215_
            )
            .m_401692_(Legacy.Blocks.GLASS_SILICA);
        m_401961_(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES)
            .addTags(Tags.Blocks.GLASS_PANES_COLORLESS)
            .m_401973_(
                Blocks.f_50303_,
                Blocks.f_50304_,
                Blocks.f_50305_,
                Blocks.f_50306_,
                Blocks.f_50307_,
                Blocks.f_50361_,
                Blocks.f_50362_,
                Blocks.f_50363_,
                Blocks.f_50364_,
                Blocks.f_50365_,
                Blocks.f_50366_,
                Blocks.f_50367_,
                Blocks.f_50368_,
                Blocks.f_50369_,
                Blocks.f_50370_,
                Blocks.f_50371_
            )
            .m_401692_(Legacy.Blocks.GLASS_PANES);
        m_401961_(Tags.Blocks.GLASS_PANES_COLORLESS, Tags.Items.GLASS_PANES_COLORLESS)
            .m_402050_(Blocks.f_50185_)
            .m_401692_(forgeTagKey("glass_panes/colorless"));
        m_401961_(Tags.Blocks.GLAZED_TERRACOTTAS, Tags.Items.GLAZED_TERRACOTTAS)
            .m_401973_(
                Blocks.f_50526_,
                Blocks.f_50527_,
                Blocks.f_50528_,
                Blocks.f_50529_,
                Blocks.f_50530_,
                Blocks.f_50531_,
                Blocks.f_50532_,
                Blocks.f_50533_,
                Blocks.f_50534_,
                Blocks.f_50535_,
                Blocks.f_50536_,
                Blocks.f_50537_,
                Blocks.f_50538_,
                Blocks.f_50539_,
                Blocks.f_50540_,
                Blocks.f_50541_
            );
        //copy(forgeBlockTagKey("gravel"), forgeItemTagKey("gravel"));
        m_401961_(Tags.Blocks.GRAVEL, Tags.Items.GRAVEL)
            .m_402050_(Blocks.f_49994_); // forge:gravel
        //copy(forgeBlockTagKey("netherrack"), forgeItemTagKey("netherrack"));
        m_401961_(Tags.Blocks.NETHERRACK, Tags.Items.NETHERRACK)
            .m_402050_(Blocks.f_50134_); // forge:netherrack
        m_401961_(Tags.Blocks.OBSIDIANS, Tags.Items.OBSIDIANS)
            .addTags(
                Tags.Blocks.OBSIDIANS_NORMAL,
                Tags.Blocks.OBSIDIANS_CRYING
            )
            .m_401692_(Legacy.Blocks.OBSIDIAN);
        m_401961_(Tags.Blocks.OBSIDIANS_NORMAL, Tags.Items.OBSIDIANS_NORMAL)
            .m_402050_(Blocks.f_50080_);
        m_401961_(Tags.Blocks.OBSIDIANS_CRYING, Tags.Items.OBSIDIANS_CRYING)
            .m_402050_(Blocks.f_50723_);
        m_401961_(Tags.Blocks.ORE_BEARING_GROUND_DEEPSLATE, Tags.Items.ORE_BEARING_GROUND_DEEPSLATE)
            .m_402050_(Blocks.f_152550_); // forge:ore_bearing_ground/deepslate
        m_401961_(Tags.Blocks.ORE_BEARING_GROUND_NETHERRACK, Tags.Items.ORE_BEARING_GROUND_NETHERRACK)
            .m_402050_(Blocks.f_50134_); // forge:ore_bearing_ground/netherrack
        m_401961_(Tags.Blocks.ORE_BEARING_GROUND_STONE, Tags.Items.ORE_BEARING_GROUND_STONE)
            .m_402050_(Blocks.f_50069_); // forge:ore_bearing_ground/stone
        m_401961_(Tags.Blocks.ORE_RATES_DENSE, Tags.Items.ORE_RATES_DENSE)
            .m_401973_(
                Blocks.f_152505_,
                Blocks.f_152506_,
                Blocks.f_152472_,
                Blocks.f_152473_,
                Blocks.f_50059_,
                Blocks.f_50173_
            );
        m_401961_(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR)
            .m_401973_(
                Blocks.f_50722_,
                Blocks.f_49997_,
                Blocks.f_152469_,
                Blocks.f_152474_,
                Blocks.f_152479_,
                Blocks.f_152467_,
                Blocks.f_152468_,
                Blocks.f_50089_,
                Blocks.f_50264_,
                Blocks.f_49995_,
                Blocks.f_49996_,
                Blocks.f_50331_
            );
        m_401961_(Tags.Blocks.ORE_RATES_SPARSE, Tags.Items.ORE_RATES_SPARSE)
            .m_402050_(Blocks.f_49998_);
        m_401961_(Tags.Blocks.ORES, Tags.Items.ORES)
            .addTags(
                Tags.Blocks.ORES_COAL,
                Tags.Blocks.ORES_COPPER,
                Tags.Blocks.ORES_DIAMOND,
                Tags.Blocks.ORES_EMERALD,
                Tags.Blocks.ORES_GOLD,
                Tags.Blocks.ORES_IRON,
                Tags.Blocks.ORES_LAPIS,
                Tags.Blocks.ORES_NETHERITE_SCRAP,
                Tags.Blocks.ORES_REDSTONE,
                Tags.Blocks.ORES_QUARTZ
            )
            .m_401692_(Legacy.Blocks.ORES);
        m_401961_(Tags.Blocks.ORES_COAL, Tags.Items.ORES_COAL)
            .m_401951_(BlockTags.f_144262_); // forge:ores/coal
        m_401961_(Tags.Blocks.ORES_COPPER, Tags.Items.ORES_COPPER)
            .m_401951_(BlockTags.f_144264_); // forge:ores/copper
        m_401961_(Tags.Blocks.ORES_DIAMOND, Tags.Items.ORES_DIAMOND)
            .m_401951_(BlockTags.f_144259_); // forge:ores/diamond
        m_401961_(Tags.Blocks.ORES_EMERALD, Tags.Items.ORES_EMERALD)
            .m_401951_(BlockTags.f_144263_); // forge:ores/emerald
        m_401961_(Tags.Blocks.ORES_GOLD, Tags.Items.ORES_GOLD)
            .m_401951_(BlockTags.f_13043_); // forge:ores/gold
        m_401961_(Tags.Blocks.ORES_IRON, Tags.Items.ORES_IRON)
            .m_401951_(BlockTags.f_144258_); // forge:ores/iron
        m_401961_(Tags.Blocks.ORES_LAPIS, Tags.Items.ORES_LAPIS)
            .m_401951_(BlockTags.f_144261_); // forge:ores/lapis
        tag(Tags.Blocks.ORES_QUARTZ, Tags.Items.ORES_QUARTZ, Legacy.Blocks.ORES_QUARTZ, Legacy.Items.QUARTZ_ORES)
            .m_402050_(Blocks.f_50331_)
            .m_401692_(Legacy.Blocks.ORES_QUARTZ);
        m_401961_(Tags.Blocks.ORES_REDSTONE, Tags.Items.ORES_REDSTONE)
            .m_401951_(BlockTags.f_144260_); // forge:ores/redstone
        tag(Tags.Blocks.ORES_NETHERITE_SCRAP, Tags.Items.ORES_NETHERITE_SCRAP, Legacy.Blocks.ORES_NETHERITE_SCRAP, Legacy.Items.ORES_NEHTERITE_SCRAP)
            .m_402050_(Blocks.f_50722_)
            .m_401692_(Legacy.Blocks.ORES_NETHERITE_SCRAP);
        m_401961_(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE)
            .m_401973_(
                Blocks.f_152469_,
                Blocks.f_152506_,
                Blocks.f_152474_,
                Blocks.f_152479_,
                Blocks.f_152467_,
                Blocks.f_152468_,
                Blocks.f_152472_,
                Blocks.f_152473_
            );
        m_401961_(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK)
            .m_401973_(
                Blocks.f_49998_,
                Blocks.f_50331_
            );
        m_401961_(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE)
            .m_401973_(
                Blocks.f_49997_,
                Blocks.f_152505_,
                Blocks.f_50089_,
                Blocks.f_50264_,
                Blocks.f_49995_,
                Blocks.f_49996_,
                Blocks.f_50059_,
                Blocks.f_50173_
            );
        m_401961_(Tags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES, Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
            .m_402050_(Blocks.f_50091_);
        m_401961_(Tags.Blocks.PLAYER_WORKSTATIONS_FURNACES, Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
            .m_402050_(Blocks.f_50094_);
        m_401961_(Tags.Blocks.PUMPKINS, Tags.Items.PUMPKINS)
            .addTags(
                Tags.Blocks.PUMPKINS_NORMAL,
                Tags.Blocks.PUMPKINS_CARVED,
                Tags.Blocks.PUMPKINS_JACK_O_LANTERNS
            );
        m_401961_(Tags.Blocks.PUMPKINS_NORMAL, Tags.Items.PUMPKINS_NORMAL)
            .m_402050_(Blocks.f_50133_);
        m_401961_(Tags.Blocks.PUMPKINS_CARVED, Tags.Items.PUMPKINS_CARVED)
            .m_402050_(Blocks.f_50143_);
        m_401961_(Tags.Blocks.PUMPKINS_JACK_O_LANTERNS, Tags.Items.PUMPKINS_JACK_O_LANTERNS)
            .m_402050_(Blocks.f_50144_);
        m_401961_(Tags.Blocks.ROPES, Tags.Items.ROPES);
        m_401961_(Tags.Blocks.SAND, Tags.Items.SAND) // forge:sand
            .addTags(
                Tags.Blocks.SAND_COLORLESS,
                Tags.Blocks.SAND_RED
            );
        m_401961_(Tags.Blocks.SAND_COLORLESS, Tags.Items.SAND_COLORLESS) // forge:sand/colorless
            .m_402050_(Blocks.f_49992_);
        m_401961_(Tags.Blocks.SAND_RED, Tags.Items.SAND_RED) // forge:sand/red
            .m_402050_(Blocks.f_49993_);
        m_401961_(Tags.Blocks.SANDSTONE_BLOCKS, Tags.Items.SANDSTONE_BLOCKS)
            .addTags(
                Tags.Blocks.SANDSTONE_RED_BLOCKS,
                Tags.Blocks.SANDSTONE_UNCOLORED_BLOCKS
            )
            .m_401692_(Legacy.Blocks.SANDSTONE);
        m_401961_(Tags.Blocks.SANDSTONE_SLABS, Tags.Items.SANDSTONE_SLABS)
            .addTags(
                Tags.Blocks.SANDSTONE_RED_SLABS,
                Tags.Blocks.SANDSTONE_UNCOLORED_SLABS
            );
        m_401961_(Tags.Blocks.SANDSTONE_STAIRS, Tags.Items.SANDSTONE_STAIRS)
            .addTags(
                Tags.Blocks.SANDSTONE_RED_STAIRS,
                Tags.Blocks.SANDSTONE_UNCOLORED_STAIRS
            );
        m_401961_(Tags.Blocks.SANDSTONE_RED_BLOCKS, Tags.Items.SANDSTONE_RED_BLOCKS)
            .m_401973_(
                Blocks.f_50394_,
                Blocks.f_50396_,
                Blocks.f_50395_,
                Blocks.f_50473_
            );
        m_401961_(Tags.Blocks.SANDSTONE_RED_SLABS, Tags.Items.SANDSTONE_RED_SLABS)
            .m_401973_(
                Blocks.f_50467_,
                Blocks.f_50468_,
                Blocks.f_50644_
            );
        m_401961_(Tags.Blocks.SANDSTONE_RED_STAIRS, Tags.Items.SANDSTONE_RED_STAIRS)
            .m_401973_(
                Blocks.f_50397_,
                Blocks.f_50630_
            );
        m_401961_(Tags.Blocks.SANDSTONE_UNCOLORED_BLOCKS, Tags.Items.SANDSTONE_UNCOLORED_BLOCKS)
            .m_401973_(
                Blocks.f_50062_,
                Blocks.f_50064_,
                Blocks.f_50063_,
                Blocks.f_50471_
            );
        m_401961_(Tags.Blocks.SANDSTONE_UNCOLORED_SLABS, Tags.Items.SANDSTONE_UNCOLORED_SLABS)
            .m_401973_(
                Blocks.f_50406_,
                Blocks.f_50407_,
                Blocks.f_50649_
            );
        m_401961_(Tags.Blocks.SANDSTONE_UNCOLORED_STAIRS, Tags.Items.SANDSTONE_UNCOLORED_STAIRS)
            .m_401973_(
                Blocks.f_50263_,
                Blocks.f_50636_
            );
        m_401961_(Tags.Blocks.STONES, Tags.Items.STONES)
            .m_401973_(
                Blocks.f_50334_,
                Blocks.f_50228_,
                Blocks.f_50122_,
                Blocks.f_50069_,
                Blocks.f_152550_,
                Blocks.f_152496_
            );
            //.addOptionalTag(forgeTagKey("stone")); // can't add this because it would include infested/polished variants which aren't contained in Fabric's `c:stones`
        m_401961_(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)
            .addTags(
                Tags.Blocks.STORAGE_BLOCKS_BONE_MEAL,
                Tags.Blocks.STORAGE_BLOCKS_COAL,
                Tags.Blocks.STORAGE_BLOCKS_COPPER,
                Tags.Blocks.STORAGE_BLOCKS_DIAMOND,
                Tags.Blocks.STORAGE_BLOCKS_DRIED_KELP,
                Tags.Blocks.STORAGE_BLOCKS_EMERALD,
                Tags.Blocks.STORAGE_BLOCKS_GOLD,
                Tags.Blocks.STORAGE_BLOCKS_IRON,
                Tags.Blocks.STORAGE_BLOCKS_LAPIS,
                Tags.Blocks.STORAGE_BLOCKS_NETHERITE,
                Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER,
                Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD,
                Tags.Blocks.STORAGE_BLOCKS_RAW_IRON,
                Tags.Blocks.STORAGE_BLOCKS_REDSTONE,
                Tags.Blocks.STORAGE_BLOCKS_SLIME,
                Tags.Blocks.STORAGE_BLOCKS_WHEAT
            )
            //.addOptionalTag(forgeTagKey("storage_blocks")); // can't add this because it would include contents from the non-common forge:storage_blocks/amethyst and forge:storage_blocks/quartz, which are not in the c namespace
            .addOptionalTags(
                Legacy.Blocks.STORAGE_BLOCKS_COAL,
                Legacy.Blocks.STORAGE_BLOCKS_COPPER,
                Legacy.Blocks.STORAGE_BLOCKS_DIAMOND,
                Legacy.Blocks.STORAGE_BLOCKS_EMERALD,
                Legacy.Blocks.STORAGE_BLOCKS_GOLD,
                Legacy.Blocks.STORAGE_BLOCKS_IRON,
                Legacy.Blocks.STORAGE_BLOCKS_LAPIS,
                Legacy.Blocks.STORAGE_BLOCKS_NETHERITE,
                Legacy.Blocks.STORAGE_BLOCKS_RAW_COPPER,
                Legacy.Blocks.STORAGE_BLOCKS_RAW_GOLD,
                Legacy.Blocks.STORAGE_BLOCKS_RAW_IRON,
                Legacy.Blocks.STORAGE_BLOCKS_REDSTONE
            );
        m_401961_(Tags.Blocks.STORAGE_BLOCKS_AMETHYST, Tags.Items.STORAGE_BLOCKS_AMETHYST)
            .m_402050_(Blocks.f_152490_);
        m_401961_(Tags.Blocks.STORAGE_BLOCKS_BONE_MEAL, Tags.Items.STORAGE_BLOCKS_BONE_MEAL)
            .m_402050_(Blocks.f_50453_);
        tag(Tags.Blocks.STORAGE_BLOCKS_COAL, Tags.Items.STORAGE_BLOCKS_COAL, Legacy.Blocks.STORAGE_BLOCKS_COAL, Legacy.Items.STORAGE_BLOCKS_COAL)
            .m_402050_(Blocks.f_50353_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_COAL);
        tag(Tags.Blocks.STORAGE_BLOCKS_COPPER, Tags.Items.STORAGE_BLOCKS_COPPER, Legacy.Blocks.STORAGE_BLOCKS_COPPER, Legacy.Items.STORAGE_BLOCKS_COPPER)
            .m_402050_(Blocks.f_152504_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_COPPER);
        tag(Tags.Blocks.STORAGE_BLOCKS_DIAMOND, Tags.Items.STORAGE_BLOCKS_DIAMOND, Legacy.Blocks.STORAGE_BLOCKS_DIAMOND, Legacy.Items.STORAGE_BLOCKS_DIAMOND)
            .m_402050_(Blocks.f_50090_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_DIAMOND);
        m_401961_(Tags.Blocks.STORAGE_BLOCKS_DRIED_KELP, Tags.Items.STORAGE_BLOCKS_DRIED_KELP)
            .m_402050_(Blocks.f_50577_);
        tag(Tags.Blocks.STORAGE_BLOCKS_EMERALD, Tags.Items.STORAGE_BLOCKS_EMERALD, Legacy.Blocks.STORAGE_BLOCKS_EMERALD, Legacy.Items.STORAGE_BLOCKS_EMERALD)
            .m_402050_(Blocks.f_50268_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_EMERALD);
        tag(Tags.Blocks.STORAGE_BLOCKS_GOLD, Tags.Items.STORAGE_BLOCKS_GOLD, Legacy.Blocks.STORAGE_BLOCKS_GOLD, Legacy.Items.STORAGE_BLOCKS_GOLD)
            .m_402050_(Blocks.f_50074_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_GOLD);
        tag(Tags.Blocks.STORAGE_BLOCKS_IRON, Tags.Items.STORAGE_BLOCKS_IRON, Legacy.Blocks.STORAGE_BLOCKS_IRON, Legacy.Items.STORAGE_BLOCKS_IRON)
            .m_402050_(Blocks.f_50075_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_IRON);
        tag(Tags.Blocks.STORAGE_BLOCKS_LAPIS, Tags.Items.STORAGE_BLOCKS_LAPIS, Legacy.Blocks.STORAGE_BLOCKS_LAPIS, Legacy.Items.STORAGE_BLOCKS_LAPIS)
            .m_402050_(Blocks.f_50060_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_LAPIS);
        tag(Tags.Blocks.STORAGE_BLOCKS_NETHERITE, Tags.Items.STORAGE_BLOCKS_NETHERITE, Legacy.Blocks.STORAGE_BLOCKS_NETHERITE, Legacy.Items.STORAGE_BLOCKS_NETHERITE)
            .m_402050_(Blocks.f_50721_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_NETHERITE);
        m_401961_(Tags.Blocks.STORAGE_BLOCKS_QUARTZ, Tags.Items.STORAGE_BLOCKS_QUARTZ)
            .m_402050_(Blocks.f_50333_);
        tag(Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER, Tags.Items.STORAGE_BLOCKS_RAW_COPPER, Legacy.Blocks.STORAGE_BLOCKS_RAW_COPPER, Legacy.Items.STORAGE_BLOCKS_RAW_COPPER)
            .m_402050_(Blocks.f_152599_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_RAW_COPPER);
        tag(Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD, Tags.Items.STORAGE_BLOCKS_RAW_GOLD, Legacy.Blocks.STORAGE_BLOCKS_RAW_GOLD, Legacy.Items.STORAGE_BLOCKS_RAW_GOLD)
            .m_402050_(Blocks.f_152600_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_RAW_GOLD);
        tag(Tags.Blocks.STORAGE_BLOCKS_RAW_IRON, Tags.Items.STORAGE_BLOCKS_RAW_IRON, Legacy.Blocks.STORAGE_BLOCKS_RAW_IRON, Legacy.Items.STORAGE_BLOCKS_RAW_IRON)
            .m_402050_(Blocks.f_152598_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_RAW_IRON);
        tag(Tags.Blocks.STORAGE_BLOCKS_REDSTONE, Tags.Items.STORAGE_BLOCKS_REDSTONE, Legacy.Blocks.STORAGE_BLOCKS_REDSTONE, Legacy.Items.STORAGE_BLOCKS_REDSTONE)
            .m_402050_(Blocks.f_50330_)
            .m_401692_(Legacy.Blocks.STORAGE_BLOCKS_REDSTONE);
        m_401961_(Tags.Blocks.STORAGE_BLOCKS_SLIME, Tags.Items.STORAGE_BLOCKS_SLIME)
            .m_402050_(Blocks.f_50374_);
        m_401961_(Tags.Blocks.STORAGE_BLOCKS_WHEAT, Tags.Items.STORAGE_BLOCKS_WHEAT)
            .m_402050_(Blocks.f_50335_);
        m_401961_(Tags.Blocks.STRIPPED_LOGS, Tags.Items.STRIPPED_LOGS)
            .m_401973_(
                Blocks.f_50008_,
                Blocks.f_256740_,
                Blocks.f_50006_,
                Blocks.f_271326_,
                Blocks.f_50009_,
                Blocks.f_50007_,
                Blocks.f_220835_,
                Blocks.f_50010_,
                Blocks.f_50005_
            );
        m_401961_(Tags.Blocks.STRIPPED_WOODS, Tags.Items.STRIPPED_WOODS)
            .m_401973_(
                Blocks.f_50048_,
                Blocks.f_50046_,
                Blocks.f_271145_,
                Blocks.f_50049_,
                Blocks.f_50047_,
                Blocks.f_220837_,
                Blocks.f_50044_,
                Blocks.f_50045_
            );

        // Backwards compat definitions for pre-1.21 legacy `forge:` tags.
        // TODO: [Forge][Tags][Old] Remove backwards compat tag entries in 1.22
        m_401961_(Legacy.Blocks.BARRELS, Legacy.Items.BARRELS)
            .m_401951_(Legacy.Blocks.BARRELS_WOODEN);

        m_401961_(Legacy.Blocks.CHESTS, Legacy.Items.CHESTS)
            .addTags(
                Tags.Blocks.CHESTS_ENDER,
                Tags.Blocks.CHESTS_TRAPPED,
                Legacy.Blocks.CHESTS_WOODEN
            );

        //copy(forgeBlockTagKey("glass/tinted"), forgeItemTagKey("glass/tinted"));
        addColored(m_401961_(Tags.Blocks.STAINED_GLASS, Tags.Items.STAINED_GLASS)::m_402050_, Tags.Blocks.GLASS, "{color}_stained_glass");
        addColored(m_401961_(Tags.Blocks.STAINED_GLASS_PANES, Tags.Items.STAINED_GLASS_PANES)::m_402050_, Tags.Blocks.GLASS_PANES, "{color}_stained_glass_pane");

        // tag(Tags.Blocks.GLASS, Tags.Items.GLASS, forgeBlockTagKey("glass"), forgeItemTagKey("glass")) // these are the same thing
        m_401961_(Tags.Blocks.GLASS, Tags.Items.GLASS)
            .addTags(
                Tags.Blocks.GLASS_COLORLESS,
                Tags.Blocks.STAINED_GLASS,
                Legacy.Blocks.GLASS_TINTED
            );
        m_401961_(Tags.Blocks.GLASS_COLORLESS, Tags.Items.GLASS_COLORLESS)
            .m_402050_(Blocks.f_50058_);
        m_401961_(Legacy.Blocks.GLASS_PANES, Legacy.Items.GLASS_PANES)
            .addTags(
                Legacy.Blocks.GLASS_PANES_COLORLESS,
                Tags.Blocks.STAINED_GLASS_PANES
            );
        m_401961_(Legacy.Blocks.GLASS_PANES_COLORLESS, Legacy.Items.GLASS_PANES_COLORLESS)
            .m_402050_(Blocks.f_50185_);
        m_401961_(Legacy.Blocks.GLASS_TINTED, Legacy.Items.GLASS_TINTED)
            .m_402050_(Blocks.f_152498_);
        m_401961_(Legacy.Blocks.OBSIDIAN, Legacy.Items.OBSIDIAN)
            .m_402050_(Blocks.f_50080_);
        m_401961_(Legacy.Blocks.ORES, Legacy.Items.ORES)
            .addTags(
                Tags.Blocks.ORES_COAL,
                Tags.Blocks.ORES_COPPER,
                Tags.Blocks.ORES_DIAMOND,
                Tags.Blocks.ORES_EMERALD,
                Tags.Blocks.ORES_GOLD,
                Tags.Blocks.ORES_IRON,
                Tags.Blocks.ORES_LAPIS,
                Tags.Blocks.ORES_REDSTONE,
                Legacy.Blocks.ORES_QUARTZ,
                Legacy.Blocks.ORES_NETHERITE_SCRAP
            );
        m_401961_(Legacy.Blocks.SANDSTONE, Legacy.Items.SANDSTONE)
            .m_401973_(
                Blocks.f_50062_,
                Blocks.f_50064_,
                Blocks.f_50063_,
                Blocks.f_50471_,
                Blocks.f_50394_,
                Blocks.f_50396_,
                Blocks.f_50395_,
                Blocks.f_50473_
            );
        m_401961_(Legacy.Blocks.STONE, Legacy.Items.STONE)
            .m_401973_(
                Blocks.f_50334_,
                Blocks.f_50228_,
                Blocks.f_50122_,
                Blocks.f_50226_,
                Blocks.f_50069_,
                Blocks.f_50387_,
                Blocks.f_50281_,
                Blocks.f_50175_,
                Blocks.f_152550_,
                Blocks.f_152555_,
                Blocks.f_152596_,
                Blocks.f_152496_
            );

        m_401961_(Legacy.Blocks.STORAGE_BLOCKS, Legacy.Items.STORAGE_BLOCKS)
            .addTags(
                Tags.Blocks.STORAGE_BLOCKS_AMETHYST,
                Legacy.Blocks.STORAGE_BLOCKS_COAL,
                Legacy.Blocks.STORAGE_BLOCKS_COPPER,
                Legacy.Blocks.STORAGE_BLOCKS_DIAMOND,
                Legacy.Blocks.STORAGE_BLOCKS_EMERALD,
                Legacy.Blocks.STORAGE_BLOCKS_GOLD,
                Legacy.Blocks.STORAGE_BLOCKS_IRON,
                Legacy.Blocks.STORAGE_BLOCKS_LAPIS,
                Tags.Blocks.STORAGE_BLOCKS_QUARTZ,
                Legacy.Blocks.STORAGE_BLOCKS_RAW_COPPER,
                Legacy.Blocks.STORAGE_BLOCKS_RAW_GOLD,
                Legacy.Blocks.STORAGE_BLOCKS_RAW_IRON,
                Legacy.Blocks.STORAGE_BLOCKS_REDSTONE,
                Legacy.Blocks.STORAGE_BLOCKS_NETHERITE
            );
    }

    private static TagKey<Block> forgeTagKey(String path) {
        return BlockTags.create(ResourceLocation.m_339182_("forge", path));
    }

    private static TagKey<Block> tagKey(String name) {
        return BlockTags.create(ResourceLocation.m_340282_(name));
    }

    private void addColored(Consumer<Block> consumer, TagKey<Block> group, String pattern) {
        String prefix = group.f_203868_().m_135815_().toUpperCase(Locale.ENGLISH) + '_';
        for (DyeColor color  : DyeColor.values()) {
            ResourceLocation key = ResourceLocation.m_339182_("minecraft", pattern.replace("{color}",  color.m_41065_()));
            TagKey<Block> blockTag = getForgeTag(Tags.Blocks.class, prefix + color.m_41065_());
            TagKey<Item> itemTag = getForgeTag(Tags.Items.class, prefix + color.m_41065_());
            Block block = ForgeRegistries.BLOCKS.getValue(key);
            if (block == null || block  == Blocks.f_50016_)
                throw new IllegalStateException("Unknown vanilla block: " + key.toString());
            m_401961_(blockTag, itemTag).m_402050_(block);
            consumer.accept(block);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> TagKey<T> getForgeTag(Class<?> cls, String name) {
        try {
            name = name.toUpperCase(Locale.ENGLISH);
            return (TagKey<T>)cls.getDeclaredField(name).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(cls.getName() + " is missing tag name: " + name);
        }
    }

    private static ResourceLocation forgeRl(String path) {
        return ResourceLocation.m_339182_("forge", path);
    }

    private TagAppender<Block, Block> tag(TagKey<Block> block, TagKey<Item> item, TagKey<Block> oldBlock, TagKey<Item> oldItem) {
        var tag = m_401961_(block, item);
        var old = m_401961_(oldBlock, oldItem);
        return wrap(tag, old, oldBlock);
    }

    private static TagAppender<Block, Block> wrap(TagAppender<Block, Block> tag, TagAppender<Block, Block> old, TagKey<Block> oldBlock) {
        return new TagAppender<Block, Block>() {
            @Override
            public TagAppender<Block, Block> m_402050_(Block value) {
                tag.m_402050_(value);
                old.m_402050_(value);
                return this;
            }

            @Override
            public TagAppender<Block, Block> m_401650_(Block value) {
                tag.m_401650_(value);
                old.m_401650_(value);
                return this;
            }

            @Override
            public TagAppender<Block, Block> m_401951_(TagKey<Block> value) {
                tag.m_401951_(value);
                old.m_401951_(value);
                return this;
            }

            @Override
            public TagAppender<Block, Block> m_401692_(TagKey<Block> value) {
                tag.m_401692_(value);
                if (value != oldBlock)
                    old.m_401692_(value);
                return this;
            }

            @Override
            public TagAppender<Block, Block> replace(boolean value) {
                tag.replace(value);
                old.replace(value);
                return this;
            }

            @Override
            public TagAppender<Block, Block> remove(ResourceLocation value) {
                tag.remove(value);
                old.remove(value);
                return this;
            }

            @Override
            public TagAppender<Block, Block> remove(TagKey<Block> value) {
                tag.remove(value);
                old.remove(value);
                return this;
            }

            @Override
            public TagAppender<Block, Block> remove(Block value) {
                tag.remove(value);
                old.remove(value);
                return this;
            }

            @Override
            public String getSourceName() {
                return tag.getSourceName();
            }
        };
    }

    private static class Legacy {
        @Deprecated
        private static class Blocks {
            private static TagKey<Block> tag(String path) {
                return BlockTags.create(forgeRl(path));
            }

            public static final TagKey<Block> BARRELS = tag("barrels");
            public static final TagKey<Block> BARRELS_WOODEN = tag("barrels/wooden");
            public static final TagKey<Block> BOOKSHELVES = tag("bookshelves");
            public static final TagKey<Block> CHESTS = tag("chests");
            public static final TagKey<Block> CHESTS_WOODEN = tag("chests/wooden");
            public static final TagKey<Block> COBBLESTONE = tag("cobblestone");
            public static final TagKey<Block> GLASS_PANES = tag("glass_panes");
            public static final TagKey<Block> GLASS_PANES_COLORLESS = tag("glass_panes/colorless");
            public static final TagKey<Block> GLASS_SILICA = tag("glass/silica");
            public static final TagKey<Block> GLASS_TINTED = tag("glass/tinted");
            public static final TagKey<Block> OBSIDIAN = tag("obsidian");
            public static final TagKey<Block> ORES = tag("ores");
            public static final TagKey<Block> ORES_QUARTZ = tag("ores/quartz");
            public static final TagKey<Block> ORES_NETHERITE_SCRAP = tag("ores/netherite_scrap");
            public static final TagKey<Block> SANDSTONE = tag("sandstone");
            public static final TagKey<Block> STONE = tag("stone");
            public static final TagKey<Block> STORAGE_BLOCKS = tag("storage_blocks");
            public static final TagKey<Block> STORAGE_BLOCKS_COAL = tag("storage_blocks/coal");
            public static final TagKey<Block> STORAGE_BLOCKS_COPPER = tag("storage_blocks/copper");
            public static final TagKey<Block> STORAGE_BLOCKS_DIAMOND = tag("storage_blocks/diamond");
            public static final TagKey<Block> STORAGE_BLOCKS_EMERALD = tag("storage_blocks/emerald");
            public static final TagKey<Block> STORAGE_BLOCKS_GOLD = tag("storage_blocks/gold");
            public static final TagKey<Block> STORAGE_BLOCKS_IRON = tag("storage_blocks/iron");
            public static final TagKey<Block> STORAGE_BLOCKS_LAPIS = tag("storage_blocks/lapis");
            public static final TagKey<Block> STORAGE_BLOCKS_NETHERITE = tag("storage_blocks/netherite");
            public static final TagKey<Block> STORAGE_BLOCKS_RAW_COPPER = tag("storage_blocks/raw_copper");
            public static final TagKey<Block> STORAGE_BLOCKS_RAW_GOLD = tag("storage_blocks/raw_gold");
            public static final TagKey<Block> STORAGE_BLOCKS_RAW_IRON = tag("storage_blocks/raw_iron");
            public static final TagKey<Block> STORAGE_BLOCKS_REDSTONE = tag("storage_blocks/redstone");
        }

        @Deprecated
        private static class Items {
            private static TagKey<Item> tag(String path) {
                return ItemTags.create(forgeRl(path));
            }

            public static final TagKey<Item> BARRELS = tag("barrels");
            public static final TagKey<Item> BARRELS_WOODEN = tag("barrels/wooden");
            public static final TagKey<Item> BOOKSHELVES = tag("bookshelves");
            public static final TagKey<Item> CHESTS = tag("chests");
            public static final TagKey<Item> CHESTS_WOODEN = tag("chests/wooden");
            public static final TagKey<Item> COBBLESTONE = tag("cobblestone");
            public static final TagKey<Item> GLASS_PANES = tag("glass_panes");
            public static final TagKey<Item> GLASS_PANES_COLORLESS = tag("glass_panes/colorless");
            public static final TagKey<Item> GLASS_SILICA = tag("glass/silica");
            public static final TagKey<Item> GLASS_TINTED = tag("glass/tinted");
            public static final TagKey<Item> OBSIDIAN = tag("obsidian");
            public static final TagKey<Item> ORES = tag("ores");
            public static final TagKey<Item> QUARTZ_ORES = tag("ores/quartz");
            public static final TagKey<Item> ORES_NEHTERITE_SCRAP = tag("ores/netherite_scrap");
            public static final TagKey<Item> SANDSTONE = tag("sandstone");
            public static final TagKey<Item> STONE = tag("stone");
            public static final TagKey<Item> STORAGE_BLOCKS = tag("storage_blocks");
            public static final TagKey<Item> STORAGE_BLOCKS_COAL = tag("storage_blocks/coal");
            public static final TagKey<Item> STORAGE_BLOCKS_COPPER = tag("storage_blocks/copper");
            public static final TagKey<Item> STORAGE_BLOCKS_DIAMOND = tag("storage_blocks/diamond");
            public static final TagKey<Item> STORAGE_BLOCKS_EMERALD = tag("storage_blocks/emerald");
            public static final TagKey<Item> STORAGE_BLOCKS_GOLD = tag("storage_blocks/gold");
            public static final TagKey<Item> STORAGE_BLOCKS_IRON = tag("storage_blocks/iron");
            public static final TagKey<Item> STORAGE_BLOCKS_LAPIS = tag("storage_blocks/lapis");
            public static final TagKey<Item> STORAGE_BLOCKS_NETHERITE = tag("storage_blocks/netherite");
            public static final TagKey<Item> STORAGE_BLOCKS_RAW_COPPER = tag("storage_blocks/raw_copper");
            public static final TagKey<Item> STORAGE_BLOCKS_RAW_GOLD = tag("storage_blocks/raw_gold");
            public static final TagKey<Item> STORAGE_BLOCKS_RAW_IRON = tag("storage_blocks/raw_iron");
            public static final TagKey<Item> STORAGE_BLOCKS_REDSTONE = tag("storage_blocks/redstone");
        }
    }
}
