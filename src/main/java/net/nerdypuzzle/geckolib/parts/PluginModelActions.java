package net.nerdypuzzle.geckolib.parts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.mcreator.generator.GeneratorUtils;
import net.mcreator.io.FileIO;
import net.mcreator.io.Transliteration;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.action.impl.workspace.resources.ModelImportActions;
import net.mcreator.ui.dialogs.file.FileDialogs;
import net.mcreator.ui.init.L10N;
import net.nerdypuzzle.geckolib.registry.PluginActions;

import javax.annotation.Nullable;
import javax.swing.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PluginModelActions extends ModelImportActions {

	/**
	 * GeckoLib 4.x (Forge 1.20.1 / NeoForge 1.21.1): assets/&lt;modid&gt;/geo and animations
	 * GeckoLib 5.x (NeoForge 26.1.2): assets/&lt;modid&gt;/geckolib/models and geckolib/animations
	 */
	private static boolean usesGeckoLib5AssetLayout(MCreator mcreator) {
		try {
			String name = mcreator.getWorkspace().getGenerator().getGeneratorName();
			if (name == null)
				return true;
			return name.contains("26.") || name.contains("1.21.8") || name.contains("1.21.4")
					|| name.contains("1.21.5") || name.contains("1.21.6") || name.contains("1.21.7");
		} catch (Exception e) {
			return true;
		}
	}

	public static File getAnimationsDir(MCreator mcreator) {
		File root = assetsRoot(mcreator);
		if (usesGeckoLib5AssetLayout(mcreator))
			// GeckoLib 5: bake keys include subfolders (entity/, item/, block/, …)
			// Import into models root; users/docs prefer entity/ or item/ subdirs.
			// Default shared import still uses entity/ for entity-first workflow;
			// item models also fall back to entity/ and flat paths in templates.
			return new File(root, "geckolib/animations/entity/");
		// GeckoLib 4 recommended layout: animations/entity/
		return new File(root, "animations/entity/");
	}

	public static File getGeometryDir(MCreator mcreator) {
		File root = assetsRoot(mcreator);
		if (usesGeckoLib5AssetLayout(mcreator))
			// GeckoLib 5 Defaulted*GeoModel: entity/ for entities, item/ for items.
			// Shared importer defaults to entity/; item GeoModels fall back to entity/ + flat.
			return new File(root, "geckolib/models/entity/");
		// GeckoLib 4 recommended layout: geo/entity/
		return new File(root, "geo/entity/");
	}

	public static File getDisplaySettingsDir(MCreator mcreator) {
		return new File(assetsRoot(mcreator), "models/displaysettings/");
	}

	private static File assetsRoot(MCreator mcreator) {
		return new File(GeneratorUtils.getSpecificRoot(mcreator.getWorkspace(),
				mcreator.getWorkspace().getGeneratorConfiguration(), "mod_assets_root"), "");
	}

	private static void ensureDir(File dir) {
		if (!dir.exists() && !dir.mkdirs()) {
			// ignore; copy will fail and be reported by FileIO if needed
		}
	}

	private static List<File> listModelsInDir(@Nullable File dir) {
		if (dir == null || !dir.isDirectory()) {
			return Collections.emptyList();
		}
		List<File> retval = new ArrayList<>();
		File[] children = dir.listFiles();
		if (children == null)
			return retval;
		for (File element : children) {
			if (element.isDirectory()) {
				// Recurse so geo/entity/*.geo.json and flat geo/*.geo.json both appear
				retval.addAll(listModelsInDir(element));
			} else if (element.getName().endsWith(".geo.json")) {
				retval.add(element);
			}
		}
		return retval;
	}

	private static List<File> listDisplaySettingsInDir(@Nullable File dir) {
		if (dir == null) {
			return Collections.emptyList();
		} else {
			List<File> retval = new ArrayList<>();
			File[] block = dir.listFiles();
			File[] var3 = block != null ? block : new File[0];
			int var4 = var3.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				File element = var3[var5];
				if (element.getName().endsWith(".json")) {
					retval.add(element);
				}
			}

			return retval;
		}
	}

	public static List<File> getGeomodels(MCreator mcreator) {
		Set<File> all = new LinkedHashSet<>();
		all.addAll(listModelsInDir(getGeometryDir(mcreator)));
		File root = assetsRoot(mcreator);
		all.addAll(listModelsInDir(new File(root, "geo/")));
		all.addAll(listModelsInDir(new File(root, "geckolib/models/")));
		return new ArrayList<>(all);
	}

	public static List<File> getDisplaysettings(MCreator mcreator) {
		return listDisplaySettingsInDir(getDisplaySettingsDir(mcreator));
	}

	public static class GECKOLIB extends BasicAction {
		public GECKOLIB(PluginActions actionRegistry) {
			super(actionRegistry, L10N.t("action.workspace.resources.import_geckolib_model"), actionEvent -> {
				// MCreator 2026.2 FileDialogs reject compound extensions like ".geo.json"
				// (IllegalArgumentException: Extension must not contain '.' or '*').
				// Open as plain JSON and validate the suffix ourselves.
				File geoModel = FileDialogs.getOpenDialog(actionRegistry.getMCreator(), new String[]{".json"});
				if (geoModel != null) {
					if (!geoModel.getName().toLowerCase(Locale.ENGLISH).endsWith(".geo.json")) {
						JOptionPane.showMessageDialog(actionRegistry.getMCreator(),
								"Please select a Blockbench geometry file ending with .geo.json",
								L10N.t("action.workspace.resources.import_geckolib_model"),
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					importGeckoLibModels(actionRegistry.getMCreator(), geoModel);
				}
			});
		}
	}

	public static class DISPLAYSETTINGS extends BasicAction {
		public DISPLAYSETTINGS(PluginActions actionRegistry) {
			super(actionRegistry, L10N.t("action.workspace.resources.import_display_settings"), actionEvent -> {
				File displaySettings = FileDialogs.getOpenDialog(actionRegistry.getMCreator(), new String[]{".json"});
				if (displaySettings != null)
					importDisplaySettings(actionRegistry.getMCreator(), displaySettings);
			});
		}
	}

	public static void importGeckoLibModels(MCreator mcreator, File geoModel) {
		File geoDir = PluginModelActions.getGeometryDir(mcreator);
		ensureDir(geoDir);
		String baseName = Transliteration.transliterateString(geoModel.getName()).toLowerCase(Locale.ENGLISH).trim()
				.replace(":", "").replace(" ", "_");
		FileIO.copyFile(geoModel, new File(geoDir, baseName));

		// Second dialog for animation (plain .json filter; require .animation.json suffix)
		File animation = FileDialogs.getOpenDialog(mcreator, new String[]{".json"});
		if (animation != null) {
			if (!animation.getName().toLowerCase(Locale.ENGLISH).endsWith(".animation.json")) {
				JOptionPane.showMessageDialog(mcreator,
						"Please select a Blockbench animation file ending with .animation.json",
						L10N.t("action.workspace.resources.import_geckolib_model"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			File animDir = PluginModelActions.getAnimationsDir(mcreator);
			ensureDir(animDir);
			String animName = baseName.replace(".geo.", ".animation.");
			// If user renamed differently, still prefer geo-derived name for pairing
			if (!animName.endsWith(".animation.json"))
				animName = baseName.replace(".geo.json", ".animation.json");
			FileIO.copyFile(animation, new File(animDir, animName));
		}
	}

	public static void importDisplaySettings(MCreator mcreator, File displaySettings) {
		File dir = PluginModelActions.getDisplaySettingsDir(mcreator);
		ensureDir(dir);
		String fileName = Transliteration.transliterateString(displaySettings.getName()).toLowerCase(Locale.ENGLISH)
				.trim().replace(":", "").replace(" ", "_");
		File dest = new File(dir, fileName);
		FileIO.copyFile(displaySettings, dest);
		// GeckoLib 4 requires parent "builtin/entity" so inventory/hand rendering
		// routes through BlockEntityWithoutLevelRenderer (GeoItemRenderer).
		// GeckoLib 5 special models also rely on this for display transforms.
		ensureBuiltinEntityParent(dest);
	}

	/**
	 * Ensures display-settings JSON has {@code "parent": "builtin/entity"}.
	 * Without it, Minecraft treats the file as an empty model (display-only)
	 * and never calls the GeckoLib item renderer in inventory or hand.
	 */
	public static void ensureBuiltinEntityParent(File displaySettingsFile) {
		if (displaySettingsFile == null || !displaySettingsFile.isFile())
			return;
		try {
			String raw = Files.readString(displaySettingsFile.toPath(), StandardCharsets.UTF_8);
			JsonElement parsed = JsonParser.parseString(raw);
			if (!parsed.isJsonObject())
				return;
			JsonObject root = parsed.getAsJsonObject();
			JsonElement parent = root.get("parent");
			if (parent != null && parent.isJsonPrimitive()
					&& "builtin/entity".equals(parent.getAsString()))
				return;
			// Insert parent first for readability
			JsonObject ordered = new JsonObject();
			ordered.addProperty("parent", "builtin/entity");
			for (var entry : root.entrySet()) {
				if (!"parent".equals(entry.getKey()))
					ordered.add(entry.getKey(), entry.getValue());
			}
			// Pretty-print lightly via Gson default (compact is fine for models)
			Files.writeString(displaySettingsFile.toPath(), ordered.toString(), StandardCharsets.UTF_8);
		} catch (Exception ignored) {
			// leave original file if JSON is invalid / unreadable
		}
	}

}
