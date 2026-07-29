#!/usr/bin/env bash
#
# Syntax-checks every .ftl in src/main/resources against the FreeMarker version
# MCreator ships. See tools/ValidateTemplates.java for what this does and does
# not cover.
#
set -euo pipefail

MCREATOR_HOME="${MCREATOR_HOME:-C:/Program Files/Pylo/MCreator}"
JDK="$MCREATOR_HOME/jdk/bin"
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR"

FREEMARKER="$(ls "$MCREATOR_HOME"/lib/freemarker-*.jar | head -1)"

mkdir -p build/tools
{
  echo "-cp \"$FREEMARKER\""
  echo "-d build/tools"
  echo "tools/ValidateTemplates.java"
} > build/validate.args

"$JDK/javac.exe" @build/validate.args

{
  echo "-cp \"build/tools;$FREEMARKER\""
  echo "ValidateTemplates"
  echo "src/main/resources"
} > build/validate-run.args

"$JDK/java.exe" @build/validate-run.args
