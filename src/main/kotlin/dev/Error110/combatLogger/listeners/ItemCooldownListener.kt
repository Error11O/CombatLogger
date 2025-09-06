package dev.Error110.combatLogger.listeners

import dev.Error110.combatLogger.CombatLogger
import io.papermc.paper.event.player.PlayerItemCooldownEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent

class ItemCooldownListener : Listener {

    @EventHandler
    fun onItemEat(event : PlayerItemConsumeEvent) {
        if (setCooldown(event.item.type)) {
            event.player.setCooldown(event.item.type, CombatLogger.config!!.cooldowns.items.seconds[event.item.type.toString()]!! * 20)
        }
    }

    @EventHandler
    fun onItemCooldown(event : PlayerItemCooldownEvent) {
        if(setCooldown(event.type)) {
            event.cooldown = CombatLogger.config!!.cooldowns.items.seconds[event.type.toString()]!! * 20
        }
    }

    // might remove this function or implement it better later unsure
    @EventHandler
    fun onItemInteract(event: EntityInteractEvent) {
        val player = event.entity as? Player ?: return
        val item = player.inventory.itemInMainHand
        if (setCooldown(item.type)) {
            // exclude ender pearls, golden apples and shields from cooldowns as we do above
            if (item.type == Material.ENDER_PEARL ||
                item.type == Material.GOLDEN_APPLE ||
                item.type == Material.SHIELD) return
            player.setCooldown(item.type, CombatLogger.config!!.cooldowns.items.seconds[item.type.toString()]!! * 20)
        }
    }

    fun setCooldown(type : Material) : Boolean {
        // loop material from config
        if (!CombatLogger.config!!.cooldowns.items.enabled) return false
        for (mat in CombatLogger.config!!.cooldowns.items.seconds.keys) {
            if (Material.getMaterial(mat) == null) continue
            if (type != Material.getMaterial(mat)) continue
            // return true if the material is found in the config
            return true
        }
        // return false if the material is not found in the config
        return false
    }
}