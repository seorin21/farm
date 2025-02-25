package io.github.seorin21.farm.crops

import io.github.seorin21.farm.loader.LibraryLoader

interface Crops {
    val type: CropsConfig

    val temperature: Double
    val duration: Int
}

interface CropsSupport {
    companion object : CropsSupport by LibraryLoader.loadImplement(CropsSupport::class.java)

    fun getConfig(type: CropsType): CropsConfig?

    fun getConfig(type: CropsType, key: String): String?

    fun setConfig(config: CropsConfig): Boolean

    fun setConfig(type: CropsType, config: CropsConfig): Boolean

    fun setConfigValue(type: CropsType, key: String, value: Int): Boolean

    fun setConfigValue(type: CropsType, key: String, value: Double): Boolean
}