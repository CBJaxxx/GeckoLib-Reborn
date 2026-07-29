#!/usr/bin/env bash
#
# Regenerates libs/mcreator-api.jar from the installed MCreator.
#
# MCreator ships its classes inside mcreator.exe (a Windows EXE with an appended
# zip archive). `jar tf` and `unzip` tolerate the EXE prefix, but javac's zip
# reader does not ("zip END header not found"), so the contents are extracted
# and repackaged as a plain jar for use on the compile classpath.
#
# Re-run this after updating MCreator to a new version.
#
set -euo pipefail

MCREATOR_HOME="${MCREATOR_HOME:-C:/Program Files/Pylo/MCreator}"
JDK="$MCREATOR_HOME/jdk/bin"
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

TMP_DIR="$(mktemp -d)"
trap 'rm -rf "$TMP_DIR"' EXIT

echo "Extracting $MCREATOR_HOME/mcreator.exe ..."
( cd "$TMP_DIR" && unzip -qo "$MCREATOR_HOME/mcreator.exe" )

mkdir -p "$PROJECT_DIR/libs"
"$JDK/jar.exe" cf "$PROJECT_DIR/libs/mcreator-api.jar" -C "$TMP_DIR" .

echo "Wrote libs/mcreator-api.jar ($(stat -c %s "$PROJECT_DIR/libs/mcreator-api.jar") bytes)"
