package dev.Error110.combatLogger.listeners

import dev.Error110.combatLogger.CombatLogger
import dev.Error110.combatLogger.CombatManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.AbstractHorse
import org.bukkit.entity.Boat
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerTeleportEvent

class BannedTransportListener : Listener {

    @EventHandler
    fun ridingVehicle(event: EntityMountEvent) {
        val player = event.entity as? Player ?: return
        if (!CombatManager().isTagged(player)) return

        val cfg = CombatLogger.config!!.transport
        when (event.mount) {
            is Boat -> if (cfg.banBoats) {
                player.sendMessage("${ChatColor.RED}You cannot use a boat while in combat!")
                event.isCancelled = true
            }

            is AbstractHorse -> if (cfg.banHorses) {
                player.sendMessage("${ChatColor.RED}You cannot use a horse while in combat!")
                event.isCancelled = true
            }

            is Minecart -> if (cfg.banMinecarts) {
                player.sendMessage("${ChatColor.RED}You cannot use a minecart while in combat!")
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun usingElytra(event: EntityToggleGlideEvent) {
        if (CombatLogger.config!!.transport.banElytra) {
            if (event.entity is Player) {
                val player = event.entity as Player
                if (event.isGliding) {
                    if (!CombatManager().isTagged(player)) return
                    player.sendMessage("${ChatColor.RED}You cannot use an elytra while in combat!")
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun blockTridentUse(event: PlayerInteractEvent) {
        if (!CombatLogger.config!!.transport.banTridents) return
        val player = event.player
        if (!CombatManager().isTagged(player)) return
        val item = event.item ?: return
        if (item.type != Material.TRIDENT) return
        if (!item.containsEnchantment(Enchantment.RIPTIDE)) return

        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            player.sendMessage("${ChatColor.RED}You cannot use a trident while in combat!")
            event.isCancelled = true
        }
    }

    @EventHandler
    fun usingPearl(event: PlayerTeleportEvent) {
        if (!CombatManager().isTagged(event.player)) return
        if (CombatLogger.config!!.transport.banPearls && event.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.player.sendMessage("${ChatColor.RED}You cannot use an ender pearl while in combat!")
            event.isCancelled = true
        }
    }

    @EventHandler
    fun usingChorus(event: PlayerItemConsumeEvent) {
        if (!CombatManager().isTagged(event.player)) return
        if (event.item.type == Material.CHORUS_FRUIT && CombatLogger.config!!.transport.banChorus) {
            event.player.sendMessage("You cannot use chorus fruit while in combat!")
            event.isCancelled = true
        }
    }

}