package com.launchers_plugin.renderer.buildscript

import com.launchers_plugin.renderer.buildscript.RendererConfig.Env
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@DslMarker
private annotation class EnvConfigDsl
@EnvConfigDsl
interface EnvConfigScope {
    /**
     * 创建一个普通的环境变量
     * @see Env.NormalEnv
     */
    fun normal(key: String, value: String)

    /**
     * 创建一个根据预设值自由选择值的环境变量
     * @see Env.SelectableEnv
     */
    fun selectable(
        key: String,
        items: RendererConfig.EnvItems,
        title: RendererConfig.MetaString? = null
    )

    /**
     * 创建一个可由用户自行编辑值的环境变量
     * @see Env.CustomizableEnv
     */
    fun customizable(
        key: String,
        defaultValue: String? = null,
        title: RendererConfig.MetaString? = null
    )

    /**
     * 创建一个可开关的环境变量（使用/不使用）
     * @see Env.ToggleableEnv
     */
    fun toggleable(
        key: String,
        value: String,
        toggle: Boolean = true,
        title: RendererConfig.MetaString? = null
    )
}

private class EnvConfigBuilder: EnvConfigScope {
    private val envs = mutableListOf<Env>()

    override fun normal(
        key: String,
        value: String
    ) {
        envs.add(Env.NormalEnv(key, value))
    }

    override fun selectable(
        key: String,
        items: RendererConfig.EnvItems,
        title: RendererConfig.MetaString?
    ) {
        envs.add(Env.SelectableEnv(key, title, items))
    }

    override fun customizable(
        key: String,
        defaultValue: String?,
        title: RendererConfig.MetaString?
    ) {
        envs.add(Env.CustomizableEnv(key, title, defaultValue))
    }

    override fun toggleable(
        key: String,
        value: String,
        toggle: Boolean,
        title: RendererConfig.MetaString?
    ) {
        envs.add(Env.ToggleableEnv(key, value, title, toggle))
    }

    fun build(): List<Env> = envs.toList()
}

/**
 * 创建渲染器配置
 * @see RendererConfig
 */
fun renderer(
    displayName: String,
    rendererId: String,
    rendererGLPath: String,
    rendererEGLPath: String,
    dlopenLibPaths: List<String>,
    env: List<Env>,
    minMCVer: String?,
    maxMCVer: String?,
) = RendererConfig(
    displayName = displayName,
    rendererId = rendererId,
    rendererGLPath = rendererGLPath,
    rendererEGLPath = rendererEGLPath,
    dlopenLibPaths = dlopenLibPaths,
    env = env,
    minMCVer = minMCVer,
    maxMCVer = maxMCVer,
)

/**
 * 拼接 nativeLibraryDir
 *
 * 将特殊标识符 `**|` 拼接到字符串，启动器识别到后将自动替换为插件的 nativeLibraryDir
 */
fun nativePath(libFileName: String): String {
    return "**|$libFileName"
}

/**
 * 构建环境变量集合
 * @see Env.NormalEnv
 * @see Env.SelectableEnv
 */
fun buildEnvs(
    block: EnvConfigScope.() -> Unit
): List<Env> {
    val builder = EnvConfigBuilder()
    return builder.apply(block).build()
}

inline fun <reified T> buildJsonValue(
    block: () -> T
): String {
    val json = Json { ignoreUnknownKeys = true }
    return json.encodeToString(block()).replace("\"", "\\\"")
}
