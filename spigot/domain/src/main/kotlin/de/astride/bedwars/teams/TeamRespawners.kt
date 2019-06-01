/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.teams

import de.astride.bedwars.functions.toBukkitLocation
import de.astride.bedwars.services.configService
import de.astride.bedwars.teams.respawner.Respawner
import de.astride.bedwars.teams.respawner.TeamRespawner
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.team
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.teams
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultBlockLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.team.GameTeam
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:11.
 * Last edit 30.05.2019
 */
class TeamRespawners(
    private val respawners: Set<TeamRespawner> = configService.teamRespawnersRespawners,
    private val replacement: Material = configService.teamRespawnersReplacement
) {

    private val GameTeam.respawner: TeamRespawner? get() = respawners.find { it.team == this }

    fun breakRespawner(player: Player, location: DefaultBlockLocation) {

        val playerTeam = player.team ?: return
        if (!hasRespawner(playerTeam, location)) return

        val respawner: Respawner = getTeam(location)?.respawner ?: return
        if (respawner.clearAllOnBreak)
            respawner.respawners.forEach { it.toReplacement() }
        else location.toReplacement()

    }

    fun hasRespawner(team: GameTeam, location: DefaultBlockLocation? = null): Boolean {
        val respawner: Respawner = team.respawner ?: return false
        val respawners = respawner.respawners
        return respawners.any { respawner.checkTypes(it) } && (location == null || respawners.any { it == location })
    }

    fun getTeam(location: DefaultBlockLocation): GameTeam? = teams.find { hasRespawner(it, location) }

    private fun Respawner.checkTypes(location: DefaultBlockLocation): Boolean =
        types.any { location.toBukkitLocation().block.type == it }

    private fun DefaultBlockLocation.toReplacement() {
        toBukkitLocation().block.type = replacement
    }

}


