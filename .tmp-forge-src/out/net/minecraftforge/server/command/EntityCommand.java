/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.server.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

class EntityCommand
{
    static ArgumentBuilder<CommandSourceStack, ?> register()
    {
        return Commands.m_82127_("entity")
                .then(EntityListCommand.register()); //TODO: //Kill, spawn, etc..
    }

    private static class EntityListCommand
    {
        private static final SimpleCommandExceptionType INVALID_FILTER = new SimpleCommandExceptionType(Component.m_237115_("commands.forge.entity.list.invalid"));
        private static final DynamicCommandExceptionType INVALID_DIMENSION = new DynamicCommandExceptionType(dim -> Component.m_237110_("commands.forge.entity.list.invalidworld", dim));
        private static final SimpleCommandExceptionType NO_ENTITIES = new SimpleCommandExceptionType(Component.m_237115_("commands.forge.entity.list.none"));
        static ArgumentBuilder<CommandSourceStack, ?> register()
        {
            return Commands.m_82127_("list")
                .requires(cs->cs.m_81369_(2)) //permission
                .then(Commands.m_82129_("filter", StringArgumentType.string())
                    .suggests((ctx, builder) -> SharedSuggestionProvider.m_82981_(ForgeRegistries.ENTITY_TYPES.getKeys().stream().map(ResourceLocation::toString).map(StringArgumentType::escapeIfRequired), builder))
                    .then(Commands.m_82129_("dim", DimensionArgument.m_88805_())
                        .executes(ctx -> execute(ctx.getSource(), StringArgumentType.getString(ctx, "filter"), DimensionArgument.m_88808_(ctx, "dim").m_46472_()))
                    )
                    .executes(ctx -> execute(ctx.getSource(), StringArgumentType.getString(ctx, "filter"), ctx.getSource().m_81372_().m_46472_()))
                )
                .executes(ctx -> execute(ctx.getSource(), "*", ctx.getSource().m_81372_().m_46472_()));
        }

        private static int execute(CommandSourceStack sender, String filter, ResourceKey<Level> dim) throws CommandSyntaxException
        {
            final String cleanFilter = filter.replace("?", ".?").replace("*", ".*?");

            Set<ResourceLocation> names = ForgeRegistries.ENTITY_TYPES.getKeys().stream().filter(n -> n.toString().matches(cleanFilter)).collect(Collectors.toSet());

            if (names.isEmpty())
                throw INVALID_FILTER.create();

            ServerLevel level = sender.m_81377_().m_129880_(dim); //TODO: DimensionManager so we can hotload? DimensionManager.getWorld(sender.getServer(), dim, false, false);
            if (level == null)
                throw INVALID_DIMENSION.create(dim);

            Map<ResourceLocation, MutablePair<Integer, Map<ChunkPos, Integer>>> list = Maps.newHashMap();
            level.m_142646_().m_142273_().forEach(e -> {
                MutablePair<Integer, Map<ChunkPos, Integer>> info = list.computeIfAbsent(ForgeRegistries.ENTITY_TYPES.getKey(e.m_6095_()), k -> MutablePair.of(0, Maps.newHashMap()));
                ChunkPos chunk = new ChunkPos(e.m_20183_());
                info.left++;
                info.right.put(chunk, info.right.getOrDefault(chunk, 0) + 1);
            });

            if (names.size() == 1)
            {
                ResourceLocation name = names.iterator().next();
                Pair<Integer, Map<ChunkPos, Integer>> info = list.get(name);
                if (info == null)
                    throw NO_ENTITIES.create();

                sender.m_288197_(() -> Component.m_237110_("commands.forge.entity.list.single.header", name.toString(), info.getLeft()), false);
                List<Map.Entry<ChunkPos, Integer>> toSort = new ArrayList<>();
                toSort.addAll(info.getRight().entrySet());
                toSort.sort((a, b) -> {
                    if (Objects.equals(a.getValue(), b.getValue()))
                        return a.getKey().toString().compareTo(b.getKey().toString());
                    else
                        return b.getValue() - a.getValue();
                });

                long limit = 10;
                for (Map.Entry<ChunkPos, Integer> e : toSort)
                {
                    if (limit-- == 0) break;
                    sender.m_288197_(() -> Component.m_237113_("  " + e.getValue() + ": " + e.getKey().f_45578_ + ", " + e.getKey().f_45579_), false);
                }
                return toSort.size();
            }
            else
            {

                List<Pair<ResourceLocation, Integer>> info = new ArrayList<>();
                list.forEach((key, value) -> {
                    if (names.contains(key))
                    {
                        Pair<ResourceLocation, Integer> of = Pair.of(key, value.left);
                        info.add(of);
                    }
                });
                info.sort((a, b) -> {
                    if (Objects.equals(a.getRight(), b.getRight()))
                        return a.getKey().toString().compareTo(b.getKey().toString());
                    else
                        return b.getRight() - a.getRight();
                });

                if (info.isEmpty())
                    throw NO_ENTITIES.create();

                int count = info.stream().mapToInt(Pair::getRight).sum();
                sender.m_288197_(() -> Component.m_237110_("commands.forge.entity.list.multiple.header", count), false);
                info.forEach(e -> sender.m_288197_(() -> Component.m_237113_("  " + e.getValue() + ": " + e.getKey()), false));
                return info.size();
            }
        }
    }

}
