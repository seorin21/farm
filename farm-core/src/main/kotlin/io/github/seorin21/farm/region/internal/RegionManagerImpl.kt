package io.github.seorin21.farm.region.internal

import io.github.monun.tap.config.ConfigSupport
import io.github.monun.tap.event.EntityEventManager

import io.github.seorin21.farm.data.PersistentDataSupport
import io.github.seorin21.farm.data.PlayerRegionKeys.regionKey
import io.github.seorin21.farm.data.RegionData
import io.github.seorin21.farm.data.persistentData
import io.github.seorin21.farm.region.RegionConfig
import io.github.seorin21.farm.region.RegionManager

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.PluginClassLoader

import java.io.File

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


@Suppress("unused")
class RegionManagerImpl: RegionManager {
    internal val plugin: Plugin
    internal val manager: EntityEventManager

    private var config: RegionConfig
    private val CONFIG_PATH: File

    private val event: RegionEventListener

    init {
        val pluginClassLoader = PersistentDataSupport::class.java.classLoader as PluginClassLoader
        val plugin = pluginClassLoader.plugin

        this.plugin = plugin ?: throw IllegalStateException("Cannot find plugin instance")
        this.manager = EntityEventManager(this.plugin)

        this.CONFIG_PATH = File(plugin.dataFolder, "config.yml")

        this.config = this.getRegionConfig()

        this.event = RegionEventListener(this.plugin)
    }

    override fun hasRegion(player: Player): Boolean {
        val chunk = player.location.chunk

        val chunkX = chunk.x
        val chunkZ = chunk.z

        return hasRegion(player, chunkX, chunkZ)
    }

    override fun hasRegion(player: Player, chunkX: Int, chunkZ: Int): Boolean {
        val data = player.persistentData
        val region = data[regionKey] ?: return false

        return region.chunkX == chunkX && region.chunkZ == chunkZ
    }

    override fun setRegion(player: Player) {
        val chunk = player.location.chunk

        val chunkX = chunk.x
        val chunkZ = chunk.z

        setRegion(player, chunkX, chunkZ)
    }

    override fun setRegion(player: Player, chunkX: Int, chunkZ: Int) {
        val data = player.persistentData
        require(data[regionKey] == null) { "이미 구역이 존재합니다." }

        data[regionKey] = RegionData(chunkX, chunkZ)
    }

    override fun unsetRegion(player: Player) {
        val chunk = player.location.chunk

        val chunkX = chunk.x
        val chunkZ = chunk.z

        unsetRegion(player, chunkX, chunkZ)
    }

    override fun unsetRegion(player: Player, chunkX: Int, chunkZ: Int) {
        val data = player.persistentData
        require(data[regionKey] != null) { "구역이 존재하지 않습니다." }

        data.remove(regionKey)
    }

    override fun getRegion(player: Player): Pair<Int, Int>? {
        val data = player.persistentData

        val region = data[regionKey]
        if (region == null)
            return null

        return region.chunkX to region.chunkZ
    }

    override fun getRegionConfig(): RegionConfig {
        val config = RegionConfig()
        ConfigSupport.compute(config, CONFIG_PATH, separateByClass = true)

        return config
    }

    override fun getRegionConfigValue(): HashMap<String, Boolean> {
        val configs = HashMap<String, Boolean>()
        this.config::class.memberProperties.forEach { property ->
            if ((property.returnType.classifier as? KClass<*>) != Boolean::class)
                return@forEach

            property.isAccessible = true

            val value = property.getter.call(config) as? Boolean
            configs[property.name] = value == true
        }
        return configs
    }

    override fun getRegionConfigValue(key: String): Boolean? {
        val property = this.config::class.memberProperties.find {
            it.name.equals(key, ignoreCase = true) && (it.returnType.classifier as? KClass<*>) == Boolean::class
        } ?: return null

        property.isAccessible = true

        return property.getter.call(config) as? Boolean
    }

    override fun setRegionConfig(): Boolean {
        if (!CONFIG_PATH.delete())
            return false

        return !ConfigSupport.compute(this.config, CONFIG_PATH, separateByClass = true)
    }

    override fun setRegionConfig(config: RegionConfig): Boolean {
        if (!CONFIG_PATH.delete())
            return false

        return !ConfigSupport.compute(config, CONFIG_PATH, separateByClass = true)
    }

    override fun setRegionConfigValue(value: Boolean): Boolean {
        var result = true
        this.config::class.memberProperties.forEach { property ->
            if ((property.returnType.classifier as? KClass<*>) != Boolean::class || property !is KMutableProperty<*>)
                return@forEach

            property.isAccessible = true
            if (!property.setter.isAccessible)
                property.setter.isAccessible = true

            try {
                property.setter.call(config, value)
            } catch (e: Exception) {
                result = false
            }
        }

        setRegionConfig()


        return result
    }

    override fun setRegionConfigValue(key: String, value: Boolean): Boolean {
        val property = this.config::class.memberProperties.find {
            it.name.equals(key, ignoreCase = true) && (it.returnType.classifier as? KClass<*>) == Boolean::class
        } ?: return false

        property.isAccessible = true
        if (property !is KMutableProperty<*>)
            return false

        if (property.setter.isAccessible.not())
            property.setter.isAccessible = true

        property.setter.call(config, value)

        setRegionConfig()


        return true
    }

    override fun registerEvents() {
        plugin.server.pluginManager.registerEvents(this.event, plugin)
    }

    override fun unregisterEvents() {
        HandlerList.unregisterAll(this.event)
    }

    override fun getWorldName(): String {
        return this.config.world
    }

    override fun setWorldName(world: String) {
        this.config.world = world
    }
}