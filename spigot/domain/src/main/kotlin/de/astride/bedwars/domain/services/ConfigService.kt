/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.services

import com.google.gson.JsonNull
import com.google.gson.JsonObject
import de.astride.bedwars.domain.functions.javaPlugin
import de.astride.bedwars.domain.itemspawner.ItemSpawner
import de.astride.bedwars.domain.players
import de.astride.bedwars.domain.shop.categories.ShopCategory
import de.astride.bedwars.domain.shop.items.ShopItem
import de.astride.bedwars.domain.teams.respawner.TeamRespawner
import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue
import net.darkdevelopers.darkbedrock.darkness.general.functions.JsonArray
import net.darkdevelopers.darkbedrock.darkness.general.functions.toJsonElement
import net.darkdevelopers.darkbedrock.darkness.general.functions.toJsonObject
import net.darkdevelopers.darkbedrock.darkness.general.functions.toNotNull
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.provider
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.register
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultBlockLocation
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicesManager
import java.util.*

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 27.05.2019 01:24.
 * Last edit 30.05.2019
 */
@Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")
class ConfigService(values: Map<String, Any?>) {

    val breakingHandlerWhitelisted by values.default { setOf(Material.DOUBLE_PLANT, Material.LONG_GRASS) }
    val breakingHandlerLocations by values.default { listOf<DefaultBlockLocation>() }
    val teamRespawnersReplacement by values.default { Material.AIR }
    val teamRespawnersRespawners by values.default { setOf<TeamRespawner>() }
    val itemSpawnersDelay by values.default { 10L }
    //    val moneyTypes by values.default {
//        setOf(
//            ItemBuilder(Material.CLAY_BRICK, 2).setName("${IMPORTANT}Bronze").build(),
//            ItemBuilder(Material.IRON_INGOT).setName("${IMPORTANT}Iron").build(),
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
    val lobbySpawn by values.default { Location(javaPlugin.server.worlds[0], 0.0, 100.0, 0.0) }
    val lobbyMinPlayerToStart by values.default { 2 }
    val lobbyGameName by values.default { "BedWars" }
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

//todo add to darkness
fun Any.toConfigMap(): JsonObject = TreeMap(javaClass.declaredMethods.mapNotNull { method ->
    method.isAccessible = true
    val prefix = "get"
    if (!method.name.startsWith(prefix)) return@mapNotNull null
    val invoke = method.invoke(this)
    val output = if (invoke is Iterable<*>) JsonArray(invoke.mapNotNull {
        it?.toConfigMap() ?: JsonNull.INSTANCE
    }) else invoke.toJsonElement()
    (method.name.drop(prefix.length).decapitalize().replace("[A-Z]".toRegex()) {
        "-${it.value.toLowerCase()}"
    } to output).toNotNull()
}.toMap()).toJsonObject()