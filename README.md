# GeckoLib Reborn Plugin for MCreator 2026.2

Adds GeckoLib animated block/item/entity/armor elements for:

| Generator | GeckoLib | Asset layout |
|-----------|----------|--------------|
| **NeoForge 1.21.1** | **4.9.2** | `geo/` + `animations/` |
| **NeoForge 26.1.2** | **5.5.2** | `geckolib/models/` + `geckolib/animations/` |

> **Note:** GeckoLib **5.2.2** is not published for NeoForge 26.1.2. The plugin uses **5.5.2**, the current GeckoLib 5.x release for that loader.

## Requirements

- **MCreator 2026.2** (supported version id `2026002`)
- Java **25** (MCreator-bundled JBR works for building the plugin)

## Install

1. Build: `gradlew jar` (sets `org.gradle.java.home` to MCreator’s JDK)
2. Copy `build/libs/GeckoLib_Reborn_Plugin.zip` into `.mcreator/plugins/`
3. Restart MCreator
4. Enable the **GeckoLib** API in workspace settings

## Developing against MCreator source

```properties
# gradle.properties
mcreator_path=C:/Users/YourUserHere/IdeaProjects/MCreator
org.gradle.java.home=C:/Program Files/Pylo/MCreator/jdk
```

Checkout MCreator tag `2026.2.29418` (or matching 2026.2 release) at that path.
