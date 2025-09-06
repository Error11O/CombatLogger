package dev.Error110.combatLogger.commands

import dev.Error110.combatLogger.CombatLogger
import dev.Error110.combatLogger.CombatLoggerPlugin
import dev.Error110.combatLogger.CombatManager
import dev.Error110.combatLogger.config.ConfigManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class Commands : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (args[0].lowercase()) {
            "reload" -> {
                ConfigManager.reload(CombatLogger.plugin!!)
                sender.sendMessage("Config reloaded.")
                return true
            }
            "tag" -> {
                if (sender is Player && sender.isOp) {
                 val player = sender as Player
                    CombatManager().applyTag(player)
                    player.sendMessage("${ChatColor.RED}You have been force tagged for testing.")
                } else sender.sendMessage("Only opped players can use this command.")
            }

            else -> {
                sender.sendMessage("Unknown subcommand. Available subcommands: reload, tag")
                return true
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>
    ): List<String?>? {
        return if (label == "combat") {
            listOf("reload", "tag").filter {
                it.startsWith(args[0].lowercase())
            }
        } else emptyList()
    }
}