package com.launchers_plugin.renderer.buildscript

@DslMarker
private annotation class LegacyManifestDsl

@LegacyManifestDsl
class LegacyManifestBuilder {
    var displayName: String = ""
    var rendererName: String = ""
    var rendererLib: String = ""
    var eglLib: String = ""
    var minMCVer: String = ""
    var maxMCVer: String = ""

    private val boatEnv = mutableMapOf<String, String>()
    private val pojavEnv = mutableMapOf<String, String>()

    fun boatEnv(block: MutableMap<String, String>.() -> Unit) {
        boatEnv.apply(block)
    }
    fun pojavEnv(block: MutableMap<String, String>.() -> Unit) {
        pojavEnv.apply(block)
    }

    internal fun build(): Map<String, String> = buildMap {
        put("des", displayName)
        put("renderer", "$rendererName:$rendererLib:$eglLib")
        put("boatEnv", formatEnv(boatEnv))
        put("pojavEnv", formatEnv(pojavEnv))
        put("minMCVer", minMCVer)
        put("maxMCVer", maxMCVer)
    }

    private fun formatEnv(env: Map<String, String>): String =
        if (env.isEmpty()) ""
        else env.entries.joinToString(":") { "${it.key}=${it.value}" }
}

fun legacyManifest(block: LegacyManifestBuilder.() -> Unit): Map<String, String> =
    LegacyManifestBuilder().apply(block).build()
