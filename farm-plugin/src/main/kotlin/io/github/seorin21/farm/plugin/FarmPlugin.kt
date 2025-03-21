package io.github.seorin21.farm.plugin

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.kotlin.coroutines.annotations.installCoroutineSupport
import org.incendo.cloud.paper.LegacyPaperCommandManager

class FarmPlugin: JavaPlugin() {
    override fun onEnable() {
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
    }

    override fun onDisable() {
    }
}