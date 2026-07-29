/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.gui;

import com.google.common.base.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraftforge.common.ForgeI18n;
import net.minecraftforge.fml.LoadingFailedException;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.loading.FMLPaths;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LoadingErrorScreen extends ErrorScreen {
    private final Path modsDir;
    private final Path logFile;
    private final List<ModLoadingException> modLoadErrors;
    private final List<ModLoadingWarning> modLoadWarnings;
    private final Path dumpedLocation;
    private LoadingEntryList entryList;
    private Component errorHeader;
    private Component warningHeader;

    public LoadingErrorScreen(LoadingFailedException loadingException, List<ModLoadingWarning> warnings, final File dumpedLocation) {
        super(Component.m_237113_("Loading Error"), null);
        this.modLoadWarnings = warnings;
        this.modLoadErrors = loadingException == null ? Collections.emptyList() : loadingException.getErrors();
        this.modsDir = FMLPaths.MODSDIR.get();
        this.logFile = FMLPaths.GAMEDIR.get().resolve(Paths.get("logs","latest.log"));
        this.dumpedLocation = dumpedLocation != null ? dumpedLocation.toPath() : null;
    }

    @Override
    public void m_7856_() {
        super.m_7856_();
        this.m_169413_();

        this.errorHeader = Component.m_237113_(ChatFormatting.RED + ForgeI18n.parseMessage("fml.loadingerrorscreen.errorheader", this.modLoadErrors.size()) + ChatFormatting.RESET);
        this.warningHeader = Component.m_237113_(ChatFormatting.YELLOW + ForgeI18n.parseMessage("fml.loadingerrorscreen.warningheader", this.modLoadErrors.size()) + ChatFormatting.RESET);

        int yOffset = 46;
        this.m_142416_(new ExtendedButton(50, this.f_96544_ - yOffset, this.f_96543_ / 2 - 55, 20, Component.m_237113_(ForgeI18n.parseMessage("fml.button.open.mods.folder")), b -> Util.m_137581_().m_137644_(modsDir.toFile())));
        this.m_142416_(new ExtendedButton(this.f_96543_ / 2 + 5, this.f_96544_ - yOffset, this.f_96543_ / 2 - 55, 20, Component.m_237113_(ForgeI18n.parseMessage("fml.button.open.file", logFile.getFileName())), b -> Util.m_137581_().m_137644_(logFile.toFile())));
        if (this.modLoadErrors.isEmpty())
            this.m_142416_(new ExtendedButton(this.f_96543_ / 4, this.f_96544_ - 24, this.f_96543_ / 2, 20, Component.m_237113_(ForgeI18n.parseMessage("fml.button.continue.launch")), b -> this.f_96541_.m_91152_(null)));
        else
            this.m_142416_(new ExtendedButton(this.f_96543_ / 4, this.f_96544_ - 24, this.f_96543_ / 2, 20, Component.m_237113_(ForgeI18n.parseMessage("fml.button.open.file", dumpedLocation.getFileName())), b -> Util.m_137581_().m_137644_(dumpedLocation.toFile())));

        this.entryList = new LoadingEntryList(this, this.modLoadErrors, this.modLoadWarnings);
        this.m_7787_(this.entryList);
        this.m_7522_(this.entryList);
    }

    @Override
    public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.m_280273_(guiGraphics, mouseX, mouseY, partialTick);
        this.entryList.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
        drawMultiLineCenteredString(guiGraphics, f_96547_, this.modLoadErrors.isEmpty() ? warningHeader : errorHeader, this.f_96543_ / 2, 10);
        this.f_169369_.forEach(button -> button.m_88315_(guiGraphics, mouseX, mouseY, partialTick));
    }

    private void drawMultiLineCenteredString(GuiGraphics guiGraphics, Font fr, Component str, int x, int y) {
        for (FormattedCharSequence s : fr.m_92923_(str, this.f_96543_)) {
            guiGraphics.m_280614_(fr, s, x - fr.m_92724_(s) / 2, y, 0xFFFFFFFF);
            y += fr.f_92710_;
        }
    }

    public static class LoadingEntryList extends ObjectSelectionList<LoadingEntryList.LoadingMessageEntry> {
        LoadingEntryList(final LoadingErrorScreen parent, final List<ModLoadingException> errors, final List<ModLoadingWarning> warnings) {
            super(Objects.requireNonNull(parent.f_96541_), parent.f_96543_, parent.f_96544_, 35,
                Math.max(
                    errors.stream().mapToInt(error -> parent.f_96547_.m_92923_(Component.m_237113_(error.getMessage() != null ? error.getMessage() : ""), parent.f_96543_ - 20).size()).max().orElse(0),
                    warnings.stream().mapToInt(warning -> parent.f_96547_.m_92923_(Component.m_237113_(warning.formatToString() != null ? warning.formatToString() : ""), parent.f_96543_ - 20).size()).max().orElse(0)
                ) * parent.f_96541_.f_91062_.f_92710_ + 8
            );
            boolean both = !errors.isEmpty() && !warnings.isEmpty();
            if (both)
                m_7085_(new LoadingMessageEntry(parent.errorHeader, true));
            errors.forEach(e->m_7085_(new LoadingMessageEntry(Component.m_237113_(e.formatToString()))));
            if (both) {
                int maxChars = (this.f_93618_ - 10) / parent.f_96541_.f_91062_.m_92895_("-");
                m_7085_(new LoadingMessageEntry(Component.m_237113_("\n" + Strings.repeat("-", maxChars) + "\n")));
                m_7085_(new LoadingMessageEntry(parent.warningHeader, true));
            }
            warnings.forEach(w->m_7085_(new LoadingMessageEntry(Component.m_237113_(w.formatToString()))));
        }

        @Override
        public int m_5759_() {
            return this.f_93618_;
        }

        public class LoadingMessageEntry extends ObjectSelectionList.Entry<LoadingMessageEntry> {
            private final Component message;
            private final boolean center;

            LoadingMessageEntry(final Component message) {
                this(message, false);
            }

            LoadingMessageEntry(final Component message, final boolean center) {
                this.message = Objects.requireNonNull(message);
                this.center = center;
            }

            @Override
            public Component m_142172_() {
                return Component.m_237110_("narrator.select", message);
            }

            @Override
            public void m_6311_(GuiGraphics guiGraphics, int entryIdx, int top, int left, final int entryWidth, final int entryHeight, final int mouseX, final int mouseY, final boolean p_194999_5_, final float partialTick) {
                var mc = Minecraft.m_91087_();
                Font font = mc.f_91062_;
                var strings = font.m_92923_(message, LoadingEntryList.this.f_93618_ - 20);
                int y = top + 2;
                for (var string : strings) {
                    if (center)
                        guiGraphics.m_280648_(font, string, left + (f_93618_ / 2) - (font.m_92724_(string) / 2), y, 0xFFFFFFFF, false);
                    else
                        guiGraphics.m_280648_(font, string, left + 5, y, 0xFFFFFFFF, false);
                    y += font.f_92710_;
                }
            }
        }

    }
}
