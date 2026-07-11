plugins {
    `java-gradle-plugin`
    `maven-publish`
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.launchers_plugin.renderer"
version = "1.0-alpha2"

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

gradlePlugin {
    plugins {
        create("rendererPluginV2Dsl") {
            id = "com.launchers_plugin.renderer.dsl"
            implementationClass = "com.launchers_plugin.renderer.buildscript.RendererPlugin"
            displayName = "Renderer Plugin V2 DSL"
            description = "Gradle configuration DSL for Android Minecraft Java Edition launcher renderer plugins"
        }
    }
}
