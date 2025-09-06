package dev.Error110.combatLogger

import dev.Error110.combatLogger.tasks.CombatExpire
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CombatManager {
    companion object {
        val combatTags: MutableMap<UUID, Long> = ConcurrentHashMap<UUID, Long>()
        init {
            CombatExpire().runTaskTimerAsynchronously(CombatLogger.plugin!!,10L, 10L)
        }
    }

    public fun applyTag(player: Player) {
        if (!isTagged(player)) {
            player.closeInventory()
            player.sendMessage("${ChatColor.RED}You have been combat tagged for " + (CombatLogger.config!!.combat.tagSeconds.toLong()) + " seconds! Do not log out or you will get killed instantly.")
        }
        combatTags.put(player.getUniqueId(), System.currentTimeMillis() + (CombatLogger.config!!.combat.tagSeconds * 1000).toLong())
    }

    public fun removeTag(player: Player) {
        combatTags.remove(player.getUniqueId())
    }

    public fun isTagged(player: Player): Boolean {
        return combatTags.containsKey(player.getUniqueId()) && combatTags.get(player.getUniqueId())!! > System.currentTimeMillis()
    }

    public fun getRemaining(player: Player): Long {
        if (!combatTags.containsKey(player.getUniqueId())) return -1
        return combatTags.get(player.getUniqueId())!! - System.currentTimeMillis()
    }
}