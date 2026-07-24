package net.nerdypuzzle.geckolib.parts;

import net.mcreator.ui.minecraft.states.PropertyData;
import net.mcreator.workspace.Workspace;
import net.mcreator.workspace.elements.ModElement;
import net.nerdypuzzle.geckolib.element.types.AnimatedEntity;
import net.mcreator.element.types.LivingEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility helpers formerly used by the JavaFX Blockly bridge.
 * MCreator 2026.2 uses CEF + {@code BlocklyJavascriptBridge}; this class only
 * retains animated-entity data list helpers for potential future bridge hooks.
 */
public final class JavabridgeReplacement {

	private JavabridgeReplacement() {
	}

	public static List<String> loadEntityDataListFromCustomEntity(Workspace workspace, String entityName,
			Class<? extends PropertyData<?>> type) {
		if (entityName == null)
			return new ArrayList<>();

		ModElement me = workspace.getModElementByName(entityName.replace("CUSTOM:", ""));
		if (me == null)
			return new ArrayList<>();

		Object ent = me.getGeneratableElement();
		if (ent instanceof LivingEntity entity) {
			return entity.entityDataEntries.stream().filter(e -> e.property().getClass().equals(type))
					.map(e -> e.property().getName()).toList();
		}
		if (ent instanceof AnimatedEntity entity) {
			return entity.entityDataEntries.stream().filter(e -> e.property().getClass().equals(type))
					.map(e -> e.property().getName()).toList();
		}
		return new ArrayList<>();
	}

}
