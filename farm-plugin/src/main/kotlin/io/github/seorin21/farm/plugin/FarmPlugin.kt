package io.github.seorin21.farm.plugin

import io.github.seorin21.farm.plugin.command.CropsCommand
import io.github.seorin21.farm.plugin.command.RegionCommand
import io.github.seorin21.farm.region.RegionManager
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.kotlin.coroutines.annotations.installCoroutineSupport
import org.incendo.cloud.paper.LegacyPaperCommandManager

class FarmPlugin: JavaPlugin() {
    override fun onEnable() {
        RegionManager.registerEvents()


        val commandManager = LegacyPaperCommandManager.createNative(
            this,
            ExecutionCoordinator.simpleCoordinator()
        )

        if (commandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER))
            commandManager.registerBrigadier()

        if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION))
            commandManager.registerAsynchronousCompletions()

        val annotationParser = AnnotationParser<CommandSender>(
            commandManager,
            CommandSender::class.java
        )

        annotationParser.installCoroutineSupport()

        //annotationParser.parse(this)
        annotationParser.parse(CropsCommand)
        annotationParser.parse(RegionCommand)
    }

    override fun onDisable() {
    }
}