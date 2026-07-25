package net.nerdypuzzle.geckolib.parts;

import net.mcreator.element.types.LivingEntity;
import net.mcreator.ui.blockly.BlocklyElementUtil;
import net.mcreator.ui.minecraft.states.PropertyData;
import net.mcreator.workspace.Workspace;
import net.mcreator.workspace.elements.ModElement;
import net.nerdypuzzle.geckolib.Launcher;
import net.nerdypuzzle.geckolib.element.types.AnimatedEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Entity-data helpers for procedure Blockly selectors.
 * <p>
 * Vanilla MCreator only resolves synced data from {@link LivingEntity}. Animated
 * entities store the same {@code entityDataEntries} list but are a separate
 * generatable type, so selectors were always empty for them.
 * <p>
 * On load we replace the {@code entitydata_*} providers in
 * {@link BlocklyElementUtil} so logic/integer/string data pickers include
 * Animated Entity parameters.
 */
public final class JavabridgeReplacement {

	private JavabridgeReplacement() {
	}

	/**
	 * Install entitydata_* list providers that support both living and animated entities.
	 * Safe to call more than once (idempotent put of the same keys).
	 */
	public static void installEntityDataListProviders() {
		try {
			Map<String, BlocklyElementUtil.StringArrayEntrySelectorProvider> providers =
					BlocklyElementUtil.getStringArrayEntryProviders();
			if (providers == null)
				return;

			providers.put("entitydata_logic",
					(ws, entity) -> loadEntityDataListFromCustomEntity(ws, entity, PropertyData.LogicType.class)
							.toArray(String[]::new));
			providers.put("entitydata_integer",
					(ws, entity) -> loadEntityDataListFromCustomEntity(ws, entity, PropertyData.IntegerType.class)
							.toArray(String[]::new));
			providers.put("entitydata_string",
					(ws, entity) -> loadEntityDataListFromCustomEntity(ws, entity, PropertyData.StringType.class)
							.toArray(String[]::new));

			Launcher.LOG.info("Installed Animated Entity entity-data Blockly selectors");
		} catch (Throwable t) {
			Launcher.LOG.warn("Failed to install Animated Entity entity-data selectors", t);
		}
	}

	/**
	 * Names of synced data parameters of the given property type on a custom entity
	 * (Living Entity or Animated Entity).
	 */
	public static List<String> loadEntityDataListFromCustomEntity(Workspace workspace, String entityName,
			Class<? extends PropertyData<?>> type) {
		if (entityName == null || workspace == null)
			return new ArrayList<>();

		ModElement me = workspace.getModElementByName(entityName.replace("CUSTOM:", ""));
		if (me == null)
			return new ArrayList<>();

		Object ent = me.getGeneratableElement();
		if (ent instanceof LivingEntity living) {
			return filterDataNames(living.entityDataEntries, type);
		}
		if (ent instanceof AnimatedEntity animated) {
			return filterDataNames(animated.entityDataEntries, type);
		}
		return new ArrayList<>();
	}

	private static List<String> filterDataNames(List<? extends net.mcreator.ui.minecraft.states.PropertyDataWithValue<?>> entries,
			Class<? extends PropertyData<?>> type) {
		if (entries == null || entries.isEmpty())
			return new ArrayList<>();
		return entries.stream()
				.filter(e -> e != null && e.property() != null && e.property().getClass().equals(type))
				.map(e -> e.property().getName())
				.toList();
	}

}
