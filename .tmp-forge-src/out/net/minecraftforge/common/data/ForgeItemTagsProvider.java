/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@ApiStatus.Internal
public final class ForgeItemTagsProvider extends VanillaItemTagsProvider {
    public ForgeItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "forge", existingFileHelper);
    }

    @SuppressWarnings({ "unchecked", "removal" })
    @Override
    public void m_6577_(HolderLookup.Provider lookupProvider) {
        (new ForgeBlockItemTagsProvider() {
            @Override
            protected TagAppender<Block, Block> m_401961_(TagKey<Block> p_409856_, TagKey<Item> p_406371_) {
                return new VanillaItemTagsProvider.BlockToItemConverter(ForgeItemTagsProvider.this.m_255286_(p_406371_));
            }
        }).m_401405_();
        m_255286_(Tags.Items.BONES).m_402050_(Items.f_42500_);
        m_255286_(Tags.Items.BRICKS).addTags(Tags.Items.BRICKS_NORMAL, Tags.Items.BRICKS_NETHER);
        m_255286_(Tags.Items.BRICKS_NORMAL).m_402050_(Items.f_42460_);
        m_255286_(Tags.Items.BRICKS_NETHER).m_402050_(Items.f_42691_);
        m_255286_(Tags.Items.BUCKETS_EMPTY).m_402050_(Items.f_42446_);
        m_255286_(Tags.Items.BUCKETS_WATER).m_402050_(Items.f_42447_);
        m_255286_(Tags.Items.BUCKETS_LAVA).m_402050_(Items.f_42448_);
        m_255286_(Tags.Items.BUCKETS_MILK).m_402050_(Items.f_42455_);
        m_255286_(Tags.Items.BUCKETS_POWDER_SNOW).m_402050_(Items.f_151055_);
        m_255286_(Tags.Items.BUCKETS_ENTITY_WATER).m_401973_(Items.f_151057_, Items.f_42458_, Items.f_42456_, Items.f_220210_, Items.f_42459_, Items.f_42457_);
        m_255286_(Tags.Items.BUCKETS).addTags(Tags.Items.BUCKETS_EMPTY, Tags.Items.BUCKETS_WATER, Tags.Items.BUCKETS_LAVA, Tags.Items.BUCKETS_MILK, Tags.Items.BUCKETS_POWDER_SNOW, Tags.Items.BUCKETS_ENTITY_WATER);
        m_255286_(Tags.Items.CONCRETE_POWDERS)
                .m_401973_(Items.f_42315_, Items.f_42316_, Items.f_42317_,
                        Items.f_42318_, Items.f_42319_, Items.f_42320_,
                        Items.f_42321_, Items.f_42322_, Items.f_42323_,
                        Items.f_42324_, Items.f_42325_, Items.f_42326_,
                        Items.f_42327_, Items.f_42328_, Items.f_42277_,
                        Items.f_42278_);
        m_255286_(Tags.Items.CROPS).addTags(
                Tags.Items.CROPS_BEETROOT, Tags.Items.CROPS_CACTUS, Tags.Items.CROPS_CARROT,
                Tags.Items.CROPS_COCOA_BEAN, Tags.Items.CROPS_MELON, Tags.Items.CROPS_NETHER_WART,
                Tags.Items.CROPS_POTATO, Tags.Items.CROPS_PUMPKIN, Tags.Items.CROPS_SUGAR_CANE,
                Tags.Items.CROPS_WHEAT
        ).m_401692_(forgeItemTagKey("crops"));
        m_255286_(Tags.Items.CROPS_BEETROOT)
                .m_402050_(Items.f_42732_)
                .m_401692_(forgeItemTagKey("crops/beetroot"));
        m_255286_(Tags.Items.CROPS_CACTUS).m_402050_(Items.f_41982_);
        m_255286_(Tags.Items.CROPS_CARROT)
                .m_402050_(Items.f_42619_)
                .m_401692_(forgeItemTagKey("crops/carrot"));
        m_255286_(Tags.Items.CROPS_COCOA_BEAN).m_402050_(Items.f_42533_);
        m_255286_(Tags.Items.CROPS_MELON).m_402050_(Items.f_42028_);
        m_255286_(Tags.Items.CROPS_NETHER_WART)
                .m_402050_(Items.f_42588_)
                .m_401692_(forgeItemTagKey("crops/nether_wart"));
        m_255286_(Tags.Items.CROPS_POTATO)
                .m_402050_(Items.f_42620_)
                .m_401692_(forgeItemTagKey("crops/potato"));
        m_255286_(Tags.Items.CROPS_PUMPKIN).m_402050_(Items.f_42046_);
        m_255286_(Tags.Items.CROPS_SUGAR_CANE).m_402050_(Items.f_41909_);
        m_255286_(Tags.Items.CROPS_WHEAT)
                .m_402050_(Items.f_42405_)
                .m_401692_(forgeItemTagKey("crops/wheat"));
        addColored(Tags.Items.DYED, "{color}_banner");
        addColored(Tags.Items.DYED, "{color}_bed");
        addColored(Tags.Items.DYED, "{color}_candle");
        addColored(Tags.Items.DYED, "{color}_carpet");
        addColored(Tags.Items.DYED, "{color}_concrete");
        addColored(Tags.Items.DYED, "{color}_concrete_powder");
        addColored(Tags.Items.DYED, "{color}_glazed_terracotta");
        addColored(Tags.Items.DYED, "{color}_shulker_box");
        addColored(Tags.Items.DYED, "{color}_stained_glass");
        addColored(Tags.Items.DYED, "{color}_stained_glass_pane");
        addColored(Tags.Items.DYED, "{color}_terracotta");
        addColored(Tags.Items.DYED, "{color}_wool");
        addColoredTags(m_255286_(Tags.Items.DYED)::addTags, Tags.Items.DYED);
        m_255286_(Tags.Items.DUSTS).addTags(Tags.Items.DUSTS_GLOWSTONE, Tags.Items.DUSTS_REDSTONE);
        m_255286_(Tags.Items.DUSTS_GLOWSTONE)
                .m_402050_(Items.f_42525_)
                .m_401692_(forgeItemTagKey("dusts/glowstone"));
        m_255286_(Tags.Items.DUSTS_REDSTONE)
                .m_402050_(Items.f_42451_)
                .m_401692_(forgeItemTagKey("dusts/redstone"));
        addColored(Tags.Items.DYES, "{color}_dye");
        addColoredTags(m_255286_(Tags.Items.DYES)::addTags, Tags.Items.DYES);
        m_255286_(Tags.Items.EGGS).m_402050_(Items.f_42521_); // forge:eggs
        m_255286_(Tags.Items.ENCHANTING_FUELS).m_401951_(Tags.Items.GEMS_LAPIS); // forge:enchanting_fuels
        m_255286_(Tags.Items.ENDER_PEARLS)
                .m_402050_(Items.f_42584_)
                .m_401692_(forgeItemTagKey("ender_pearls"));
        m_255286_(Tags.Items.FEATHERS)
                .m_402050_(Items.f_42402_)
                .m_401692_(forgeItemTagKey("feathers"));
        m_255286_(Tags.Items.FERTILIZERS).m_402050_(Items.f_42499_);
        m_255286_(Tags.Items.FOODS_FRUIT).m_401973_(Items.f_42410_, Items.f_42436_, Items.f_42437_, Items.f_42730_, Items.f_42575_);
        m_255286_(Tags.Items.FOODS_VEGETABLE).m_401973_(Items.f_42619_, Items.f_42677_, Items.f_42620_, Items.f_42732_);
        m_255286_(Tags.Items.FOODS_BERRY).m_401973_(Items.f_42780_, Items.f_151079_);
        m_255286_(Tags.Items.FOODS_BREAD).m_402050_(Items.f_42406_);
        m_255286_(Tags.Items.FOODS_COOKIE).m_402050_(Items.f_42572_);
        m_255286_(Tags.Items.FOODS_RAW_MEAT).m_401973_(Items.f_42579_, Items.f_42485_, Items.f_42581_, Items.f_42697_, Items.f_42658_);
        m_255286_(Tags.Items.FOODS_RAW_FISH).m_401973_(Items.f_42526_, Items.f_42527_, Items.f_42528_, Items.f_42529_);
        m_255286_(Tags.Items.FOODS_COOKED_MEAT).m_401973_(Items.f_42580_, Items.f_42486_, Items.f_42582_, Items.f_42698_, Items.f_42659_);
        m_255286_(Tags.Items.FOODS_COOKED_FISH).m_401973_(Items.f_42530_, Items.f_42531_);
        m_255286_(Tags.Items.FOODS_SOUP).m_401973_(Items.f_42734_, Items.f_42400_, Items.f_42699_, Items.f_42718_);
        m_255286_(Tags.Items.FOODS_CANDY);
        m_255286_(Tags.Items.FOODS_PIE).m_402050_(Items.f_42687_).m_401692_(forgeItemTagKey("foods/pie"));
        m_255286_(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).m_402050_(Items.f_42502_);
        m_255286_(Tags.Items.FOODS_FOOD_POISONING).m_401973_(Items.f_42675_, Items.f_42529_, Items.f_42591_, Items.f_42581_, Items.f_42583_);
        m_255286_(Tags.Items.FOODS_GOLDEN).m_401973_(Items.f_42436_, Items.f_42437_, Items.f_42677_);
        m_255286_(Tags.Items.FOODS)
                .m_401973_(Items.f_42674_, Items.f_42787_, Items.f_316650_, Items.f_42576_)
                .addTags(Tags.Items.FOODS_FRUIT, Tags.Items.FOODS_VEGETABLE, Tags.Items.FOODS_BERRY, Tags.Items.FOODS_BREAD, Tags.Items.FOODS_COOKIE,
                        Tags.Items.FOODS_RAW_MEAT, Tags.Items.FOODS_RAW_FISH, Tags.Items.FOODS_COOKED_MEAT, Tags.Items.FOODS_COOKED_FISH,
                        Tags.Items.FOODS_SOUP, Tags.Items.FOODS_CANDY, Tags.Items.FOODS_PIE, Tags.Items.FOODS_GOLDEN,
                        Tags.Items.FOODS_EDIBLE_WHEN_PLACED, Tags.Items.FOODS_FOOD_POISONING);
        m_255286_(Tags.Items.ANIMAL_FOODS)
                .addTags(ItemTags.f_316663_, ItemTags.f_316264_, ItemTags.f_316276_, ItemTags.f_315451_,
                        ItemTags.f_314035_, ItemTags.f_316289_, ItemTags.f_315238_, ItemTags.f_144311_, ItemTags.f_314631_,
                        ItemTags.f_316983_, ItemTags.f_315349_, ItemTags.f_316234_, ItemTags.f_314714_, ItemTags.f_316292_,
                        ItemTags.f_316653_, ItemTags.f_316572_, ItemTags.f_314144_, ItemTags.f_144310_, ItemTags.f_314532_,
                        ItemTags.f_314146_, ItemTags.f_271449_, ItemTags.f_314410_, ItemTags.f_314831_, ItemTags.f_316425_);
        m_255286_(Tags.Items.GEMS)
                .addTags(Tags.Items.GEMS_AMETHYST, Tags.Items.GEMS_DIAMOND, Tags.Items.GEMS_EMERALD, Tags.Items.GEMS_LAPIS, Tags.Items.GEMS_PRISMARINE, Tags.Items.GEMS_QUARTZ)
                .m_401692_(forgeItemTagKey("gems"));
        m_255286_(Tags.Items.GEMS_AMETHYST)
                .m_402050_(Items.f_151049_)
                .m_401692_(forgeItemTagKey("gems/amethyst"));
        m_255286_(Tags.Items.GEMS_DIAMOND)
                .m_402050_(Items.f_42415_)
                .m_401692_(forgeItemTagKey("gems/diamond"));
        m_255286_(Tags.Items.GEMS_EMERALD)
                .m_402050_(Items.f_42616_)
                .m_401692_(forgeItemTagKey("gems/emerald"));
        m_255286_(Tags.Items.GEMS_LAPIS)
                .m_402050_(Items.f_42534_)
                .m_401692_(forgeItemTagKey("gems/lapis"));
        m_255286_(Tags.Items.GEMS_PRISMARINE)
                .m_402050_(Items.f_42696_)
                .m_401692_(forgeItemTagKey("gems/prismarine"));
        m_255286_(Tags.Items.GEMS_QUARTZ)
                .m_402050_(Items.f_42692_)
                .m_401692_(forgeItemTagKey("gems/quartz"));
        m_255286_(Tags.Items.GUNPOWDER).m_402050_(Items.f_42403_); // forge:gunpowder
        m_255286_(Tags.Items.HIDDEN_FROM_RECIPE_VIEWERS);
        m_255286_(Tags.Items.INGOTS)
                .addTags(Tags.Items.INGOTS_COPPER, Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_IRON, Tags.Items.INGOTS_NETHERITE);
                //.addOptionalTag(forgeItemTagKey("ingots")); // can't add because it would contain the contents of forge:ingots/brick and forge:ingots/nether_brick which are not in the c namespace
        m_255286_(Tags.Items.INGOTS_COPPER)
                .m_402050_(Items.f_151052_)
                .m_401692_(forgeItemTagKey("ingots/copper"));
        m_255286_(Tags.Items.INGOTS_GOLD)
                .m_402050_(Items.f_42417_)
                .m_401692_(forgeItemTagKey("ingots/gold"));
        m_255286_(Tags.Items.INGOTS_IRON)
                .m_402050_(Items.f_42416_)
                .m_401692_(forgeItemTagKey("ingots/iron"));
        m_255286_(Tags.Items.INGOTS_NETHERITE)
                .m_402050_(Items.f_42418_)
                .m_401692_(forgeItemTagKey("ingots/netherite"));
        m_255286_(Tags.Items.LEATHERS)
                .m_402050_(Items.f_42454_)
                .m_401692_(forgeItemTagKey("leather"));
        m_255286_(Tags.Items.MUSHROOMS)
                .m_401973_(Items.f_41952_, Items.f_41953_)
                .m_401692_(forgeItemTagKey("mushrooms"));
        m_255286_(Tags.Items.MUSIC_DISCS).m_401973_(Items.f_42752_, Items.f_42701_, Items.f_42702_, Items.f_42703_,
                Items.f_42704_, Items.f_42705_, Items.f_42706_, Items.f_42707_, Items.f_42708_,
                Items.f_42709_, Items.f_42710_, Items.f_42711_, Items.f_186363_, Items.f_220217_,
                Items.f_42712_, Items.f_283830_, Items.f_337043_, Items.f_337528_,
                Items.f_337210_);
        m_255286_(Tags.Items.NETHER_STARS)
                .m_402050_(Items.f_42686_)
                .m_401692_(forgeItemTagKey("nether_stars"));
        m_255286_(Tags.Items.NUGGETS)
                .addTags(Tags.Items.NUGGETS_GOLD, Tags.Items.NUGGETS_IRON)
                .m_401692_(forgeItemTagKey("nuggets"));
        m_255286_(Tags.Items.NUGGETS_IRON)
                .m_402050_(Items.f_42749_)
                .m_401692_(forgeItemTagKey("nuggets/iron"));
        m_255286_(Tags.Items.NUGGETS_GOLD)
                .m_402050_(Items.f_42587_)
                .m_401692_(forgeItemTagKey("nuggets/gold"));
        m_255286_(Tags.Items.RAW_MATERIALS)
                .addTags(Tags.Items.RAW_MATERIALS_COPPER, Tags.Items.RAW_MATERIALS_GOLD, Tags.Items.RAW_MATERIALS_IRON)
                .m_401692_(forgeItemTagKey("raw_materials"));
        m_255286_(Tags.Items.RAW_MATERIALS_COPPER)
                .m_402050_(Items.f_151051_)
                .m_401692_(forgeItemTagKey("raw_materials/copper"));
        m_255286_(Tags.Items.RAW_MATERIALS_GOLD)
                .m_402050_(Items.f_151053_)
                .m_401692_(forgeItemTagKey("raw_materials/gold"));
        m_255286_(Tags.Items.RAW_MATERIALS_IRON)
                .m_402050_(Items.f_151050_)
                .m_401692_(forgeItemTagKey("raw_materials/iron"));
        m_255286_(Tags.Items.RODS)
                .addTags(Tags.Items.RODS_WOODEN, Tags.Items.RODS_BLAZE, Tags.Items.RODS_BREEZE)
                .m_401692_(forgeItemTagKey("rods"));
        m_255286_(Tags.Items.RODS_BLAZE)
                .m_402050_(Items.f_42585_)
                .m_401692_(forgeItemTagKey("rods/blaze"));
        m_255286_(Tags.Items.RODS_BREEZE).m_402050_(Items.f_315544_);
        m_255286_(Tags.Items.RODS_WOODEN)
                .m_402050_(Items.f_42398_)
                .m_401692_(forgeItemTagKey("rods/wooden"));
        m_255286_(Tags.Items.SEEDS).addTags(Tags.Items.SEEDS_BEETROOT, Tags.Items.SEEDS_MELON, Tags.Items.SEEDS_PUMPKIN, Tags.Items.SEEDS_WHEAT);
        m_255286_(Tags.Items.SEEDS_BEETROOT).m_402050_(Items.f_42733_);
        m_255286_(Tags.Items.SEEDS_MELON).m_402050_(Items.f_42578_);
        m_255286_(Tags.Items.SEEDS_PUMPKIN).m_402050_(Items.f_42577_);
        m_255286_(Tags.Items.SEEDS_WHEAT).m_402050_(Items.f_42404_);
        m_255286_(Tags.Items.SLIME_BALLS)
                .m_402050_(Items.f_42518_)
                .m_401692_(forgeItemTagKey("slimeballs"));
        m_255286_(Tags.Items.SHULKER_BOXES)
                .m_401973_(Items.f_42265_, Items.f_42266_, Items.f_42267_,
                        Items.f_42268_, Items.f_42269_, Items.f_42270_,
                        Items.f_42271_, Items.f_42272_, Items.f_42273_,
                        Items.f_42274_, Items.f_42275_, Items.f_42224_,
                        Items.f_42225_, Items.f_42226_, Items.f_42227_,
                        Items.f_42228_, Items.f_42229_);
        m_255286_(Tags.Items.STRINGS)
                .m_402050_(Items.f_42401_)
                .m_401692_(forgeItemTagKey("strings"));
        m_255286_(Tags.Items.VILLAGER_JOB_SITES).m_401973_(
                Items.f_42768_, Items.f_42770_, Items.f_42543_, Items.f_42771_,
                Items.f_42544_, Items.f_42726_, Items.f_42772_, Items.f_42773_,
                Items.f_42774_, Items.f_42719_, Items.f_42775_, Items.f_42769_, Items.f_42776_);

        // Tools and Armors
        m_255286_(Tags.Items.TOOLS_SHIELD)
                .m_402050_(Items.f_42740_)
                .m_401692_(forgeItemTagKey("tools/shields"));
        m_255286_(Tags.Items.TOOLS_BOW)
                .m_402050_(Items.f_42411_)
                .m_401692_(forgeItemTagKey("tools/bows"));
        m_255286_(Tags.Items.TOOLS_BRUSH).m_402050_(Items.f_271356_);
        m_255286_(Tags.Items.TOOLS_CROSSBOW)
                .m_402050_(Items.f_42717_)
                .m_401692_(forgeItemTagKey("tools/crossbows"));
        m_255286_(Tags.Items.TOOLS_FISHING_ROD)
                .m_402050_(Items.f_42523_)
                .m_401692_(forgeItemTagKey("tools/fishing_rods"));
        m_255286_(Tags.Items.TOOLS_SHEAR)
                .m_402050_(Items.f_42574_)
                .m_401692_(forgeItemTagKey("tools/shears"));
        m_255286_(Tags.Items.TOOLS_SPEAR).m_402050_(Items.f_42713_);
        m_255286_(Tags.Items.TOOLS_MACE).m_402050_(Items.f_314862_);
        m_255286_(Tags.Items.TOOLS_IGNITER).m_402050_(Items.f_42409_);
        m_255286_(Tags.Items.MINING_TOOL_TOOLS).m_401973_(Items.f_42422_, Items.f_42427_, Items.f_42385_, Items.f_42432_, Items.f_42390_, Items.f_42395_);
        m_255286_(Tags.Items.MELEE_WEAPON_TOOLS).m_401973_(
                Items.f_314862_, Items.f_42713_,
                Items.f_42420_, Items.f_42425_, Items.f_42430_, Items.f_42383_, Items.f_42388_, Items.f_42393_,
                Items.f_42423_, Items.f_42428_, Items.f_42433_, Items.f_42386_, Items.f_42391_, Items.f_42396_
        );
        m_255286_(Tags.Items.RANGED_WEAPON_TOOLS).m_401973_(Items.f_42411_, Items.f_42717_, Items.f_42713_);
        m_255286_(Tags.Items.TOOLS)
                .addTags(ItemTags.f_271207_, ItemTags.f_271298_, ItemTags.f_271360_, ItemTags.f_271138_, ItemTags.f_271388_)
                .addTags(Tags.Items.TOOLS_BOW, Tags.Items.TOOLS_BRUSH, Tags.Items.TOOLS_CROSSBOW, Tags.Items.TOOLS_FISHING_ROD, Tags.Items.TOOLS_SHEAR, Tags.Items.TOOLS_IGNITER, Tags.Items.TOOLS_SHIELD, Tags.Items.TOOLS_SPEAR, Tags.Items.TOOLS_MACE, Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.RANGED_WEAPON_TOOLS);
        m_255286_(Tags.Items.ARMORS)
                .addTags(ItemTags.f_316976_, ItemTags.f_314764_, ItemTags.f_316741_, ItemTags.f_317094_)
                .m_401692_(forgeItemTagKey("armors"));
        m_255286_(Tags.Items.ENCHANTABLES).addTags(ItemTags.f_317078_, ItemTags.f_317097_, ItemTags.f_316107_, ItemTags.f_316261_, ItemTags.f_314984_, ItemTags.f_314570_, ItemTags.f_313995_, ItemTags.f_316827_, ItemTags.f_317054_, ItemTags.f_317092_, ItemTags.f_314471_, ItemTags.f_314461_, ItemTags.f_314809_, ItemTags.f_314986_);

        m_255286_(forgeItemTagKey("bones")).m_402050_(Items.f_42500_);
        // Backwards compat definitions for pre-1.21 legacy `forge:` tags.
        // TODO: Remove backwards compat tag entries in 1.22
        m_255286_(forgeItemTagKey("crops"))
                .addTags(forgeItemTagKey("crops/beetroot"), forgeItemTagKey("crops/carrot"), forgeItemTagKey("crops/nether_wart"),
                        forgeItemTagKey("crops/potato"), forgeItemTagKey("crops/wheat"));
        m_255286_(forgeItemTagKey("crops/beetroot")).m_402050_(Items.f_42732_);
        m_255286_(forgeItemTagKey("crops/carrot")).m_402050_(Items.f_42619_);
        m_255286_(forgeItemTagKey("crops/nether_wart")).m_402050_(Items.f_42588_);
        m_255286_(forgeItemTagKey("crops/potato")).m_402050_(Items.f_42620_);
        m_255286_(forgeItemTagKey("crops/wheat")).m_402050_(Items.f_42405_);
        m_255286_(forgeItemTagKey("foods/pie")).m_402050_(Items.f_42687_);
        m_255286_(forgeItemTagKey("dusts")).addTags(forgeItemTagKey("dusts/glowstone"), Tags.Items.DUSTS_PRISMARINE, forgeItemTagKey("dusts/redstone"));
        m_255286_(forgeItemTagKey("dusts/glowstone")).m_402050_(Items.f_42525_);
        m_255286_(forgeItemTagKey("dusts/prismarine")).m_402050_(Items.f_42695_);
        m_255286_(forgeItemTagKey("dusts/redstone")).m_402050_(Items.f_42451_);
        addColored(m_255286_(forgeItemTagKey("dyes"))::addTags, forgeItemTagKey("dyes"), "{color}_dye");
        m_255286_(forgeItemTagKey("eggs")).m_402050_(Items.f_42521_);
        m_255286_(forgeItemTagKey("enchanting_fuels")).m_401951_(forgeItemTagKey("gems/lapis"));
        m_255286_(forgeItemTagKey("ender_pearls")).m_402050_(Items.f_42584_);
        m_255286_(forgeItemTagKey("feathers")).m_402050_(Items.f_42402_);
        m_255286_(forgeItemTagKey("gems"))
                .addTags(forgeItemTagKey("gems/amethyst"), forgeItemTagKey("gems/diamond"), forgeItemTagKey("gems/emerald"),
                        forgeItemTagKey("gems/lapis"), forgeItemTagKey("gems/prismarine"), forgeItemTagKey("gems/quartz"));
        m_255286_(forgeItemTagKey("gems/amethyst")).m_402050_(Items.f_151049_);
        m_255286_(forgeItemTagKey("gems/diamond")).m_402050_(Items.f_42415_);
        m_255286_(forgeItemTagKey("gems/emerald")).m_402050_(Items.f_42616_);
        m_255286_(forgeItemTagKey("gems/lapis")).m_402050_(Items.f_42534_);
        m_255286_(forgeItemTagKey("gems/prismarine")).m_402050_(Items.f_42696_);
        m_255286_(forgeItemTagKey("gems/quartz")).m_402050_(Items.f_42692_);
        m_255286_(forgeItemTagKey("gunpowder")).m_402050_(Items.f_42403_);
        m_255286_(forgeItemTagKey("heads")).m_401973_(Items.f_42682_, Items.f_42683_, Items.f_42680_, Items.f_42678_, Items.f_42679_, Items.f_42681_);
        m_255286_(forgeItemTagKey("ingots"))
                .addTags(forgeItemTagKey("ingots/brick"), forgeItemTagKey("ingots/copper"), forgeItemTagKey("ingots/gold"),
                        forgeItemTagKey("ingots/iron"), forgeItemTagKey("ingots/netherite"), forgeItemTagKey("ingots/nether_brick"));
        m_255286_(forgeItemTagKey("ingots/brick")).m_402050_(Items.f_42460_);
        m_255286_(forgeItemTagKey("ingots/copper")).m_402050_(Items.f_151052_);
        m_255286_(forgeItemTagKey("ingots/gold")).m_402050_(Items.f_42417_);
        m_255286_(forgeItemTagKey("ingots/iron")).m_402050_(Items.f_42416_);
        m_255286_(forgeItemTagKey("ingots/netherite")).m_402050_(Items.f_42418_);
        m_255286_(forgeItemTagKey("ingots/nether_brick")).m_402050_(Items.f_42691_);
        m_255286_(forgeItemTagKey("leather")).m_402050_(Items.f_42454_);
        m_255286_(forgeItemTagKey("mushrooms")).m_401973_(Items.f_41952_, Items.f_41953_);
        m_255286_(forgeItemTagKey("nether_stars")).m_402050_(Items.f_42686_);
        m_255286_(forgeItemTagKey("nuggets")).addTags(forgeItemTagKey("nuggets/iron"), forgeItemTagKey("nuggets/gold"));
        m_255286_(forgeItemTagKey("nuggets/iron")).m_402050_(Items.f_42749_);
        m_255286_(forgeItemTagKey("nuggets/gold")).m_402050_(Items.f_42587_);
        m_255286_(forgeItemTagKey("raw_materials")).addTags(forgeItemTagKey("raw_materials/copper"), forgeItemTagKey("raw_materials/gold"), forgeItemTagKey("raw_materials/iron"));
        m_255286_(forgeItemTagKey("raw_materials/copper")).m_402050_(Items.f_151051_);
        m_255286_(forgeItemTagKey("raw_materials/gold")).m_402050_(Items.f_151053_);
        m_255286_(forgeItemTagKey("raw_materials/iron")).m_402050_(Items.f_151050_);
        m_255286_(forgeItemTagKey("rods")).addTags(forgeItemTagKey("rods/blaze"), forgeItemTagKey("rods/wooden"));
        m_255286_(forgeItemTagKey("rods/blaze")).m_402050_(Items.f_42585_);
        m_255286_(forgeItemTagKey("rods/wooden")).m_402050_(Items.f_42398_);
        m_255286_(Tags.Items.SEEDS).addTags(Tags.Items.SEEDS_BEETROOT, Tags.Items.SEEDS_MELON, Tags.Items.SEEDS_PUMPKIN, Tags.Items.SEEDS_WHEAT);
        m_255286_(Tags.Items.SEEDS_BEETROOT).m_402050_(Items.f_42733_);
        m_255286_(Tags.Items.SEEDS_MELON).m_402050_(Items.f_42578_);
        m_255286_(Tags.Items.SEEDS_PUMPKIN).m_402050_(Items.f_42577_);
        m_255286_(Tags.Items.SEEDS_WHEAT).m_402050_(Items.f_42404_);
        m_255286_(forgeItemTagKey("shears")).m_402050_(Items.f_42574_); // yes, it's forge:shears not forge:tools/shears
        m_255286_(forgeItemTagKey("slimeballs")).m_402050_(Items.f_42518_);
        m_255286_(forgeItemTagKey("string")).m_402050_(Items.f_42401_);
        m_255286_(forgeItemTagKey("tools/shields")).m_402050_(Items.f_42740_);
        m_255286_(forgeItemTagKey("tools/bows")).m_402050_(Items.f_42411_);
        m_255286_(forgeItemTagKey("tools/crossbows")).m_402050_(Items.f_42717_);
        m_255286_(forgeItemTagKey("tools/fishing_rods")).m_402050_(Items.f_42523_);
        m_255286_(forgeItemTagKey("tools/tridents")).m_402050_(Items.f_42713_);
        m_255286_(forgeItemTagKey("tools"))
                .addTags(ItemTags.f_271388_, ItemTags.f_271207_, ItemTags.f_271360_, ItemTags.f_271138_, ItemTags.f_271298_)
                .addTags(forgeItemTagKey("tools/shields"), forgeItemTagKey("tools/bows"), forgeItemTagKey("tools/crossbows"), forgeItemTagKey("tools/fishing_rods"), forgeItemTagKey("tools/tridents"));
        m_255286_(Tags.Items.ARMORS_HELMETS).m_401973_(Items.f_42407_, Items.f_42354_, Items.f_42464_, Items.f_42468_, Items.f_42476_, Items.f_42472_, Items.f_42480_);
        m_255286_(Tags.Items.ARMORS_CHESTPLATES).m_401973_(Items.f_42408_, Items.f_42465_, Items.f_42469_, Items.f_42477_, Items.f_42473_, Items.f_42481_);
        m_255286_(Tags.Items.ARMORS_LEGGINGS).m_401973_(Items.f_42462_, Items.f_42466_, Items.f_42470_, Items.f_42478_, Items.f_42474_, Items.f_42482_);
        m_255286_(Tags.Items.ARMORS_BOOTS).m_401973_(Items.f_42463_, Items.f_42467_, Items.f_42471_, Items.f_42479_, Items.f_42475_, Items.f_42483_);
        m_255286_(forgeItemTagKey("armors")).addTags(Tags.Items.ARMORS_HELMETS, Tags.Items.ARMORS_CHESTPLATES, Tags.Items.ARMORS_LEGGINGS, Tags.Items.ARMORS_BOOTS);
    }

    private void addColored(TagKey<Item> group, String pattern) {
        String prefix = group.f_203868_().m_135815_().toUpperCase(Locale.ENGLISH) + '_';
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation key = ResourceLocation.m_339182_("minecraft", pattern.replace("{color}", color.m_41065_()));
            TagKey<Item> tag = getForgeItemTag(prefix + color.m_41065_());
            Item item = BuiltInRegistries.f_257033_.m_122327_(key);
            if (item == null || item == Items.f_41852_)
                throw new IllegalStateException("Unknown vanilla item: " + key);
            m_255286_(tag).m_402050_(item);
        }
    }

    private void addColored(Consumer<TagKey<Item>> consumer, TagKey<Item> group, String pattern) {
        String prefix = group.f_203868_().m_135815_() + '/';
        for (DyeColor color  : DyeColor.values()) {
            ResourceLocation key = ResourceLocation.m_339182_("minecraft", pattern.replace("{color}",  color.m_41065_()));
            TagKey<Item> tag = forgeItemTagKey(prefix + color.m_41065_());
            Item item = ForgeRegistries.ITEMS.getValue(key);
            if (item == null || item  == Items.f_41852_)
                throw new IllegalStateException("Unknown vanilla item: " + key.toString());
            m_255286_(tag).m_402050_(item);
            consumer.accept(tag);
        }
    }

    private static void addColoredTags(Consumer<TagKey<Item>> consumer, TagKey<Item> group) {
        String prefix = group.f_203868_().m_135815_().toUpperCase(Locale.ENGLISH) + '_';
        for (DyeColor color : DyeColor.values()) {
            TagKey<Item> tag = getForgeItemTag(prefix + color.m_41065_());
            consumer.accept(tag);
        }
    }

    @SuppressWarnings("unchecked")
    private static TagKey<Item> getForgeItemTag(String name) {
        try {
            name = name.toUpperCase(Locale.ENGLISH);
            return (TagKey<Item>) Tags.Items.class.getDeclaredField(name).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(Tags.Items.class.getName() + " is missing tag name: " + name);
        }
    }

    private static ResourceLocation forgeRl(String path) {
        return ResourceLocation.m_339182_("forge", path);
    }

    private static TagKey<Item> forgeItemTagKey(String path) {
        return ItemTags.create(forgeRl(path));
    }

    @Override
    public String m_6055_() {
        return "Forge Item Tags";
    }
}
