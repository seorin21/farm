package io.github.seorin21.farm.region.internal.event

import io.github.monun.tap.event.EntityProvider
import io.github.monun.tap.event.TargetEntity
import io.github.seorin21.farm.region.RegionManager
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerBedLeaveEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.projectiles.BlockProjectileSource
import org.bukkit.scheduler.BukkitRunnable

class RegionEventListener(private val plugin: JavaPlugin): Listener {
    // Player 
    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.PlayerDamageByEntity::class)
    fun onPlayerDamageByEntity(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val player = event.entity as Player
        if (isRegionValid(player)) {
            val damager = event.damager

            if (isChunkValid(player.chunk, damager.chunk))
                return

            if (isConfigValid("PlayerDamageByEntity"))
                return
        }

        event.isCancelled = true
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByBlock.PlayerDamageByBlock::class)
    fun onPlayerDamageByBlock(event: EntityDamageByBlockEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val damager = event.damager ?: return
        val player = event.entity as Player

        if (isRegionValid(player)) {
            if (isChunkValid(damager.chunk, player.chunk))
                return

            if (isConfigValid("PlayerDamageByBlock"))
                return
        }

        event.isCancelled = true
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.PlayerDamageByPlayer::class)
    fun onPlayerDamageByPlayer(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val damager = event.damager as Player
        if (isRegionValid(damager)) {
            val player = event.entity as Player

            if (isChunkValid(damager.chunk, player.chunk))
                return

            if (isConfigValid("PlayerDamageByPlayer"))
                return
        }

        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerMovement(event: PlayerMoveEvent) {
        val world = event.from.world
        if (!isWorldValid(world))
            return

        val player = event.player
        if (isRegionValid(player)) {
            val from = event.from
            val to = event.to

            if (isChunkValid(from.chunk, to.chunk))
                return

            if (isConfigValid("PlayerMovement"))
                return
        }

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
        val world = event.from.world
        if (!isWorldValid(world))
            return

        val player = event.player
        if (isRegionValid(player)) {
            val from = event.from
            val to = event.to

            if (isChunkValid(from.chunk, to.chunk))
                return

            if (isConfigValid("PlayerTeleport"))
                return
        }

        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerTeleportByBed(event: PlayerBedLeaveEvent) {
        val world = event.player.world
        if (!isWorldValid(world))
            return

        val player = event.player
        if (isRegionValid(player)) {
            val bed = event.bed

            if (isChunkValid(bed.chunk, player.chunk))
                return

            if (isConfigValid("PlayerTeleportByBed"))
                return
        }

        val location = player.location
        val y = location.y.toInt()

        val pitch = location.pitch
        val yaw = location.yaw

        val floor = event.bed.chunk.getBlock(8, y, 8).location.add(0.5, 0.0, 0.5)

        floor.y = y.toDouble()
        floor.pitch = pitch
        floor.yaw = yaw

        player.teleport(floor)
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityTarget.PlayerTargetByEntity::class)
    fun onPlayerTargetByEntity(event: EntityTargetEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val entity = event.entity
        val target = event.target as? Player ?: return

        if (isChunkValid(entity.chunk, target.chunk))
            return

        if (isConfigValid("PlayerTargetByEntity"))
            return

        event.isCancelled = true
    }

    // Entity 
    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.EntityDamageByEntity::class)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val entity = event.entity
        val damager = event.damager

        if (isChunkValid(entity.chunk, damager.chunk))
            return

        if (isConfigValid("EntityDamageByEntity"))
            return

        event.isCancelled = true
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.EntityDamageByPlayer::class)
    fun onEntityDamageByPlayer(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val player = event.damager as Player
        if (isRegionValid(player)) {
            val damagee = event.entity

            if (isChunkValid(player.chunk, damagee.chunk))
                return

            if (isConfigValid("EntityDamageByPlayer"))
                return
        }

        event.isCancelled = true
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByBlock.EntityDamageByBlock::class)
    fun onEntityDamageByBlock(event: EntityDamageByBlockEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val damager = event.damager ?: return
        val entity = event.entity

        if (isChunkValid(damager.chunk, entity.chunk))
            return

        if (isConfigValid("EntityDamageByBlock"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityTeleportEvent(event: EntityTeleportEvent) {
        val world = event.from.world
        if (!isWorldValid(world))
            return

        val from = event.from
        val to = event.to ?: return

        if (isChunkValid(from.chunk, to.chunk))
            return

        if (isConfigValid("EntityTeleport"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityExplosion(event: EntityExplodeEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        if (isConfigValid("EntityExplosion")) {
            if (isConfigValid("BlockExplosionByEntity"))
                return

            val chunk = event.location.chunk
            val blocks = mutableListOf<Block>()

            for (block in event.blockList()) {
                if (isChunkValid(chunk, block.chunk))
                    continue

                blocks.add(block)
            }

            event.blockList().removeAll(blocks)

            return
        }

        event.isCancelled = true
    }

    @EventHandler
    @TargetEntity(EntityProvider.PlayerInteractEntity.Clicked::class)
    fun onEntityInteractionByPlayer(event: PlayerInteractEntityEvent) {
        val world = event.player.world.name
        if (world != RegionManager.Companion.getWorldName())
            return

        val player = event.player
        if (isRegionValid(player)) {
            val entity = event.rightClicked

            if (isChunkValid(player.chunk, entity.chunk))
                return

            if (isConfigValid("EntityInteractionByPlayer"))
                return
        }

        event.isCancelled = true
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityTarget.EntityTargetByEntity::class)
    fun onEntityTargetByEntity(event: EntityTargetEvent)  {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val entity = event.entity
        val target = event.target ?: return

        if (isChunkValid(entity.chunk, target.chunk))
            return

        if (isConfigValid("EntityTargetByEntity"))
            return

        event.isCancelled = true
    }

    // Block Reladted

    @EventHandler
    fun onBlockPlacementByPlayer(event: BlockPlaceEvent) {
        val world = event.player.world
        if (!isWorldValid(world))
            return

        val player = event.player
        if (isRegionValid(player)) {
            val block = event.blockPlaced

            if (isChunkValid(player.chunk, block.chunk))
                return

            if (isConfigValid("BlockPlaceByPlayer"))
                return
        }

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockBreakByPlayer(event: BlockBreakEvent) {
        val world = event.player.world
        if (!isWorldValid(world))
            return

        val player = event.player
        if (isRegionValid(player)) {
            val block = event.block

            if (isChunkValid(player.chunk, block.chunk))
                return

            if (isConfigValid("BlockBreakByPlayer"))
                return
        }

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockInteractionByPlayer(event: PlayerInteractEvent) {
        val world = event.player.world
        if (!isWorldValid(world))
            return

        val player = event.player
        if (isRegionValid(player)) {
            val block = event.clickedBlock ?: return
            if (isChunkValid(player.chunk, block.chunk))
                return

            if (isConfigValid("BlockInteractionByPlayer"))
                return
        }

        event.isCancelled = true
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityInteract.BlockInteractionByEntity::class)
    fun onBlockInteractionByEntity(event: EntityInteractEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val entity = event.entity
        val block = event.block

        if (isChunkValid(entity.chunk, block.chunk))
            return

        if (isConfigValid("BlockInteractionByEntity"))
            return

        event.isCancelled = true
    }

    // Liquid

    @EventHandler
    fun onLiquidPlacementByPlayer(event: PlayerBucketEmptyEvent) {
        val world = event.player.world
        if (!isWorldValid(world))
            return

        val player = event.player
        val block = event.block

        if (isRegionValid(player)) {
            if (isChunkValid(player.chunk, block.chunk))
                return

            if (isConfigValid("LiquidPlacementByPlayer"))
                return
        }

        event.isCancelled = true
    }

    // Projectlie Damage Reladted

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.PlayerProjectileDamageByEntity::class)
    fun onPlayerProjectileDamageByEntity(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val player = event.entity as Player
        val damager = (event.damager as Projectile).shooter as Entity

        if (isRegionValid(player)) {
            if (isChunkValid(player.chunk, damager.chunk))
                return

            if (isConfigValid("PlayerProjectileDamageByEntity"))
                return
        }

        event.isCancelled = true
        event.damager.remove()
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.PlayerProjectileDamageByBlock::class)
    fun onPlayerProjectileDamageByBlock(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val player = event.entity as Player
        val damager = ((event.damager as Projectile).shooter as BlockProjectileSource).block

        if (isChunkValid(player.chunk, damager.chunk))
            return

        if (isConfigValid("PlayerProjectileDamageByBlock"))
            return

        event.isCancelled = true
        event.damager.remove()
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.PlayerProjectileDamageByPlayer::class)
    fun onPlayerProjectileDamageByPlayer(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val damager = (event.damager as Projectile).shooter as Player
        if (isRegionValid(damager)) {
            val player = event.entity as Player

            if (isChunkValid(damager.chunk, player.chunk))
                return

            if (isConfigValid("PlayerProjectileDamageByPlayer"))
                return
        }

        event.isCancelled = true
        event.damager.remove()
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.EntityProjectileDamageByEntity::class)
    fun onEntityProjectileDamageByEntity(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val entity = event.entity
        val damager = ((event.damager as Projectile).shooter as Entity)

        if (isChunkValid(entity.chunk, damager.chunk))
            return

        if (isConfigValid("EntityProjectileDamageByEntity"))
            return

        event.isCancelled = true
        event.damager.remove()
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.EntityProjectileDamageByBlock::class)
    fun onEntityProjectileDamageByBlock(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val entity = event.entity
        val damager = ((event.damager as Projectile).shooter as BlockProjectileSource).block

        if (isChunkValid(entity.chunk, damager.chunk))
            return

        if (isConfigValid("EntityProjectileDamageByBlock"))
            return

        event.isCancelled = true
        event.damager.remove()
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.EntityDamageByEntity.EntityProjectileDamageByPlayer::class)
    fun onEntityProjectileDamageByPlayer(event: EntityDamageByEntityEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val player = (event.damager as Projectile).shooter as Player
        if (isRegionValid(player)) {
            val damagee = event.entity

            if (isChunkValid(player.chunk, damagee.chunk))
                return

            if (isConfigValid("EntityProjectlieDamageByPlayer"))
                return
        }

        event.isCancelled = true
        event.damager.remove() // 제거하지 않으면 투사체가 튕겨 나가요.
    }

    // Potion 

    @EventHandler
    @TargetEntity(EntityRegionProvider.PotionSplash.PotionThrrowByEntity::class)
    fun onPotionThrrowByEntity(event: PotionSplashEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val potion = event.potion
        if (potion.item.type == Material.LINGERING_POTION)  {
            if (isConfigValid("LingeringPotionSplash"))
                return
        }
        else if (isConfigValid("PotionThrrowByEntity")) {
            val shooter = event.entity

            if (isChunkValid(shooter.chunk, potion.chunk)) {
                if (isConfigValid("PotionSplash"))
                    return

                val entities = event.affectedEntities.toList()
                for (entity in entities) {
                    if (isChunkValid(potion.chunk, entity.chunk))
                        continue

                    event.setIntensity(entity, 0.0)
                }
            }
        }

        event.isCancelled = true
    }

    @EventHandler
    @TargetEntity(EntityRegionProvider.PotionSplash.PotionThrrowByPlayer::class)
    fun onPotionThrrowByPlayer(event: PotionSplashEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        val shooter = event.entity as Player
        if (isRegionValid(shooter))
            if (isConfigValid("PotionThrrowByPlayer")) {
                val potion = event.potion

                if (isChunkValid(shooter.chunk, potion.chunk)) {
                    if (isConfigValid("PotionSplash"))
                        return

                    val entities = event.affectedEntities.toList()
                    for (entity in entities) {
                        if (isChunkValid(potion.chunk, entity.chunk))
                            continue

                        event.setIntensity(entity, 0.0)
                    }
                }
            }

        event.isCancelled = true
    }

    @EventHandler
    fun onLingeringPotionSplash(event: LingeringPotionSplashEvent) {
        val world = event.entity.world
        if (!isWorldValid(world))
            return

        if (isConfigValid("LingeringPotionThrrow")) {
            if (isConfigValid("LingeringPotionSplash"))
                return

            val potion = event.entity

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
                    val entities = cloud.world.getNearbyEntities(location, radius, radius, radius)
                    for (entity in entities) {
                        if (entity !is LivingEntity)
                            continue

                        if (potion.chunk == entity.chunk)
                            continue

                        if (effectType != null) {
                            val isStrong = baseType.let { basePotionType ->
                                val hasEffect = entity.getPotionEffect(effectType) ?: return@let true

                                if (base.isUpgraded && hasEffect.amplifier < 1)
                                    return@let true

                                val baseDuration = if (base.isExtended) 480 else 180
                                return@let hasEffect.duration > baseDuration * 20
                            }

                            if (isStrong != true)
                                entity.removePotionEffect(effectType)
                        }

                        for (effect in effects) {
                            val hasEffect = entity.getPotionEffect(effect.type)
                            if (hasEffect != null)
                                if (hasEffect.amplifier > effect.amplifier || (hasEffect.amplifier == effect.amplifier && hasEffect.duration > effect.duration))
                                    continue

                            entity.removePotionEffect(effect.type)
                        }
                    }
                }
            }, wait, 1L)

            Bukkit.getServer().scheduler.runTaskLater(plugin, object : Runnable {
                override fun run() {
                    Bukkit.getServer().scheduler.cancelTask(task.taskId)
                }
            }, wait + duration)

            return
        }

        event.isCancelled = true
    }
}

/**
 * 구역을 적용할 세계인 지 확인합니다.
 *
 * @param world 확일할 세계
 */
internal fun isWorldValid(world: World): Boolean {
    return world.name == RegionManager.getWorldName()
}

/**
 * 청크 두 개의 (x) 좌표와 (z) 좌표가 동일한 지 비교합니다.
 *
 * @param chunk 원본이 되는 청크
 * @param target 비교할 청크
 */
internal fun isChunkValid(chunk: Chunk, target: Chunk): Boolean {
    return chunk.x == target.x && chunk.z == target.z
}

/**
 * 구역 설정값이 참인 지 확인합니다.
 *
 * @param key 확인할 설정 이름
 */
internal fun isConfigValid(key: String): Boolean {
    return RegionManager.Companion.getRegionConfigValue(key) == true
}

/**
 * 해당 플레이어가 구역을 가지고 있는 지 확인합니다.
 *
 * @param player 확인할 플레이어
 */
internal fun isRegionValid(player: Player): Boolean {
    return RegionManager.Companion.hasRegion(player)
}