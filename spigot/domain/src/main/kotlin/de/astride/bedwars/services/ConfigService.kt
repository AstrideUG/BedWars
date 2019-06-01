/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.services

import de.astride.bedwars.functions.javaPlugin
import de.astride.bedwars.itemspawner.ItemSpawner
import de.astride.bedwars.players
import de.astride.bedwars.shop.categories.ShopCategory
import de.astride.bedwars.shop.items.ShopItem
import de.astride.bedwars.teams.respawner.TeamRespawner
import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.provider
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.register
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultBlockLocation
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicesManager

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 27.05.2019 01:24.
 * Last edit 30.05.2019
 */
@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
class ConfigService(values: Map<String, Any?>) {

    val delay by values.default { 10 }
    val breakingHandlerWhitelisted by values.default { setOf(Material.DOUBLE_PLANT, Material.LONG_GRASS) }
    val breakingHandlerLocations by values.default { listOf<DefaultBlockLocation>() }
    val teamRespawnersReplacement by values.default { Material.AIR }
    val teamRespawnersRespawners by values.default { setOf<TeamRespawner>() }
    val itemSpawnersDelay by values.default { 10L }
    //    val moneyTypes by values.default {
//        setOf(
//            ItemBuilder(Material.CLAY_BRICK, 2).setName("${IMPORTANT}Bronze").build(),
//            ItemBuilder(Material.IRON_INGOT).setName("${IMPORTANT}Eisen").build(),
//            ItemBuilder(Material.GOLD_INGOT).setName("${IMPORTANT}Gold").build()
//        )
//    }
//    setOf(
//    DataItemSpawner(moneyTypes[0], setOf()) { true },
//DataItemSpawner(moneyTypes[1], setOf()) { it == itemSpawnersRound / 5u || it == itemSpawnersRound / 3u || it == itemSpawnersRound / 1u },
//DataItemSpawner(moneyTypes[2], setOf()) { it == itemSpawnersRound }
//)
    val itemSpawnersSpawners by values.default { setOf<ItemSpawner>() }
    val itemSpawnersRound: UInt by values.default {
        when (players.size) {
            in 0..4 -> 60u
            in 4..8 -> 40u
            in 8..12 -> 20u
            else -> 10u
        }
    }
    val shopCategorizedItems by values.default { mapOf<ShopCategory, Iterable<ShopItem>>() }

}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 29.05.2019 16:00.
 * Current Version: 1.0 (29.05.2019 - 29.05.2019)
 */
var configService: ConfigService
    get() = javaPlugin.server.servicesManager.provider()!!
    set(value) {
        unregisterConfigService()
        registerConfigService(instance = value)
    }

fun unregisterConfigService(
    plugin: Plugin = javaPlugin,
    servicesManager: ServicesManager = plugin.server.servicesManager,
    instance: ConfigService? = servicesManager.provider()
): Unit = servicesManager.unregister(instance)

fun registerConfigService(
    plugin: Plugin = javaPlugin,
    servicesManager: ServicesManager = plugin.server.servicesManager,
    instance: ConfigService
): Unit = servicesManager.register(instance, plugin)