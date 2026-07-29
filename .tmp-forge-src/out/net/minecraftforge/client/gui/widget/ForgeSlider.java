/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;

/**
 * Slider widget implementation which allows inputting values in a certain range with optional step size.
 */
public class ForgeSlider extends AbstractSliderButton {
    protected Component prefix;
    protected Component suffix;

    protected double minValue;
    protected double maxValue;

    /** Allows input of discontinuous values with a certain step */
    protected double stepSize;

    protected boolean drawString;

    private final DecimalFormat format;

    /**
     * @param x x position of upper left corner
     * @param y y position of upper left corner
     * @param width Width of the widget
     * @param height Height of the widget
     * @param prefix {@link Component} displayed before the value string
     * @param suffix {@link Component} displayed after the value string
     * @param minValue Minimum (left) value of slider
     * @param maxValue Maximum (right) value of slider
     * @param currentValue Starting value when widget is first displayed
     * @param stepSize Size of step used. Precision will automatically be calculated based on this value if this value is not 0.
     * @param precision Only used when {@code stepSize} is 0. Limited to a maximum of 4 (inclusive).
     * @param drawString Should text be displayed on the widget
     */
    public ForgeSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString) {
        super(x, y, width, height, Component.m_237119_(), 0D);
        this.prefix = prefix;
        this.suffix = suffix;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = Math.abs(stepSize);
        this.f_93577_ = this.snapToNearest((currentValue - minValue) / (maxValue - minValue));
        this.drawString = drawString;

        if (stepSize == 0D) {
            precision = Math.min(precision, 4);

            StringBuilder builder = new StringBuilder("0");

            if (precision > 0)
                builder.append('.');

            while (precision-- > 0)
                builder.append('0');

            this.format = new DecimalFormat(builder.toString());
        } else if (Mth.m_14082_(this.stepSize, Math.floor(this.stepSize))) {
            this.format = new DecimalFormat("0");
        } else {
            this.format = new DecimalFormat(Double.toString(this.stepSize).replaceAll("\\d", "0"));
        }

        this.m_5695_();
    }

    /**
     * Overload with {@code stepSize} set to 1, useful for sliders with whole number values.
     */
    public ForgeSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString) {
        this(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, 1D, 0, drawString);
    }

    /**
     * @return Current slider value as a double
     */
    public double getValue() {
        return this.f_93577_ * (maxValue - minValue) + minValue;
    }

    /**
     * @return Current slider value as an long
     */
    public long getValueLong() {
        return Math.round(this.getValue());
    }

    /**
     * @return Current slider value as an int
     */
    public int getValueInt() {
        return (int) this.getValueLong();
    }

    /**
     * @param value The new slider value
     */
    public void setValue(double value) {
        double oldValue = this.f_93577_;
        this.f_93577_ = this.snapToNearest((value - this.minValue) / (this.maxValue - this.minValue));
        if (!Mth.m_14082_(oldValue, this.f_93577_))
            this.m_5697_();
        this.m_5695_();
    }

    public String getValueString() {
        return this.format.format(this.getValue());
    }

    @Override
    public void m_5716_(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    @Override
    protected void m_7212_(double mouseX, double mouseY, double dragX, double dragY) {
        super.m_7212_(mouseX, mouseY, dragX, dragY);
        this.setValueFromMouse(mouseX);
    }

    @Override
    public boolean m_7933_(int keyCode, int scanCode, int modifiers) {
        boolean flag = keyCode == GLFW.GLFW_KEY_LEFT;
        if (flag || keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (this.minValue > this.maxValue)
                flag = !flag;
            float f = flag ? -1F : 1F;
            if (stepSize <= 0D)
                this.setSliderValue(this.f_93577_ + (f / (this.f_93618_ - 8)));
            else
                this.setValue(this.getValue() + f * this.stepSize);
        }

        return false;
    }

    private void setValueFromMouse(double mouseX) {
        this.setSliderValue((mouseX - (this.m_252754_() + 4)) / (this.f_93618_ - 8));
    }

    /**
     * @param value Percentage of slider range
     */
    private void setSliderValue(double value) {
        double oldValue = this.f_93577_;
        this.f_93577_ = this.snapToNearest(value);
        if (!Mth.m_14082_(oldValue, this.f_93577_))
            this.m_5697_();

        this.m_5695_();
    }

    /**
     * Snaps the value, so that the displayed value is the nearest multiple of {@code stepSize}.
     * If {@code stepSize} is 0, no snapping occurs.
     */
    private double snapToNearest(double value) {
        if(stepSize <= 0D)
            return Mth.m_14008_(value, 0D, 1D);

        value = Mth.m_14139_(Mth.m_14008_(value, 0D, 1D), this.minValue, this.maxValue);

        value = (stepSize * Math.round(value / stepSize));

        if (this.minValue > this.maxValue)
            value = Mth.m_14008_(value, this.maxValue, this.minValue);
        else
            value = Mth.m_14008_(value, this.minValue, this.maxValue);

        return Mth.m_144914_(value, this.minValue, this.maxValue, 0D, 1D);
    }

    @Override
    protected void m_5695_() {
        if (this.drawString)
            this.m_93666_(Component.m_237113_("").m_7220_(prefix).m_130946_(this.getValueString()).m_7220_(suffix));
        else
            this.m_93666_(Component.m_237119_());
    }

    @Override
    protected void m_5697_() {}

    @Override
    public void m_87963_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        final Minecraft mc = Minecraft.m_91087_();
        guiGraphics.m_403642_(RenderPipelines.f_379833_, this.m_293389_(), this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_());
        guiGraphics.m_403642_(RenderPipelines.f_379833_, this.m_293290_(), this.m_252754_() + (int)(this.f_93577_ * (double)(this.f_93618_ - 8)), this.m_252907_(), 8, this.m_93694_());

        m_280372_(guiGraphics, mc.f_91062_, 2, getFGColor() | Mth.m_14167_(this.f_93625_ * 255.0F) << 24);
    }
}
