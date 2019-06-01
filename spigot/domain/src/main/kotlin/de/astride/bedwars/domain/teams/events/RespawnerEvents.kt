/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.teams.events

import de.astride.bedwars.domain.action.callAction
import de.astride.bedwars.domain.functions.toDefaultBlockLocation
import de.astride.bedwars.domain.teams.TeamRespawners
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:16.
 * Last edit 30.05.2019
 */
object RespawnerEvents : EventsTemplate() {

    fun setup(plugin: Plugin, teamRespawners: TeamRespawners) = listen<BlockBreakEvent>(plugin) { event ->

        val blockLocation = event.block.location.toDefaultBlockLocation()
        teamRespawners.breakRespawner(event.player, blockLocation)

        val mapOf = mapOf(
            "teamRespawners" to teamRespawners,
            "event" to event
        )
        "team.respawner.destroyed.by".callAction(mapOf)

    }.add()

}