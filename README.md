# adbstart-plugin

A tiny Gradle plugin that adds start<Variant> tasks (e.g., startDebug,
startRelease) to Android/React Native apps. Each task depends on install<Variant>
and then launches the app via adb using the Gradle-resolved applicationId
(including any suffix).

## Requirements

- Gradle 8.x, Java 17+
- Android Gradle Plugin 8.x in the consumer project

## Quick setup

1) settings.gradle

```groovy
pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}
```

2) app/build.gradle

```groovy
plugins {
  id 'com.android.application'
  id 'org.fbluemle.adbstart'
}
```

```
adbStart {
  activity = '.MainActivity'   // or fully qualified name
  // deviceSerial = 'emulator-5554'
  // extraAmArgs = '-W'
  // adbPath = 'adb'
}
```

## Usage

- `./gradlew :app:startDebug`
- `./gradlew :app:startRelease`

## Notes

- AGP is used with compileOnly and accessed reflectively so non-Android builds don't fail.
- If activity starts with '.', it is prefixed with the resolved applicationId; otherwise it is treated as fully
  qualified.
- Optional flags:
   - deviceSerial: pass a specific device to adb via -s
   - extraAmArgs: extra arguments to am start (e.g., -W)
   - adbPath: custom adb path if needed
