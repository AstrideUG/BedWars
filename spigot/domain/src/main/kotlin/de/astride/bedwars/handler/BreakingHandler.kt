/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.handler

import de.astride.bedwars.functions.toLocation3I
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.location.extensions.toLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultBlockLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 25.04.2019 08:57.
 * Current Version: 1.0 (25.04.2019 - 27.05.2019)
 */
class BreakingHandler(
    private val whitelisted: List<Material> = listOf(Material.DOUBLE_PLANT, Material.LONG_GRASS),
    private val locations: MutableList<DefaultBlockLocation> = mutableListOf()
) : EventsTemplate() {

    fun setup(plugin: Plugin) {

        listen<BlockPlaceEvent>(plugin) { event ->
            locations += event.block.location.toLocation().toLocation3I()
        }.add()
        listen<BlockBreakEvent>(plugin) { event ->
            val block = event.block ?: return@listen
            val location = block.location.toLocation().toLocation3I()
            if (!location.isBreakable(block.type)) event.cancel()
            if (!event.isCancelled) locations -= location
        }.add()

    }

    private fun DefaultBlockLocation.isBreakable(type: Material): Boolean =
        if (whitelisted.any { it == type }) true else this in locations

}
