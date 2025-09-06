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
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class CombatLoggerPlugin : JavaPlugin() {

    var bossbarTask = Bossbar()

    lateinit var conf: PluginConfig
        private set

    override fun onEnable() {
        saveDefaultConfig()

        conf = ConfigManager.load(this)
        logger.info("Loaded config v${conf.config_version}")

        CombatLogger.init(this, conf)
        bossbarTask.runTaskTimer(this, 10, 10)

        val hasTowny = server.pluginManager.isPluginEnabled("Towny")

        this.getCommand("combat")?.setExecutor(Commands())
        this.getCommand("combat")?.tabCompleter = Commands() as TabCompleter

        server.pluginManager.registerEvents(BlockCooldownListener(), this)
        server.pluginManager.registerEvents(ItemCooldownListener(), this)
        server.pluginManager.registerEvents(PlayerListener(), this)
        server.pluginManager.registerEvents(BannedTransportListener(), this)
        if (hasTowny) {
            logger.info("Towny detected, enabling Towny support")
            server.pluginManager.registerEvents(TownyListener(), this)
        }
        server.pluginManager.registerEvents(BannedItemsListener(), this)

        logger.info("CombatLogger enabled!")
    }

    fun reload(): Boolean {
        return try {
            conf = ConfigManager.reload(this)
            logger.info("Config reloaded")
            conf.logAll(this.logger)
            true
        } catch (t: Throwable) {
            logger.severe("Config reload failed: ${t.message}")
            false
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
