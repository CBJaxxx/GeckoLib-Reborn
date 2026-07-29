package net.nerdypuzzle.geckolib.parts.controller_list;

import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.entries.JSimpleListEntry;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.validation.ValidationResult;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.component.VTextField;
import net.nerdypuzzle.geckolib.element.types.AnimatedEntity;

import javax.swing.*;
import java.util.List;

/**
 * One row of the custom animation controller list: the controller's name plus
 * whether it is registered after the built-in {@code movement} controller.
 */
public class JControllerEntry extends JSimpleListEntry<AnimatedEntity.ControllerEntry> {

    private final VTextField name = new VTextField(18);
    private final JCheckBox additive = L10N.checkbox("elementgui.animatedentity.controller_additive");
    private final JCheckBox ownTransition = L10N.checkbox("elementgui.animatedentity.controller_own_transition");
    private final JSpinner transitionTicks = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

    public JControllerEntry(MCreator mcreator, IHelpContext gui, JPanel parent, List<JControllerEntry> entryList) {
        super(parent, entryList);

        additive.setOpaque(false);
        additive.setToolTipText(L10N.t("elementgui.animatedentity.controller_additive_tooltip"));

        // The name ends up in generated Java identifiers, so reject anything that
        // is not a plain identifier right in the editor instead of at build time.
        name.setValidator(new Validator() {
            @Override public ValidationResult validate() {
                String value = name.getText();
                if (value == null || value.isBlank())
                    return new ValidationResult(ValidationResult.Type.ERROR,
                            L10N.t("elementgui.animatedentity.controller_error_empty"));
                if (AnimatedEntity.isReservedControllerName(value))
                    return new ValidationResult(ValidationResult.Type.ERROR,
                            L10N.t("elementgui.animatedentity.controller_error_reserved", value));
                if (!AnimatedEntity.isValidControllerName(value))
                    return new ValidationResult(ValidationResult.Type.ERROR,
                            L10N.t("elementgui.animatedentity.controller_error_name", value));
                return ValidationResult.PASSED;
            }
        });
        name.enableRealtimeValidation();

        // Unchecked means "inherit the entity's Animation transition ticks", which is
        // also what entries saved before this option existed fall back to.
        ownTransition.setOpaque(false);
        ownTransition.setToolTipText(L10N.t("elementgui.animatedentity.controller_own_transition_tooltip"));
        transitionTicks.setEnabled(false);
        ownTransition.addActionListener(event -> transitionTicks.setEnabled(ownTransition.isSelected()));

        this.line.add(L10N.label("elementgui.animatedentity.controller_name"));
        this.line.add(name);
        this.line.add(additive);
        this.line.add(ownTransition);
        this.line.add(transitionTicks);
    }

    @Override protected void setEntryEnabled(boolean enabled) {
        name.setEnabled(enabled);
        additive.setEnabled(enabled);
        ownTransition.setEnabled(enabled);
        transitionTicks.setEnabled(enabled && ownTransition.isSelected());
    }

    @Override public AnimatedEntity.ControllerEntry getEntry() {
        AnimatedEntity.ControllerEntry entry = new AnimatedEntity.ControllerEntry();
        entry.name = name.getText().trim();
        entry.additive = additive.isSelected();
        entry.transitionTicks = ownTransition.isSelected() ? (Integer) transitionTicks.getValue() : null;
        return entry;
    }

    @Override public void setEntry(AnimatedEntity.ControllerEntry entry) {
        name.setText(entry.name == null ? "" : entry.name);
        additive.setSelected(entry.additive);
        ownTransition.setSelected(entry.transitionTicks != null);
        transitionTicks.setValue(entry.transitionTicks == null ? 0 : entry.transitionTicks);
        transitionTicks.setEnabled(ownTransition.isSelected());
    }
}
