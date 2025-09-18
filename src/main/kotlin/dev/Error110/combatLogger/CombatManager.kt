package dev.Error110.combatLogger

import dev.Error110.combatLogger.tasks.CombatExpire
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object CombatManager {
    val combatTags: MutableMap<UUID, Long> = ConcurrentHashMap<UUID, Long>()

    // applys the combat tag to a player
    public fun applyTag(player: Player) {
        if (!isTagged(player)) {
            player.closeInventory()
            player.sendMessage("${ChatColor.RED}You have been combat tagged for " + (CombatLogger.config!!.combat.tagSeconds.toLong()) + " seconds! Do not log out or you will get killed instantly.")
        }
        combatTags[player.uniqueId] =
            System.currentTimeMillis() + (CombatLogger.config!!.combat.tagSeconds * 1000).toLong()
    }

    // removes the combat tag from a player
    public fun removeTag(player: Player) {
        combatTags.remove(player.uniqueId)
    }

    // checks if a player is combat tagged
    public fun isTagged(player: Player): Boolean {
        return combatTags.containsKey(player.uniqueId) && combatTags[player.uniqueId]!! > System.currentTimeMillis()
    }

    // get left over time on a combat tag, returns -1 if not tagged
    public fun getRemaining(player: Player): Long {
        if (!combatTags.containsKey(player.uniqueId)) return -1
        return combatTags[player.uniqueId]!! - System.currentTimeMillis()
    }
}