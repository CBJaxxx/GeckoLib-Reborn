/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.extensions;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;

/**
 * Extension interface for {@link Font}.
 */
public interface IForgeFont {
    FormattedText ELLIPSIS = FormattedText.m_130775_("...");

    default Font self() {
        return (Font)this;
    }

    /**
     * If the width of the text exceeds {@code maxWidth}, an ellipse is added and the text is substringed.
     *
     * @param text     the text to ellipsize if needed
     * @param maxWidth the maximum width of the text
     * @return the ellipsized text
     */
    default FormattedText ellipsize(FormattedText text, int maxWidth) {
        final Font self = self();
        final int strWidth = self.m_92852_(text);
        final int ellipsisWidth = self.m_92852_(ELLIPSIS);
        if (strWidth > maxWidth) {
            if (ellipsisWidth >= maxWidth)
                return self.m_92854_(text, maxWidth);
            return FormattedText.m_130773_(
                self.m_92854_(text, maxWidth - ellipsisWidth),
                ELLIPSIS
            );
        }
        return text;
    }
}
