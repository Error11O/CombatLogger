package dev.Error110.combatLogger.listeners

import dev.Error110.combatLogger.CombatLogger
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class BannedItemsListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onJoin(e: PlayerJoinEvent) {
        purgeLater(e.player)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onClick(e: InventoryClickEvent) {
        purgeLater(e.whoClicked as Player)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDrag(e: InventoryDragEvent) {
        purgeLater(e.whoClicked as Player)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onHeld(e: PlayerItemHeldEvent) {
        purgeLater(e.player)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onSwap(e: PlayerSwapHandItemsEvent) {
        purgeLater(e.player)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPickup(e: PlayerAttemptPickupItemEvent) {
        purgeLater(e.player)
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCraft(e: CraftItemEvent) {
        purgeLater(e.whoClicked as Player)
    }

    fun purgeLater(player: Player) {
        if (CombatLogger.config!!.restrictions.banned_items.enabled) {
            Bukkit.getScheduler().runTask(CombatLogger.plugin!!, Runnable {
                purge(player)
            })
        }
    }

    fun purge(player: Player) {
        purge(player.inventory)
    }

    private fun purge(inv: PlayerInventory) {
        if (CombatLogger.config!!.restrictions.banned_items.items.isEmpty()) return
        val banned = CombatLogger.config!!.restrictions.banned_items.items.mapNotNull { Material.getMaterial(it) }.toSet()
        // main storage
        val storage = inv.storageContents
        for (i in storage.indices) {
            val it = storage[i] ?: continue
            if (it.type in banned) inv.setItem(i, null)
        }

        // armor
        val armor = inv.armorContents
        var armorChanged = false
        for (i in armor.indices) {
            val it = armor[i] ?: continue
            if (it.type in banned) {
                armor[i] = null
                armorChanged = true
            }
        }
        if (armorChanged) inv.armorContents = armor

        // offhand
        if (inv.itemInOffHand.type in banned) {
            inv.setItemInOffHand(ItemStack(Material.AIR))
        }

        // cursor
        val p = inv.holder
        if (p is Player) {
            val cursor = p.itemOnCursor
            if (cursor != null && cursor.type in banned) {
                p.setItemOnCursor(null)
            }
        }
    }
}