package io.github.seorin21.farm.region

import io.github.seorin21.farm.loader.LibraryLoader
import org.bukkit.entity.Player

interface RegionManager {
    companion object : RegionManager by LibraryLoader.loadImplement(RegionManager::class.java)

    fun hasRegion(player: Player): Boolean

    fun hasRegion(player: Player, chunkX: Int, chunkZ: Int): Boolean

    fun setRegion(player: Player)

    fun setRegion(player: Player, chunkX: Int, chunkZ: Int)

    fun unsetRegion(player: Player)

    fun unsetRegion(player: Player, chunkX: Int, chunkZ: Int)

    fun getRegion(player: Player): Pair<Int, Int>?

    fun getRegionConfig(): RegionConfig

    fun getRegionConfigValue(): HashMap<String, Boolean>

    fun getRegionConfigValue(key: String): Boolean?

    fun setRegionConfig(): Boolean

    fun setRegionConfig(config: RegionConfig): Boolean

    fun setRegionConfigValue(value: Boolean): Boolean

    fun setRegionConfigValue(key: String, value: Boolean): Boolean

    fun registerEvents()

    fun unregisterEvents()

    fun getWorldName(): String

    fun setWorldName(world: String)
}