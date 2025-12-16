#!/usr/bin/env sh
# Simplified Gradle wrapper start script
DIR="$(cd "$(dirname "$0")" && pwd)"
GRADLE_WRAPPER_JAR="$DIR/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$GRADLE_WRAPPER_JAR" ]; then
  echo "Missing gradle-wrapper.jar"
  exit 1
fi
java ${JAVA_OPTS} -classpath "$GRADLE_WRAPPER_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
