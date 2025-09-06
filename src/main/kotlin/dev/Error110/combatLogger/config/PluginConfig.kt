package dev.Error110.combatLogger


// 1st time using hoplite its pretty neat
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
    val reTagOnPearl : Boolean = false,
    val damageMultiplier: Double = 1.0,
    val legacy_1_8: Boolean = false
)

data class Storage(
    val preventChests : Boolean = false,
    val preventEnderChests : Boolean = false,
    val preventBarrels : Boolean = false,
    val preventShulkerBoxes : Boolean = false,
)

data class Transport(
    val banOutOfCombat: Boolean = false,
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
    val decay: Decay = Decay()
)

data class Decay(
    val enabled: Boolean = true
)
