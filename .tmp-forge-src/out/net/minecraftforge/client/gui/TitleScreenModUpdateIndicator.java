/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.client.loading.ClientModLoader;

public class TitleScreenModUpdateIndicator extends Screen {

    private static final ResourceLocation VERSION_CHECK_ICONS = ResourceLocation.m_339182_(ForgeVersion.MOD_ID, "textures/gui/version_check_icons.png");

    private final Button modButton;
    private VersionChecker.Status showNotification = null;
    private boolean hasCheckedForUpdates = false;

    public TitleScreenModUpdateIndicator(Button modButton) {
        super(Component.m_237115_("forge.menu.updatescreen.title"));
        this.modButton = modButton;
    }

    @Override
    public void m_7856_() {
        if (!hasCheckedForUpdates) {
            if (modButton != null)
                showNotification = ClientModLoader.checkForUpdates();
            hasCheckedForUpdates = true;
        }
    }

    @Override
    public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (showNotification == null || !showNotification.shouldDraw() || !FMLConfig.getBoolConfigValue(FMLConfig.ConfigValue.VERSION_CHECK))
            return;

        int x = modButton.m_252754_();
        int y = modButton.m_252907_();
        int w = modButton.m_5711_();
        int h = modButton.m_93694_();

        guiGraphics.m_280218_(RenderPipelines.f_379833_, VERSION_CHECK_ICONS, x + w - (h / 2 + 4), y + (h / 2 - 4), showNotification.getSheetOffset() * 8, (showNotification.isAnimated() && ((System.currentTimeMillis() / 800 & 1) == 1)) ? 8 : 0, 8, 8, 64, 16);
    }

    public static TitleScreenModUpdateIndicator init(TitleScreen guiMainMenu, Button modButton) {
        TitleScreenModUpdateIndicator titleScreenModUpdateIndicator = new TitleScreenModUpdateIndicator(modButton);
        //titleScreenModUpdateIndicator.resize(guiMainMenu.getMinecraft(), guiMainMenu.width, guiMainMenu.height);
        titleScreenModUpdateIndicator.m_7856_();
        return titleScreenModUpdateIndicator;
    }

}
