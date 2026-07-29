package net.nerdypuzzle.geckolib.parts.controller_list;

import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleEntriesList;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.themes.Theme;
import net.nerdypuzzle.geckolib.element.types.AnimatedEntity;

import javax.swing.*;
import java.util.List;

/**
 * Editor for the entity's custom GeckoLib animation controllers.
 *
 * <p>Entries can be reordered because registration order is what decides layering
 * in GeckoLib: within the same additive group, a controller further down the list
 * is registered later and therefore wins on any bone both animations touch.
 */
public class JControllerList extends JSimpleEntriesList<JControllerEntry, AnimatedEntity.ControllerEntry> {

    public JControllerList(MCreator mcreator, IHelpContext gui) {
        super(mcreator, gui, true);
        this.add.setText(L10N.t("elementgui.animatedentity.add_controller"));
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.current().getForegroundColor(), 1),
                L10N.t("elementgui.animatedentity.controllers_boarder"), 0, 0,
                this.getFont().deriveFont(12.0F), Theme.current().getForegroundColor()));
    }

    @Override protected JControllerEntry newEntry(JPanel parent, List<JControllerEntry> entryList,
            boolean userAction) {
        return new JControllerEntry(this.mcreator, this.gui, parent, entryList);
    }
}
