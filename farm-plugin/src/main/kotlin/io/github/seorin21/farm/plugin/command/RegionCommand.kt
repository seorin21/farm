package io.github.seorin21.farm.plugin.command

import io.github.monun.tap.config.Config
import io.github.seorin21.farm.region.RegionConfig
import io.github.seorin21.farm.region.RegionManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.suggestion.Suggestion
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Suppress("unused")
@Command("region")
object RegionCommand {
    @Command("has")
    fun has(
        sender: CommandSender
    ) {
        val player = sender as? Player ?: return sender.sendMessage("해당 명령어는 플레이어만 사용 가능합니다.")

        val chunkX = player.chunk.x
        val chunkZ = player.chunk.z

        val result = RegionManager.hasRegion(player, chunkX = chunkX, chunkZ = chunkZ)
        sender.sendMessage("hasRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ):: $result")
    }

    @Command("has chunk <chunkX> <chunkZ>")
    fun has(
        sender: CommandSender,
        @Argument("chunkX") chunkX: Int,
        @Argument("chunkZ") chunkZ: Int
    ) {
        val player = sender as? Player ?: return sender.sendMessage("해당 명령어는 플레이어만 사용 가능합니다.")

        val result = RegionManager.hasRegion(player, chunkX = chunkX, chunkZ = chunkZ)
        sender.sendMessage("hasRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ):: $result")
    }

    @Command("has player <name>")
    fun has(
        sender: CommandSender,
        @Argument("name") name: String
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용가능합니다.")

        val player = Bukkit.getPlayer(name) ?: return sender.sendMessage("`$name`이라는 플레이어가 존재하지 않습니다.")

        val chunkX = player.chunk.x
        val chunkZ = player.chunk.z

        val result = RegionManager.hasRegion(player, chunkX = chunkX, chunkZ = chunkZ)
        sender.sendMessage("hasRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ):: $result")
    }

    @Command("has player <name> chunk <chunkX> <chunkZ>")
    fun has(
        sender: CommandSender,
        @Argument("name") name: String,
        @Argument("chunkX") chunkX: Int,
        @Argument("chunkZ") chunkZ: Int
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용가능합니다.")

        val player = Bukkit.getPlayer(name) ?: return sender.sendMessage("`$name`이라는 플레이어가 존재하지 않습니다.")

        val result = RegionManager.hasRegion(player, chunkX = chunkX, chunkZ = chunkZ)
        sender.sendMessage("hasRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ):: $result")
    }

    @Command("set")
    fun set(
        sender: CommandSender
    ) {
        val player = sender as? Player ?: return sender.sendMessage("해당 명령어는 플레이어만 사용 가능합니다.")
        RegionManager.setRegion(player)


        sender.sendMessage("setRegion(`${player.name}`)")
    }

    @Command("set player <name>")
    fun set(
        sender: CommandSender,
        @Argument("name") name: String
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용가능합니다.")

        val player = Bukkit.getPlayer(name) ?: return sender.sendMessage("`$name`이라는 플레이어가 존재하지 않습니다.")

        val chunkX = player.chunk.x
        val chunkZ = player.chunk.z

        RegionManager.setRegion(player, chunkX = chunkX, chunkZ = chunkZ)


        sender.sendMessage("setRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ)")
    }

    @Command("set player <name> chunk <chunkX> <chunkZ>")
    fun set(
        sender: CommandSender,
        @Argument("name") name: String,
        @Argument("chunkX") chunkX: Int,
        @Argument("chunkZ") chunkZ: Int
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용가능합니다.")

        val player = Bukkit.getPlayer(name) ?: return sender.sendMessage("`$name`이라는 플레이어가 존재하지 않습니다.")
        RegionManager.setRegion(player, chunkX = chunkX, chunkZ = chunkZ)


        sender.sendMessage("setRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ)")
    }

    @Command("unset")
    fun unset(
        sender: CommandSender
    ) {
        val player = sender as? Player ?: return sender.sendMessage("해당 명령어는 플레이어만 사용 가능합니다.")

        val chunkX = player.chunk.x
        val chunkZ = player.chunk.z

        val result = RegionManager.unsetRegion(player, chunkX = chunkX, chunkZ = chunkZ)
        sender.sendMessage("hasRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ):: $result")
    }

    @Command("unset chunk <chunkX> <chunkZ>")
    fun unset(
        sender: CommandSender,
        @Argument("chunkX") chunkX: Int,
        @Argument("chunkZ") chunkZ: Int
    ) {
        val player = sender as? Player ?: return sender.sendMessage("해당 명령어는 플레이어만 사용 가능합니다.")

        val result = RegionManager.unsetRegion(player, chunkX = chunkX, chunkZ = chunkZ)
        sender.sendMessage("hasRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ):: $result")
    }

    @Command("unset player <name> ")
    fun unset(
        sender: CommandSender,
        @Argument("name") name: String
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용가능합니다.")

        val player = Bukkit.getPlayer(name) ?: return sender.sendMessage("`$name`이라는 플레이어가 존재하지 않습니다.")

        val chunkX = player.chunk.x
        val chunkZ = player.chunk.z

        val result = RegionManager.unsetRegion(player, chunkX = chunkX, chunkZ = chunkZ)
        sender.sendMessage("hasRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ):: $result")
    }

    @Command("unset player <name> chunk <chunkX> <chunkZ>")
    fun unset(
        sender: CommandSender,
        @Argument("name") name: String,
        @Argument("chunkX") chunkX: Int,
        @Argument("chunkZ") chunkZ: Int
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용가능합니다.")

        val player = Bukkit.getPlayer(name) ?: return sender.sendMessage("`$name`이라는 플레이어가 존재하지 않습니다.")

        val result = RegionManager.unsetRegion(player, chunkX = chunkX, chunkZ = chunkZ)
        sender.sendMessage("hasRegion(`${player.name}`, chunkX = $chunkX, chunkZ = $chunkZ):: $result")
    }

    @Command("config get")
    fun getConfig(
        sender: CommandSender
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용가능합니다.")

        getConfigKeys().forEach {
            sender.sendMessage(it)
        }

        sender.sendMessage(RegionManager.getRegionConfig().toString())
    }

    @Command("config get <key>")
    fun getConfigValue(
        sender: CommandSender,
        @Argument("key", suggestions = "CONFIG_KEYS") key: String
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용 가능합니다.")

        if (key == "ALL") {
            val result = RegionManager.getRegionConfigValue().map { "`${it.key}:: ${it.value}`" }
            for (i in 0 until result.size)
                sender.sendMessage(result[i])

            return
        }

        val result = RegionManager.getRegionConfigValue(key)
        sender.sendMessage("getRegionConfigValue(`$key`):: $result")
    }

    @Command("config set <key> <value>")
    fun setConfigValue(
        sender: CommandSender,
        @Argument("key", suggestions = "CONFIG_KEYS") key: String,
        @Argument("value") value: Boolean
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용 가능합니다.")

        if (key == "ALL") {
            val result = RegionManager.setRegionConfigValue(value)
            return sender.sendMessage("setRegionConfigValue($value):: $result")
        }

        val result = RegionManager.setRegionConfigValue(key, value)
        sender.sendMessage("setRegionConfigValue(`$key`, $value):: $result")
    }

    @Suggestions("CONFIG_KEYS")
    fun getConfigKeys(): List<String> {
        return RegionConfig::class.memberProperties
            .filter { (it.returnType.classifier as? KClass<*>) == Boolean::class }
            .map { it.apply { isAccessible = true }.name }
            .plus("ALL")
    }

    @Command("event register")
    fun registerEvents(
        sender: CommandSender
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용 가능합니다.")

        RegionManager.registerEvents()
    }

    @Command("event unregister")
    fun unregisterEvents(
        sender: CommandSender
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용 가능합니다.")

        RegionManager.unregisterEvents()
    }
}