/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.data;

import com.google.gson.JsonElement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.unsafe.UnsafeFieldAccess;
import net.minecraftforge.unsafe.UnsafeHacks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ForgeRecipeProvider extends VanillaRecipeProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private final RegistryLookup<Item> items;
    private final Map<Item, TagKey<Item>> replacements = HashMap.newHashMap(12);
    private final Set<ResourceKey<Recipe<?>>> excludes = HashSet.newHashSet(16);
    private final UnsafeFieldAccess<ShapelessRecipe, List<Ingredient>> INGREDIENTS = UnsafeHacks.findField(ShapelessRecipe.class, "ingredients");
    private final UnsafeFieldAccess<ShapedRecipe, ShapedRecipePattern> PATTERN = UnsafeHacks.findField(ShapedRecipe.class, "pattern");
    private final UnsafeFieldAccess<Ingredient, HolderSet<Item>> VALUES = UnsafeHacks.findField(Ingredient.class, "values");

    private ForgeRecipeProvider(HolderLookup.Provider lookup, RecipeOutput output) {
        super(lookup, new Wrapped(output));
        ((Wrapped)this.f_349511_).setSelf(this);
        this.items = lookup.m_254974_(Registries.f_256913_);
    }

    private void exclude(ItemLike item) {
        exclude(BuiltInRegistries.f_257033_.m_7981_(item.m_5456_()).toString());
    }

    private void exclude(String name) {
        excludes.add(ResourceKey.m_135785_(Registries.f_337225_, ResourceLocation.m_338530_(name)));
    }

    private void replace(ItemLike item, TagKey<Item> tag) {
        replacements.put(item.m_5456_(), tag);
    }

    @Override
    protected void m_246343_() {
        replace(Items.f_42398_, Tags.Items.RODS_WOODEN);
        replace(Items.f_42417_, Tags.Items.INGOTS_GOLD);
        replace(Items.f_42416_, Tags.Items.INGOTS_IRON);
        replace(Items.f_42418_, Tags.Items.INGOTS_NETHERITE);
        replace(Items.f_151052_, Tags.Items.INGOTS_COPPER);
        replace(Items.f_151049_, Tags.Items.GEMS_AMETHYST);
        replace(Items.f_42415_, Tags.Items.GEMS_DIAMOND);
        replace(Items.f_42616_, Tags.Items.GEMS_EMERALD);
        replace(Items.f_42009_, Tags.Items.CHESTS_WOODEN);
        replace(Blocks.f_50652_, Tags.Items.COBBLESTONE_NORMAL);
        replace(Blocks.f_152551_, Tags.Items.COBBLESTONE_DEEPSLATE);

        replace(Items.f_42401_, Tags.Items.STRINGS);
        exclude(m_176517_(Blocks.f_50041_, Items.f_42401_));

        exclude(Blocks.f_50074_);
        exclude(Items.f_42587_);
        exclude(Blocks.f_50075_);
        exclude(Items.f_42749_);
        exclude(Blocks.f_50090_);
        exclude(Blocks.f_50268_);
        exclude(Blocks.f_50721_);
        exclude(Blocks.f_152504_);
        exclude(Blocks.f_152490_);

        exclude(Blocks.f_50157_);
        exclude(Blocks.f_50409_);
        exclude(Blocks.f_50274_);
        exclude(Blocks.f_152552_);
        exclude(Blocks.f_152553_);
        exclude(Blocks.f_152554_);

        super.m_246343_();
    }

    @Nullable
    private Recipe<?> enhance(ResourceKey<Recipe<?>> id, Recipe<?> vanilla) {
        if (vanilla instanceof ShapelessRecipe shapeless)
            return enhance(id, shapeless);
        if (vanilla instanceof ShapedRecipe shaped)
            return enhance(id, shaped);
        return null;
    }

    @Nullable
    private Recipe<?> enhance(ResourceKey<Recipe<?>> id, ShapelessRecipe vanilla) {
        List<Ingredient> ingredients = INGREDIENTS.get(vanilla);
        boolean modified = false;
        for (int x = 0; x < ingredients.size(); x++) {
            Ingredient ing = enhance(id, ingredients.get(x));
            if (ing != null) {
                ingredients.set(x, ing);
                modified = true;
            }
        }
        return modified ? vanilla : null;
    }

    @Nullable
    private Recipe<?> enhance(ResourceKey<Recipe<?>> id, ShapedRecipe vanilla) {
        ShapedRecipePattern pattern = PATTERN.get(vanilla);
        var data = pattern.data().orElseThrow(() -> new IllegalStateException("Weird shaped recipe, data is missing? " + id + " " + vanilla));
        Map<Character, Ingredient> ingredients = data.f_302495_();
        boolean modified = false;
        for (Character x : ingredients.keySet()) {
            Ingredient ing = enhance(id, ingredients.get(x));
            if (ing != null) {
                ingredients.put(x, ing);
                modified = true;
            }
        }
        return modified ? vanilla : null;
    }

    @Nullable
    private Ingredient enhance(ResourceKey<Recipe<?>> name, Ingredient vanilla) {
        if (excludes.contains(name))
            return null;

        HolderSet<Item> vanillaItems = VALUES.get(vanilla);
        var unwraped = vanillaItems.m_203440_();

        if (unwraped.left().isPresent()) // Already a tag
            return null;

        Ingredient ret = null;
        var items = new ArrayList<Holder<Item>>();
        for (var entry : unwraped.right().get()) {
            var item = entry.get();
            var replacement = replacements.get(item);
            if (replacement != null) {
                if (ret != null) {
                    LOGGER.warn("Failed to enahnce {} ingredient has multiple input items", name);
                    return null;
                }
                ret = Ingredient.m_204132_(this.items.m_254956_(replacement));
            } else
                items.add(entry);
        }

        if (ret != null && !items.isEmpty()) {
            LOGGER.warn("Failed to enahnce {} ingredient has multiple input items", name);
            return null;
        }

        return ret;
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<Provider> registries) {
            super(output, registries);
        }

        @Override
        public String m_6055_() {
            return ForgeRecipeProvider.class.getSimpleName();
        }

        @Override
        protected RecipeProvider m_351970_(Provider registries, RecipeOutput output) {
            return new ForgeRecipeProvider(registries, output);
        }
    }

    private static class Wrapped implements RecipeOutput {
        private final RecipeOutput wrapped;
        private ForgeRecipeProvider self;

        private Wrapped(RecipeOutput wrapped) {
            this.wrapped = wrapped;
        }

        private void setSelf(ForgeRecipeProvider self) {
            this.self = self;
        }

        @Override
        public void m_352158_(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancement) {
            var modified = self.enhance(id, recipe);
            if (modified != null)
                wrapped.m_352158_(id, modified, null);
        }

        @Override
        public Builder m_293552_() {
            return wrapped.m_293552_();
        }

        @Override
        public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, ResourceLocation advancementId, JsonElement advancement) {
            var modified = self.enhance(id, recipe);
            if (modified != null)
                wrapped.m_352158_(id, modified, null);
        }

        @Override
        public Provider registry() {
            return wrapped.registry();
        }

        @Override
        public void m_292927_() {}
    }
}
