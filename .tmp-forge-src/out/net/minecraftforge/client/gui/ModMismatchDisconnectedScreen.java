/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.gui;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.common.ForgeI18n;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkContext.NetworkMismatchData;
import net.minecraftforge.network.packets.ModVersions;

//TODO: Oh god... Whoever wrote this was on something very very strong.
import net.minecraft.client.gui.narration.NarratableEntry.NarrationPriority;

public class ModMismatchDisconnectedScreen extends Screen {
    private final Component reason;
    private MultiLineLabel message = MultiLineLabel.f_94331_;
    private final Screen parent;
    private int textHeight;
    private final Path modsDir;
    private final Path logFile;
    private final int listHeight;
    private final NetworkMismatchData data;
    private final boolean hasMismatches;

    private final List<String> allModIds;
    private final Map<String, String> presentModUrls;

    public ModMismatchDisconnectedScreen(Screen parentScreen, Component title, Component reason, NetworkMismatchData data) {
        super(title);
        this.parent = parentScreen;
        this.reason = reason;
        this.modsDir = FMLPaths.MODSDIR.get();
        this.logFile = FMLPaths.GAMEDIR.get().resolve(Paths.get("logs","latest.log"));
        this.data = data;
        this.hasMismatches = !data.mismatched().isEmpty() || !data.missing().isEmpty();
        this.listHeight = hasMismatches ? 140 : 0;

        this.allModIds = data.mods().keySet().stream().distinct().collect(Collectors.toList());
        this.presentModUrls = ModList.get().getMods().stream()
            .filter(info -> allModIds.contains(info.getModId()))
            .map(info -> Pair.of(info.getModId(), (String)info.getConfig().getConfigElement("displayURL").orElse("")))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    @Override
    protected void m_7856_() {
        this.message = MultiLineLabel.m_94345_(this.f_96547_, this.reason, this.f_96543_ - 50);
        this.textHeight = this.message.m_5770_() * 9;

        int listLeft = Math.max(8, this.f_96543_ / 2 - 220);
        int listWidth = Math.min(440, this.f_96543_ - 16);
        int upperButtonHeight = Math.min((this.f_96544_ + this.listHeight + this.textHeight) / 2 + 10, this.f_96544_ - 50);
        int lowerButtonHeight = Math.min((this.f_96544_ + this.listHeight + this.textHeight) / 2 + 35, this.f_96544_ - 25);
        if (hasMismatches)
            this.m_142416_(new MismatchInfoPanel(f_96541_, listWidth, listHeight, (this.f_96544_ - this.listHeight) / 2, listLeft));

        int buttonWidth = Math.min(210, this.f_96543_ / 2 - 20);
        this.m_142416_(Button.m_253074_(Component.m_237113_(ForgeI18n.parseMessage("fml.button.open.file", logFile.getFileName())), button -> Util.m_137581_().m_137644_(logFile.toFile()))
                .m_252987_(Math.max(this.f_96543_ / 4 - buttonWidth / 2, listLeft), upperButtonHeight, buttonWidth, 20)
                .m_253136_());
        this.m_142416_(Button.m_253074_(Component.m_237113_(ForgeI18n.parseMessage("fml.button.open.mods.folder")), button -> Util.m_137581_().m_137644_(modsDir.toFile()))
                .m_252987_(Math.min(this.f_96543_ * 3 / 4 - buttonWidth / 2, listLeft + listWidth - buttonWidth), upperButtonHeight, buttonWidth, 20)
                .m_253136_());
        this.m_142416_(Button.m_253074_(Component.m_237115_("gui.toMenu"), button -> this.f_96541_.m_91152_(this.parent))
                .m_252987_((this.f_96543_ - buttonWidth) / 2, lowerButtonHeight, buttonWidth, 20)
                .m_253136_());
    }

    @Override
    public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics, mouseX, mouseY, partialTicks);
        int textYOffset = hasMismatches ? 18 : 0;
        guiGraphics.m_280653_(this.f_96547_, this.f_96539_, this.f_96543_ / 2, (this.f_96544_ - this.listHeight - this.textHeight) / 2 - textYOffset - 9 * 2, 0xAAAAAA);
        this.message.m_6276_(guiGraphics, this.f_96543_ / 2, (this.f_96544_ - this.listHeight - this.textHeight) / 2 - textYOffset);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
    }

    class MismatchInfoPanel extends ScrollPanel {
        private final List<Pair<FormattedCharSequence, Pair<FormattedCharSequence, FormattedCharSequence>>> lineTable;
        private final int contentSize;
        private final int nameIndent = 10;
        private final int tableWidth = width - border * 2 - 6 - nameIndent;
        private final int nameWidth = tableWidth * 3 / 5;
        private final int versionWidth = (tableWidth - nameWidth) / 2;

        public MismatchInfoPanel(Minecraft client, int width, int height, int top, int left) {
            super(client, width, height, top, left);

            //The raw list of the strings in a table row, the components may still be too long for the final table and will be split up later. The first row element may have a style assigned to it that will be used for the whole content row.
            List<Pair<MutableComponent, Pair<String, String>>> rawTable = new ArrayList<>();
            if (!data.missing().isEmpty()) {
                //The header of the section, colored in gray
                rawTable.add(Pair.of(
                    Component.m_237113_(ForgeI18n.parseMessage(data.fromServer() ? "fml.modmismatchscreen.missingmods.server" : "fml.modmismatchscreen.missingmods.client")).m_130940_(ChatFormatting.GRAY),
                    null
                ));
                //This table section contains the mod name and mod version of each mod that has a missing remote counterpart (if the mod is missing on the server, the client mod version is displayed, and vice versa)
                rawTable.add(Pair.of(
                    Component.m_237113_(ForgeI18n.parseMessage("fml.modmismatchscreen.table.modname")).m_130940_(ChatFormatting.UNDERLINE),
                    Pair.of(
                        "",
                        ForgeI18n.parseMessage(data.fromServer() ? "fml.modmismatchscreen.table.youhave" : "fml.modmismatchscreen.table.youneed")
                    )
                ));
                int i = 0;
                for (ResourceLocation mod : data.missing()) {
                    rawTable.add(Pair.of(
                        toModNameComponent(mod, data.mods().get(mod.m_135827_()).name(), i),
                        Pair.of(
                            "",
                            data.mods().getOrDefault(mod, new ModVersions.Info("", "")).version()
                        )
                    ));
                    if (++i >= 10) {
                        //If too many missing mod entries are present, append a line referencing how to see the full list and stop rendering any more entries
                        rawTable.add(Pair.of(
                            Component.m_237113_(ForgeI18n.parseMessage("fml.modmismatchscreen.additional", data.missing().size() - i)).m_130940_(ChatFormatting.ITALIC),
                            Pair.of("", "")
                        ));
                        break;
                    }
                }
                rawTable.add(Pair.of(Component.m_237113_(" "), null)); //padding
            }

            if (!data.mismatched().isEmpty()) {
                //The header of the table section, colored in gray
                rawTable.add(Pair.of(Component.m_237113_(ForgeI18n.parseMessage("fml.modmismatchscreen.mismatchedmods")).m_130940_(ChatFormatting.GRAY), null));
                //This table section contains the mod name and both mod versions of each mod that has a mismatching client and server version
                rawTable.add(Pair.of(
                    Component.m_237113_(ForgeI18n.parseMessage("fml.modmismatchscreen.table.modname")).m_130940_(ChatFormatting.UNDERLINE),
                    Pair.of(
                        ForgeI18n.parseMessage(data.fromServer() ? "fml.modmismatchscreen.table.youhave" : "fml.modmismatchscreen.table.serverhas"),
                        ForgeI18n.parseMessage(data.fromServer() ? "fml.modmismatchscreen.table.serverhas" : "fml.modmismatchscreen.table.youhave")
                    )
                ));
                int i = 0;
                for (var modData : data.mismatched().entrySet()) {
                    rawTable.add(Pair.of(
                        toModNameComponent(modData.getKey(), data.mods().get(modData.getKey().m_135827_()).name(), i),
                        Pair.of(
                            data.mods().getOrDefault(modData.getKey(), new ModVersions.Info("", "")).version(),
                            modData.getValue().received()
                        )
                    ));
                    if (++i >= 10) {
                        //If too many mismatched mod entries are present, append a line referencing how to see the full list and stop rendering any more entries
                        rawTable.add(Pair.of(Component.m_237113_(ForgeI18n.parseMessage("fml.modmismatchscreen.additional", data.mismatched().size() - i)).m_130940_(ChatFormatting.ITALIC), Pair.of("", "")));
                        break;
                    }
                }
                rawTable.add(Pair.of(Component.m_237113_(" "), null)); //padding
            }

            this.lineTable = rawTable.stream().flatMap(p -> splitLineToWidth(p.getKey(), p.getValue()).stream()).collect(Collectors.toList());
            this.contentSize = lineTable.size();
        }

        /**
         * Splits the raw name and version strings, making them use multiple lines if needed, to fit within the table dimensions.
         * The style assigned to the name element is then applied to the entire content row.
         * @param name The first element of the content row, usually representing a table section header or the name of a mod entry
         * @param versions The last two elements of the content row, usually representing the mod versions. If either one or both of them are not given, the first element may take up more space within the table.
         * @return A list of table rows consisting of 3 elements each which consist of the same content as was given by the parameters, but split up to fit within the table dimensions.
         */
        private List<Pair<FormattedCharSequence, Pair<FormattedCharSequence, FormattedCharSequence>>> splitLineToWidth(MutableComponent name, Pair<String, String> versions) {
            Style style = name.m_7383_();
            int versionColumns = versions == null ? 0 : (versions.getLeft().isEmpty() ? (versions.getRight().isEmpty() ? 0 : 1) : 2);
            int adaptedNameWidth = nameWidth + versionWidth * (2 - versionColumns) - 4; //the name width may be expanded when the version column string is missing
            List<FormattedCharSequence> nameLines = f_96547_.m_92923_(name, adaptedNameWidth);
            List<FormattedCharSequence> clientVersionLines = f_96547_.m_92923_(Component.m_237113_(versions != null ? versions.getLeft() : "").m_6270_(style), versionWidth - 4);
            List<FormattedCharSequence> serverVersionLines = f_96547_.m_92923_(Component.m_237113_(versions != null ? versions.getRight() : "").m_6270_(style), versionWidth - 4);
            List<Pair<FormattedCharSequence, Pair<FormattedCharSequence, FormattedCharSequence>>> splitLines = new ArrayList<>();
            int rowsOccupied = Math.max(nameLines.size(), Math.max(clientVersionLines.size(), serverVersionLines.size()));
            for (int i = 0; i < rowsOccupied; i++) {
                splitLines.add(Pair.of(
                    i < nameLines.size() ? nameLines.get(i) : FormattedCharSequence.f_13691_,
                    versions == null ? null : Pair.of(
                        i < clientVersionLines.size() ? clientVersionLines.get(i) : FormattedCharSequence.f_13691_,
                        i < serverVersionLines.size() ? serverVersionLines.get(i) : FormattedCharSequence.f_13691_
                    )
                ));
            }
            return splitLines;
        }

        /**
         * Adds a style information to the given mod name string. The style assigned to the returned component contains the color of the mod name,
         * a hover event containing the given id, and an optional click event, which opens the homepage of mod, if present.
         * @param id An id that gets displayed in the hover event. Depending on the origin it may only consist of a namespace (the mod id) or a namespace + path (a channel id associated with the mod).
         * @param modName The name of the mod. It will be rendered as the main text component.
         * @param color Defines the color of the returned style information. An odd number will result in a yellow, an even one in a gold color. This color variation makes it easier for users to distinguish different mod entries.
         * @return A component with the mod name as the main text component, and an assigned style which will be used for the whole content row.
         */
        private MutableComponent toModNameComponent(ResourceLocation id, String modName, int color) {
            String modId = id.m_135827_();
            String tooltipId = id.m_135815_().isEmpty() ? id.m_135827_() : id.toString();
            return Component.m_237113_(modName).m_130940_(color % 2 == 0 ? ChatFormatting.GOLD : ChatFormatting.YELLOW)
                    .m_130938_(s -> s.m_131144_(new HoverEvent.ShowText(Component.m_237113_(tooltipId + (!presentModUrls.getOrDefault(modId, "").isEmpty() ? "\n" + ForgeI18n.parseMessage("fml.modmismatchscreen.homepage") : "")))))
                    .m_130938_(s -> s.m_131142_(openUrl(presentModUrls.getOrDefault(modId, ""))));
        }

        private static ClickEvent.OpenUrl openUrl(String url) {
            try {
                return url.isEmpty() ? null : new ClickEvent.OpenUrl(new URI(url));
            } catch (URISyntaxException e) {
                return null;
            }
        }

        @Override
        protected int getContentHeight() {
            int height = contentSize * (f_96547_.f_92710_ + 3);

            if (height < bottom - top - 4)
                height = bottom - top - 4;

            return height;
        }

        @Override
        protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, int mouseX, int mouseY) {
            int i = 0;

            for (Pair<FormattedCharSequence, Pair<FormattedCharSequence, FormattedCharSequence>> line : lineTable) {
                FormattedCharSequence name = line.getLeft();
                Pair<FormattedCharSequence, FormattedCharSequence> versions = line.getRight();
                //Since font#draw does not respect the color of the given component, we have to read it out here and then use it as the last parameter
                int color = Optional.ofNullable(f_96547_.m_92865_().m_92338_(name, 0)).map(Style::m_131135_).map(TextColor::m_131265_).orElse(0xFFFFFF);
                //Only indent the given name if a version string is present. This makes it easier to distinguish table section headers and mod entries
                int nameLeft = left + border + (versions == null ? 0 : nameIndent);
                guiGraphics.m_280648_(f_96547_, name, nameLeft, relativeY + i * 12, color, false);
                if (versions != null) {
                    guiGraphics.m_280648_(f_96547_, versions.getLeft(), left + border + nameIndent + nameWidth, relativeY + i * 12, color, false);
                    guiGraphics.m_280648_(f_96547_, versions.getRight(), left + border + nameIndent + nameWidth + versionWidth, relativeY + i * 12, color, false);
                }

                i++;
            }
        }

        @Override
        public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
            Style style = getComponentStyleAt(mouseX, mouseY);
            if (style != null && style.m_131186_() != null)
                guiGraphics.m_280304_(f_96547_, style, mouseX, mouseY);
        }

        public Style getComponentStyleAt(double x, double y) {
            if (this.m_5953_(x, y)) {
                double relativeY = y - this.top + this.scrollDistance - border;
                int slotIndex = (int)(relativeY + (border / 2)) / 12;
                if (slotIndex < contentSize) {
                    //The relative x needs to take the potentially missing indent of the row into account. It does that by checking if the line has a version associated to it
                    double relativeX = x - left - border - (lineTable.get(slotIndex).getRight() == null ? 0 : nameIndent);
                    if (relativeX >= 0)
                        return f_96547_.m_92865_().m_92338_(lineTable.get(slotIndex).getLeft(), (int)relativeX);
                }
            }

            return null;
        }

        @Override
        public boolean m_6375_(final double mouseX, final double mouseY, final int button) {
            Style style = getComponentStyleAt(mouseX, mouseY);
            if (style != null) {
                m_5561_(style);
                return true;
            }
            return super.m_6375_(mouseX, mouseY, button);
        }

        @Override
        public NarrationPriority m_142684_() {
            return NarrationPriority.NONE;
        }

        @Override
        public void m_142291_(NarrationElementOutput output) {
        }
    }
}
