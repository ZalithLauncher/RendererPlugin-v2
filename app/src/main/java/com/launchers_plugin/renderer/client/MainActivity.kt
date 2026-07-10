package com.launchers_plugin.renderer.client

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.launchers_plugin.renderer.R
import com.launchers_plugin.renderer.buildRendererConfig
import kotlinx.serialization.json.Json

class MainActivity : Activity() {
    companion object {
        /** 插件推送渲染器配置的 Intent Action */
        const val ACTION_PUSH_RENDERER_CONFIG = "com.launchers_plugin.renderer.PUSH_RENDERER_CONFIG"
        const val EXTRA_CONFIG_JSON = "config"
        const val EXTRA_SENDER_PACKAGE = "sender_package"
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<Button>(R.id.push_button).setOnClickListener {
            pushConfigToHost()
        }
    }

    /**
     * 将渲染器配置推送给启动器
     */
    private fun pushConfigToHost() {
        val intent = Intent(ACTION_PUSH_RENDERER_CONFIG).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }

        val resolved = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resolved.isEmpty()) {
            Toast.makeText(this, getString(R.string.push_not_found), Toast.LENGTH_LONG).show()
            return
        }

        val config = buildRendererConfig()
        val configJson = json.encodeToString(config)

        intent.putExtra(EXTRA_CONFIG_JSON, configJson)
        intent.putExtra(EXTRA_SENDER_PACKAGE, packageName)

        runCatching {
            startActivity(intent)
        }
    }
}