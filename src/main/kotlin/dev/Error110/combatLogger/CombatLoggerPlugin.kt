package dev.Error110.combatLogger

import dev.Error110.combatLogger.commands.Commands
import dev.Error110.combatLogger.config.ConfigManager
import dev.Error110.combatLogger.config.PluginConfig
import dev.Error110.combatLogger.config.logAll
import dev.Error110.combatLogger.listeners.BannedItemsListener
import dev.Error110.combatLogger.listeners.BannedTransportListener
import dev.Error110.combatLogger.listeners.BlockCooldownListener
import dev.Error110.combatLogger.listeners.ItemCooldownListener
import dev.Error110.combatLogger.listeners.PlayerListener
import dev.Error110.combatLogger.listeners.TownyListener
import dev.Error110.combatLogger.tasks.Bossbar
import dev.Error110.combatLogger.tasks.CombatExpire
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.logging.Logger

class CombatLoggerPlugin : JavaPlugin() {

    var bossbarTask = Bossbar()

    override fun onEnable() {
        saveDefaultConfig()
        val config = ConfigManager.load(this)
        CombatLogger.init(this, config)

        bossbarTask.runTaskTimer(this, 10, 10)
        CombatExpire().runTaskTimerAsynchronously(CombatLogger.plugin!!, 10L, 10L)

        this.getCommand("combat")?.setExecutor(Commands())
        this.getCommand("combat")?.tabCompleter = Commands() as TabCompleter

        server.pluginManager.registerEvents(BlockCooldownListener(), this)
        server.pluginManager.registerEvents(ItemCooldownListener(), this)
        server.pluginManager.registerEvents(PlayerListener(), this)
        server.pluginManager.registerEvents(BannedTransportListener(), this)
        if (server.pluginManager.isPluginEnabled("Towny")) {
            logger.info("Towny detected, enabling Towny support")
            server.pluginManager.registerEvents(TownyListener(), this)
        }
        server.pluginManager.registerEvents(BannedItemsListener(), this)

        logger.info("CombatLogger enabled!")
    }

    fun reload(): Boolean {
        return try {
            val newConfig = ConfigManager.reload(this)
            CombatLogger.init(this, newConfig)
            logger.info("Config reloaded")
            newConfig.logAll(this.logger)
            true
        } catch (t: Throwable) {
            logger.severe("Config reload failed: ${t.message}")
            false
        }
    }

    override fun onDisable() {
        logger.info("CombatLogger disabled!")
    }
}
