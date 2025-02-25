package io.github.seorin21.farm.plugin.command

import io.github.seorin21.farm.crops.CropsConfig
import io.github.seorin21.farm.crops.CropsSupport
import io.github.seorin21.farm.crops.CropsType
import org.bukkit.command.CommandSender
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.suggestion.Suggestions
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Suppress("unused")
@Command("crops")
object CropsCommand {
    @Command("config get <type> <key>")
    fun getCropsConfigValue(
        sender: CommandSender,
        @Argument("type", suggestions = "CROPS_TYPE_KEYS") type: String,
        @Argument("key", suggestions = "CROPS_CONFIG_KEYS") key: String
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용 가능합니다.")

        val result = CropsSupport.getConfig(CropsType.valueOf(type), key)
        sender.sendMessage("getRegionConfigValue(`${CropsType.valueOf(type)}`, `$key`):: $result")
    }

    @Command("config set <type> <key> <value>")
    fun setCropsConfigValue(
        sender: CommandSender,
        @Argument("type", suggestions = "CROPS_TYPE_KEYS") type: String,
        @Argument("key", suggestions = "CROPS_CONFIG_KEYS") key: String,
        @Argument("value") value: String
    ) {
        if (!sender.isOp)
            return sender.sendMessage("해당 명령어는 관리자만 사용 가능합니다.")

        val parsedValue: Any = when {
            value.toIntOrNull() != null -> value.toInt()
            value.toDoubleOrNull() != null -> value.toDouble()
            else -> value
        }

        val result = when (parsedValue) {
            is Int -> CropsSupport.setConfigValue(CropsType.valueOf(type), key, parsedValue)
            is Double -> CropsSupport.setConfigValue(CropsType.valueOf(type), key, parsedValue)
            else -> return sender.sendMessage("해당 명령어의 <value>는 숫자거나 소수여야 합니다.")
        }

        sender.sendMessage("setRegionConfigValue(`${CropsType.valueOf(type)}` `$key`, $parsedValue):: $result")
    }

    @Suggestions("CROPS_TYPE_KEYS")
    fun getCropsTypeKeys(): List<String> {
        return CropsType.entries.map { it.name }
    }

    @Suggestions("CROPS_CONFIG_KEYS")
    fun getCropsConfigKeys(): List<String> {
        return CropsConfig::class.memberProperties
            .filter { (it.returnType.classifier as? KClass<*>) == Int::class || (it.returnType.classifier as? KClass<*>) == Double::class }
            .map { it.apply { isAccessible = true }.name }
    }
}