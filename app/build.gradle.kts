import com.launchers_plugin.renderer.buildscript.RendererConfig
import com.launchers_plugin.renderer.buildscript.buildEnvs
import com.launchers_plugin.renderer.buildscript.buildJsonValue
import com.launchers_plugin.renderer.buildscript.nativePath
import com.launchers_plugin.renderer.buildscript.renderer
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
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
                    displayName = "XXX",
                    rendererId = "XXX",
                    rendererGLPath = nativePath("libXXX.so"),
                    rendererEGLPath = nativePath("libXXX.so"),
                    dlopenLibPaths = buildList {
                        // 提供完整的路径
                        add(nativePath("libXXX.so"))
                    },
                    env = buildEnvs {
                        normal("XXX", "AAA")
                        // 部分需要使用到完整路径的配置，如 LIB_MESA_NAME
                        // 提供完整的路径
                        normal("LIB_MESA_NAME", nativePath("libXXX.so"))

                        // 可配置的环境变量
                        editable("EEE", RendererConfig.EnvItems(
                            defaultValue = "DDD", // 默认选择的环境变量（启动器默认将其视作可选项之一，不必添加到values）
                            // 所有可选的环境变量配置项
                            values = buildList {
                                add("CCC")
                                add("FFF")
                            },
                            // 可选：该环境变量配置项的标题
                            // 在 AndroidManifest.xml 中增加 meta-data，指向本插件的本地化资源
                            title = RendererConfig.MetaString("title_eee")
                        ))
                    },
                    minMCVer = "1.17",
                    maxMCVer = null,
                )
            })


            // 兼容旧版渲染器插件架构

            //渲染器在启动器内显示的名称
            //The name displayed by the renderer in the launcher
            manifestPlaceholders["des"] = ""
            //渲染器的具体定义 格式为 名称:渲染器库名:EGL库名 例如 LTW:libltw.so:libltw.so
            //The specific definition format of a renderer is ${name}:${renderer library name}:${EGL library name}, for example:   LTW:libltw.so:libltw.so
            manifestPlaceholders["renderer"] = ""

            //特殊Env
            //Special Env
            //DLOPEN=libxxx.so 用于加载额外库文件
            //DLOPEN=libxxx.so used to load external library
            //如果有多个库,可以使用","隔开,例如  DLOPEN=libxxx.so,libyyy.so
            //If there are multiple libraries, you can use "," to separate them, for example  DLOPEN=libxxx.so,libyyy.so
            manifestPlaceholders["boatEnv"] = mutableMapOf<String,String>().apply {

            }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }

            manifestPlaceholders["pojavEnv"] = mutableMapOf<String,String>().apply {

            }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }

            //最小支持的MC版本
            //The minimum supported MC version
            manifestPlaceholders["minMCVer"] = ""
            //最大支持的MC版本
            //The maximum supported MC version
            manifestPlaceholders["maxMCVer"] = ""
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
