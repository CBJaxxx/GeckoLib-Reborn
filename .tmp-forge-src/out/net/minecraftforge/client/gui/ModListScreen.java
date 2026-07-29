/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.gui;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.gui.widget.ModListWidget;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.apache.maven.artifact.versioning.ComparableVersion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;

import net.minecraft.client.renderer.texture.DynamicTexture;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Size2i;
import net.minecraftforge.common.ForgeI18n;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.forgespi.language.IModInfo;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.slf4j.Logger;

import net.minecraft.client.gui.narration.NarratableEntry.NarrationPriority;

public class ModListScreen extends Screen {
    private static final ResourceLocation LOGO = ResourceLocation.m_339182_("forge", "mod_logo");
    private static String stripControlCodes(String value) { return net.minecraft.util.StringUtil.m_14406_(value); }
    private static final Logger LOGGER = LogUtils.getLogger();
    private enum SortType implements Comparator<IModInfo> {
        NORMAL,
        A_TO_Z{ @Override protected int compare(String name1, String name2){ return name1.compareTo(name2); }},
        Z_TO_A{ @Override protected int compare(String name1, String name2){ return name2.compareTo(name1); }};

        Button button;
        protected int compare(String name1, String name2){ return 0; }
        @Override
        public int compare(IModInfo o1, IModInfo o2) {
            String name1 = StringUtils.toLowerCase(stripControlCodes(o1.getDisplayName()));
            String name2 = StringUtils.toLowerCase(stripControlCodes(o2.getDisplayName()));
            return compare(name1, name2);
        }

        Component getButtonText() {
            return Component.m_237115_("fml.menu.mods." + StringUtils.toLowerCase(name()));
        }
    }

    private static final int PADDING = 6;
    private static final int BUTTON_MARGIN = 1;
    private static final int NUM_BUTTONS = SortType.values().length;
    private static final int BUTTON_HEIGHT = 20;

    private final Screen parentScreen;

    private ModListWidget modList;
    private InfoPanel modInfo;
    private ModListWidget.ModEntry selected = null;
    private int listWidth;
    private List<IModInfo> mods;
    private final List<IModInfo> unsortedMods;
    private Button configButton, openModsFolderButton, doneButton;

    private String lastFilterText = "";

    private StringWidget searchText;
    private EditBox search;

    private boolean sorted = false;
    private SortType sortType = SortType.NORMAL;

    public ModListScreen(Screen parentScreen) {
        super(Component.m_237115_("fml.menu.mods.title"));
        this.parentScreen = parentScreen;
        this.mods = ModList.get().getMods();
        this.unsortedMods = List.copyOf(this.mods);
    }

    class InfoPanel extends ScrollPanel {
        private ResourceLocation logoPath;
        private Size2i logoDims = new Size2i(0, 0);
        private List<FormattedCharSequence> lines = Collections.emptyList();

        InfoPanel(Minecraft mcIn, int widthIn, int heightIn, int topIn) {
            super(mcIn, widthIn, heightIn, topIn, modList.m_307439_() + PADDING);
        }

        void setInfo(List<String> lines, ResourceLocation logoPath, Size2i logoDims) {
            this.logoPath = logoPath;
            this.logoDims = logoDims;
            this.lines = resizeContent(lines);
        }

        void clearInfo() {
            this.logoPath = null;
            this.logoDims = new Size2i(0, 0);
            this.lines = Collections.emptyList();
        }

        private List<FormattedCharSequence> resizeContent(List<String> lines) {
            List<FormattedCharSequence> ret = new ArrayList<>();
            for (String line : lines) {
                if (line == null) {
                    ret.add(null);
                    continue;
                }

                Component chat = ForgeHooks.newChatWithLinks(line, false);
                int maxTextLength = this.width - 12;
                if (maxTextLength >= 0)
                    ret.addAll(Language.m_128107_().m_128112_(f_96547_.m_92865_().m_92414_(chat, maxTextLength, Style.f_131099_)));
            }
            return ret;
        }

        @Override
        public int getContentHeight() {
            int height = 50;
            height += (lines.size() * f_96547_.f_92710_);
            if (height < this.bottom - this.top - 8)
                height = this.bottom - this.top - 8;
            return height;
        }

        @Override
        protected int getScrollAmount() {
            return f_96547_.f_92710_ * 3;
        }

        @Override
        protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, int mouseX, int mouseY) {
            if (logoPath != null) {
                //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                // Draw the logo image inscribed in a rectangle with width entryWidth (minus some padding) and height 50
                int headerHeight = 50;
                guiGraphics.blitInscribed(logoPath, left + PADDING, relativeY, width - (PADDING * 2), headerHeight, logoDims.width(), logoDims.height(), false, true);
                relativeY += headerHeight + PADDING;
            }

            for (FormattedCharSequence line : lines) {
                if (line != null)
                    guiGraphics.m_280614_(ModListScreen.this.f_96547_, line, left + PADDING, relativeY, ARGB.m_357071_(1));
                relativeY += f_96547_.f_92710_;
            }

            final Style component = findTextLine(mouseX, mouseY);
            if (component!=null)
                guiGraphics.m_280304_(ModListScreen.this.f_96547_, component, mouseX, mouseY);
        }

        private Style findTextLine(final int mouseX, final int mouseY) {
            if (!m_5953_(mouseX, mouseY))
                return null;

            double offset = (mouseY - top - PADDING - border) + scrollDistance;
            if (logoPath != null)
                offset -= 50;
            if (offset <= 0)
                return null;

            int lineIdx = (int) (offset / f_96547_.f_92710_);
            if (lineIdx >= lines.size() || lineIdx < 0)
                return null;

            FormattedCharSequence line = lines.get(lineIdx);
            if (line != null)
                return f_96547_.m_92865_().m_92338_(line, mouseX - left - border);
            return null;
        }

        @Override
        public boolean m_6375_(final double mouseX, final double mouseY, final int button) {
            final Style component = findTextLine((int) mouseX, (int) mouseY);
            if (component != null) {
                ModListScreen.this.m_5561_(component);
                return true;
            }
            return super.m_6375_(mouseX, mouseY, button);
        }

        @Override
        public NarrationPriority m_142684_() {
            return NarrationPriority.NONE;
        }

        @Override
        public void m_142291_(NarrationElementOutput p_169152_) {
        }
    }

    @Override
    public void m_7856_() {
        var font = getFontRenderer();

        for (IModInfo mod : mods) {
            listWidth = Math.max(listWidth, font.m_92895_(mod.getDisplayName()) + 10);
            listWidth = Math.max(listWidth, font.m_92895_(MavenVersionStringHelper.artifactVersionToString(mod.getVersion())) + 5);
        }

        listWidth = Math.max(Math.min(listWidth, f_96543_ / 3), 100);
        listWidth += listWidth % NUM_BUTTONS != 0 ? (NUM_BUTTONS - listWidth % NUM_BUTTONS) : 0;

        final int fullButtonHeight = PADDING + BUTTON_HEIGHT + PADDING;
        final int modInfoWidth = this.f_96543_ - this.listWidth - (PADDING * 3);
        final int doneButtonWidth = Math.min(modInfoWidth, 200);

        int y = this.f_96544_ - BUTTON_HEIGHT - PADDING;
        doneButton = Button.m_253074_(Component.m_237115_("gui.done"), b -> ModListScreen.this.m_7379_())
                .m_252987_(((listWidth + PADDING + this.f_96543_ - doneButtonWidth) / 2), y, doneButtonWidth, BUTTON_HEIGHT)
                .m_253136_();

        openModsFolderButton = Button.m_253074_(Component.m_237115_("fml.menu.mods.openmodsfolder"), b -> Util.m_137581_().m_137644_(FMLPaths.MODSDIR.get().toFile()))
                .m_252987_(6, y, this.listWidth, BUTTON_HEIGHT)
                .m_253136_();

        y -= BUTTON_HEIGHT + PADDING;
        configButton = Button.m_253074_(Component.m_237115_("fml.menu.mods.config"), b -> ModListScreen.this.displayModConfig())
                .m_252987_(6, y, this.listWidth, BUTTON_HEIGHT)
                .m_253136_();

        y -= 14 + PADDING;
        search = new EditBox(getFontRenderer(), PADDING + 1, y, listWidth - 2, 14, Component.m_237115_("fml.menu.mods.search"));

        y -= font.f_92710_;
        int width = font.m_92724_(search.m_6035_().m_7532_());
        searchText = new StringWidget(search.m_252754_() + (search.m_5711_() /2) - (width / 2), y, width, font.f_92710_, search.m_6035_(), font);

        int height = y - (PADDING + BUTTON_HEIGHT + PADDING) - PADDING;
        this.modList = new ModListWidget(this, listWidth, fullButtonHeight, height);
        this.modList.m_252865_(6);
        this.modInfo = new InfoPanel(this.f_96541_, modInfoWidth, this.f_96544_ - PADDING - fullButtonHeight, PADDING);

        this.m_142416_(modList);
        this.m_142416_(modInfo);
        this.m_142416_(searchText);
        this.m_142416_(search);
        this.m_142416_(doneButton);
        this.m_142416_(configButton);
        this.m_142416_(openModsFolderButton);

        search.m_93692_(false);
        search.m_94190_(true);
        configButton.f_93623_ = false;

        width = listWidth / NUM_BUTTONS;
        int x = PADDING;
        m_142416_(SortType.NORMAL.button = Button.m_253074_(SortType.NORMAL.getButtonText(), b -> resortMods(SortType.NORMAL))
                .m_252987_(x, PADDING, width - BUTTON_MARGIN, BUTTON_HEIGHT)
                .m_253136_());

        x += width + BUTTON_MARGIN;
        m_142416_(SortType.A_TO_Z.button = Button.m_253074_(SortType.A_TO_Z.getButtonText(), b -> resortMods(SortType.A_TO_Z))
                .m_252987_(x, PADDING, width - BUTTON_MARGIN, BUTTON_HEIGHT)
                .m_253136_());

        x += width + BUTTON_MARGIN;
        m_142416_(SortType.Z_TO_A.button = Button.m_253074_(SortType.Z_TO_A.getButtonText(), b -> resortMods(SortType.Z_TO_A))
                .m_252987_(x, PADDING, width - BUTTON_MARGIN, BUTTON_HEIGHT)
                .m_253136_());

        resortMods(SortType.NORMAL);
        updateCache();
    }

    private void displayModConfig() {
        if (selected == null)
            return;

        try {
            ConfigScreenHandler.getScreenFactoryFor(selected.getInfo())
                    .map(f -> f.apply(this.f_96541_, this))
                    .ifPresent(newScreen -> this.f_96541_.m_91152_(newScreen));
        } catch (final Exception e) {
            LOGGER.error("There was a critical issue trying to build the config GUI for {}", selected.getInfo().getModId(), e);
        }
    }

    @Override
    public void m_86600_() {
        modList.m_6987_(selected);

        if (!search.m_94155_().equals(lastFilterText)) {
            reloadMods();
            sorted = false;
        }

        if (!sorted) {
            reloadMods();
            mods.sort(sortType);
            modList.refreshList();
            if (selected != null) {
                selected = modList.m_6702_().stream()
                        .filter(e -> e.getInfo() == selected.getInfo())
                        .findFirst()
                        .orElse(null);
                updateCache();
            }
            sorted = true;
        }
    }

    public <T extends ObjectSelectionList.Entry<T>> void buildModList(Consumer<T> modListViewConsumer, Function<IModInfo, T> newEntry) {
        for (IModInfo mod : mods) {
            modListViewConsumer.accept(newEntry.apply(mod));
        }
    }

    private void reloadMods() {
        this.mods = this.unsortedMods
            .stream()
            .filter(mi ->
                StringUtils.toLowerCase(stripControlCodes(mi.getDisplayName()))
                    .contains(StringUtils.toLowerCase(search.m_94155_()))
            ).collect(Collectors.toList());
        lastFilterText = search.m_94155_();
    }

    private void resortMods(SortType newSort) {
        this.sortType = newSort;

        for (SortType sort : SortType.values()) {
            if (sort.button != null)
                sort.button.f_93623_ = sortType != sort;
        }

        sorted = false;
    }

    @Override
    public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.modInfo != null)
            this.modInfo.m_88315_(guiGraphics, mouseX, mouseY, partialTick);

        super.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
    }

    public Minecraft getMinecraftInstance() {
        return f_96541_;
    }

    public Font getFontRenderer() {
        return f_96547_;
    }

    public void setSelected(ModListWidget.ModEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        updateCache();
    }

    record Logo(ResourceLocation texture, Size2i size) {}
    private static final Logo NONE = new Logo(null, new Size2i(0, 0));

    private void updateCache() {
        if (selected == null) {
            this.configButton.f_93623_ = false;
            this.modInfo.clearInfo();
            return;
        }

        IModInfo selectedMod = selected.getInfo();
        this.configButton.f_93623_ = ConfigScreenHandler.getScreenFactoryFor(selectedMod).isPresent();
        List<String> lines = new ArrayList<>();
        VersionChecker.CheckResult vercheck = VersionChecker.getResult(selectedMod);

        var logoData = NONE;
        var logoFile = selectedMod.getLogoFile().orElse(null);
        if (logoFile != null) {
            TextureManager tm = this.f_96541_.m_91097_();

            try {
                NativeImage logo = null;
                var modfile = ModList.get().getModFileById(selectedMod.getModId());
                if (modfile != null) {
                    var path = modfile.getFile().findResource(logoFile);
                    if (Files.exists(path))
                        logo = NativeImage.m_85058_(Files.newInputStream(path));
                }

                if (logo != null) {

                    var texture = new DynamicTexture(() -> logoFile, logo); /* {
                        @Override
                        public void upload() {
                            var pixels = this.getPixels();
                            if (pixels != null) {
                                this.bind();
                                // Use custom "blur" value which controls texture filtering (nearest-neighbor vs linear)
                                pixels.upload(0, 0, 0, selectedMod.getLogoBlur());
                            }
                        }
                    };
                    */

                    tm.m_118495_(LOGO, texture);
                    var size = new Size2i(logo.m_84982_(), logo.m_85084_());

                    logoData = new Logo(LOGO, size);
                }
            } catch (IOException e) { }
        }

        lines.add(selectedMod.getDisplayName());
        lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.version", MavenVersionStringHelper.artifactVersionToString(selectedMod.getVersion())));
        lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.idstate", selectedMod.getModId(), ModList.get().getModContainerById(selectedMod.getModId()).
                map(ModContainer::getCurrentState).map(Object::toString).orElse("NONE")));

        selectedMod.getConfig().getConfigElement("credits").ifPresent(credits->
                lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.credits", credits)));
        selectedMod.getConfig().getConfigElement("authors").ifPresent(authors ->
                lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.authors", authors)));
        selectedMod.getConfig().getConfigElement("displayURL").ifPresent(displayURL ->
                lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.displayurl", displayURL)));
        if (selectedMod.getOwningFile() == null || selectedMod.getOwningFile().getMods().size() == 1)
            lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.nochildmods"));
        else
            lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.childmods", selectedMod.getOwningFile().getMods().stream().map(IModInfo::getDisplayName).collect(Collectors.joining(","))));

        if (vercheck.status().isOutdated())
            lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.updateavailable", vercheck.url() == null ? "" : vercheck.url()));
        lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.license", ((ModFileInfo) selectedMod.getOwningFile()).getLicense()));
        lines.add(null);
        lines.add(selectedMod.getDescription());

        /* Removed because people bitched that this information was misleading.
        lines.add(null);
        if (FMLEnvironment.secureJarsEnabled) {
            lines.add(ForgeI18getOwningFile().getFile().n.parseMessage("fml.menu.mods.info.signature", selectedMod.getOwningFile().getCodeSigningFingerprint().orElse(ForgeI18n.parseMessage("fml.menu.mods.info.signature.unsigned"))));
            lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.trust", selectedMod.getOwningFile().getTrustData().orElse(ForgeI18n.parseMessage("fml.menu.mods.info.trust.noauthority"))));
        } else {
            lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.securejardisabled"));
        }
        */

        if ((vercheck.status().isOutdated()) && !vercheck.changes().isEmpty()) {
            lines.add(null);
            lines.add(ForgeI18n.parseMessage("fml.menu.mods.info.changelogheader"));
            for (Entry<ComparableVersion, String> entry : vercheck.changes().entrySet()) {
                lines.add("  " + entry.getKey() + ":");
                lines.add(entry.getValue());
                lines.add(null);
            }
        }

        modInfo.setInfo(lines, logoData.texture(), logoData.size());
    }

    @Override
    public void m_6574_(Minecraft mc, int width, int height) {
        String s = this.search.m_94155_();
        SortType sort = this.sortType;
        ModListWidget.ModEntry selected = this.selected;
        this.m_6575_(mc, width, height);
        this.search.m_94144_(s);
        this.selected = selected;

        if (!this.search.m_94155_().isEmpty())
            reloadMods();

        if (sort != SortType.NORMAL)
            resortMods(sort);

        updateCache();
    }

    @Override
    public void m_7379_() {
        this.f_96541_.m_91152_(this.parentScreen);
    }

    @Override
    protected void m_401276_(Minecraft mc, ClickEvent event) {
        if (mc.f_91074_ == null)
            m_403955_(event, mc, this);
        else
            m_401746_(event, mc, this);
    }
}
