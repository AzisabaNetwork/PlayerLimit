plugins {
    kotlin("jvm") version "2.4.21"
    id("com.gradleup.shadow") version "9.6.1"
}

group = "net.azisaba"
version = "2.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    // the first Folia API; still loads on the latest Folia
    compileOnly("dev.folia:folia-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
}

// Folia 1.19.4 requires Java 17
kotlin {
    jvmToolchain(17)
}

tasks {
    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        relocate("kotlin", "net.azisaba.playerLimit.libs.kotlin")
        dependencies {
            exclude(dependency("org.jetbrains:annotations"))
        }
        exclude("META-INF/*.kotlin_module", "META-INF/versions/**")
        minimize()
    }

    jar {
        enabled = false
    }

    build {
        dependsOn(shadowJar)
    }
}
