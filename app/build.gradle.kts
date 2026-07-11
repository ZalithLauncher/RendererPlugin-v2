import com.launchers_plugin.renderer.buildscript.RendererConfig
import com.launchers_plugin.renderer.buildscript.buildEnvs
import com.launchers_plugin.renderer.buildscript.buildJsonValue
import com.launchers_plugin.renderer.buildscript.legacyManifest
import com.launchers_plugin.renderer.buildscript.nativePath
import com.launchers_plugin.renderer.buildscript.renderer
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.launchers_plugin.renderer.dsl")
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
    id("kotlinx-serialization")
}

android {
    namespace = "com.launchers_plugin.renderer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.launchers_plugin.renderer"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        resValues = true
    }

    packagingOptions {
        jniLibs {
            useLegacyPackaging = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
        configureEach {
            //应用名
            //app name
            resValue("string","app_name","XXX Renderer")
            //包名后缀
            //package name Suffix
            applicationIdSuffix = ".xxx"

            //新架构的渲染器配置
            resValue("string", "config", buildJsonValue {
                renderer(
                    displayName = "MobileGL Espryt",
                    rendererId = "opengles3",
                    rendererGLPath = "libMobileGL.so",
                    rendererEGLPath = nativePath("libMobileGL.so"),
                    dlopenLibPaths = emptyList(), // 如需 dlopen，请尽量使用 nativePath 拼接库的绝对路径
                    env = buildEnvs {
                        normal("LIBGL_ES", "3")

                        // 可配置的环境变量
                        editable(
                            key = "MOBILEGL_BACKEND_TYPE",
                            // 可选：该环境变量配置项的标题
                            // 在 AndroidManifest.xml 中增加 meta-data，指向本插件的本地化资源
                            title = RendererConfig.MetaString("title_backend_type"),
                            items = RendererConfig.EnvItems(
                                defaultValue = "DirectGLES", // 默认选择的环境变量（启动器默认将其视作可选项之一，不必添加到values）
                                // 所有可选的环境变量配置项
                                values = buildList {
                                    add("DirectVulkan")
                                }
                            )
                        )
                    },
                    minMCVer = null, // Minecraft 版本号，如 "1.17"
                    maxMCVer = null,
                )
            })


            // 兼容旧版渲染器插件架构
            manifestPlaceholders.putAll(legacyManifest {
                // 渲染器在启动器内显示的名称
                displayName     = "MobileGL Espryt"
                // 渲染器的具体定义
                // 旧版格式为        名称:渲染器库名:EGL库名
                // 此处方便配置拆解为  rendererName:rendererLib:eglLib
                rendererName    = "MobileGL Espryt"
                rendererLib     = "libMobileGL.so"
                eglLib          = "/libMobileGL.so"
                // 最小支持的MC版本
                minMCVer        = ""
                // 最大支持的MC版本
                maxMCVer        = ""

                // 特殊Env
                // DLOPEN=libxxx.so 用于加载额外库文件
                // 如果有多个库,可以使用","隔开,例如  DLOPEN=libxxx.so,libyyy.so
                boatEnv {
                    put("LIBGL_ES", "3")
                    put("POJAV_RENDERER", "opengles3")
                    put("MOBILEGL_BACKEND_TYPE", "DirectGLES")
                }
                pojavEnv {
                    put("LIBGL_ES", "3")
                    put("POJAV_RENDERER", "opengles3")
                    put("MOBILEGL_BACKEND_TYPE", "DirectGLES")
                }
            })
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {
    implementation(libs.androidx.annotation)
}
