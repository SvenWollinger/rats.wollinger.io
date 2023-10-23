import org.jetbrains.kotlin.cli.jvm.main
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType

plugins {
    kotlin("js") version "1.8.20"
}

group = "io.wollinger.rats"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.8.0")
}


kotlin {
    js(IR) {
        browser {
            webpackTask {
                this.outputFileName = "app.js"
            }
        }
        binaries.executable()
    }
}