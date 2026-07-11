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
     * 创建一个可由启动器编辑的环境变量
     * @see Env.EditableEnv
     */
    fun editable(
        key: String,
        items: RendererConfig.EnvItems,
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

    override fun editable(
        key: String,
        items: RendererConfig.EnvItems,
        title: RendererConfig.MetaString?
    ){
        envs.add(Env.EditableEnv(key, title, items))
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
 * @see Env.EditableEnv
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
