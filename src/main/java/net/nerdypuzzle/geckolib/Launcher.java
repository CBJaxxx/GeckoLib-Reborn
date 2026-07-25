package net.nerdypuzzle.geckolib;

import net.mcreator.plugin.events.ui.ModElementGUIEvent;
import net.mcreator.plugin.events.workspace.MCreatorLoadedEvent;
import net.nerdypuzzle.geckolib.parts.JavabridgeReplacement;
import net.nerdypuzzle.geckolib.registry.PluginActions;
import net.nerdypuzzle.geckolib.registry.PluginElementTypes;
import net.mcreator.plugin.JavaPlugin;
import net.mcreator.plugin.Plugin;
import net.mcreator.plugin.events.PreGeneratorsLoadingEvent;
import net.nerdypuzzle.geckolib.registry.PluginEventTriggers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class Launcher extends JavaPlugin {

	public static final Logger LOG = LogManager.getLogger("GeckoLib Plugin");
	public static PluginActions ACTION_REGISTRY;
	public static Set<Plugin> PLUGIN_INSTANCE = new HashSet<>();

	public Launcher(Plugin plugin) {
		super(plugin);
		PLUGIN_INSTANCE.add(plugin);
		addListener(PreGeneratorsLoadingEvent.class, event -> {
			PluginElementTypes.load();
			// After BlocklyElementUtil static init: enable animated-entity data pickers
			JavabridgeReplacement.installEntityDataListProviders();
		});
		addListener(ModElementGUIEvent.BeforeLoading.class, event -> SwingUtilities.invokeLater(() -> {
			PluginEventTriggers.dependencyWarning(event.getMCreator(), event.getModElementGUI());
			PluginEventTriggers.interceptProcedurePanel(event.getMCreator(), event.getModElementGUI());
		}));
		// Also install on workspace load in case PreGenerators already ran without us
		addListener(MCreatorLoadedEvent.class, event -> {
			JavabridgeReplacement.installEntityDataListProviders();
			ACTION_REGISTRY = new PluginActions(event.getMCreator());
			SwingUtilities.invokeLater(() -> PluginEventTriggers.modifyMenus(event.getMCreator()));
		});

		LOG.info("Plugin was loaded");
	}

}