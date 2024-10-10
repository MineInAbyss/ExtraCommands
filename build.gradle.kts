import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    alias(idofrontLibs.plugins.mia.kotlin.jvm)
    alias(idofrontLibs.plugins.kotlinx.serialization)
    alias(idofrontLibs.plugins.mia.papermc)
    alias(idofrontLibs.plugins.mia.copyjar)
    alias(idofrontLibs.plugins.mia.nms)
    alias(idofrontLibs.plugins.mia.publication)
    alias(idofrontLibs.plugins.mia.autoversion)
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenCentral()
    maven("https://repo.mineinabyss.com/releases")
    maven("https://repo.mineinabyss.com/snapshots")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.william278.net/releases")
    mavenLocal()
}

dependencies {
    compileOnly(idofrontLibs.bundles.idofront.core)
    compileOnly(idofrontLibs.minecraft.mccoroutine)
    compileOnly(libs.geary.papermc)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.huskhomes)
    compileOnly(libs.maintenance)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
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
        register("PlaceholderAPI") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
        register("HuskHomes") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
        register("Maintenance") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
    }
}
