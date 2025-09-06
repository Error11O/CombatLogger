package dev.Error110.combatLogger.config

data class PluginConfig(
    val config_version: Int = 1,
    val combat: Combat = Combat(),
    val storage: Storage = Storage(),
    val transport: Transport = Transport(),
    val commands: Commands = Commands(),
    val restrictions: Restrictions = Restrictions(),
    val cooldowns: Cooldowns = Cooldowns()
)

data class Combat(
    val tagSeconds: Int = 45,
    val reTagOnPearl: Boolean = false,
    val damageMultiplier: Double = 1.0,
    val banDebuffs: Boolean = true,
    val banPotions: Boolean = false
)

data class Storage(
    val preventChests: Boolean = false,
    val preventEnderChests: Boolean = false,
    val preventBarrels: Boolean = false,
    val preventShulkerBoxes: Boolean = false,
)

data class Transport(
    val banElytra: Boolean = true,
    val banTridents: Boolean = true,
    val banPearls: Boolean = false,
    val banChorus: Boolean = false,
    val banHorses: Boolean = false,
    val banBoats: Boolean = false,
    val banMinecarts: Boolean = false
)

data class Commands(
    val whitelist: Whitelist = Whitelist()
)

data class Whitelist(
    val enabled: Boolean = true,
    val list: List<String> = emptyList()
)

data class Restrictions(
    val banned_items: BannedItems = BannedItems()
)

data class BannedItems(
    val enabled: Boolean = true,
    val items: List<String> = emptyList()
)

data class Cooldowns(
    val items: ItemCooldowns = ItemCooldowns(),
    val blocks: BlockCooldowns = BlockCooldowns()
)

data class ItemCooldowns(
    val enabled: Boolean = true,
    val seconds: Map<String, Int> = emptyMap()
)

data class BlockCooldowns(
    val enabled: Boolean = true,
    val rules: Map<String, BlockRule> = emptyMap()
)

data class BlockRule(
    val seconds: Int = 0,
    val decay_enabled: Boolean = true
)

// gpted but cool logging (cba to write all that)
fun PluginConfig.prettyString(): String = buildString {
    appendLine("PluginConfig {")
    appendLine("  config_version=$config_version")

    appendLine("  combat {")
    appendLine("    tagSeconds=${combat.tagSeconds}")
    appendLine("    reTagOnPearl=${combat.reTagOnPearl}")
    appendLine("    damageMultiplier=${combat.damageMultiplier}")
    appendLine("    banDebuffs=${combat.banDebuffs}")
    appendLine("    banPotions=${combat.banPotions}")
    appendLine("  }")

    appendLine("  storage {")
    appendLine("    preventChests=${storage.preventChests}")
    appendLine("    preventEnderChests=${storage.preventEnderChests}")
    appendLine("    preventBarrels=${storage.preventBarrels}")
    appendLine("    preventShulkerBoxes=${storage.preventShulkerBoxes}")
    appendLine("  }")

    appendLine("  transport {")
    appendLine("    banElytra=${transport.banElytra}")
    appendLine("    banTridents=${transport.banTridents}")
    appendLine("    banPearls=${transport.banPearls}")
    appendLine("    banChorus=${transport.banChorus}")
    appendLine("    banHorses=${transport.banHorses}")
    appendLine("    banBoats=${transport.banBoats}")
    appendLine("    banMinecarts=${transport.banMinecarts}")
    appendLine("  }")

    appendLine("  commands {")
    appendLine("    whitelist {")
    appendLine("      enabled=${commands.whitelist.enabled}")
    appendLine("      list=${commands.whitelist.list}")
    appendLine("    }")
    appendLine("  }")

    appendLine("  restrictions {")
    appendLine("    banned_items {")
    appendLine("      enabled=${restrictions.banned_items.enabled}")
    appendLine("      items=${restrictions.banned_items.items}")
    appendLine("    }")
    appendLine("  }")

    appendLine("  cooldowns {")
    appendLine("    items {")
    appendLine("      enabled=${cooldowns.items.enabled}")
    appendLine("      seconds=${cooldowns.items.seconds}")
    appendLine("    }")
    appendLine("    blocks {")
    appendLine("      enabled=${cooldowns.blocks.enabled}")
    if (cooldowns.blocks.rules.isEmpty()) {
        appendLine("      rules={}")
    } else {
        appendLine("      rules {")
        cooldowns.blocks.rules.forEach { (key, rule) ->
            appendLine("        $key: seconds=${rule.seconds}, decay_enabled=${rule.decay_enabled}")
        }
        appendLine("      }")
    }
    appendLine("    }")
    appendLine("  }")

    appendLine("}")
}

fun PluginConfig.logAll(logger: java.util.logging.Logger) {
    prettyString().lineSequence().forEach { logger.info(it) }
}