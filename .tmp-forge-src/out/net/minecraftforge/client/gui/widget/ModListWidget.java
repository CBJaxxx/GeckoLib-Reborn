/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.locale.Language;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.forgespi.language.IModInfo;

public class ModListWidget extends ObjectSelectionList<ModListWidget.ModEntry> {
    private static String stripControlCodes(String value) { return net.minecraft.util.StringUtil.m_14406_(value); }
    private static final ResourceLocation VERSION_CHECK_ICONS = ResourceLocation.m_339182_(ForgeVersion.MOD_ID, "textures/gui/version_check_icons.png");
    private final int listWidth;

    private final ModListScreen parent;

    public ModListWidget(ModListScreen parent, int listWidth, int top, int bottom) {
        super(parent.getMinecraftInstance(), listWidth, bottom, top, parent.getFontRenderer().f_92710_ * 2 + 8);
        this.parent = parent;
        this.listWidth = listWidth;
        this.refreshList();
    }

    @Override
    public int m_5759_() {
        return this.listWidth;
    }

    @Override
    protected int m_372556_() {
        return this.m_93520_() - 8;
    }

    @Override
    protected void m_240140_(GuiGraphics gui, int top, int rowWidth, int height, int borderColor, int backgroundColor) {
        int barOffset = this.m_373053_() ? 6 : 0;
        int left  = this.m_252754_() + (this.f_93618_ - rowWidth) / 2;
        int right = this.m_252754_() + (this.f_93618_ + rowWidth) / 2 - barOffset;
        gui.m_280509_(left,     top - 2, right,     top + height + 2, borderColor);
        gui.m_280509_(left + 1, top - 1, right - 1, top + height + 1, backgroundColor);
    }

    public void refreshList() {
        this.m_93516_();
        parent.buildModList(this::m_7085_, mod->new ModEntry(mod, this.parent));
    }

    public class ModEntry extends ObjectSelectionList.Entry<ModEntry> {
        private final IModInfo modInfo;
        private final ModListScreen parent;

        ModEntry(IModInfo info, ModListScreen parent) {
            this.modInfo = info;
            this.parent = parent;
        }

        @Override
        public Component m_142172_() {
            return Component.m_237110_("narrator.select", modInfo.getDisplayName());
        }

        @Override
        public void m_6311_(GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            Component name = Component.m_237113_(stripControlCodes(modInfo.getDisplayName()));
            Component version = Component.m_237113_(stripControlCodes(MavenVersionStringHelper.artifactVersionToString(modInfo.getVersion())));
            VersionChecker.CheckResult vercheck = VersionChecker.getResult(modInfo);
            Font font = this.parent.getFontRenderer();
            var barOffset = ModListWidget.this.m_373053_() ? 6 : 0;
            guiGraphics.m_280648_(font, Language.m_128107_().m_5536_(FormattedText.m_130773_(font.m_92854_(name,    listWidth - 6 - barOffset))), left + 3, top + 2, 0xFFFFFFFF, false);
            guiGraphics.m_280648_(font, Language.m_128107_().m_5536_(FormattedText.m_130773_(font.m_92854_(version, listWidth - 6 - barOffset))), left + 3, top + 2 + font.f_92710_, 0xFFCCCCCC, false);
            if (vercheck.status().shouldDraw()) {
                //TODO: [Forge][ModList] Consider adding more icons for visualization
                guiGraphics.m_280168_().pushMatrix();
                guiGraphics.m_280218_(RenderPipelines.f_379833_, VERSION_CHECK_ICONS, m_252754_() + f_93618_ - 12 - barOffset, top + entryHeight / 4, vercheck.status().getSheetOffset() * 8, (vercheck.status().isAnimated() && ((System.currentTimeMillis() / 800 & 1)) == 1) ? 8 : 0, 8, 8, 64, 16);
                guiGraphics.m_280168_().popMatrix();

            }
        }

        @Override
        public boolean m_6375_(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            parent.setSelected(this);
            ModListWidget.this.m_6987_(this);
            return false;
        }

        public IModInfo getInfo() {
            return modInfo;
        }
    }
}
