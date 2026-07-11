package com.launchers_plugin.renderer.buildscript

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Renderer buildscript DSL plugin.
 *
 * This plugin provides Kotlin DSL functions for configuring Minecraft Java Edition
 * renderer plugins. Apply this plugin to make the DSL functions (`renderer`,
 * `buildJsonValue`, `buildEnvs`, `nativePath`, `legacyManifest`, etc.) available
 * in your build scripts.
 *
 * The plugin itself does not perform any action at apply time; its purpose is to
 * place the DSL classes on the buildscript classpath.
 */
class RendererPlugin : Plugin<Project> {
    override fun apply(target: Project) = Unit
}
