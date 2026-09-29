#!/bin/sh

set -e

if [ -n "${MAVEN_HOME}" ]; then
  MAVEN_BIN="$MAVEN_HOME/bin/mvn"
elif command -v mvn >/dev/null 2>&1; then
  MAVEN_BIN="mvn"
else
  echo "Maven is not installed or not on PATH." >&2
  exit 1
fi

exec "$MAVEN_BIN" "$@"
