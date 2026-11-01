import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    alias(miaLibs.plugins.mia.kotlin.jvm)
    alias(miaLibs.plugins.kotlinx.serialization)
    alias(miaLibs.plugins.mia.papermc)
    alias(miaLibs.plugins.mia.copyjar)
    alias(miaLibs.plugins.mia.nms)
    alias(miaLibs.plugins.mia.publication)
    alias(miaLibs.plugins.mia.autoversion)
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenCentral()
    maven("https://repo.mineinabyss.com/releases")
    maven("https://repo.mineinabyss.com/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.william278.net/releases")
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    compileOnly(miaLibs.bundles.idofront.core)
    compileOnly(miaLibs.minecraft.mccoroutine)
    compileOnly(miaLibs.geary.papermc)
    compileOnly(miaLibs.chatty)
    compileOnly(miaLibs.minecraft.plugin.placeholderapi)
    compileOnly(libs.huskhomes)
    compileOnly(libs.openinv)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}


paper {
    main = "com.mineinabyss.extracommands.ExtraCommands"
    name = "ExtraCommands"
    prefix = "ExtraCommands"
    val version: String by project
    this.version = version
    authors = listOf("boy0000")
    apiVersion = "1.21"

    serverDependencies {
        register("Idofront") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
        register("Geary") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
        }
        register("Chatty") {
            required = false
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
        register("OpenInv") {
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

val schemas by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output + sourceSets.main.get().compileClasspath
    runtimeClasspath += output + compileClasspath
}

dependencies {
    "schemasImplementation"("com.mineinabyss.jsonschema-kt:writer:0.2.0")
}

tasks.register<JavaExec>("generateSchemas") {
    group = "documentation"
    description = "Writes JSON schemas for configs, pass -PschemaOutput=<server-config> to write into a server config repo"
    classpath = schemas.runtimeClasspath
    mainClass = "SchemasKt"
    args(providers.gradleProperty("schemaOutput").orElse(layout.buildDirectory.dir("schemas").map { it.asFile.path }).get())
}
