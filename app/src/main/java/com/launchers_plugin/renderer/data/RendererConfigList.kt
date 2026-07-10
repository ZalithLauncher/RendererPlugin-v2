package com.launchers_plugin.renderer.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RendererConfigList(
    @SerialName("data")
    val data: List<RendererConfig>
)