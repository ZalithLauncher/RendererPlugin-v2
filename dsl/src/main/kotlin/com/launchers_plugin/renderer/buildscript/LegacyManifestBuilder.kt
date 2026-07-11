package com.launchers_plugin.renderer.buildscript

@DslMarker
private annotation class LegacyManifestDsl

@LegacyManifestDsl
class LegacyManifestBuilder {
    /** 渲染器在启动器内显示的名称 */
    var displayName: String = ""

    /** 渲染器名称，与 [rendererLib]、[eglLib] 拼接为 `名称:渲染器库名:EGL库名` 格式 */
    var rendererName: String = ""

    /**
     * 渲染器库文件名，例如 `"libxxx.so"`，
     * 以 `/` 开头时，启动器将自动拼接插件的 nativeLibraryDir
     */
    var rendererLib: String = ""

    /**
     * EGL 库文件名，例如 `"libxxx.so"`，
     * 以 `/` 开头时，启动器将自动拼接插件的 nativeLibraryDir
     */
    var eglLib: String = ""

    /** 最低支持的 Minecraft 版本号，如 `"1.17"`，空字符串不限制 */
    var minMCVer: String = ""

    /** 最高支持的 Minecraft 版本号，如 `"1.20.4"`，空字符串不限制 */
    var maxMCVer: String = ""

    private val boatEnv = mutableMapOf<String, String>()
    private val pojavEnv = mutableMapOf<String, String>()

    /**
     * 特殊环境变量：
     * - `DLOPEN=libxxx.so` dlopen 额外的 native 库文件
     * - 多个库使用逗号分隔：`DLOPEN=libxxx.so,libyyy.so`
     */
    fun boatEnv(block: MutableMap<String, String>.() -> Unit) {
        boatEnv.apply(block)
    }

    /**
     * 特殊环境变量：
     * - `DLOPEN=libxxx.so` dlopen 额外的 native 库文件
     * - 多个库使用逗号分隔：`DLOPEN=libxxx.so,libyyy.so`
     */
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

/**
 * 构建旧版渲染器插件的 manifest placeholders
 * @return 包含所有 manifest 占位符键值对的 [Map]
 */
fun legacyManifest(block: LegacyManifestBuilder.() -> Unit): Map<String, String> =
    LegacyManifestBuilder().apply(block).build()
