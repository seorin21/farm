package io.github.seorin21.farm.crops.internal

import io.github.monun.tap.config.ConfigSupport
import io.github.seorin21.farm.crops.CropsConfig
import io.github.seorin21.farm.crops.CropsSupport
import io.github.seorin21.farm.crops.CropsType
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class CropsSupportImpl: CropsSupport {
    private val plugin: JavaPlugin = JavaPlugin.getProvidingPlugin(CropsSupportImpl::class.java)

    private val CROPS_PATH: File = File(plugin.dataFolder, "crops")
    private val CROPS_CONFIG_PATH: File = File(CROPS_PATH, "config")

    private val crops: HashMap<String, CropsConfig> = hashMapOf()

    init {
        CropsType.entries.forEach { crop ->
            val config = getConfig(crop) ?: return@forEach
            crops[crop.name] = config
        }
    }

    override fun getConfig(type: CropsType): CropsConfig? {
        return crops.getOrElse(type.name) {
            val CONFIG_FILE = File(CROPS_CONFIG_PATH, "${type.name}.yml")
            CropsConfig().apply {
                ConfigSupport.compute(this, CONFIG_FILE, separateByClass = true)
            }.takeIf { it.material.name == type.name }
        }
    }

    override fun getConfig(type: CropsType, key: String): String? {
        val crop = crops[type.name] ?: return null
        return crop::class.memberProperties
            .find { it.name.equals(key, ignoreCase = true) && ((it.returnType.classifier as? KClass<*>) == Int::class || (it.returnType.classifier as? KClass<*>) == Double::class) }
            ?.let {
                it.isAccessible = true
                it.getter.call(crop).toString()
            }
    }

    override fun setConfig(config: CropsConfig): Boolean {
        val CONFIG_FILE = File(CROPS_CONFIG_PATH, "${config.material.name}.yml")
        return CONFIG_FILE.delete() && !ConfigSupport.compute(config, CONFIG_FILE, separateByClass = true)
    }

    override fun setConfig(type: CropsType, config: CropsConfig): Boolean {
        val CONFIG_FILE = File(CROPS_CONFIG_PATH, "${type.name}.yml")
        return CONFIG_FILE.delete() && !ConfigSupport.compute(config, CONFIG_FILE, separateByClass = true)
    }

    override fun setConfigValue(type: CropsType, key: String, value: Int): Boolean {
        val crop = crops[type.name] ?: return false
        val property = crop::class.memberProperties
            .find { it.name.equals(key, ignoreCase = true) && (it.returnType.classifier as? KClass<*>) == Int::class }
                as? KMutableProperty<*> ?: return false

        property.isAccessible = true
        property.setter.apply {
            isAccessible = true
            call(crop, value)
        }

        return setConfig(crop)
    }

    override fun setConfigValue(type: CropsType, key: String, value: Double): Boolean {
        val crop = crops[type.name] ?: return false
        val property = crop::class.memberProperties
            .find { it.name.equals(key, ignoreCase = true) && ((it.returnType.classifier as? KClass<*>) == Double::class) }
                as? KMutableProperty<*> ?: return false

        property.isAccessible = true
        property.setter.apply {
            isAccessible = true
            call(crop, value)
        }

        return setConfig(crop)
    }
}