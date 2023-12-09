import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    alias(libs.plugins.mia.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.mia.papermc)
    alias(libs.plugins.mia.copyjar)
    alias(libs.plugins.mia.publication)
    alias(libs.plugins.mia.autoversion)
    alias(libs.plugins.shadowjar)
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenCentral()
    maven("https://repo.mineinabyss.com/releases")
    maven("https://repo.mineinabyss.com/snapshots")
    mavenLocal()
}

dependencies {
    compileOnly(libs.bundles.idofront.core)
    compileOnly(extraLibs.geary.papermc)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}


paper {
    main = "com.mineinabyss.extracommands.ExtraCommands"
    name = "ExtraCommands"
    prefix = "ExtraCommands"
    val version: String by project
    this.version = version
    authors = listOf("boy0000")
    apiVersion = "1.20"

    serverDependencies {
        register("Idofront") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
        register("Geary") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
    }
}
