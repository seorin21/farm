package io.github.seorin21.farm.data

import kotlinx.serialization.Serializable

object PlayerRegionKeys: PersistentDataKeychain() {
    val regionKey = complex<RegionData>("regionKey")
}

@Serializable
data class RegionData(
    // val world: String,
    val chunkX: Int,
    val chunkZ: Int
)