package io.github.seorin21.farm.region

interface Region {
    val chunkX: Int
    val chunkZ: Int

    val items: MutableList<RegionItem>
}