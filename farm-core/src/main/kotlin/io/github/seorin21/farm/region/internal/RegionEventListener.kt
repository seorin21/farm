package io.github.seorin21.farm.region.internal

import io.github.seorin21.farm.region.RegionManager

import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.*
import org.bukkit.event.world.StructureGrowEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.projectiles.BlockProjectileSource
import org.bukkit.scheduler.BukkitRunnable


class RegionEventListener(private val plugin: JavaPlugin): Listener {
    internal fun isWorldValid(world: World): Boolean {
        return world.name == RegionManager.Companion.getWorldName()
    }

    internal fun isChunkValid(chunk: Chunk, target: Chunk): Boolean {
        return chunk.x == target.x && chunk.z == target.z
    }

    internal fun isConfigValid(key: String): Boolean {
        return RegionManager.Companion.getRegionConfigValue(key) == true
    }

    internal fun hasRegion(player: Player): Boolean {
        return RegionManager.Companion.hasRegion(player)
    }

    internal fun getEntityOrPlayer(entity: Entity): String {
        return when(entity) {
            is Player -> "Player"
            else -> "Entity"
        }
    }

    // Events
    // Block
    @EventHandler
    fun onBlockDispensation(event: BlockDispenseEvent) {
        val from = event.block
        val to = from.location.add(event.velocity)

        if (!isWorldValid(from.world))
            return

        if (event.item.type.name.contains("POTION", ignoreCase = true))
            return

        if (isChunkValid(from.chunk, to.chunk)) return
        if (isConfigValid("BlockDispensatio")) return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockInteractionFromTo(event: BlockFromToEvent) {
        val from = event.block
        val to = event.toBlock

        if (!isWorldValid(from.world))
            return

        if (isChunkValid(from.chunk, to.chunk)) return
        if (isConfigValid("BlockInteractionFromTo")) return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockInteractionFromTo(event: SpongeAbsorbEvent) {
        val from = event.block
        val toto = event.blocks

        if (!isWorldValid(from.world))
            return

        if (isConfigValid("BlockInteractionFromTo"))
            return

        val removes = mutableListOf<BlockState>()
        for (to in toto) {
            if (!isChunkValid(from.chunk, to.chunk)) removes.add(to)
        }

        if (removes.isEmpty()) event.blocks.removeAll(removes)
        else event.isCancelled = true
    }

    @EventHandler
    fun onBlockSpread(event: BlockSpreadEvent) {
        val from = event.source
        val to = event.block

        if (!isWorldValid(from.world))
            return

        if (isChunkValid(from.chunk, to.chunk)) return
        if (isConfigValid("BlockSpread")) return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockExplosion(event: BlockExplodeEvent) {
        val from = event.block
        if (!isWorldValid(from.world)) return

        if (isConfigValid("BlockExplosion")) {
            if (isConfigValid("BlockExplosionByBlock"))
                return

            val toto = event.blockList()
            val removes = mutableListOf<Block>()

            for (to in toto) {
                if (!isChunkValid(from.chunk, to.chunk)) removes.add(to)
            }

            event.blockList().removeAll(removes)
        } else {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreakByPlayer(event: BlockBreakEvent) {
        val from = event.player
        if (!isWorldValid(from.world)) return

        val to = event.block
        if (isChunkValid(from.chunk, to.chunk))
            if (hasRegion(from)) return

        if (isConfigValid("BlockBreakByPlayer"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockInteractionByPlayer(event: InventoryOpenEvent) {
        val from = event.player as? Player ?: return
        val to = event.inventory.location ?: return

        if (!isWorldValid(from.world))
            return

        if (event.inventory.type == InventoryType.CHEST) return
        if (isChunkValid(from.chunk, to.chunk))
            if (hasRegion(from)) return

        if (isConfigValid("BlockInteractionByPlayer"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockInteractionByPlayer(event: PlayerInteractEvent) {
        val from = event.player
        val to = event.clickedBlock ?: return

        if (!isWorldValid(from.world))
            return

        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return

        event.let {
            if (to.type.name.contains("CHEST", ignoreCase = true) || to.type.name.contains("BUTTON", ignoreCase = true) || to.type.name.contains("DOOR", ignoreCase = true) ||
                to.type.name.contains("BED", ignoreCase = true) || to.type.name.contains("GATE", ignoreCase = true) || to.type.name.contains("LEVER", ignoreCase = true) || to.type == Material.RESPAWN_ANCHOR
            ) {
                if (from.isSneaking) return
                else return@let
            }

            val mainHand = from.inventory.itemInMainHand
            val offHand = from.inventory.itemInOffHand

            if (mainHand.type.isBlock && !mainHand.type.isEmpty) return
            if (!offHand.type.isBlock && from.isSneaking) return

            if (offHand.type.isBlock && !offHand.type.isEmpty) {
                if (to.type == Material.GRASS_BLOCK || to.type.name.contains("DIRT", ignoreCase = true)) {
                    if (mainHand.type.name.contains("SHOVEL", ignoreCase = true) || mainHand.type.name.contains("HOE", ignoreCase = true)) return@let
                }

                if (to.type.name.contains("LOG", ignoreCase = true) || to.type.name.contains("WOOD", ignoreCase = true) || to.type.name.contains("COPPER", ignoreCase = true)) {
                    if (mainHand.type.name.contains("AXE", ignoreCase = true) && !mainHand.type.name.contains("PICKAXE", ignoreCase = true)) return@let

                    if (to.type.name.contains("COPPER", ignoreCase = true))
                        if (mainHand.type == Material.HONEYCOMB) return@let
                }

                if (to.type.name.contains("SUSPICIOUS", ignoreCase = true)) {
                    if (mainHand.type.name.contains("BRUSH", ignoreCase = true)) return@let
                }

                return
            }
        }

        if (isChunkValid(from.chunk, to.chunk))
            if (hasRegion(from)) return

        if (isConfigValid("BlockInteractionByPlayer"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockMovementByPiston(event: BlockPistonExtendEvent) {
        val from = event.block
        val toto = event.blocks

        if (!isWorldValid(from.world)) return
        if (isConfigValid("BlockMovementByPiston")) return

        if (!toto.any { to -> !isChunkValid(from.chunk, to.chunk) })
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockMovementByPiston(event: BlockPistonRetractEvent) {
        val from = event.block
        val toto = event.blocks

        if (!isWorldValid(from.world)) return
        if (isConfigValid("BlockMovementByPiston")) return

        if (!toto.any { to -> !isChunkValid(from.chunk, to.chunk) })
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockPlacementByPlayer(event: BlockPlaceEvent) {
        val from = event.player
        val to = event.block

        if (!isWorldValid(from.world))
            return

        event.let {
            val itemInMainHand = from.inventory.itemInMainHand
            val itemInOffHand = from.inventory.itemInOffHand

            if (itemInMainHand.type == Material.BONE_MEAL) {
                return@let
            }
            else if (itemInOffHand.type == Material.BONE_MEAL) {
                if (itemInMainHand.type.isBlock) {
                    if (itemInMainHand.type.isEmpty) return@let
                } else return@let
            }

            if (isChunkValid(from.chunk, to.chunk))
                if (hasRegion(from)) return
        }


        if (isConfigValid("BlockPlacementByPlayer"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockPlacementByPlayer(event: PlayerBucketEmptyEvent) {
        val from = event.player
        val to = event.block

        if (!isWorldValid(from.world))
            return

        if (isChunkValid(from.chunk, to.chunk))
            if (hasRegion(from)) return

        if (isConfigValid("BlockPlacementByPlayer"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockPlacementByPlayer(event: StructureGrowEvent) {
        val from = event.location
        val toto = event.blocks

        if (!isWorldValid(from.world))
            return

        if (isConfigValid("BlockPlacementByPlayer")) return

        val removes = mutableListOf<BlockState>()
        for (to in toto) {
            if (!isChunkValid(from.chunk, to.chunk))
                removes.add(to)
        }

        event.blocks.removeAll(removes)
    }


    // Player
    @EventHandler
    fun onPlayerBedEntry(event: PlayerBedEnterEvent) {
        val from = event.player
        val to = event.bed

        if (!isWorldValid(from.world))
            return

        if (isChunkValid(from.chunk, to.chunk))
            if (hasRegion(from)) return

        if (isConfigValid("PlayerBedEntry"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerMovement(event: PlayerMoveEvent) {
        val player = event.player
        if (!isWorldValid(player.world)) return

        val from = event.from
        val to = event.to

        if (isChunkValid(from.chunk, to.chunk)) 
            if (hasRegion(player)) return

        if (isConfigValid("PlayerMovement")) return

        event.isCancelled = true

        val vehicle = player.vehicle ?: return
        player.leaveVehicle()

        object : BukkitRunnable() {
            override fun run() {
                if (vehicle.isEmpty) {
                    vehicle.remove()
                    this.cancel()
                }

                val passengers = vehicle.passengers.toList()
                for (passenger in passengers) {
                    vehicle.removePassenger(passenger)
                }
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val from = event.from
        val to = event.to

        if (!isWorldValid(from.world) || !isWorldValid(to.world))
            return

        val player = event.player
        if (isChunkValid(from.chunk, to.chunk))
            if (hasRegion(player)) return

        if (isConfigValid("PlayerTeleport"))
            return

        event.isCancelled = true
    }


    // Entitㅛ
    @EventHandler
    fun onEntityTeleport(event: EntityTeleportEvent) {
        val from = event.from
        val to = event.to ?: return

        if (!isWorldValid(from.world) || !isWorldValid(to.world))
            return

        val entity = event.entity
        if (entity is Player) return

        if (isConfigValid("EntityTeleport")) return
        if (isChunkValid(from.chunk, to.chunk)) return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityExplosion(event: EntityExplodeEvent) {
        val from = event.entity
        if (!isWorldValid(from.world)) return

        val entityType = when (from) {
            is TNTPrimed -> "TNT"
            else -> "Entity"
        }

        if (isConfigValid("${entityType}Explosion")) {
            if (isConfigValid("BlockExplosionBy${entityType}"))
                return

            val toto = event.blockList()
            val removes = mutableListOf<Block>()

            for (to in toto) {
                if (!isChunkValid(from.chunk, to.chunk)) removes.add(to)
            }

            event.blockList().removeAll(removes)
        } else {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityInteractionByPlayer(event: PlayerInteractEntityEvent) {
        val from = event.player
        val to = event.rightClicked

        if (!isWorldValid(from.world))
            return

        if (isChunkValid(from.chunk, to.chunk))
            if (hasRegion(from)) return

        if (isConfigValid("EntityInteractionByPlayer"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        var damager = event.damager
        val damagee = event.entity

        if (!isWorldValid(damager.world))
            return

        var block: Block? = null
        val damageeType = getEntityOrPlayer(damager)

        val config = when (damager) {
            is Projectile -> {
                val shooter = damager.shooter
                when (shooter) {
                    is BlockProjectileSource -> {
                        block = shooter.block
                        "${damageeType}ProjectileDamageByBlock"
                    }
                    is Entity -> {
                        damager = shooter
                        "${damageeType}ProjectileDamage"
                    }
                    else -> "${damageeType}ProjectileDamage"
                }
            }
            else -> "${damageeType}DamageBy$damageeType"
        }

        if (isConfigValid(config))
            return

        val damagerChunk = block?.chunk ?: damager.chunk
        if (isChunkValid(damagerChunk, damagee.chunk))
            if (damager is Player) if (hasRegion(damager)) return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityTargetByEntity(event: EntityTargetLivingEntityEvent) {
        val entity = event.entity
        val target = event.target as? Entity ?: return

        if (!isWorldValid(entity.world))
            return

        val entityType = getEntityOrPlayer(entity)
        val targetType = getEntityOrPlayer(target)

        val config = "${targetType}TargetBy${entityType}"
        if (isConfigValid(config)) return

        if (isChunkValid(entity.chunk, target.chunk))
            return

        event.isCancelled = true
    }


    // Potion
    @EventHandler
    fun onPotionThrrowByEntity(event: PotionSplashEvent) {
        val from = event.entity
        if (!isWorldValid(from.world)) return

        var shooter: Entity? = null
        var block: Block? = null

        val shuuter = from.shooter
        val shooterType = when (shuuter) {
            is BlockProjectileSource -> {
                block = shuuter.block
                "Block"
            }
            is Player -> {
                shooter = shuuter.player
                "Player"
            }
            is Entity -> {
                shooter = shuuter
                "Entity"
            }
            else -> return
        }

        if (shooter == null && block == null)
            return

        val chunk = block?.chunk ?: shooter?.chunk!!
        event.let {
            if (isChunkValid(from.chunk, chunk)) {
                if (shooter is Player)
                    if (hasRegion(shooter)) return@let

                if (isConfigValid("PotionThrrowBy$shooterType")) return@let
            }

            event.isCancelled = true
            return
        }

        if (isConfigValid("PotionSplash"))
            return

        val toto = event.affectedEntities.toList()
        for (to in toto) {
            if (!isChunkValid(from.chunk, to.chunk)) event.setIntensity(to, 0.0)
        }
    }

    @EventHandler
    fun onLingeringPotionSplash(event: LingeringPotionSplashEvent) {
        val from = event.entity
        if (!isWorldValid(from.world)) return

        var shooter: Entity? = null
        var block: Block? = null

        val shuuter = from.shooter
        val shooterType = when (shuuter) {
            is BlockProjectileSource -> {
                block = shuuter.block
                "Block"
            }
            is Player -> {
                shooter = shuuter.player
                "Player"
            }
            is Entity -> {
                shooter = shuuter
                "Entity"
            }
            else -> return
        }

        if (shooter == null && block == null)
            return

        val chunk = block?.chunk ?: shooter?.chunk!!
        event.let {
            if (isChunkValid(from.chunk, chunk)) {
                if (shooter is Player)
                    if (hasRegion(shooter)) return@let

                if (isConfigValid("LingeringPotionThrrowBy$shooterType")) return@let
            }

            event.isCancelled = true
            return
        }

        if (isConfigValid("LingeringPotionSplash"))
            return

        val cloud = event.areaEffectCloud
        val effects = cloud.customEffects

        val base = event.areaEffectCloud.basePotionData
        val baseType = base.type
        val effectType = baseType.effectType

        val location = cloud.location
        val radius = cloud.radius.toDouble()

        val wait = cloud.waitTime.toLong()
        val duration = cloud.duration.toLong()

        val task = Bukkit.getServer().scheduler.runTaskTimer(plugin, object : Runnable {
            override fun run() {
                val toto = cloud.world.getNearbyEntities(location, radius, radius, radius)
                for (to in toto) {
                    if (to !is LivingEntity)
                        continue

                    if (isChunkValid(from.chunk, to.chunk))
                        continue

                    if (effectType != null) {
                        val isStrong = baseType.let { basePotionType ->
                            val hasEffect = to.getPotionEffect(effectType) ?: return@let true

                            if (base.isUpgraded && hasEffect.amplifier < 1)
                                return@let true

                            val baseDuration = if (base.isExtended) 480 else 180
                            return@let hasEffect.duration > baseDuration * 20
                        }

                        if (isStrong != true)
                            to.removePotionEffect(effectType)
                    }

                    for (effect in effects) {
                        val hasEffect = to.getPotionEffect(effect.type)
                        if (hasEffect != null)
                            if (hasEffect.amplifier > effect.amplifier || (hasEffect.amplifier == effect.amplifier && hasEffect.duration > effect.duration))
                                continue

                        to.removePotionEffect(effect.type)
                    }
                }
            }
        }, wait, 1L)

        Bukkit.getServer().scheduler.runTaskLater(plugin, object : Runnable {
            override fun run() {
                Bukkit.getServer().scheduler.cancelTask(task.taskId)
            }
        }, wait + duration)
    }


    // Other ..
    @EventHandler
    fun onItemMovementFromTo(event: InventoryMoveItemEvent) {
        val from = event.initiator.location ?: return
        val to = event.destination.location ?: return

        if (!isWorldValid(from.world))
            return

        if (isChunkValid(from.chunk, to.chunk)) return
        if (isConfigValid("ItemMovementFromTo")) return

        event.isCancelled = true
    }
}