package com.launchers_plugin.renderer

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import com.launchers_plugin.renderer.data.buildConfigs
import kotlinx.serialization.json.Json

class RendererProvider : ContentProvider() {
    private val json = Json { ignoreUnknownKeys = true }

    override fun onCreate(): Boolean = true

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        return when (method) {
            "fclPlugin_V2" -> Bundle().apply {
                val config = buildConfigs {
                    val config0 = renderer(
                        renderSuffix = "a",
                        displayName = "XXX",
                        rendererId = "XXX",
                        rendererGLPath = context!!.nativePath("libXXX.so"),
                        rendererEGLPath = context!!.nativePath("libXXX.so"),
                        dlopenLibPaths = buildList {
                            // 提供完整的路径
                            add(context!!.nativePath("libXXX.so"))
                        },
                        env = buildMap {
                            put("XXX", "AAA")
                            // 部分需要使用到完整路径的配置，如 LIB_MESA_NAME
                            // 提供完整的路径
                            put("LIB_MESA_NAME", context!!.nativePath("libXXX.so"))
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

//                    renderer(
//                        renderSuffix = "ltw",
//                        displayName = "LTW (OpenGL 4.6)",
//                        rendererId = "opengles3_ltw",
//                        rendererGLPath = context!!.nativePath("libltw.so"),
//                        rendererEGLPath = context!!.nativePath("libltw.so"),
//                        env = buildMap {
//                            put("LIBGL_ES", "3")
//                        },
//                        minMCVer = "1.17"
//                    )
                }
                putString("renderers", json.encodeToString(config))
            }
            else -> null
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor {
        throw UnsupportedOperationException("Please use the `call(String method, String arg, Bundle extras)` method to get the configuration")
    }
    override fun getType(uri: Uri): String {
        throw UnsupportedOperationException()
    }
    override fun insert(uri: Uri, values: ContentValues?): Uri {
        throw UnsupportedOperationException("The renderer plugin is read-only.")
    }
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String?>?): Int {
        throw UnsupportedOperationException("The renderer plugin is read-only.")
    }
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String?>?): Int{
        throw UnsupportedOperationException("The renderer plugin is read-only.")
    }
}
