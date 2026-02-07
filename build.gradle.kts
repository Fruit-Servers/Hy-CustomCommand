plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.3.1"
    id("run-hytale")
}

group = findProperty("pluginGroup") as String? ?: "com.hycustomcommand"
version = findProperty("pluginVersion") as String? ?: "1.0.0"
description = findProperty("pluginDescription") as String? ?: "Create custom commands via config"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly(files("libs/hytale-server.jar"))

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains:annotations:24.1.0")
}

runHytale {
    jarUrl = "https://fill-data.papermc.io/v1/objects/d5f47f6393aa647759f101f02231fa8200e5bccd36081a3ee8b6a5fd96739057/paper-1.21.10-115.jar"
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 25
        options.compilerArgs.add("-Xlint:deprecation")
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        val props = mapOf(
            "group" to project.group,
            "version" to project.version,
            "description" to project.description
        )
        inputs.properties(props)

        filesMatching("manifest.json") {
            expand(props)
        }
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")

        relocate("com.google.gson", "com.hycustomcommand.libs.gson")

        minimize()
    }

    build {
        dependsOn(shadowJar)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
