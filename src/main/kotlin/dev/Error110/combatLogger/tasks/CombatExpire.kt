package dev.Error110.combatLogger.tasks

import dev.Error110.combatLogger.CombatManager.Companion.combatTags
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.Iterator
import java.util.Map.Entry

class CombatExpire : BukkitRunnable() {
    override fun run() {
        val iterator: Iterator<Entry<UUID, Long>> = combatTags.entries.iterator() as Iterator<Entry<UUID, Long>>
        while (iterator.hasNext()) {
            val entry: Entry<UUID, Long> = iterator.next()

            if (entry.value > System.currentTimeMillis())
                continue

            iterator.remove()

            val uuid = entry.key
            val player = Bukkit.getPlayer(uuid)
            if (player == null || !player.isOnline())
                continue
            player.sendMessage("${ChatColor.GREEN}You are no longer in combat.")
        }
    }
}