package io.github.seorin21.farm.region

import io.github.monun.tap.config.Config

/**
 * 모든 이벤트는 서로 다른 청크간의 상호작용만을 막습니다.
 */
@Suppress("unused")
open class RegionConfig {
    @Config(value = "WorldName", required = false)
    var world: String = "world"


    // Block
    // BlockDispense
    @Config(value = "BlockDispensation", required = true)
    var BlockDispensation: Boolean = false

    // BlockFromTo
    /**
     * 블럭이 흐르는(예를 들면 물, 용암) 이벤트입니다.
     */
    @Config(value = "BlockInteractionFromTo", required = true)
    var BlockInteractionFromTo: Boolean = false

    // BlockSpread
    /**
     * 블럭이 번지는(예를 들면 불) 이벤트입니다.
     */
    @Config(value = "BlockSpread", required = true)
    var BlockSpread: Boolean = false

    // BlockExplode
    /**
     * 엔티티의 폭발 이벤트입니다.
     *
     * > 이 이벤트는 예외적으로, 실행 자체를 관리합니다.
     */
    @Config(value = "BlockExplosion", required = true)
    var BlockExplosion: Boolean = false

    // BlockExplode
    /**
     * 블럭이 다른 블럭의 폭발에 의해 부숴지는 이벤트입니다.
     */
    @Config(value = "BlockExplosionByBlock", required = true)
    var BlockExplosionByBlock: Boolean = false

    // PlayerInteract
    /**
     * 플레이어가 블럭과 상호작용하는 이벤트입니다.
     *
     * 문, 다락문의 이벤트는 막혀있지 않습니다.
     */
    @Config(value = "BlockInteractionByPlayer", required = true)
    var BlockInteractionByPlayer: Boolean = false

    // BlockPistonExtend/Retract
    /**
     * 피스톤이 블럭을 밀고 당기는 이벤트입니다.
     */
    @Config(value = "BlockMovementByPiston", required = true)
    var BlockMovementByPiston: Boolean = false

    // BlockPlace
    @Config(value = "BlockPlacementByPlayer", required = true)
    var BlockPlacementByPlayer: Boolean = false

    // BlockBreak
    @Config(value = "BlockBreakByPlayer", required = true)
    var BlockBreakByPlayer: Boolean = false


    // Player
    // PlayerBedEntry
    /**
     * 플레이어가 침대에 잠드는 이벤트입니다.
     *
     * 플레이어의 스폰 포인트는 막지 않습니다. (침대 사용을 주의하세요)
     *
     * > 이 이벤트는 예외적으로, 실행 자체를 관리합니다.
     */
    @Config(value = "PlayerBedEntry", required = true)
    var PlayerBedEntry: Boolean = false

    // PlayerMove
    @Config(value = "PlayerMovement", required = true)
    var PlayerMovement: Boolean = false

    // PlayerTeleport
    @Config(value = "PlayerTeleport", required = true)
    var PlayerTeleport: Boolean = false

    // EntityTeleport
    @Config(value = "EntityTeleport", required = true)
    var EntityTeleport: Boolean = false

    // EntityExplode
    /**
     * 엔티티의 폭발 이벤트입니다.
     *
     * > 이 이벤트는 예외적으로, 실행 자체를 관리합니다.
     */
    @Config(value = "EntityExplosion", required = true)
    var EntityExplosion: Boolean = false

    // EntityExplode
    @Config(value = "BlockExplosionByEntity", required = true)
    var BlockExplosionByEntity: Boolean = false

    // EntityExplode
    /**
     * 엔티티의 폭발 이벤트입니다.
     *
     * > 이 이벤트는 예외적으로, 실행 자체를 관리합니다.
     */
    @Config(value = "TNTExplosion", required = true)
    var TNTExplosion: Boolean = false

    // EntityExplode
    @Config(value = "BlockExplosionByTNT", required = true)
    var TNTExplosionByEntity: Boolean = false

    // EntityInteractionByPlayer
    @Config(value = "EntityInteractionByPlayer", required = true)
    var EntityInteractionByPlayer: Boolean = false

    // EntityDamageByEntity
    @Config(value = "PlayerDamageByBlock", required = true)
    var PlayerDamageByBlock: Boolean = false

    // EntityDamageByEntity
    @Config(value = "PlayerDamageByPlayer", required = true)
    var PlayerDamageByPlayer: Boolean = false

    // EntityDamageByEntity
    @Config(value = "PlayerDamageByEntity", required = true)
    var PlayerDamageByEntity: Boolean = false

    // EntityDamageByEntity
    @Config(value = "EntityDamageByBlock", required = true)
    var EntityDamageByBlock: Boolean = false

    // EntityDamageByEntity
    @Config(value = "EntityDamageByPlayer", required = true)
    var EntityDamageByPlayer: Boolean = false

    // EntityDamageByEntity
    @Config(value = "EntityDamageByEntity", required = true)
    var EntityDamageByEntity: Boolean = false

    // EntityDamageByEntity
    @Config(value = "PlayerProjectileDamageByBlock", required = true)
    var PlayerProjectileDamageByBlock: Boolean = false

    // EntityDamageByEntity
    @Config(value = "PlayerProjectileDamageByPlayer", required = true)
    var PlayerProjectileDamageByPlayer: Boolean = false

    // EntityDamageByEntity
    @Config(value = "PlayerProjectileDamageByEntity", required = true)
    var PlayerProjectileDamageByEntity: Boolean = false

    // EntityDamageByEntity
    @Config(value = "EntityProjectileDamageByBlock", required = true)
    var EntityProjectileDamageByBlock: Boolean = false

    // EntityDamageByEntity
    @Config(value = "EntityProjectileDamageByPlayer", required = true)
    var EntityProjectileDamageByPlayer: Boolean = false

    // EntityDamageByEntity
    @Config(value = "EntityProjectileDamageByEntity", required = true)
    var EntityProjectileDamageByEntity: Boolean = false

    // EntityTargetLivingEntity
    @Config(value = "PlayerTargetByEntity", required = true)
    var PlayerTargetByEntity: Boolean = false

    // EntityTargetLivingEntity
    @Config(value = "EntityTargetByEntity", required = true)
    var EntityTargetByEntity: Boolean = false


    // Potion
    // PotionSplash
    @Config(value = "PotionThrrowByBlock", required = true)
    var PotionThrrowByBlock: Boolean = false

    // PotionSplash
    @Config(value = "PotionThrrowByPlayer", required = true)
    var PotionThrrowByPlayer: Boolean = false

    // PotionSplash
    @Config(value = "PotionThrrowByEntity", required = true)
    var PotionThrrowByEntity: Boolean = false

    // PotionSplash
    @Config(value = "LingeringPotionThrrowByBlock", required = true)
    var LingeringPotionThrrowByBlock: Boolean = false

    // PotionSplash
    @Config(value = "LingeringPotionThrrowByPlayer", required = true)
    var LingeringPotionThrrowByPlayer: Boolean = false

    // LingeringPotionSplash
    @Config(value = "LingeringPotionThrrowByEntity", required = true)
    var LingeringPotionThrrowByEntity: Boolean = false

    // PotionSplash
    /**
     * 물약이 퍼지는 이벤트입니다.
     *
     * 다른 청크에 있는 엔티티에겐 효과가 적용되지 않습니다.
     */
    @Config(value = "PotionSplash", required = true)
    var PotionSplash: Boolean = false

    // LingeringPotionSplash
    /**
     * 잔류형 물약이 퍼지는 이벤트입니다.
     */
    @Config(value = "LingeringPotionSplash", required = true)
    var LingeringPotionSplash: Boolean = false


    // Other
    // InventoryMoveItem
    /**
     * 아이템이 블럭 인벤 안에서 움직이는 이벤트입니다.
     *
     * 호퍼 안에 있는 아이템이 서로다른 청크로 이동하는 것을 감사합니다.
     */
    @Config(value = "ItemMovementFromTo", required = true)
    var ItemMovementInInventory: Boolean = false
}