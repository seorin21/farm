package io.github.seorin21.farm.region.internal.event

import io.github.monun.tap.event.EntityProvider
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityInteractEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.LingeringPotionSplashEvent
import org.bukkit.event.entity.PotionSplashEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.projectiles.BlockProjectileSource

@Suppress("unused")
fun interface EntityRegionProvider<T : Event>: EntityProvider<T> {
    class EntityDamageByEntity {
        class PlayerDamageByEntity: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager is Player || damager is Projectile)
                    return null

                val entity = event.entity
                if (entity !is Player)
                    return null

                return entity
            }
        }

        class PlayerDamageByPlayer: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager !is Player || damager is Projectile)
                    return null

                val entity = event.entity
                if (entity !is Player)
                    return null

                return entity
            }
        }


        class EntityDamageByEntity: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager is Player || damager is Projectile)
                    return null

                val entity = event.entity
                if (entity is Player)
                    return null

                return entity
            }
        }

        class EntityDamageByPlayer: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager !is Player || damager is Projectile)
                    return null

                val entity = event.entity
                if (entity is Player)
                    return null

                return entity
            }
        }


        class PlayerProjectileDamageByEntity: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager !is Projectile)
                    return null

                val shooter = damager.shooter
                if (shooter is Player || shooter !is Entity)
                    return null

                val entity = event.entity
                if (entity !is Player)
                    return null

                return entity
            }
        }

        class PlayerProjectileDamageByBlock: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager !is Projectile)
                    return null

                if (damager.shooter !is BlockProjectileSource)
                    return null

                val entity = event.entity
                if (entity !is Player)
                    return null

                return entity
            }
        }

        class PlayerProjectileDamageByPlayer: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager !is Projectile)
                    return null

                if (damager.shooter !is Player)
                    return null

                val entity = event.entity
                if (entity !is Player)
                    return null

                return entity
            }
        }


        class EntityProjectileDamageByEntity: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager !is Projectile)
                    return null

                val shooter = damager.shooter
                if (shooter is Player || shooter !is Entity)
                    return null

                val entity = event.entity
                if (entity is Player)
                    return null

                return entity
            }
        }

        class EntityProjectileDamageByBlock: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager !is Projectile)
                    return null

                if (damager.shooter !is BlockProjectileSource)
                    return null

                val entity = event.entity
                if (entity is Player)
                    return null

                return entity
            }
        }

        class EntityProjectileDamageByPlayer: EntityProvider<EntityDamageByEntityEvent> {
            override fun getFrom(event: EntityDamageByEntityEvent): Entity? {
                val damager = event.damager
                if (damager !is Projectile)
                    return null

                if (damager.shooter !is Player)
                    return null

                val entity = event.entity
                if (entity is Player)
                    return null

                return entity
            }
        }
    }

    class EntityDamageByBlock {
        class EntityDamageByBlock: EntityProvider<EntityDamageByBlockEvent> {
            override fun getFrom(event: EntityDamageByBlockEvent): Entity? {
                val entity = event.entity
                if (entity is Player)
                    return null

                return entity
            }
        }

        class PlayerDamageByBlock: EntityProvider<EntityDamageByBlockEvent> {
            override fun getFrom(event: EntityDamageByBlockEvent): Entity? {
                val entity = event.entity
                if (entity !is Player)
                    return null

                return entity
            }
        }
    }

    class EntityInteract {
        class BlockInteractionByEntity: EntityProvider<EntityInteractEvent> {
            override fun getFrom(event: EntityInteractEvent): Entity? {
                val entity = event.entity
                if (entity is Player)
                    return null

                return entity
            }
        }
    }

    class PotionSplash {
        class PotionThrrowByEntity: EntityProvider<PotionSplashEvent> {
            override fun getFrom(event: PotionSplashEvent): Entity? {
                val entity = event.entity
                if (entity is Player)
                    return null

                return entity
            }
        }

        class PotionThrrowByPlayer: EntityProvider<PotionSplashEvent> {
            override fun getFrom(event: PotionSplashEvent): Entity? {
                val entity = event.entity
                if (entity !is Player)
                    return null

                return entity
            }
        }
    }

    class EntityTarget {
        class EntityTargetByEntity: EntityProvider<EntityTargetEvent> {
            override fun getFrom(event: EntityTargetEvent): Entity? {
                val target = event.target ?: return null
                if (target is Player)
                    return null

                return target
            }
        }

        class PlayerTargetByEntity: EntityProvider<EntityTargetEvent> {
            override fun getFrom(event: EntityTargetEvent): Entity? {
                val target = event.target ?: return null
                if (target !is Player)
                    return null

                return target
            }
        }
    }
}