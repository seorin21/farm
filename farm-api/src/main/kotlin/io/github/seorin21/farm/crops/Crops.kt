package io.github.seorin21.farm.crops

import io.github.monun.tap.config.Config
import io.github.seorin21.farm.loader.LibraryLoader

open class Crops {
    @Config(required = true)
    val type: CropsType = CropsType.WHEAT

    @Config(required = true)
    val x: Int = -1
    @Config(required = true)
    val y: Int = -1
    @Config(required = true)
    val z: Int = -1

    @Config(required = true)
    val created: Long = System.currentTimeMillis()
    @Config(required = true)
    val duration: Int = 0
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