#!/usr/bin/env bash
#
# Runs tools/TestControllerCodegen against the compiled plugin classes.
# Requires build-plugin.sh to have run at least once (needs build/classes).
#
set -euo pipefail

MCREATOR_HOME="${MCREATOR_HOME:-C:/Program Files/Pylo/MCreator}"
JDK="$MCREATOR_HOME/jdk/bin"
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR"

[ -d build/classes ] || { echo "build/classes missing - run ./build-plugin.sh first" >&2; exit 1; }

CP="build/classes;libs/mcreator-api.jar"
for jar in "$MCREATOR_HOME"/lib/*.jar; do
  CP="$CP;$jar"
done

mkdir -p build/tools
{
  echo "-cp \"$CP\""
  echo "-d build/tools"
  echo "tools/TestControllerCodegen.java"
} > build/test.args

"$JDK/javac.exe" -nowarn @build/test.args

{
  echo "-cp \"build/tools;$CP\""
  echo "TestControllerCodegen"
} > build/test-run.args

"$JDK/java.exe" @build/test-run.args
