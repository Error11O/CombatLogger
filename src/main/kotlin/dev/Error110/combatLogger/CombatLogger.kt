package dev.Error110.combatLogger

import dev.Error110.combatLogger.config.ConfigManager
import dev.Error110.combatLogger.config.PluginConfig
import dev.Error110.combatLogger.config.logAll
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

object CombatLogger {

    var plugin : Plugin? = null
    var logger : Logger? = null
    var config : PluginConfig? = null

    fun init(p : Plugin, c: PluginConfig) {
        plugin = p
        logger = p.logger
        config = c
        p.logger.info("Config loaded with version ${config!!.config_version}")
        config!!.logAll(p.logger)
    }
}