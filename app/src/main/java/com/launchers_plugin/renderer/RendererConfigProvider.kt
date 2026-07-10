package com.launchers_plugin.renderer

import androidx.annotation.Keep
import kotlinx.serialization.json.Json

@Keep // 若开启 R8，必须避免入口被混淆！
@Suppress("unused")
class RendererConfigProvider {
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * 启动器通过加载 dex 反射调用此方法，类名不可变
     * @param nativeLibDir 插件 APK 的 native 库目录路径
     * @return 序列化后的渲染器配置 JSON 字符串
     */
    @Keep // 若开启 R8，必须避免入口被混淆！
    @Suppress("unused")
    fun getConfig(nativeLibDir: String): String {
        val config = buildRendererConfig(nativeLibDir)
        return json.encodeToString(config)
    }
}
