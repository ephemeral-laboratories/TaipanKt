import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0"
    id("utf8-workarounds")
    id("com.diffplug.spotless") version "6.3.0"
}

group = "garden.ephemeral.games"
version = "1.0.0-SNAPSHOT"
description = "Taipan! Reproduction for Compose"

dependencies {
    val komplexVersion: String by project.extra
    val antlrVersion: String by project.extra
    val junitVersion: String by project.extra
    val assertkVersion: String by project.extra

    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)

    @OptIn(ExperimentalComposeLibrary::class)
    testImplementation(compose.uiTestJUnit4)
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:$assertkVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion")
}

tasks.withType<AntlrTask> {
    arguments = arguments + listOf("-package", "garden.ephemeral.calculator.grammar")
    outputDirectory = file("$buildDir/generated-src/antlr/main/garden/ephemeral/calculator/grammar")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "garden.ephemeral.games.taipan.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = project.name.capitalizeAsciiOnly()
            packageVersion = "1.0.0"
            description = project.description
            vendor = "Ephemeral Laboratories"
            copyright = "Copyright © 2022 $vendor"

            windows {
                upgradeUuid = "78916c33-30ec-4f9a-8ad5-7d316cd5f90e"
                menuGroup = packageName
                perUserInstall = true
                iconFile.set(file("???"))
            }
        }
    }
}

spotless {
    kotlin {
        ktlint("0.44.0").userData(mapOf("disabled_rules" to "no-wildcard-imports"))
    }
    kotlinGradle {
        ktlint("0.44.0")
    }
}
