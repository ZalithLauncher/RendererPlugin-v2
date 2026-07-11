package com.launchers_plugin.renderer

import android.content.Context
import com.launchers_plugin.renderer.data.RendererConfigList
import com.launchers_plugin.renderer.data.buildConfigs

fun buildRendererConfig(context: Context): RendererConfigList {
    return buildConfigs(context) {
        val config0 = renderer(
            renderSuffix = "a",
            displayName = "XXX",
            rendererId = "XXX",
            rendererGLPath = nativePath("libXXX.so"),
            rendererEGLPath = nativePath("libXXX.so"),
            dlopenLibPaths = buildList {
                // 提供完整的路径
                add(nativePath("libXXX.so"))
            },
            env = buildMap {
                put("XXX", "AAA")
                // 部分需要使用到完整路径的配置，如 LIB_MESA_NAME
                // 提供完整的路径
                put("LIB_MESA_NAME", nativePath("libXXX.so"))
            },
            minMCVer = "1.17",
            maxMCVer = null,
        )

        // 基于第一个渲染器配置，添加另一个参数一致但环境变量不同的渲染器配置
        renderer(
            config0.copy(
                renderSuffix = "b",
                env = buildMap {
                    put("XXX", "BBB")
                }
            )
        )
//
//        renderer(
//            renderSuffix = "ltw",
//            displayName = "LTW (OpenGL 4.6)",
//            rendererId = "opengles3_ltw",
//            rendererGLPath = nativePath("libltw.so"),
//            rendererEGLPath = nativePath("libltw.so"),
//            env = buildMap {
//                put("LIBGL_ES", "3")
//            },
//            minMCVer = "1.17"
//        )
    }
}