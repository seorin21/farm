package io.github.seorin21.farm

import io.github.seorin21.farm.loader.LibraryLoader

interface Sample {
    companion object: Sample by LibraryLoader.loadImplement(Sample::class.java)

    fun printCoreMessage()

    fun version(): String
}