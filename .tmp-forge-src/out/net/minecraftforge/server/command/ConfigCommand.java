/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;

import java.io.File;

public class ConfigCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.m_82127_("config").
                        then(ShowFile.register())
        );
    }

    public static class ShowFile {
        static ArgumentBuilder<CommandSourceStack, ?> register() {
            return Commands.m_82127_("showfile").
                    requires(cs->cs.m_81369_(0)).
                    then(Commands.m_82129_("mod", ModIdArgument.modIdArgument()).
                        then(Commands.m_82129_("type", EnumArgument.enumArgument(ModConfig.Type.class)).
                            executes(ShowFile::showFile)
                        )
                    );
        }

        private static int showFile(final CommandContext<CommandSourceStack> context) {
            final String modId = context.getArgument("mod", String.class);
            final ModConfig.Type type = context.getArgument("type", ModConfig.Type.class);
            final String configFileName = ConfigTracker.INSTANCE.getConfigFileName(modId, type);
            if (configFileName != null) {
                File f = new File(configFileName);
                context.getSource().m_288197_(() -> Component.m_237110_("commands.config.getwithtype",
                        modId, type,
                        Component.m_237113_(f.getName()).m_130940_(ChatFormatting.UNDERLINE).
                                m_130938_((style) -> style.m_131142_(new ClickEvent.OpenFile(f.getAbsolutePath())))
                ), true);
            } else {
                context.getSource().m_288197_(() -> Component.m_237110_("commands.config.noconfig", modId, type),
                        true);
            }
            return 0;
        }
    }
}
