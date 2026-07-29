/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.server.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mojang.brigadier.builder.ArgumentBuilder;

class DimensionsCommand {
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.m_82127_("dimensions")
            .requires(cs->cs.m_81369_(0)) //permission
            .executes(ctx -> {
                ctx.getSource().m_288197_(() -> Component.m_237115_("commands.forge.dimensions.list"), true);
                final Registry<DimensionType> reg = ctx.getSource().m_5894_().m_254974_(Registries.f_256787_);

                Map<ResourceLocation, List<ResourceLocation>> types = new HashMap<>();
                for (ServerLevel dim : ctx.getSource().m_81377_().m_129785_()) {
                    types.computeIfAbsent(reg.m_7981_(dim.m_6042_()), k -> new ArrayList<>()).add(dim.m_46472_().m_135782_());
                }

                types.keySet().stream().sorted().forEach(key -> {
                    ctx.getSource().m_288197_(() -> Component.m_237113_(key + ": " + types.get(key).stream().map(ResourceLocation::toString).sorted().collect(Collectors.joining(", "))), false);
                });
                return 0;
            });
    }
}
