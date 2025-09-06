package dev.Error110.combatLogger.listeners

import dev.Error110.combatLogger.CombatLogger
import dev.Error110.combatLogger.CombatLogger.plugin
import dev.Error110.combatLogger.CombatManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class BlockCooldownListener : Listener {

    @EventHandler
    fun blockPlace(event : BlockPlaceEvent) {
        if (!CombatLogger.config!!.cooldowns.blocks.enabled) return
        val player = event.player
        if (!CombatManager().isTagged(player)) return
        val block = event.block
        for ((mat, rule) in CombatLogger.config!!.cooldowns.blocks.rules) {
            // if config is bad we skip it TODO: maybe log it?
            val material = Material.getMaterial(mat) ?: continue
            // if the block placed is not the same as the config we skip it
            if (block.type !== Material.getMaterial(mat)) continue
            player.setCooldown(material, rule.seconds * 20)
            if (rule.decay_enabled) {
                // schedule a task to remove the block after the decay time
                Bukkit.getScheduler().runTaskLater(plugin!!, Runnable {
                    block.type = Material.AIR
                }, ((rule.seconds * 2) * 20).toLong())
            }
        }
    }

}