package io.github.seorin21.farm.internal

import io.github.seorin21.farm.loader.LibraryLoader
import io.github.seorin21.farm.Sample

class SampleImpl: Sample {
    private val version = LibraryLoader.loadNMS(Version::class.java)

    override fun printCoreMessage() {
        println("This is core, version = ${version.value}")
    }

    override fun version(): String {
        return version.value
    }
}

interface Version {
    val value: String
}