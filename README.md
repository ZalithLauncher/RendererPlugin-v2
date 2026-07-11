# RendererPlugin V2

安卓端 Minecraft Java 版启动器的**渲染器插件**模板  
本模板提供了一套 Kotlin DSL，用于在构建阶段将渲染器配置序列化为 JSON 并写入应用字符串资源  
启动器通过读取 `AndroidManifest.xml` 中的 `meta-data` 来识别插件并加载渲染器库  

## 使用模板

在 [AndroidManifest.xml](./app/src/main/AndroidManifest.xml) 中添加插件标记

```xml
<!-- 引用新架构渲染器配置 -->
<meta-data
    android:name="fclPlugin_V2"
    android:resource="@string/config" />

<!-- 可选：兼容旧版插件架构 -->
<!-- 如插件支持新架构，启动器不会同时加载旧版本配置 -->
<meta-data
    android:name="fclPlugin"
    android:value="true" />
```

启用 `resValues`，否则配置字符串无法正常写入

```kotlin
android {
    buildFeatures {
        resValues = true
    }
}
```

放置渲染器库  
将编译好的 `.so` 文件放入 `app/src/main/jniLibs/` 对应的 ABI 目录：

``` txt
app/src/main/jniLibs/
├── arm64-v8a/
│   └── libxxx.so
├── armeabi-v7a/
│   └── libxxx.so
├── x86/
│   └── libxxx.so
└── x86_64/
    └── libxxx.so
```

## DSL API 参考

### `renderer(...)`


| 参数                | 类型             | 说明                                   |
|-------------------|----------------|--------------------------------------|
| `displayName`     | `String`       | 在启动器中显示的渲染器名称                        |
| `rendererId`      | `String`       | 渲染器 ID，启动器将其配置到环境变量 `POJAV_RENDERER` |
| `rendererGLPath`  | `String`       | 渲染器库路径，使用 `nativePath()` 构建          |
| `rendererEGLPath` | `String`       | 渲染器 EGL 库路径，使用 `nativePath()` 构建     |
| `dlopenLibPaths`  | `List<String>` | 需要额外 dlopen 的库路径列表                   |
| `env`             | `List<Env>`    | 环境变量列表，使用 `buildEnvs()` 构建           |
| `minMCVer`        | `String?`      | 最低支持的 MC 版本号（如 `"1.17"`），`null` 不限制  |
| `maxMCVer`        | `String?`      | 最高支持的 MC 版本号，`null` 不限制              |

### `nativePath(string)`

```kotlin
nativePath("libXXX.so") // 返回 "**|libXXX.so"
```

将 `**|` 前缀拼接到库文件名前，启动器识别到此前缀后，会替换为插件实际的 `nativeLibraryDir` 路径

### `buildEnvs {}`

#### `normal(key, value)` 

固定环境变量，不可配置

```kotlin
normal("LIB_MESA_NAME", nativePath("libMesa.so"))
```

#### `editable(key, title?, items)`

可配置环境变量，启动器会根据插件提供的选项，提供配置入口

```kotlin
editable(
    key = "GL_VERSION",
    title = RendererConfig.MetaString("title_gl_version"), // 可选：标题资源索引
    items = RendererConfig.EnvItems(
        defaultValue = "4.6",       // 默认值，且会被视为列表的其中一项，不必重复添加到 values 中
        values = buildList {        // 所有可选项
            add("4.5")
            add("3.3")
        }
    )
)
```

`title` 用于在 [AndroidManifest.xml](./app/src/main/AndroidManifest.xml) 中关联插件本地化字符串资源，需要额外声明 meta-data：

```xml
<meta-data
    android:name="title_gl_version"
    android:resource="@string/title_gl_version" />
```
