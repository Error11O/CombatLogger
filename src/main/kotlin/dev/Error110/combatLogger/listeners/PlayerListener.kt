package dev.Error110.combatLogger.listeners

import dev.Error110.combatLogger.CombatLogger
import dev.Error110.combatLogger.CombatManager
import io.papermc.paper.event.player.PlayerItemCooldownEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*


class PlayerListener : Listener {
    val deathsForLoggingOut: MutableSet<UUID> = HashSet()

    // apply combat tag on player vs player damage
    @EventHandler(ignoreCancelled = true)
    fun onCombat(event: EntityDamageByEntityEvent) {
        if (event.damager is Player && event.entity is Player) {
            val attacker = event.damager as Player
            val victim = event.entity as Player
            if (attacker.equals(victim)) return
            if (event.finalDamage <= 0.0) return
            CombatManager.applyTag(attacker)
            CombatManager.applyTag(victim)
        }
    }
    // kill player if they log out while in combat
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        if (!CombatManager.isTagged(player)) return
        CombatManager.removeTag(player);

        deathsForLoggingOut.add(player.uniqueId);
        player.setHealth(0.0);
    }
    // Remove combat tag on death TODO: remove the killers combat tag too?
    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity

        if (deathsForLoggingOut.contains(player.getUniqueId())) {
            deathsForLoggingOut.remove(player.getUniqueId());
            event.deathMessage = "${player.name} Logged out in combat."
        }
        if (!CombatManager.isTagged(player)) return;
        CombatManager.removeTag(player);
    }

    // readd combat tag on ender pearl throw if configured to do so
    @EventHandler
    fun onThrowEvent(event : PlayerItemCooldownEvent) {
        if (event.type != Material.ENDER_PEARL) return
        if (CombatLogger.config!!.combat.reTagOnPearl) {
            val shooter = event.player
            if (CombatManager.isTagged(shooter)) {
                CombatManager.applyTag(shooter)
            }
        }
    }

    // prevent commands while in combat if not whitelisted
    // ops are exempt from this restriction
    @EventHandler
    fun onCommandInCombat(event: PlayerCommandPreprocessEvent) {
        if (!CombatLogger.config!!.commands.whitelist.enabled) return
        if (event.player.isOp) return
        val command = event.message.replace("/", "")
        if (CombatManager.isTagged(event.player)) {
            for (white in CombatLogger.config!!.commands.whitelist.list ) if (command.startsWith(white)) return else continue
            event.isCancelled = true
        }
    }

    // increase damage dealt while in combat if configured to do so (default is 1.0, so no increase)
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.getDamager() !is Player) return
        var damage = event.getDamage()
        damage *= CombatLogger.config!!.combat.damageMultiplier
        event.setDamage(damage)
    }

    @EventHandler
    fun onItemInteract(event: InventoryOpenEvent) {
        val uuid = event.player.uniqueId
        val player = Bukkit.getPlayer(uuid!!)!!
        val inventory = event.inventory
        if (!CombatManager.isTagged(player)) return

        val storage = CombatLogger.config?.storage ?: return
        val type = inventory.type

        // Determine if the inventory type is one we want to prevent
        val (prevent, label) = when (type) {
            InventoryType.CHEST -> storage.preventChests to "chests"
            InventoryType.ENDER_CHEST -> storage.preventEnderChests to "ender chests"
            InventoryType.BARREL -> storage.preventBarrels to "barrels"
            InventoryType.SHULKER_BOX -> storage.preventShulkerBoxes to "shulker boxes"
            else -> false to ""
        }

        if (prevent) {
            event.isCancelled = true
            player.sendMessage("${ChatColor.RED}You cannot open $label while in combat!")
        }
    }
}