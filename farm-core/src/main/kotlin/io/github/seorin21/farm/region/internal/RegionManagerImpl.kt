package io.github.seorin21.farm.region.internal

import io.github.monun.tap.config.ConfigSupport
import io.github.monun.tap.event.EntityEventManager
import io.github.monun.tap.fake.FakeEntityServer

import io.github.seorin21.farm.data.PersistentDataSupport
import io.github.seorin21.farm.region.PlayerRegionKeys.regionKey
import io.github.seorin21.farm.region.RegionData
import io.github.seorin21.farm.data.persistentData
import io.github.seorin21.farm.region.RegionConfig
import io.github.seorin21.farm.region.RegionManager

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.PluginClassLoader

import java.io.File

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


class RegionManagerImpl : RegionManager {
    private val plugin: JavaPlugin = JavaPlugin.getProvidingPlugin(RegionManagerImpl::class.java)

    private val server: FakeEntityServer = FakeEntityServer.create(plugin)
    //private val manager: EntityEventManager = EntityEventManager(plugin)

    private val CONFIG_PATH: File = File(plugin.dataFolder, "config.yml")
    private var config: RegionConfig = getRegionConfig()

    private val event: RegionEventListener = RegionEventListener(plugin)

    init {
        plugin.server.scheduler.runTaskTimer(plugin, server::update, 0L, 1L) // FakeEntityServer
    }

    override fun hasRegion(player: Player, chunkX: Int, chunkZ: Int): Boolean {
        return player.persistentData[regionKey]?.let { it.chunkX == chunkX && it.chunkZ == chunkZ } ?: false
    }

    override fun hasRegion(player: Player): Boolean {
        val chunk = player.location.chunk
        return hasRegion(player, chunk.x, chunk.z)
    }

    override fun setRegion(player: Player, chunkX: Int, chunkZ: Int) {
        val data = player.persistentData
        require(data[regionKey] == null) { "Region already exists." }
        data[regionKey] = RegionData(chunkX, chunkZ)
    }

    override fun setRegion(player: Player) {
        val chunk = player.location.chunk
        setRegion(player, chunk.x, chunk.z)
    }

    override fun unsetRegion(player: Player, chunkX: Int, chunkZ: Int) {
        val data = player.persistentData
        require(data[regionKey] != null) { "Region does not exist." }
        data.remove(regionKey)
    }

    override fun unsetRegion(player: Player) {
        val chunk = player.location.chunk
        unsetRegion(player, chunk.x, chunk.z)
    }

    override fun getRegion(player: Player): Pair<Int, Int>? {
        return player.persistentData[regionKey]?.let { it.chunkX to it.chunkZ }
    }

    override fun getRegionConfig(): RegionConfig {
        return RegionConfig().apply {
            ConfigSupport.compute(this, CONFIG_PATH, separateByClass = true)
        }
    }

    override fun getRegionConfigValue(): HashMap<String, Boolean> {
        return HashMap<String, Boolean>().apply {
            config::class.memberProperties
                .filter { (it.returnType.classifier as? KClass<*>) == Boolean::class }
                .forEach { property ->
                    property.isAccessible = true
                    this[property.name] = property.getter.call(config) as? Boolean ?: false
                }
        }
    }

    override fun getRegionConfigValue(key: String): Boolean? {
        return config::class.memberProperties
            .find { it.name.equals(key, ignoreCase = true) && (it.returnType.classifier as? KClass<*>) == Boolean::class }
            ?.let {
                it.isAccessible = true
                it.getter.call(config) as? Boolean
            }
    }

    override fun setRegionConfig(): Boolean {
        return CONFIG_PATH.delete() && !ConfigSupport.compute(config, CONFIG_PATH, separateByClass = true)
    }

    override fun setRegionConfig(config: RegionConfig): Boolean {
        return CONFIG_PATH.delete() && !ConfigSupport.compute(config, CONFIG_PATH, separateByClass = true)
    }

    override fun setRegionConfigValue(value: Boolean): Boolean {
        config::class.memberProperties
            .filter { (it.returnType.classifier as? KClass<*>) == Boolean::class && it is KMutableProperty<*> }
            .forEach { property ->
                property.isAccessible = true
                (property as KMutableProperty<*>).setter.apply {
                    isAccessible = true
                    call(config, value)
                }
            }
        return setRegionConfig()
    }

    override fun setRegionConfigValue(key: String, value: Boolean): Boolean {
        val property = config::class.memberProperties
            .find { it.name.equals(key, ignoreCase = true) && (it.returnType.classifier as? KClass<*>) == Boolean::class }
                as? KMutableProperty<*> ?: return false

        property.isAccessible = true
        property.setter.apply {
            isAccessible = true
            call(config, value)
        }
        return setRegionConfig()
    }

    override fun registerEvents() {
        plugin.server.pluginManager.registerEvents(event, plugin)
    }

    override fun unregisterEvents() {
        HandlerList.unregisterAll(event)
    }

    override fun getWorldName(): String = config.world

    override fun setWorldName(world: String) {
        config.world = world
    }
}