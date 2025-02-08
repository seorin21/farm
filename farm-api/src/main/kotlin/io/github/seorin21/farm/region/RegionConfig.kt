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

    @Config(value = "PlayerMove", required = true)
    var PlayerMove: Boolean = false

    @Config(value = "PlayerTeleport", required = true)
    var PlayerTeleport: Boolean = false

    @Config(value = "PlayerBedLeaveForChunk", required = true)
    var PlayerBedLeaveForChunk: Boolean = false

    @Config(value = "PlayerTargetedByEntity", required = true)
    var PlayerTargetedByEntity: Boolean = false

    // Entity 
    @Config(value = "EntityDamageByEntity", required = true)
    var EntityDamageByEntity: Boolean = false

    @Config(value = "EntityDamageByPlayer", required = true)
    var EntityDamageByPlayer: Boolean = false

    @Config(value = "EntityDamageByBlock", required = true)
    var EntityDamageByBlock: Boolean = false

    @Config(value = "EntityTeleport", required = true)
    var EntityTeleport: Boolean = false

    @Config(value = "EntityExplode", required = true)
    var EntityExplod: Boolean = false

    @Config(value = "EntityInteractionByPlayer", required = true)
    var EntityInteractionByPlayer: Boolean = false

    @Config(value = "EntityTargetedByEntity", required = true)
    var EntityTargetedByEntity: Boolean = false

    // Block 
    @Config(value = "BlockPlacedByPlayer", required = true)
    var BlockPlacedByPlayer: Boolean = false

    @Config(value = "BlockBreakedByPlayer", required = true)
    var BlockBreakedByPlayer: Boolean = false

    @Config(value = "BlockExplode", required = true)
    var BlockExplode: Boolean = false

    @Config(value = "BlockExplodedByEntity", required = true)
    var BlockExplodedByEntity: Boolean = false

    @Config(value = "BlockExplodedByBlock", required = true)
    var BlockExplodedByBlock: Boolean = false

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

    @Config(value = "LiquidPlacedByPlayer", required = true)
    var LiquidPlacedByPlayer: Boolean = false

    // Wood, Floor

    @Config(value = "StructureGrow", required = true)
    var StructureGrow: Boolean = false

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
    @Config(value = "PotionThrrowedByEntity", required = true)
    var PotionThrrowedByEntity: Boolean = false

    @Config(value = "PotionThrrowedByPlayer", required = true)
    var PotionThrrowedByPlayer: Boolean = false

    @Config(value = "LingeringPotionThrrowed", required = true)
    var LingeringPotionSplash: Boolean = false

    @Config(value = "PotionSplashed", required = true)
    var EntityPotionSplashed: Boolean = false

    @Config(value = "LingeringPotionSplashed", required = true)
    var EntityLingeringPotionSplashed: Boolean = false

    @Config(value = "InventoryMoveItem", required = true)
    var InventoryMoveItem: Boolean = false
}