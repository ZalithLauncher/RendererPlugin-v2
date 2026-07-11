package com.launchers_plugin.renderer.client

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.launchers_plugin.renderer.buildRendererConfig
import kotlinx.serialization.json.Json

/**
 * 透明 Activity，由启动器在后台静默启动
 * 插件可以自行生成配置广播回启动器
 */
class ConfigProviderActivity : Activity() {
    companion object {
        /** 启动器发送的请求广播 Action */
        const val ACTION_REQUEST_CONFIG = "com.launchers_plugin.renderer.REQUEST_CONFIG"
        /** 插件回复的响应广播 Action */
        const val ACTION_RESPONSE_CONFIG = "com.launchers_plugin.renderer.RESPONSE_CONFIG"

        const val EXTRA_CONFIG_JSON = "config_json"
        const val EXTRA_PACKAGE_NAME = "package_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configJson = buildConfigJson()

        val response = Intent(ACTION_RESPONSE_CONFIG).apply {
            putExtra(EXTRA_CONFIG_JSON, configJson)
            putExtra(EXTRA_PACKAGE_NAME, packageName)
            // 发送给启动器（设置包名确保只有启动器能收到）
            `package` = intent.getStringExtra("host_package")
        }
        sendBroadcast(response)

        finish()
    }

    private fun buildConfigJson(): String {
        val json = Json { ignoreUnknownKeys = true }
        val config = buildRendererConfig(this)
        return json.encodeToString(config)
    }
}
