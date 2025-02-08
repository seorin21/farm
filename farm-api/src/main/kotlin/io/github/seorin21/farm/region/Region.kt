package io.github.seorin21.farm.region

interface Region {
    val chunkX: Int
    val chunkZ: Int

    val groups: MutableList<RegionGroup>
}