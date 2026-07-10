package com.launchers_plugin.renderer.data

import java.io.File

@DslMarker
private annotation class RendererConfigDsl

@RendererConfigDsl
interface RendererScope {
    /**
     * 添加一个新的渲染器配置，调用该函数则将配置添加到列表，
     * 同时返回已构建的渲染器配置
     *
     * @see RendererConfig
     * @return 该函数创建的配置
     */
    fun renderer(
        renderSuffix: String,
        displayName: String,
        rendererId: String,
        rendererGLPath: String,
        rendererEGLPath: String,
        dlopenLibPaths: List<String> = emptyList(),
        env: Map<String, String> = emptyMap(),
        minMCVer: String? = null,
        maxMCVer: String? = null,
    ): RendererConfig

    /**
     * 添加一个已创建的渲染器配置
     * @see RendererConfig
     */
    fun renderer(config: RendererConfig)

    /**
     * 获取插件 Native Library 的绝对路径
     */
    fun nativePath(libFileName: String): String
}

private class RendererConfigListBuilder(
    private val nativeLibDir: String
) : RendererScope {
    private val renderers = mutableListOf<RendererConfig>()

    override fun renderer(
        renderSuffix: String,
        displayName: String,
        rendererId: String,
        rendererGLPath: String,
        rendererEGLPath: String,
        dlopenLibPaths: List<String>,
        env: Map<String, String>,
        minMCVer: String?,
        maxMCVer: String?,
    ): RendererConfig {
        return RendererConfig(
            renderSuffix = renderSuffix,
            displayName = displayName,
            rendererId = rendererId,
            rendererGLPath = rendererGLPath,
            rendererEGLPath = rendererEGLPath,
            dlopenLibPaths = dlopenLibPaths,
            env = env,
            minMCVer = minMCVer,
            maxMCVer = maxMCVer,
        ).also(renderers::add)
    }

    override fun renderer(config: RendererConfig) {
        renderers.add(config)
    }

    override fun nativePath(libFileName: String): String {
        return File(nativeLibDir, libFileName).absolutePath
    }

    fun build(): List<RendererConfig> = renderers.toList()
}

/**
 * 构建渲染器配置
 * @param nativeLibDir 插件 APK 的 native 库目录路径
 */
fun buildConfigs(
    nativeLibDir: String,
    block: RendererScope.() -> Unit
): RendererConfigList {
    val builder = RendererConfigListBuilder(nativeLibDir)
    builder.block()
    return RendererConfigList(builder.build())
}