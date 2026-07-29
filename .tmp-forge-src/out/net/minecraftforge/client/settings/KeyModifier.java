/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.settings;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public enum KeyModifier {
    CONTROL {
        @Override
        public boolean matches(InputConstants.Key key) {
            int keyCode = key.m_84873_();
            if (Minecraft.f_91002_)
                return keyCode == GLFW.GLFW_KEY_LEFT_SUPER || keyCode == GLFW.GLFW_KEY_RIGHT_SUPER;
            else
                return keyCode == GLFW.GLFW_KEY_LEFT_CONTROL || keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL;
        }

        @Override
        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            return Screen.m_96637_();
        }

        @Override
        public Component getCombinedName(InputConstants.Key key, Supplier<Component> defaultLogic) {
            String localizationFormatKey = Minecraft.f_91002_ ? "forge.controlsgui.control.mac" : "forge.controlsgui.control";
            return Component.m_237110_(localizationFormatKey, defaultLogic.get());
        }
    },
    SHIFT {
        @Override
        public boolean matches(InputConstants.Key key) {
            return key.m_84873_() == GLFW.GLFW_KEY_LEFT_SHIFT || key.m_84873_() == GLFW.GLFW_KEY_RIGHT_SHIFT;
        }

        @Override
        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            return Screen.m_96638_();
        }

        @Override
        public Component getCombinedName(InputConstants.Key key, Supplier<Component> defaultLogic) {
            return Component.m_237110_("forge.controlsgui.shift", defaultLogic.get());
        }
    },
    ALT {
        @Override
        public boolean matches(InputConstants.Key key) {
            return key.m_84873_() == GLFW.GLFW_KEY_LEFT_ALT || key.m_84873_() == GLFW.GLFW_KEY_RIGHT_ALT;
        }

        @Override
        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            return Screen.m_96639_();
        }

        @Override
        public Component getCombinedName(InputConstants.Key keyCode, Supplier<Component> defaultLogic) {
            return Component.m_237110_("forge.controlsgui.alt", defaultLogic.get());
        }
    },
    NONE {
        @Override
        public boolean matches(InputConstants.Key key) {
            return false;
        }

        @Override
        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            if (conflictContext != null && !conflictContext.conflicts(KeyConflictContext.IN_GAME)) {
                for (KeyModifier keyModifier : VALUES) {
                    if (keyModifier.isActive(conflictContext))
                        return false;
                }
            }
            return true;
        }

        @Override
        public Component getCombinedName(InputConstants.Key key, Supplier<Component> defaultLogic) {
            return defaultLogic.get();
        }
    };

    private static final KeyModifier[] VALUES = {SHIFT, CONTROL, ALT};
    private static final List<KeyModifier> VALUES_LIST = List.of(SHIFT, CONTROL, ALT);
    private static final List<KeyModifier> ALL = List.of(SHIFT, CONTROL, ALT, NONE);
    public static final List<KeyModifier> getValues(boolean includeNone) {
        return includeNone ? ALL : VALUES_LIST;
    }

    @Nullable
    public static KeyModifier getModifier(InputConstants.Key key) {
        for (var modifier : VALUES) {
            if (modifier.matches(key))
                return modifier;
        }
        return null;
    }

    public static boolean isKeyCodeModifier(InputConstants.Key key) {
        for (KeyModifier keyModifier : VALUES) {
            if (keyModifier.matches(key))
                return true;
        }
        return false;
    }

    public static KeyModifier valueFromString(String stringValue) {
        try {
            return valueOf(stringValue);
        } catch (NullPointerException | IllegalArgumentException ignored) {
            return NONE;
        }
    }

    public abstract boolean matches(InputConstants.Key key);

    public abstract boolean isActive(@Nullable IKeyConflictContext conflictContext);

    public abstract Component getCombinedName(InputConstants.Key key, Supplier<Component> defaultLogic);
}
