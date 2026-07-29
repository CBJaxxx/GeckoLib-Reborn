# GeckoLib Reborn Plugin for MCreator 2026.2

Adds GeckoLib animated block/item/entity/armor elements for:

| Generator | GeckoLib | Asset layout |
|-----------|----------|--------------|
| **NeoForge 1.21.1** | **4.9.2** | `geo/` + `animations/` |
| **NeoForge 26.1.2** | **5.5.2** | `geckolib/models/` + `geckolib/animations/` |


## Requirements

- **MCreator 2026.2** (supported version id `2026002`)
- Java **25** (MCreator-bundled JBR works for building the plugin)

## Install

1. Build: `gradlew jar` (sets `org.gradle.java.home` to MCreator’s JDK)
2. Copy `build/libs/GeckoLib_Reborn_Plugin.zip` into `.mcreator/plugins/`
3. Restart MCreator
4. Enable the **GeckoLib** API in workspace settings

## Custom animation controllers

Animated entities can define extra animation controllers on the **Animations** page,
next to the built-in `movement`, `attacking` and `procedure` ones. Each row is a
controller name, an *Additive* flag, and an optional per-controller transition-ticks
override (unchecked inherits the entity's value). The play/stop/get animation blocks
take a controller name; left at the default `procedure` they behave as before.

On the **26.1.2** generator an additive controller is registered with GeckoLib 5's
`additiveAnimations()`, so its keyframes are offsets from the base pose — an attack
authored from the rest pose plays correctly over a held stance and settles back into
it. GeckoLib 4.9.2 has no such API, so on **1.21.1** the flag only controls layering
order. Additive controllers are registered after the built-in ones, since a
non-additive controller assigns and would discard the accumulated offset.

Controller names must be identifiers and may not collide with `movement`, `attacking`
or `procedure` (case-insensitive). Invalid and duplicate rows are filtered in
`AnimatedEntity#getValidControllers` rather than emitted, so a half-filled row cannot
produce a workspace that fails to compile.

## Checks

```bash
./tools/validate-templates.sh        # FreeMarker syntax of every .ftl
./tools/test-controller-codegen.sh   # renders the real registerControllers and
                                     # predicate blocks and asserts their output
```

`test-controller-codegen.sh` needs compiled classes; `./build-plugin.sh` produces them
without a Gradle download by compiling against `libs/mcreator-api.jar`, which
`./regen-api-jar.sh` rebuilds from the installed MCreator.

## Developing against MCreator source

```properties
# gradle.properties
mcreator_path=C:/Users/YourUserHere/IdeaProjects/MCreator
org.gradle.java.home=C:/Program Files/Pylo/MCreator/jdk
```

Checkout MCreator tag `2026.2.29418` (or matching 2026.2 release) at that path.
