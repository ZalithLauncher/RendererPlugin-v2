plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "2.1.20"
}

group = "com.launchers_plugin.renderer"
version = "1.0.0"

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
