# Renderer Plugin V2 模板
安卓端 Minecraft Java 版启动器的渲染器插件模板  

旧架构依赖 `meta-data` 硬编码配置，还需启动器拥有文件访问权限，以加载额外配置  
新架构支持**动态生成**配置，甚至可以将插件本身作为渲染器配置器，开发 UI 让用户自行编辑各项设置  
现在同一插件可提供多个**不同的渲染器条目**  

启动器扫描到插件后，会启动其 `ConfigProviderActivity`（透明 Activity）。  
该 Activity 在插件自身进程中运行，拥有完整权限，读取配置后通过广播回传给启动器，随即 `finish()`。

## 使用模板

### 配置 [build.gradle.kts](./app/build.gradle.kts)
- 自定义 `applicationId`、应用名称（`app_name`）、后缀（`applicationIdSuffix`）
- 可选：配置 `manifestPlaceholders` 兼容旧版渲染器架构

> 启动器支持新架构时，不会使用旧版架构

### 配置 [AndroidManifest.xml](./app/src/main/AndroidManifest.xml)
- 声明 `fclPlugin_V2` meta-data，否则启动器不会识别新架构的插件
- 如需兼容旧版架构，需要额外声明 `fclPlugin` meta-data

### 编写配置

参考模板：[RendererConfig.kt](./app/src/main/java/com/launchers_plugin/renderer/RendererConfig.kt)
```kotlin
fun buildRendererConfig(context: Context): RendererConfigList {
    return buildConfigs(context) {
        renderer(
            //...
        )
    }
}
```
