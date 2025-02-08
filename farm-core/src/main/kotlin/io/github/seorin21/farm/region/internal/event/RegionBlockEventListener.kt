package io.github.seorin21.farm.region.internal.event

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.block.BlockPistonExtendEvent
import org.bukkit.event.block.BlockPistonRetractEvent
import org.bukkit.event.block.BlockSpreadEvent
import org.bukkit.event.block.SpongeAbsorbEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.world.StructureGrowEvent

class RegionBlockEventListener: Listener {
    // Block

    @EventHandler
    fun onBlockExplosion(event: BlockExplodeEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        if (isConfigValid("BlockExplosion")) {
            if (isConfigValid("BlockExplosionByBlock"))
                return

            val chunk = event.block.chunk
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
    fun onBlockSpread(event: BlockSpreadEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        val source = event.source
        val block = event.block

        if (isChunkValid(source.chunk, block.chunk))
            return

        if (isConfigValid("BlockSpread"))
            return

        event.isCancelled = true
    }

    internal fun isRail(type: Material): Boolean {
        return when (type) {
            Material.RAIL,
            Material.ACTIVATOR_RAIL,
            Material.POWERED_RAIL,
            Material.DETECTOR_RAIL,
            Material.LEGACY_RAILS,
            Material.LEGACY_ACTIVATOR_RAIL,
            Material.LEGACY_POWERED_RAIL,
            Material.LEGACY_DETECTOR_RAIL -> true
            else -> type.name.contains("RAIL", ignoreCase = true)
        }
    }

    internal fun isRedstone(type: Material): Boolean {
        return when (type) {
            Material.REDSTONE_WIRE,
            Material.REDSTONE_TORCH,
            Material.REDSTONE_WALL_TORCH,
            Material.REDSTONE_BLOCK,
            Material.REPEATER,
            Material.COMPARATOR,
            Material.PISTON,
            Material.STICKY_PISTON,
            Material.PISTON_HEAD,
            Material.MOVING_PISTON,
            Material.STONE_BUTTON,
            Material.OAK_BUTTON,
            Material.SPRUCE_BUTTON,
            Material.BIRCH_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.ACACIA_BUTTON,
            Material.DARK_OAK_BUTTON,
            Material.CRIMSON_BUTTON,
            Material.WARPED_BUTTON,
            Material.POLISHED_BLACKSTONE_BUTTON,
            Material.STONE_PRESSURE_PLATE,
            Material.OAK_PRESSURE_PLATE,
            Material.SPRUCE_PRESSURE_PLATE,
            Material.BIRCH_PRESSURE_PLATE,
            Material.JUNGLE_PRESSURE_PLATE,
            Material.ACACIA_PRESSURE_PLATE,
            Material.DARK_OAK_PRESSURE_PLATE,
            Material.CRIMSON_PRESSURE_PLATE,
            Material.WARPED_PRESSURE_PLATE,
            Material.POLISHED_BLACKSTONE_PRESSURE_PLATE,
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Material.LEVER,
            Material.DAYLIGHT_DETECTOR,
            Material.OBSERVER,
            Material.TRIPWIRE_HOOK,
            Material.TRAPPED_CHEST,
            Material.DISPENSER,
            Material.DROPPER,
            Material.HOPPER,
            Material.TARGET,
            Material.SCULK_SENSOR,
            Material.NOTE_BLOCK,
            Material.TNT -> true
            else -> type.name.contains("REDSTONE", ignoreCase = true)
        }
    }

    internal fun isPiston(type: Material): Boolean {
        return when (type) {
            Material.PISTON,
            Material.PISTON_HEAD,
            Material.MOVING_PISTON,
            Material.STICKY_PISTON,
            Material.LEGACY_PISTON_BASE,
            Material.LEGACY_PISTON_EXTENSION,
            Material.LEGACY_PISTON_MOVING_PIECE,
            Material.LEGACY_PISTON_STICKY_BASE -> true
            else -> type.name.contains("PISTON", ignoreCase = true)
        }
    }

    internal fun isSponge(type: Material): Boolean {
        return when (type) {
            Material.SPONGE,
            Material.WET_SPONGE,
            Material.LEGACY_SPONGE -> true
            else -> type.name.contains("SPONGE", ignoreCase = true)
        }
    }

    @EventHandler
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        val source = event.sourceBlock
        val block = event.block

        if (isChunkValid(source.chunk, block.chunk))
            return

        val type = block.type
        if (isRail(type) || isRedstone(type) || isPiston(source.type))
            return

        if (isConfigValid("BlockPhysics"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockPhysicsByRail(event: BlockPhysicsEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        val source = event.sourceBlock
        val block = event.block

        if (isChunkValid(source.chunk, block.chunk))
            return

        if (!isRail(block.type))
            return

        if (isConfigValid("BlockPhysicsByRail"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockPhysicsByRedstone(event: BlockPhysicsEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        val source = event.sourceBlock
        val block = event.block

        if (isChunkValid(source.chunk, block.chunk))
            return

        if (!isRedstone(block.type))
            return

        if (isConfigValid("BlockPhysicsByRedstone"))
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockPhysicsBySponge(event: BlockPhysicsEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        val source = event.sourceBlock
        val block = event.block

        if (isChunkValid(source.chunk, block.chunk))
            return

        if (!isSponge(source.type))
            return

        if (isConfigValid("BlockPhysicsBySponge"))
            return

        event.isCancelled = true
    }

    // Piston

    @EventHandler
    fun onBlockPhysicsByPiston(event: BlockPistonExtendEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        if (isConfigValid("BlockPhysicsByPiston"))
            return

        val piston = event.block
        if (!event.blocks.any { !isChunkValid(piston.chunk, it.chunk) })
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockPhysicsByPiston(event: BlockPistonRetractEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        if (isConfigValid("BlockPhysicsByPiston"))
            return

        val piston = event.block
        if (!event.blocks.any { !isChunkValid(piston.chunk, it.chunk) })
            return

        event.isCancelled = true
    }

    // Sponge
    @EventHandler
    fun onSpongeAbsorb(event: SpongeAbsorbEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        if (isConfigValid("SpongeAbsorb"))
            return

        val sponge = event.block
        val blocks = mutableListOf<BlockState>()

        for (block in event.blocks) {
            if (isChunkValid(sponge.chunk, block.chunk))
                continue

            blocks.add(block)
        }

        event.blocks.removeAll(blocks)
    }

    // Wood, Floor

    @EventHandler
    fun onStructureGrowth(event: StructureGrowEvent) {
        val world = event.world
        if (!isWorldValid(world))
            return

        if (isConfigValid("StructureGrowth"))
            return

        val chunk = event.location.chunk
        val blocks = mutableListOf<BlockState>()

        for (block in event.blocks) {
            if (isChunkValid(chunk, block.chunk))
                continue

            blocks.add(block)
        }

        event.blocks.removeAll(blocks)
    }

    // Liquid

    @EventHandler
    fun onLiquidFromToChunk(event: BlockFromToEvent) {
        val world = event.block.world
        if (!isWorldValid(world))
            return

        val from = event.block
        val to = event.toBlock

        if (isChunkValid(from.chunk, to.chunk))
            return

        if (isConfigValid("LiquidFromToChunk"))
            return

        event.isCancelled = true
    }

    // Etc

    @EventHandler
    fun onItemMovementInInventory(event: InventoryMoveItemEvent) {
        val from = event.initiator.location ?: return
        val to = event.destination.location ?: return

        val world = from.world
        if (!isWorldValid(world))
            return

        if (isConfigValid("ItemMovementInInventory"))
            return

        if (isChunkValid(from.chunk, to.chunk))
            return

        event.isCancelled = true
    }
}