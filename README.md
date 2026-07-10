# Renderer Plugin V2 模板
安卓端 Minecraft Java 版启动器的渲染器插件模板  

旧架构依赖 `meta-data` 硬编码配置，还需启动器拥有文件访问权限，以加载额外配置  
新架构支持**动态生成**配置，甚至可以将插件本身作为渲染器配置器，开发 UI 让用户自行编辑各项设置  
现在同一插件可提供多个**不同的渲染器条目**  

_新架构要求启动器加载插件的 dex 来实现配置读取，可能需要寻找更好的读取方案_

## 使用模板

### 配置 [build.gradle.kts](./app/build.gradle.kts)
- 自定义 `applicationId`、应用名称（`app_name`）、后缀（`applicationIdSuffix`）
- 可选：配置 `manifestPlaceholders` 兼容旧版渲染器架构

> 启动器支持新架构时，不会使用旧版架构

### 配置 [AndroidManifest.xml](./app/src/main/AndroidManifest.xml)
- 声明 `fclPlugin_V2` meta-data，否则启动器不会识别该插件
- 如需兼容旧版架构，需要额外声明 `fclPlugin` meta-data

### 创建配置入口类
参考模板：[RendererConfigProvider.kt](./app/src/main/java/com/launchers_plugin/renderer/RendererConfigProvider.kt)

该类负责向启动器提供渲染器配置，需满足以下要求：

- 提供名为 `getConfig` 的函数（**名称固定**）：
```kotlin
@Keep
@Suppress("unused")
fun getConfig(nativeLibDir: String): String {
    // ...
}
```
- 返回值为 [RendererConfigList](./app/src/main/java/com/launchers_plugin/renderer/data/RendererConfigList.kt) 的 JSON 序列化字符串
- `nativeLibDir` 由启动器侧传入，为插件 APK 的 native 库目录路径，用于拼接库文件的绝对路径
- 若开启了 R8，必须使用 `@Keep` 注解或 ProGuard 规则避免该类和函数被混淆

### 注册入口类

在 [AndroidManifest.xml](./app/src/main/AndroidManifest.xml) 中声明 meta-data，指向配置入口类的全限定名：
```xml
<meta-data
    android:name="config_class"
    android:value="com.yourname.renderer.xxx.RendererConfigProvider" />
```
