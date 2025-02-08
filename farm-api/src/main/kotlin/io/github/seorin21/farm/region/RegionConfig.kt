package io.github.seorin21.farm.region

import io.github.monun.tap.config.Config

@Suppress("unused")
open class RegionConfig {
    @Config(value = "WorldName", required = false)
    var world: String = "world"

    // Player 
    @Config(value = "PlayerDamageByEntity", required = true)
    var PlayerDamageByEntity: Boolean = false

    @Config(value = "PlayerDamageByBlock", required = true)
    var PlayerDamageByBlock: Boolean = false

    @Config(value = "PlayerDamageByPlayer", required = true)
    var PlayerDamageByPlayer: Boolean = false

    @Config(value = "PlayerMovement", required = true)
    var PlayerMovement: Boolean = false

    @Config(value = "PlayerTeleport", required = true)
    var PlayerTeleport: Boolean = false

    @Config(value = "PlayerTeleportByBed", required = true)
    var PlayerTeleportByBed: Boolean = false

    @Config(value = "PlayerTargetByEntity", required = true)
    var PlayerTargetByEntity: Boolean = false

    // Entity 
    @Config(value = "EntityDamageByEntity", required = true)
    var EntityDamageByEntity: Boolean = false

    @Config(value = "EntityDamageByPlayer", required = true)
    var EntityDamageByPlayer: Boolean = false

    @Config(value = "EntityDamageByBlock", required = true)
    var EntityDamageByBlock: Boolean = false

    @Config(value = "EntityTeleport", required = true)
    var EntityTeleport: Boolean = false

    @Config(value = "EntityExplosion", required = true)
    var EntityExplosion: Boolean = false

    @Config(value = "EntityInteractionByPlayer", required = true)
    var EntityInteractionByPlayer: Boolean = false

    @Config(value = "EntityTargetByEntity", required = true)
    var EntityTargetByEntity: Boolean = false

    // Block 
    @Config(value = "BlockPlacementByPlayer", required = true)
    var BlockPlacementByPlayer: Boolean = false

    @Config(value = "BlockBreakByPlayer", required = true)
    var BlockBreakByPlayer: Boolean = false

    @Config(value = "BlockExplosion", required = true)
    var BlockExplosion: Boolean = false

    @Config(value = "BlockExplosionByEntity", required = true)
    var BlockExplosionByEntity: Boolean = false

    @Config(value = "BlockExplosionByBlock", required = true)
    var BlockExplosionByBlock: Boolean = false

    @Config(value = "BlockInteractionByPlayer", required = true)
    var BlockInteractionByPlayer: Boolean = false

    @Config(value = "BlockSpread", required = true)
    var BlockSpread: Boolean = false

    @Config(value = "BlockPhysics", required = true)
    var BlockPhysics: Boolean = false

    @Config(value = "BlockPhysicsByRail", required = true)
    var BlockPhysicsByRail: Boolean = false

    @Config(value = "BlockPhysicsByRedstone", required = true)
    var BlockPhysicsByRedstone: Boolean = false

//    @Config(value = "BlockExtendedByPiston", required = true)
//    var BlockExtendedByPiston: Boolean = false
//
//    @Config(value = "BlockRetractedByPiston", required = true)
//    var BlockRetractedByPiston: Boolean = false

    @Config(value = "BlockPhysicsByPiston", required = true)
    var BlockPhysicsByPiston: Boolean = false

    @Config(value = "BlockPhysicsBySponge", required = true)
    var BlockPhysicsBySponge: Boolean = false

    // Liquid

    @Config(value = "LiquidFromToChunk", required = true)
    var LiquidFromToChunk: Boolean = false

    @Config(value = "LiquidPlacementByPlayer", required = true)
    var LiquidPlacementByPlayer: Boolean = false

    // Wood, Floor

    @Config(value = "StructureGrowth", required = true)
    var StructureGrowth: Boolean = false

    // Projectile
    
    @Config(value = "PlayerProjectileDamageByEntity", required = true)
    var PlayerProjectileDamageByEntity: Boolean = false

    @Config(value = "PlayerProjectileDamageByBlock", required = true)
    var PlayerProjectileDamageByBlock: Boolean = false

    @Config(value = "PlayerProjectileDamageByPlayer", required = true)
    var PlayerProjectileDamageByPlayer: Boolean = false

    @Config(value = "EntityProjectileDamageByEntity", required = true)
    var EntityProjectileDamageByEntity: Boolean = false

    @Config(value = "EntityProjectileDamageByBlock", required = true)
    var EntityProjectileDamageByBlock: Boolean = false

    @Config(value = "EntityProjectileDamageByPlayer", required = true)
    var EntityProjectileDamageByPlayer: Boolean = false

    // Potion 
    @Config(value = "PotionThrrowByEntity", required = true)
    var PotionThrrowByEntity: Boolean = false

    @Config(value = "PotionThrrowByPlayer", required = true)
    var PotionThrrowByPlayer: Boolean = false

    @Config(value = "LingeringPotionThrrow", required = true)
    var LingeringPotionSplash: Boolean = false

    @Config(value = "PotionSplash", required = true)
    var EntityPotionSplash: Boolean = false

    @Config(value = "LingeringPotionSplash", required = true)
    var EntityLingeringPotionSplash: Boolean = false

    @Config(value = "ItemMovementInInventory", required = true)
    var ItemMovementInInventory: Boolean = false
}