#!/bin/bash
# ─────────────────────────────────────────────────────────────────────────────
#  run.sh  —  Compile & launch Five-Card Draw Poker (no Maven / Gradle needed)
#
#  Usage:
#       export JAVAFX_HOME=/path/to/javafx-sdk-21/lib
#       chmod +x run.sh && ./run.sh
# ─────────────────────────────────────────────────────────────────────────────

FX="${JAVAFX_HOME:-/path/to/javafx-sdk/lib}"
SRC="src"
OUT="out"
MAIN="poker.ui.PokerApp"

if [ ! -d "$FX" ]; then
  echo "ERROR: JavaFX lib not found at: $FX"
  echo "  export JAVAFX_HOME=/path/to/javafx-sdk-21/lib"
  exit 1
fi

mkdir -p "$OUT"

echo "▸ Compiling..."
javac \
  --module-path "$FX"           \
  --add-modules javafx.controls \
  -d "$OUT"                     \
  --source-path "$SRC"          \
  src/poker/ui/PokerApp.java

[ $? -ne 0 ] && { echo "Compilation failed."; exit 1; }

echo "▸ Launching Five-Card Draw Poker..."
java \
  --module-path "$FX"           \
  --add-modules javafx.controls \
  -cp "$OUT"                    \
  "$MAIN"
