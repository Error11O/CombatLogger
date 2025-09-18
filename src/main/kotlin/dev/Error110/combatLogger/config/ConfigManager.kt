package dev.Error110.combatLogger.config

import dev.Error110.combatLogger.CombatLogger
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin

object ConfigManager {
    // load config from file into the data classes defined in PluginConfig.kt
    fun load(plugin: Plugin): PluginConfig {
        plugin.saveDefaultConfig()
        val c = plugin.config
        val combat = Combat(
            tagSeconds = c.getInt("combat.tagSeconds", 45),
            reTagOnPearl = c.getBoolean("combat.reTagOnPearl", false),
            damageMultiplier = c.getDouble("combat.damageMultiplier", 1.0),
        )
        val storage = Storage(
            preventChests = c.getBoolean("storage.preventChests", false),
            preventEnderChests = c.getBoolean("storage.preventEnderChests", false),
            preventBarrels = c.getBoolean("storage.preventBarrels", false),
            preventShulkerBoxes = c.getBoolean("storage.preventShulkerBoxes", false),
        )
        val transport = Transport(
            banElytra = c.getBoolean("transport.banElytra", true),
            banTridents = c.getBoolean("transport.banTridents", true),
            banPearls = c.getBoolean("transport.banPearls", false),
            banChorus = c.getBoolean("transport.banChorus", false),
            banHorses = c.getBoolean("transport.banHorses", false),
            banBoats = c.getBoolean("transport.banBoats", false),
            banMinecarts = c.getBoolean("transport.banMinecarts", false)
        )
        val commands = Commands(
            whitelist = Whitelist(
                enabled = c.getBoolean("commands.whitelist.enabled", true),
                list = c.getStringList("commands.whitelist.list")
            )
        )
        val restrictions = Restrictions(
            banned_items = BannedItems(
                enabled = c.getBoolean("restrictions.banned_items.enabled", true),
                items = c.getStringList("restrictions.banned_items.items")
            )
        )
        val itemSeconds = c.mapOfInts("cooldowns.items.seconds")
        val cooldowns = Cooldowns(
            items = ItemCooldowns(
                enabled = c.getBoolean("cooldowns.items.enabled", true),
                seconds = itemSeconds
            ),
            blocks = BlockCooldowns(
                enabled = c.getBoolean("cooldowns.blocks.enabled", true),
                rules = c.mapOfBlockRules("cooldowns.blocks.rules")
            )
        )
        return PluginConfig(
            config_version = c.getInt("config_version", 1),
            combat = combat,
            storage = storage,
            transport = transport,
            commands = commands,
            restrictions = restrictions,
            cooldowns = cooldowns
        )
    }

    // reload config from file
    fun reload(plugin: Plugin): PluginConfig {
        plugin.reloadConfig()
        val newConfig = load(plugin)
        newConfig.logAll(plugin.logger)
        return newConfig
    }

    private fun FileConfiguration.mapOfInts(path: String): Map<String, Int> {
        val sec = getConfigurationSection(path) ?: return emptyMap()
        return sec.getKeys(false).associateWith { k -> sec.getInt(k) }
    }

    private fun FileConfiguration.mapOfBlockRules(path: String): Map<String, BlockRule> {
        val sec = getConfigurationSection(path) ?: return emptyMap()
        val out = mutableMapOf<String, BlockRule>()
        for (key in sec.getKeys(false)) {
            val s = sec.getConfigurationSection(key)
            val seconds = s?.getInt("seconds", 0) ?: 0
            val decay = s?.getBoolean("decay_enabled", true) ?: true
            out[key] = BlockRule(seconds = seconds, decay_enabled = decay)
        }
        return out
    }
}