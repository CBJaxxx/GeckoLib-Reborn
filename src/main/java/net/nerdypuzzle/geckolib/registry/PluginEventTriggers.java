package net.nerdypuzzle.geckolib.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.mcreator.io.net.WebIO;
import net.mcreator.plugin.PluginUpdateInfo;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.MCreatorApplication;
import net.mcreator.ui.dialogs.MCreatorDialog;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.variants.modmaker.ModMaker;
import net.mcreator.util.DesktopUtils;
import net.mcreator.util.image.ImageUtils;
import net.nerdypuzzle.geckolib.Launcher;
import net.nerdypuzzle.geckolib.element.types.GeckolibElement;
import net.nerdypuzzle.geckolib.parts.PluginPanelGeckolib;
import net.nerdypuzzle.geckolib.parts.PluginPanelUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * UI hooks for GeckoLib on MCreator 2026.2.
 * <p>
 * CEF Blockly does not need JavaFX bridge replacement. Animated-entity synced data
 * selectors are installed via {@link net.nerdypuzzle.geckolib.parts.JavabridgeReplacement}
 * (replaces {@code entitydata_*} providers so Animated Entity parameters appear).
 */
public class PluginEventTriggers {

	private static final Set<PluginUpdateInfo> pluginUpdates = new HashSet<>();

	private static void checkForPluginUpdates() {
		if (!MCreatorApplication.isInternet)
			return;
		pluginUpdates.addAll(Launcher.PLUGIN_INSTANCE.parallelStream().map((plugin) -> {
			try {
				String updateUrl = plugin.getInfo().getUpdateJSONURL();
				if (updateUrl == null || updateUrl.isBlank())
					return null;
				String updateJSON = WebIO.readURLToString(updateUrl);
				if (updateJSON == null || updateJSON.isBlank())
					return null;
				JsonObject root = JsonParser.parseString(updateJSON).getAsJsonObject();
				if (!root.has(plugin.getID()))
					return null;
				JsonObject updateData = root.get(plugin.getID()).getAsJsonObject();
				String version = updateData.get("latest").getAsString();
				if (!version.equals(plugin.getPluginVersion())) {
					return new PluginUpdateInfo(plugin, version,
							updateData.has("changes") ?
									updateData.get("changes").getAsJsonArray().asList().stream()
											.map(JsonElement::getAsString).toList() :
									null);
				}
			} catch (Exception ex) {
				// Ignore update check failures (offline / missing update JSON)
			}
			return null;
		}).filter(Objects::nonNull).toList());
	}

	public static void dependencyWarning(MCreator mcreator, ModElementGUI<?> modElement) {
		if (!mcreator.getWorkspaceSettings().getDependencies().contains("geckolib")
				&& modElement instanceof GeckolibElement) {
			JOptionPane.showMessageDialog(mcreator, L10N.t("dialog.geckolib.enable_geckolib"),
					L10N.t("dialog.geckolib.error_no_dependency"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * No-op on 2026.2: Blockly is CEF-based and does not need JavaFX bridge replacement.
	 */
	public static void interceptProcedurePanel(MCreator mcreator, ModElementGUI<?> modElement) {
		// Intentionally empty
	}

	public static void forceCheckUpdates(MCreator mcreator) {
		checkForPluginUpdates();
		Collection<PluginUpdateInfo> pluginUpdateInfos = pluginUpdates;
		if (pluginUpdateInfos.isEmpty())
			return;

		JPanel pan = new JPanel(new BorderLayout(10, 15));
		JPanel plugins = new JPanel(new GridLayout(0, 1, 10, 10));
		JPanel scrollWrapper = new JPanel(new BorderLayout());
		scrollWrapper.add("North", plugins);
		pan.add("North", new JScrollPane(scrollWrapper));
		pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pan.setPreferredSize(new Dimension(560, 250));

		for (PluginUpdateInfo pluginUpdateInfo : pluginUpdateInfos) {
			StringBuilder usb = new StringBuilder(
					L10N.t("dialog.plugin_update_notify.version_message", pluginUpdateInfo.plugin().getInfo().getName(),
							pluginUpdateInfo.plugin().getInfo().getVersion(), pluginUpdateInfo.newVersion()));
			if (pluginUpdateInfo.recentChanges() != null) {
				usb.append("<br>").append(L10N.t("dialog.plugin_update_notify.changes_message")).append("<ul>");
				for (String change : pluginUpdateInfo.recentChanges()) {
					usb.append("<li>").append(change).append("</li>");
				}
			}

			JLabel label = new JLabel(usb.toString());
			JButton update = L10N.button("dialog.plugin_update_notify.update");
			update.addActionListener(e -> DesktopUtils.browseSafe(
					"https://mcreator.net/node/" + pluginUpdateInfo.plugin().getInfo().getPluginPageID()));
			JPanel labelPanel = new JPanel(new BorderLayout());
			labelPanel.setOpaque(false);
			labelPanel.add("West", label);
			labelPanel.add("East", PluginPanelUtils.join(update));
			plugins.add(labelPanel);
		}

		MCreatorDialog dialog = new MCreatorDialog(mcreator, L10N.t("dialog.plugin_update_notify.update_title"));
		dialog.setSize(700, 200);
		dialog.setLocationRelativeTo(mcreator);
		dialog.setModal(true);
		JButton close = L10N.button("dialog.plugin_update_notify.close");
		close.addActionListener(e -> dialog.setVisible(false));
		JPanel dialogCenterPanel = new JPanel(new BorderLayout());
		dialogCenterPanel.setOpaque(false);
		dialogCenterPanel.add("Center", pan);
		dialogCenterPanel.add("South", PluginPanelUtils.join(close));
		dialog.add("Center", dialogCenterPanel);
		dialog.setVisible(true);
	}

	public static void modifyMenus(MCreator mcreator) {
		JMenu geckolib = L10N.menu("menubar.geckolib");
		geckolib.setMnemonic('R');
		geckolib.setIcon(new ImageIcon(ImageUtils.resizeAA(UIRES.get("16px.geckolibicon").getImage(), 17, 17)));
		geckolib.add(Launcher.ACTION_REGISTRY.importGeckoLibModel);
		geckolib.add(Launcher.ACTION_REGISTRY.importDisplaySettings);
		geckolib.addSeparator();
		geckolib.add(Launcher.ACTION_REGISTRY.convertion_to_geckolib);
		geckolib.add(Launcher.ACTION_REGISTRY.convertion_from_geckolib);
		geckolib.addSeparator();
		geckolib.add(Launcher.ACTION_REGISTRY.tutorial);

		if (mcreator instanceof ModMaker modmaker) {
			PluginPanelGeckolib panel = new PluginPanelGeckolib(modmaker.getWorkspacePanel());
			panel.setOpaque(false);
			modmaker.getWorkspacePanel().resourcesPan.addResourcesTab("geckolib", panel);
			mcreator.getMainMenuBar().add(geckolib);
		}

		forceCheckUpdates(mcreator);
	}

}
