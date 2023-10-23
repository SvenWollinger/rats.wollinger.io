package io.wollinger.rats

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val rbSymbol: String,
    val logDebug: Boolean,
    val rats: List<String>
)

@Serializable
data class Rat (
    val name: String,
    val thumbnail: String,
    val bigPic: String,
    val born: String,
    val passed: String?,
    val images: List<String>
)