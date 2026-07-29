#!/usr/bin/env bash
#
# Builds GeckoLib_Reborn_Plugin.zip without Gradle.
#
# The project's build.gradle works too, but needs a ~150 MB Gradle 9.6 download.
# All the build actually has to do is: compile against MCreator's classes, then
# zip the classes together with src/main/resources at the archive root.
#
# libs/mcreator-api.jar is a repackaged copy of the installed MCreator's
# mcreator.exe (an EXE with an appended zip). javac cannot read that format
# directly, so it is extracted and re-jarred by regen-api-jar.sh.
#
set -euo pipefail

MCREATOR_HOME="${MCREATOR_HOME:-C:/Program Files/Pylo/MCreator}"
JDK="$MCREATOR_HOME/jdk/bin"
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

OUT_ZIP="build/libs/GeckoLib_Reborn_Plugin.zip"

[ -f libs/mcreator-api.jar ] || { echo "libs/mcreator-api.jar missing - run ./regen-api-jar.sh" >&2; exit 1; }

rm -rf build/classes "$OUT_ZIP"
mkdir -p build/classes build/libs

# Enumerate the MCreator lib jars explicitly: javac does not expand a `dir/*`
# classpath wildcard when it comes from an argfile.
CP="libs/mcreator-api.jar"
for jar in "$MCREATOR_HOME"/lib/*.jar; do
  CP="$CP;$jar"
done

# javac argfile: keeps the ';'-separated classpath away from Git Bash path
# conversion, and lets the space in "Program Files" be quoted properly.
{
  echo "-encoding UTF-8"
  echo "-nowarn"
  # Match the debug info Gradle produces by default, so a build here is
  # comparable to the upstream release artifact.
  echo "-g"
  echo "-cp \"$CP\""
  echo "-d build/classes"
  find src/main/java -name '*.java'
} > build/javac.args

echo "Compiling $(find src/main/java -name '*.java' | wc -l) source files..."
"$JDK/javac.exe" @build/javac.args

echo "Packaging $OUT_ZIP ..."
"$JDK/jar.exe" cf "$OUT_ZIP" -C build/classes . -C src/main/resources .

echo "Built: $OUT_ZIP ($(stat -c %s "$OUT_ZIP") bytes)"
