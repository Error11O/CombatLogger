package dev.Error110.combatLogger

import dev.Error110.combatLogger.config.PluginConfig
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

object CombatLogger {

    var plugin : Plugin? = null
    var logger : Logger? = null
    var config : PluginConfig? = null

    fun init(p : Plugin, c : PluginConfig) {
        plugin = p
        logger = p.logger
        config = c
    }
}