/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

import net.minecraft.client.gui.components.Button.CreateNarration;
import net.minecraft.client.gui.components.Button.OnPress;

/**
 * This class provides a button that fixes several bugs present in the vanilla GuiButton drawing code.
 * The gist of it is that it allows buttons of any size without gaps in the graphics and with the
 * borders drawn properly. It also prevents button text from extending out of the sides of the button by
 * trimming the end of the string and adding an ellipsis.<br/><br/>
 *
 * The code that handles drawing the button is in GuiUtils.
 */
public class ExtendedButton extends Button {
    public ExtendedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler) {
        this(xPos, yPos, width, height, displayString, handler, f_252438_);
    }

    public ExtendedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler, CreateNarration createNarration) {
        super(xPos, yPos, width, height, displayString, handler, createNarration);
    }

    public ExtendedButton(Button.Builder builder) {
        super(builder);
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void m_87963_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.m_91087_();
        guiGraphics.m_403642_(RenderPipelines.f_379833_, f_290895_.m_295557_(this.f_93623_, this.m_198029_()), this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_());

        final FormattedText buttonText = mc.f_91062_.ellipsize(this.m_6035_(), this.f_93618_ - 6); // Remove 6 pixels so that the text is always contained within the button's borders
        guiGraphics.m_280364_(mc.f_91062_, Language.m_128107_().m_5536_(buttonText), this.m_252754_() + this.f_93618_ / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, getFGColor());
    }
}
