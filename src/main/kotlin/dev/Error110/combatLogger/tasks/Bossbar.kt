package dev.Error110.combatLogger.tasks

import dev.Error110.combatLogger.CombatLogger
import dev.Error110.combatLogger.CombatManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class Bossbar : BukkitRunnable() {

    val bossBarMap: MutableMap<UUID?, BossBar?> = ConcurrentHashMap<UUID?, BossBar?>()

    override fun run() {
        for (players in Bukkit.getOnlinePlayers()) {
            if (CombatManager().isTagged(players)) {
                var bossbar = bossBarMap.get(players.getUniqueId())
                if (bossbar == null) {
                    bossbar = Bukkit.createBossBar("${ChatColor.RED}Combat Tag${ChatColor.GRAY}: ", BarColor.RED, BarStyle.SOLID)
                    bossbar.addPlayer(players)
                    bossBarMap.put(players.getUniqueId(), bossbar)
                }
                update(players)
                if (!bossbar.isVisible) {
                    bossbar.setVisible(true)
                }
            } else {
                val bossbar = bossBarMap.get(players.getUniqueId())
                if (bossbar != null) {
                    bossbar.setVisible(false)
                    bossbar.removePlayer(players)
                    bossBarMap.remove(players.getUniqueId())
                }
            }
        }
    }

    fun update(player: Player) {
        val bossBar = bossBarMap.get(player.getUniqueId())
        if (bossBar == null) return

        val remaining: Long = CombatManager().getRemaining(player)
        if (remaining < 0) return

        bossBar.setTitle("${ChatColor.RED}Combat Tag ${ChatColor.GRAY}: ${ChatColor.RED}${(remaining / 1000)}s")
        bossBar.setProgress(remaining.toDouble() / (CombatLogger.config!!.combat.tagSeconds * 1000).toLong())
    }

    fun remove(player: Player) {
        val bossBar = bossBarMap.get(player.getUniqueId())
        if (bossBar != null) {
            bossBar.setVisible(false)
            bossBar.removePlayer(player)
        }
        bossBarMap.remove(player.getUniqueId())
    }
}