/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.events

import de.astride.bedwars.domain.functions.toDefaultBlockLocation
import de.astride.bedwars.domain.services.configService
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultBlockLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 25.04.2019 08:57.
 * Current Version: 1.0 (25.04.2019 - 30.05.2019)
 */
class BreakingHandler(
    private val whitelisted: Set<Material> = configService.breakingHandlerWhitelisted,
    private val locations: MutableList<DefaultBlockLocation> = configService.breakingHandlerLocations.toMutableList()
) : EventsTemplate() {

    fun setup(plugin: Plugin) {

        listen<BlockPlaceEvent>(plugin) { event ->
            locations += event.block.location.toDefaultBlockLocation()
        }.add()
        listen<BlockBreakEvent>(plugin) { event ->
            val block = event.block ?: return@listen
            val location = block.location.toDefaultBlockLocation()
            if (!location.isBreakable(block.type)) event.cancel()
            if (!event.isCancelled) locations -= location
        }.add()

    }

    private fun DefaultBlockLocation.isBreakable(type: Material): Boolean =
        if (whitelisted.any { it == type }) true else this in locations

}
