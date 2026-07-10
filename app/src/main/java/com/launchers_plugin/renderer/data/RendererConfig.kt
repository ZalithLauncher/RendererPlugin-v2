package com.launchers_plugin.renderer.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param renderSuffix              渲染器后缀（请考虑硬编码每个渲染器的后缀）
 *                                  启动器将使用 `包名`+[renderSuffix] 构建该渲染器的唯一ID，用于标识渲染器
 * @param displayName               向用户展示的名称
 * @param rendererId                渲染器ID，启动器将会配置到环境变量`POJAV_RENDERER`
 * @param rendererGLPath            渲染器图形库具体路径
 * @param rendererEGLPath           渲染器EGL具体路径
 * @param dlopenLibPaths            需要 dlopen 的库的具体路径
 * @param env                       渲染器环境变量列表
 * @param minMCVer                  最低支持的 Minecraft 版本号，如`1.17`，为`null`则不限制
 * @param maxMCVer                  最高支持的 Minecraft 版本号，如`1.17`，为`null`则不限制
 * @param requestStoragePermission  是否需要启动器具有所有目录读写权限（如果渲染器的配置文件在公共目录，启动器无权限则无法加载配置）
 */
@Serializable
data class RendererConfig(
    @SerialName("renderSuffix")
    val renderSuffix: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("rendererId")
    val rendererId: String,
    @SerialName("rendererGLPath")
    val rendererGLPath: String,
    @SerialName("rendererEGLPath")
    val rendererEGLPath: String,
    @SerialName("dlopenLibPaths")
    val dlopenLibPaths: List<String>,
    @SerialName("env")
    val env: Map<String, String>,
    @SerialName("minMCVer")
    val minMCVer: String?,
    @SerialName("maxMCVer")
    val maxMCVer: String?,
    @SerialName("requestStoragePermission")
    val requestStoragePermission: Boolean = false,
)