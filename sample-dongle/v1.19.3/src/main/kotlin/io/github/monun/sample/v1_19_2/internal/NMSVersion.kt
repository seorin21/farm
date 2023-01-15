package io.github.monun.sample.v1_19_2.internal

import io.github.monun.sample.internal.Version
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_19_R2.CraftServer

class NMSVersion: Version {
    override val value: String
        get() = ((Bukkit.getServer()) as CraftServer).handle.server.serverVersion
}