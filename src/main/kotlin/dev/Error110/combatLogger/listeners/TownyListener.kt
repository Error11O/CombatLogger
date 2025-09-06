package dev.Error110.combatLogger.listeners

import com.palmergames.bukkit.towny.TownyAPI
import com.palmergames.bukkit.towny.event.damage.TownyPlayerDamagePlayerEvent
import com.palmergames.bukkit.towny.`object`.TownyWorld
import dev.Error110.combatLogger.CombatManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener


class TownyListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPvP(event: TownyPlayerDamagePlayerEvent) {
        if (!event.isCancelled()) return
        val world: TownyWorld? = TownyAPI.getInstance().getTownyWorld(event.getVictimPlayer().getWorld().getName())

        if (world == null || !world.isUsingTowny() || !world.isPVP() || !CombatManager().isTagged(event.victimPlayer)) return

        if (CombatManager().isTagged(event.victimPlayer)) {
            if (CombatManager().isTagged(event.attackingPlayer)) {
                event.setCancelled(false)
            } else return
        }
        event.setCancelled(false)
    }
}